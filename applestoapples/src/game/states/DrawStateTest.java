package game.states;

import java.util.ArrayList;
import java.util.List;
import game.HostGame;
import game.IGameNotifier;
import player.LocalPlayer;
import player.Player;
import game.Card;
import game.Deck;

/**
 * DrawStateTest provides an isolation test for DrawState.
 * Since JUnit is not available, this uses a simple main method.
 */
public class DrawStateTest {
    
    public static void main(String[] args) {
        try {
            testDrawState();
        } catch (Exception e) {
            System.err.println("An unexpected error occurred during testing:");
            e.printStackTrace();
            System.exit(1);
        }
    }

    public static void testDrawState() {
        System.out.println("Running DrawStateTest...");

        // Setup mock environment
        ArrayList<Player> players = new ArrayList<>();
        Player p1 = new LocalPlayer(1); // Starts with 0 cards
        players.add(p1);
        
        Player p2 = new LocalPlayer(2);
        // Give p2 some cards already
        p2.drawCard(new Card("Initial Card 1"));
        p2.drawCard(new Card("Initial Card 2"));
        players.add(p2);

        Deck redApples = new Deck();
        // Add enough cards to the deck via playedCard which goes to discardPile
        // DrawState will draw from deck, and if empty, it pulls from discardPile.
        for (int i = 0; i < 20; i++) {
            redApples.playedCard(new Card("Red Apple " + i));
        }

        Deck greenApples = new Deck(); // Required for HostGame constructor
        
        MockNotifier notifier = new MockNotifier();
        
        // Use the injection constructor for HostGame
        HostGame context = new HostGame(players, redApples, greenApples, notifier, 12);
        
        DrawState drawState = new DrawState();
        context.setGameState(drawState);
        
        // Execute handle
        System.out.println("Executing DrawState.handle...");
        drawState.handle(context);
        
        // Verify results
        boolean success = true;
        
        // P1 should have drawn 7 cards
        if (p1.numberOfCards() != 7) {
            System.err.println("FAILED: Player 1 should have 7 cards, but has " + p1.numberOfCards());
            success = false;
        } else {
            System.out.println("PASSED: Player 1 has 7 cards.");
        }
        
        // P2 should have drawn 5 more cards (total 7)
        if (p2.numberOfCards() != 7) {
            System.err.println("FAILED: Player 2 should have 7 cards, but has " + p2.numberOfCards());
            success = false;
        } else {
            System.out.println("PASSED: Player 2 has 7 cards.");
        }
        
        // Check notifications
        if (notifier.p1CardsNotified.size() != 7) {
            System.err.println("FAILED: Notifier should have recorded 7 cards for P1, but recorded " + notifier.p1CardsNotified.size());
            success = false;
        } else {
            System.out.println("PASSED: Notifier recorded 7 cards for P1.");
        }
        
        if (notifier.p2CardsNotified.size() != 5) {
            System.err.println("FAILED: Notifier should have recorded 5 cards for P2, but recorded " + notifier.p2CardsNotified.size());
            success = false;
        } else {
            System.out.println("PASSED: Notifier recorded 5 cards for P2.");
        }
        
        if (success) {
            System.out.println("DrawStateTest COMPLETELY PASSED!");
        } else {
            System.out.println("DrawStateTest FAILED!");
            System.exit(1);
        }
    }

    /**
     * MockNotifier for testing purposes.
     */
    static class MockNotifier implements IGameNotifier {
        public List<Card> p1CardsNotified = new ArrayList<>();
        public List<Card> p2CardsNotified = new ArrayList<>();

        @Override
        public void notifyDraw(Player player, List<Card> cards) {
            if (player.getId() == 1) {
                this.p1CardsNotified.addAll(cards);
            } else if (player.getId() == 2) {
                this.p2CardsNotified.addAll(cards);
            }
        }

        @Override
        public void askForCard(Player player, Card greenApple) {}

        @Override
        public Card askToJudge(Player judge, List<Card> playedApples) {
            return null;
        }

        @Override
        public void broadcastWinner(int winnerId, Card winningApple) {}

        @Override
        public void notifyGameFinished(int winningPlayerId) {}
    }
}
