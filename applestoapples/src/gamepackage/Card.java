package gamepackage;

public class Card {
    private String cardString; 

    public Card(String cardString){
        this.cardString = cardString;
    }

    public String getString(){
        return cardString;
    }
}