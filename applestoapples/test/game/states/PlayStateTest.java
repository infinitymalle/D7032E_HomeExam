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
    void testPlayPhaseCollectsCardsFromNonJudges_Rules7and9() {
        ArrayList<Player> players = new ArrayList<>();
        Player p1 = new LocalPlayer(1);
        Player p2 = new LocalPlayer(2); // This will be the judge
        Player p3 = new LocalPlayer(3);
        players.add(p1);
        players.add(p2);
        players.add(p3);

        Deck greenApples = new Deck();
        greenApples.playedCard(new Card("Green Apple"));
        Deck redApples = new Deck();

        MockNotifier notifier = new MockNotifier();
        HostGame context = new HostGame(players, redApples, greenApples, notifier, 4);
        context.setJudge(1); // Index 1 is Player 2
        
        PlayState playState = new PlayState();
        playState.handle(context);

        assertEquals(2, context.getPlayedApples().size(), "Only the two non-judge players should have played cards.");
        assertTrue(context.getCurrentGameState() instanceof JudgeState, "The game should transition to the JudgeState after playing.");
    }

    static class MockNotifier implements IGameNotifier {
        @Override public void notifyDraw(Player p, List<Card> c) {}
        @Override public void askForCard(Player p, Card green) {
            // Automatically play a card for testing
            p.setPlayedCard(new Card("Played by " + p.getId()));
        }
        @Override public Card askToJudge(Player j, List<Card> played) { return null; }
        @Override public void broadcastWinner(int id, Card c) {}
        @Override public void notifyGameFinished(int id) {}
    }
}