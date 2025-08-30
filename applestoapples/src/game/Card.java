package game;

public class Card {
    private final String cardString; 

    public Card(String cardString){
        this.cardString = cardString;
    }

    public String getString(){
        return cardString;
    }
}