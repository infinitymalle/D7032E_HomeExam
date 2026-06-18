package game;

/**
 * Represents a single playing card containing a text string.
 */
public class Card {
    private final String cardString; 

    public Card(String cardString){
        this.cardString = cardString;
    }

    public String getString(){
        return cardString;
    }
}