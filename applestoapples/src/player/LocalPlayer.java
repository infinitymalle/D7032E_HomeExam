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

    public Card judge(ArrayList<Card> playedApples) {

        while (true) {
            System.out.println("Choose which red apple wins:\n");
            System.out.println(playedApplesToString(playedApples));
            try {
                int choice = Integer.parseInt(keyboardInput.readLine());
                if (choice >= 1 && choice <= playedApples.size()) {
                    return playedApples.get(choice - 1);
                }
            } catch (Exception ignored) {} // TODO this is silent
            System.out.println("You have to choose a valid option!");
        }
    }

    @Override
    public Card playCard(Card playedGreenApple) {
        System.out.println("Green apple:\n" + playedGreenApple + "\n");
        System.out.println("Choose a red apple to play:");
        System.out.print(printHand());

        while (true) {
            try {
                int choice = Integer.parseInt(keyboardInput.readLine());
                if (choice >= 1 && choice <= hand.size()) {
                    this.setPlayedCard(hand.get(choice - 1));
                    return this.playedCard; 
                }
            } catch (Exception ignored) {} // TODO this is silent
            System.out.println("You have to choose a valid option!");
        }
    }

    public void notifyWhoWon(int id, Card winningApple){
        if(playerID == id){
            System.out.println("Congratulations! \n\nYou won with the red apple: \"" + winningApple + "\"!");
        }else{
            System.out.println("The winner is player: " + id + "\nWith the winning red apple: " + winningApple);
        }
    }

    @Override
    public void gamefinished(int winningPlayer) {
        System.out.println("The player with player id " + winningPlayer + " won the game!\n\n Thank you for playing Apples to Apples!");
    }    
}