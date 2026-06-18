package game.states;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import game.Card;
import game.Deck;
import game.HostGame;
import game.IGameNotifier;
import player.Player;
import player.LocalPlayer;

public class PlayStateTest {

    @Test
    void testPlayPhase_Rules6to9() {
        ArrayList<Player> players = new ArrayList<>();
        Player p1 = new LocalPlayer(1);
        Player p2 = new LocalPlayer(2); // This will be the judge
        
        // Mock player 1 selecting a card immediately when their turn thread executes
        p1.setPlayedCard(new Card("Red Apple 1"));
        players.add(p1);
        players.add(p2);

        Deck greenApples = new Deck();
        greenApples.playedCard(new Card("Green Apple")); // Put one in discard pile so it can be drawn

        MockNotifier notifier = new MockNotifier();
        HostGame context = new HostGame(players, new Deck(), greenApples, notifier, 4);
        context.setJudge(1); // Set p2 (index 1) as judge

        PlayState playState = new PlayState();
        playState.handle(context);

        assertNotNull(context.getCurrentGreenApple(), "Rule 6: A green apple should be drawn.");
        assertEquals(1, context.getPlayedApples().size(), "Rule 7 & 9: All non-judge players must play a card.");
        assertTrue(notifier.askedForCard, "Notifier should have asked player 1 for a card.");
        assertTrue(context.getCurrentGameState() instanceof JudgeState, "Game should transition to JudgeState.");
    }

    @Test
    void testRule8_PlayedApplesAreShuffled() {
        ArrayList<Player> players = new ArrayList<>();
        
        // Create 1 judge (index 0) and 10 players to ensure the shuffle is statistically noticeable
        for (int i = 0; i < 11; i++) {
            Player p = new LocalPlayer(i);
            if (i > 0) {
                p.setPlayedCard(new Card("Red Apple " + i));
            }
            players.add(p);
        }

        Deck greenApples = new Deck();
        greenApples.playedCard(new Card("Green Apple"));

        MockNotifier notifier = new MockNotifier();
        HostGame context = new HostGame(players, new Deck(), greenApples, notifier, 4);
        context.setJudge(0); // Player 0 is the judge

        PlayState playState = new PlayState();
        playState.handle(context); // Collects the cards

        // The shuffle actually happens at the start of JudgeState, so we must trigger it
        context.getCurrentGameState().handle(context);

        assertEquals(10, notifier.capturedApples.size(), "10 cards should have been played and passed to the judge.");

        // The chance of 10 items randomly shuffling into their exact original sequential order is 1 in 3,628,800.
        // If the first card is "Red Apple 1", the second is "Red Apple 2", etc., it wasn't shuffled!
        boolean isShuffled = !notifier.capturedApples.get(0).getString().equals("Red Apple 1") || 
                             !notifier.capturedApples.get(1).getString().equals("Red Apple 2");
        
        assertTrue(isShuffled, "Rule 8: Played red apples should be shuffled before judging.");
    }

    static class MockNotifier implements IGameNotifier {
        boolean askedForCard = false;
        List<Card> capturedApples = new ArrayList<>();
        
        @Override public void notifyDraw(Player p, List<Card> c) {}
        @Override public void askForCard(Player p, Card green) { askedForCard = true; }
        @Override public Card askToJudge(Player j, List<Card> played) { 
            capturedApples.addAll(played); 
            return null; 
        }
        @Override public void broadcastWinner(int id, Card c) {}
        @Override public void notifyGameFinished(int id) {}
    }
}
