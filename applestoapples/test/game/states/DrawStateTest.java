package game.states;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

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
 */
public class DrawStateTest {

    @Test
    void testDrawState() {

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
        drawState.handle(context);
        
        // Use JUnit Assertions to verify the state
        assertEquals(7, p1.numberOfCards(), "Player 1 should have exactly 7 cards.");
        assertEquals(7, p2.numberOfCards(), "Player 2 should have exactly 7 cards.");
        assertEquals(7, notifier.p1CardsNotified.size(), "Notifier should have recorded 7 cards drawn for P1.");
        assertEquals(5, notifier.p2CardsNotified.size(), "Notifier should have recorded 5 cards drawn for P2.");
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
