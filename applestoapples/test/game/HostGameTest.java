package game;

import java.util.ArrayList;
import player.Player;
import player.BotPlayer;

public class HostGameTest {
    public static void main(String[] args) {
        System.out.println("Running HostGameTest...");
        
        ArrayList<Player> players = new ArrayList<>();
        players.add(new BotPlayer(1));
        players.add(new BotPlayer(2));
        
        Deck redDeck = new Deck();
        Deck greenDeck = new Deck();
        
        HostGame game = new HostGame(players, redDeck, greenDeck, null, 4);
        
        if (game.getPlayers().size() != 2) {
            System.err.println("FAILED: Players list not initialized correctly.");
            System.exit(1);
        }
        
        if (game.getPointsToWin() != 4) {
            System.err.println("FAILED: Points to win not set correctly.");
            System.exit(1);
        }
        
        System.out.println("HostGameTest COMPLETELY PASSED!");
    }
}