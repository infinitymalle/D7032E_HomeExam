package game;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import player.Player;
import player.BotPlayer;

public class HostGameTest {

    @Test
    void testRandomJudge_Rule5() {
        // Rule 5: Randomise which player starts being the judge.
        Deck mockDeck = new Deck();
        List<Player> players = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            players.add(new BotPlayer(i));
        }

        boolean judgeChanged = false;
        int firstJudge = new HostGame(players, mockDeck, mockDeck, null).getJudge();

        // Run instantiation a few times to verify the judge isn't hardcoded to 0
        for (int i = 0; i < 50; i++) {
            int nextJudge = new HostGame(players, mockDeck, mockDeck, null).getJudge();
            if (nextJudge != firstJudge) {
                judgeChanged = true;
                break;
            }
        }
        
        assertTrue(judgeChanged, "The starting judge should be randomized, but it was always the same.");
    }

    @Test
    void testPointsToWin_Rule15() {
        // Rule 15:
        // 4 players -> 8 points
        // 5 players -> 7 points
        // 6 players -> 6 points
        // 7 players -> 5 points
        // 8+ players -> 4 points
        
        Deck mockDeck = new Deck();

        assertEquals(8, createGameWithNPlayers(4, mockDeck).getPointsToWin(), "4 players should require 8 points to win.");
        assertEquals(7, createGameWithNPlayers(5, mockDeck).getPointsToWin(), "5 players should require 7 points to win.");
        assertEquals(6, createGameWithNPlayers(6, mockDeck).getPointsToWin(), "6 players should require 6 points to win.");
        assertEquals(5, createGameWithNPlayers(7, mockDeck).getPointsToWin(), "7 players should require 5 points to win.");
        assertEquals(4, createGameWithNPlayers(8, mockDeck).getPointsToWin(), "8 players should require 4 points to win.");
        assertEquals(4, createGameWithNPlayers(10, mockDeck).getPointsToWin(), "10 players should require 4 points to win.");
    }
    
    private HostGame createGameWithNPlayers(int n, Deck mockDeck) {
        List<Player> players = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            players.add(new BotPlayer(i));
        }
        // Using our new test constructor that automatically calculates points & randomizes judge
        return new HostGame(players, mockDeck, mockDeck, null);
    }
}