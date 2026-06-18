package game.states;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import game.Card;
import game.HostGame;
import game.IGameState;
import player.Player;

/**
 * PlayState handles the phase where players (excluding the judge) select a red apple
 * to match the current green apple.
 */
public class PlayState implements IGameState {

    @Override
    public void handle(HostGame context) {
        // 1. Draw the current green apple for the round
        Card currentGreenApple = context.getGreenApples().drawCard();
        context.setCurrentGreenApple(currentGreenApple);

        List<Player> players = context.getPlayers();
        int judgeIndex = context.getJudge();
        
        // 2. Setup ExecutorService and CountDownLatch for simultaneous player turns
        ExecutorService threadpool = Executors.newFixedThreadPool(players.size() > 1 ? players.size() - 1 : 1);
        CountDownLatch latch = new CountDownLatch(players.size() > 1 ? players.size() - 1 : 0); // excluding the judge

        for (int i = 0; i < players.size(); i++) {
            if (i == judgeIndex) {
                continue;
            }
            
            final Player currentPlayer = players.get(i);
            
            threadpool.execute(() -> {
                try {
                    // 3. Ask player for their card selection via the notifier
                    if (context.getNotifier() != null) {
                        context.getNotifier().askForCard(currentPlayer, currentGreenApple);
                    }
                    
                    // 4. Record the played card in the context's list
                    Card playedCard = currentPlayer.getPlayedCard();
                    if (playedCard != null) {
                        synchronized (context.getPlayedApples()) {
                            context.getPlayedApples().add(playedCard);
                        }
                    }
                } catch (Exception e) {
                    System.out.println("Error during player card submission: " + e.getMessage());
                } finally {
                    latch.countDown();
                }
            });
        }
        
        // 5. Wait for all non-judge players to submit their cards
        threadpool.shutdown();
        try {
            if (!latch.await(60, TimeUnit.SECONDS)) {
                System.out.println("Not all players responded in time.");
                threadpool.shutdownNow();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Interrupted while waiting for players to play cards");
        }
        
        // Transition to JudgeState
        context.setGameState(new JudgeState()); 
    }

    @Override
    public String getPhaseName() {
        return "PLAY_PHASE";
    }
}
