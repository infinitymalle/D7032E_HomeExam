package gamepackage;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;


public class Deck {
    private ArrayList<Card> deck;
    private ArrayList<Card> discardPile;

    public Deck(String filepath) {
        this.deck = new ArrayList<Card>();
        this.discardPile = new ArrayList<Card>();
        this.initializeDeckFromFile(filepath);
        this.shuffleDeck();
        
    }

    public void shuffleDeck() {
        ThreadLocalRandom rnd = ThreadLocalRandom.current();
        for (int i = deck.size() - 1; i > 0; i--) {
            int index = rnd.nextInt(i + 1);
            Card temp = deck.get(index);
            deck.set(index, deck.get(i));
            deck.set(i, temp);
        }
    }


    public Card drawCard() {
        if (deck.isEmpty()) {
            deck = discardPile;
            discardPile.clear();
            shuffleDeck();
        }
        Card card = deck.remove(0);
        return card;
    }

    public void playedCard(Card card){
        discardPile.add(card);
    }

    public Card creatCard(String cardString){
        return new Card(cardString);
    }

    public ArrayList<Card> stringToCard (String cards){
        String[] cardStrings = cards.split("#");
        ArrayList<Card> cardList = new ArrayList<Card>();

        for(int i = 0; i < cardStrings.length; i++){
            Card card = creatCard(cardStrings[i]);
            cardList.add(card);
        }
        return cardList;
    }

    private void initializeDeckFromFile(String fileName) {
        try {
            List<String> lines = Files.readAllLines(Paths.get("bin" ,"apples" , fileName), StandardCharsets.ISO_8859_1);
            for (String line : lines) {
                deck.add(new Card(line));
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Failed to load cards from file: " + e.getMessage());
        }
    }
}
