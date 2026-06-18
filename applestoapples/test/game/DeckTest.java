package game;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class DeckTest {

    @Test
    void testRule1And2_ReadApplesFromFile() {
        // Rule 1: Read all green apples from a file and add to the green apples deck.
        // Rule 2: Read all red apples from a file and add to the red apples deck.
        Deck redApples = new Deck("redApples.txt");
        Deck greenApples = new Deck("greenApples.txt");

        assertNotNull(redApples.drawCard(), "Red apples deck should contain cards loaded from the file.");
        assertNotNull(greenApples.drawCard(), "Green apples deck should contain cards loaded from the file.");
    }

    @Test
    void testRule3_ShuffleDecks() {
        // Rule 3: Shuffle both the green apples and red apples decks.
        Deck redApples = new Deck("redApples.txt");
        
        // We ensure that calling shuffleDeck() manually doesn't throw exceptions and works smoothly.
        assertDoesNotThrow(() -> {
            redApples.shuffleDeck();
        }, "Shuffling the deck should not throw an exception.");
    }
}