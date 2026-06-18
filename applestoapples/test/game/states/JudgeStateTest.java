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

public class JudgeStateTest {

    @Test
    void testJudgePhaseAwardsPointAndDiscards_Rules10and11() {
        ArrayList<Player> players = new ArrayList<>();
        Player p1 = new LocalPlayer(1);
        Player p2 = new LocalPlayer(2); // Judge
        players.add(p1);
        players.add(p2);

        Card winningCard = new Card("Winning Red Apple");
        p1.setPlayedCard(winningCard);

        MockNotifier notifier = new MockNotifier(winningCard);
        HostGame context = new HostGame(players, new Deck(), new Deck(), notifier, 4);
        context.setJudge(1);
        context.setCurrentGreenApple(new Card("Green Apple"));
        context.getPlayedApples().add(winningCard);

        JudgeState judgeState = new JudgeState();
        judgeState.handle(context);

        assertEquals(1, p1.getPoints(), "Player 1 should have been awarded 1 point.");
        assertEquals(0, context.getPlayedApples().size(), "Played apples should have been cleared out and discarded.");
        assertTrue(context.getCurrentGameState() instanceof WinCheckState, "Game should transition to WinCheckState.");
    }

    static class MockNotifier implements IGameNotifier {
        private Card winningCard;
        public MockNotifier(Card win) { this.winningCard = win; }
        
        @Override public void notifyDraw(Player p, List<Card> c) {}
        @Override public void askForCard(Player p, Card green) {}
        @Override public Card askToJudge(Player j, List<Card> played) { return winningCard; }
        @Override public void broadcastWinner(int id, Card c) {}
        @Override public void notifyGameFinished(int id) {}
    }
}