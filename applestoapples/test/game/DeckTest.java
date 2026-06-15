package game;

public class DeckTest {
    public static void main(String[] args) {
        System.out.println("Running DeckTest...");
        Deck deck = new Deck(); // Empty deck
        
        // Add cards to the discard pile
        deck.playedCard(new Card("Apple 1"));
        deck.playedCard(new Card("Apple 2"));
        
        // Drawing from an empty deck should trigger a reshuffle from the discard pile
        Card drawn = deck.drawCard();
        
        if (drawn == null) {
            System.err.println("FAILED: Deck failed to draw card after reshuffling from discard pile.");
            System.exit(1);
        }
        
        System.out.println("DeckTest COMPLETELY PASSED!");
    }
}