package game;

public class CardTest {
    public static void main(String[] args) {
        System.out.println("Running CardTest...");
        Card card = new Card("Red Apple");
        
        if (!"Red Apple".equals(card.getString())) {
            System.err.println("FAILED: Card.getString() returned incorrect value.");
            System.exit(1);
        }
        
        System.out.println("CardTest COMPLETELY PASSED!");
    }
}