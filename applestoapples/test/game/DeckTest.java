package game;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class DeckTest {

    @Test
    void testReadGreenApplesFromFile_Rule1() {
        // Rule 1: Read all the green apples from a file and add to the green apples deck.
        Deck greenDeck = new Deck("greenApples.txt");
        
        Card drawn = greenDeck.drawCard();
        assertNotNull(drawn, "Deck should not be empty after loading from greenApples.txt");
        assertNotNull(drawn.getString(), "Drawn green apple should have a valid string.");
    }

    @Test
    void testReadRedApplesFromFile_Rule2() {
        // Rule 2: Read all the red apples from a file and add to the red apples deck.
        Deck redDeck = new Deck("redApples.txt");
        
        Card drawn = redDeck.drawCard();
        assertNotNull(drawn, "Deck should not be empty after loading from redApples.txt");
        assertNotNull(drawn.getString(), "Drawn red apple should have a valid string.");
    }

    @Test
    void testShuffleDeck_Rule3() {
        // Rule 3: Shuffle both the green apples and red apples decks.
        Deck deck = new Deck(); // Start with an empty deck
        
        // Add 20 ordered cards to the discard pile
        for (int i = 0; i < 20; i++) {
            deck.playedCard(new Card("Card " + i));
        }
        
        // Drawing from an empty deck forces it to pull from the discard pile and shuffle
        Card firstDrawn = deck.drawCard();
        Card secondDrawn = deck.drawCard();
        
        boolean isExactlySameOrder = "Card 0".equals(firstDrawn.getString()) && "Card 1".equals(secondDrawn.getString());
        
        assertFalse(isExactlySameOrder, "The deck should be shuffled! The cards came out in the exact same order they went in.");
    }
}