package player;

public class OnlinePlayerTest {
    public static void main(String[] args) {
        System.out.println("Running OnlinePlayerTest...");
        OnlinePlayer player = new OnlinePlayer(3);
        
        if (!player.isOnline()) {
            System.err.println("FAILED: OnlinePlayer should be marked as online.");
            System.exit(1);
        }
        
        if (player.isBot()) {
            System.err.println("FAILED: OnlinePlayer should not be a bot.");
            System.exit(1);
        }
        
        System.out.println("OnlinePlayerTest COMPLETELY PASSED!");
    }
}