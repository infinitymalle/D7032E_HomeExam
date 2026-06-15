package game.states;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import game.Card;
import game.HostGame;
import game.IGameState;
import player.Player;

/**
 * JudgeState handles the phase where the judge selects a winning red apple
 * from the cards played during the PlayState.
 */
public class JudgeState implements IGameState {

    @Override
    public void handle(HostGame context) {
        ArrayList<Card> playedApples = context.getPlayedApples();
        List<Player> players = context.getPlayers();
        int judgeIndex = context.getJudge();
        Player judgePlayer = players.get(judgeIndex);

        // 1. Shuffle the played red apples to keep the players anonymous
        ThreadLocalRandom rnd = ThreadLocalRandom.current();
        for (int i = playedApples.size() - 1; i > 0; i--) {
            int index = rnd.nextInt(i + 1);
            Card temp = playedApples.get(index);
            playedApples.set(index, playedApples.get(i));
            playedApples.set(i, temp);
        }

        // 2. Ask the judge to pick the winning apple
        Card winningApple = null;
        if (context.getNotifier() != null) {
            winningApple = context.getNotifier().askToJudge(judgePlayer, playedApples);
        }

        if (winningApple != null) {
            int winningPlayerIndex = -1;
            // 3. Identify who played the winning card and assign a point
            for (int i = 0; i < players.size(); i++) {
                if (i == judgeIndex) continue;
                
                Player p = players.get(i);
                if (p.getPlayedCard() != null && 
                    p.getPlayedCard().getString().equals(winningApple.getString())) {
                    
                    p.scorePoint(context.getCurrentGreenApple());
                    winningPlayerIndex = i;
                    break;
                }
            }

            // 4. Notify all players of the winner
            if (context.getNotifier() != null && winningPlayerIndex != -1) {
                context.getNotifier().broadcastWinner(winningPlayerIndex, winningApple);
            }
        }

        // 5. Cleanup for the next round
        playedApples.clear();
        for (Player p : players) {
            p.clearPlayedCard();
        }

        // Transition to WinCheckState
        context.setGameState(new WinCheckState());
    }

    @Override
    public String getPhaseName() {
        return "JUDGE_PHASE";
    }
}
