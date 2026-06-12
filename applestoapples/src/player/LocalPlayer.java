package player;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

import game.Card;

public class LocalPlayer extends Player {
    private BufferedReader keyboardInput;

    public LocalPlayer(int playerID) {
        super(playerID);
        this.keyboardInput = new BufferedReader(new InputStreamReader(System.in));
    }
    
    @Override
    public Card judge(ArrayList<Card> playedApples) {
        System.out.println(this.newRoundString(true));
        while (true) {
            System.out.println("Choose which red apple wins:\n");
            for(Card card : playedApples){
                System.out.println(card.getString() + "\n\n");
            }
            try {
                int choice = Integer.parseInt(keyboardInput.readLine());
                if (choice >= 1 && choice <= playedApples.size()) {
                    return playedApples.get(choice - 1);
                }
            } catch (Exception e) {
                // Ignore parsing/IO errors and let the loop prompt again
            } 
            System.out.println("You have to choose a valid option!");
        }
    }

    @Override
    public Card playCard(Card playedGreenApple) {
        System.out.println(this.newRoundString(false));
        System.out.print(printHand());
        System.out.println("Green apple:\n" + playedGreenApple.getString() + "\n");
        System.out.println("Choose a red apple to play:");

        while (true) {
            try {
                int choice = Integer.parseInt(keyboardInput.readLine());
                if (choice >= 0 && choice <= hand.size()-1) {
                    this.setPlayedCard(hand.get(choice));
                    System.out.println("card that was played: \n" + this.playedCard.getString() + "\n");
                    return this.playedCard; 
                }
            } catch (Exception e) {
                // Ignore parsing/IO errors and let the loop prompt again
            } 
            System.out.println("You have to choose a valid option!");
        }
    }
}