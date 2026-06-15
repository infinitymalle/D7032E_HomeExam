package player;

import game.Card;

public class LocalPlayerTest {
    public static void main(String[] args) {
        System.out.println("Running LocalPlayerTest...");
        LocalPlayer player = new LocalPlayer(5);
        
        if (player.getId() != 5) {
            System.err.println("FAILED: Incorrect player ID.");
            System.exit(1);
        }
        
        if (player.isBot() || player.isOnline()) {
            System.err.println("FAILED: LocalPlayer should not be bot or online.");
            System.exit(1);
        }
        
        player.drawCard(new Card("Red Apple"));
        if (player.numberOfCards() != 1) {
            System.err.println("FAILED: Did not draw card correctly.");
            System.exit(1);
        }
        
        System.out.println("LocalPlayerTest COMPLETELY PASSED!");
    }
}