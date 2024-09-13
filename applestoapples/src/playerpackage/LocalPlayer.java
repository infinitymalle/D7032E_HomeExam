package playerpackage;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

import gamepackage.Card;

public class LocalPlayer extends Player {
    private BufferedReader keyboardInput;

    public LocalPlayer(int playerID) {
        super(playerID);
        this.keyboardInput = new BufferedReader(new InputStreamReader(System.in));
    }

    @Override
    public Card judge(ArrayList<Card> playedApples) {
        int choice = -1;
        while(choice == -1)
            System.out.println("Choose which red apple wins\n\n" + playedApplesToString(playedApples));

            
            try {
                choice = Integer.parseInt(keyboardInput.readLine());
            } catch (Exception e) {
                System.out.println("You have to choose a valid option!");
            }

		return playedApples.get(-1);
    }

    @Override
    public Card playCard(Card playedGreenApple) {

        System.out.println("The following green apple were played: \n" + playedGreenApple.toString() + "\n\n choose which red apple to play:");

        System.out.print(printHand());
        int choice = -1;
        while(choice == -1){
            try {
                choice = Integer.parseInt(keyboardInput.readLine());
            } catch (Exception e) {
                System.out.println("You have to choose a valid option!");
            }
        }
        
        
        return hand.remove(choice-1);
    }

    public void notifyWhoWon(int id, Card winningApple){
        if(playerID == id){
            System.out.println("Congratulations! \n\nYou won with the red apple: \"" + winningApple + "\"!");
            points.add(winningApple);
        }else{
            System.out.println("The winner is player: " + id + "\nWith the winning red apple: " + winningApple);
        }
    }

    @Override
    public void gamefinished(int winningPlayer) {
        System.out.println("The player with player id " + winningPlayer + " won the game!\n\n Thank you for playing Apples to Apples!");
    }    
}