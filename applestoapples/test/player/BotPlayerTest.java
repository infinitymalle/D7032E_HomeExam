package player;

import game.Card;

public class BotPlayerTest {
    public static void main(String[] args) {
        System.out.println("Running BotPlayerTest...");
        BotPlayer bot = new BotPlayer(1);
        
        // Test properties
        if (bot.getId() != 1 || !bot.isBot() || bot.isOnline()) {
            System.err.println("FAILED: Bot properties are incorrect.");
            System.exit(1);
        }

        // Test scoring
        bot.scorePoint(new Card("Green Apple"));
        if (bot.getPoints() != 1) {
            System.err.println("FAILED: Bot did not score point correctly.");
            System.exit(1);
        }

        // Test playCard
        bot.drawCard(new Card("Apple 1"));
        bot.drawCard(new Card("Apple 2"));
        Card played = bot.playCard(new Card("Green Apple"));
        
        if (played == null || bot.numberOfCards() != 1) {
            System.err.println("FAILED: Bot did not play a card correctly.");
            System.exit(1);
        }

        System.out.println("BotPlayerTest COMPLETELY PASSED!");
    }
}