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

public class WinCheckStateTest {

    @Test
    void testNoWinnerTransitionsToDrawState_Rule13() {
        ArrayList<Player> players = new ArrayList<>();
        players.add(new LocalPlayer(1));
        players.add(new LocalPlayer(2));

        HostGame context = new HostGame(players, new Deck(), new Deck(), new MockNotifier(), 4);
        context.setJudge(0);

        WinCheckState winCheck = new WinCheckState();
        winCheck.handle(context);

        assertEquals(1, context.getJudge(), "The judge index should rotate to the next player.");
        assertTrue(context.getCurrentGameState() instanceof DrawState, "Game should transition back to DrawState.");
    }

    @Test
    void testWinnerEndsGame_Rule15() {
        ArrayList<Player> players = new ArrayList<>();
        Player p1 = new LocalPlayer(1);
        for(int i = 0; i < 4; i++) p1.scorePoint(new Card("Point")); // Player 1 meets the 4 point requirement
        players.add(p1);

        HostGame context = new HostGame(players, new Deck(), new Deck(), new MockNotifier(), 4);

        WinCheckState winCheck = new WinCheckState();
        winCheck.handle(context);

        assertNull(context.getCurrentGameState(), "Game state should be null when the game is over.");
    }

    static class MockNotifier implements IGameNotifier {
        // Minimal stub to satisfy interface
        @Override public void notifyDraw(Player p, List<Card> c) {} @Override public void askForCard(Player p, Card green) {} @Override public Card askToJudge(Player j, List<Card> played) { return null; } @Override public void broadcastWinner(int id, Card c) {} @Override public void notifyGameFinished(int id) {}
    }
}