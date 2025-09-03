package game;

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

        if (filepath == null || filepath.isBlank()) {
            throw new IllegalArgumentException("Deck file name must not be null/blank.");
        }else{
            this.initializeDeckFromFile(filepath);
            this.shuffleDeck();
        }        
    }

    // This is the constructur for when the deck is not used by the host
    public Deck() {
        this.deck = new ArrayList<Card>();
        this.discardPile = new ArrayList<Card>();  
    }

    public void shuffleDeck() {
        ThreadLocalRandom rnd = ThreadLocalRandom.current();
        for (int i = deck.size() - 1; i > 0; i--) {
            int index = rnd.nextInt(i + 1);
            Card temp = deck.get(index);
            this.deck.set(index, deck.get(i));
            this.deck.set(i, temp);
        }
    }


    public Card drawCard() {
        if (this.deck.isEmpty()) {
            this.deck.addAll(discardPile);
            this.discardPile.clear();
            this.shuffleDeck();
        }
        return deck.remove(0);
    }

    public void playedCard(Card card){
        this.discardPile.add(card);
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
                this.deck.add(new Card(line));
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Failed to load cards from file: " + e.getMessage());
        }
    }
}
