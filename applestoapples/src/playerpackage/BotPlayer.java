package playerpackage;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

import gamepackage.Card;

public class BotPlayer extends Player {
    public BotPlayer(int playerID) {
        super(playerID);
    }

    @Override
    public Card judge(ArrayList<Card> playedApples) {
        int numberOfOptions = playedApples.size();
        if (numberOfOptions > 0) {
            return playedApples.get(ThreadLocalRandom.current().nextInt(numberOfOptions));
        }
        return playedApples.get(0); // Default to the first option if no valid options found.
    }
    

    @Override
    public Card playCard(Card playedApple) {
        int numberOfOptions = hand.size();
        if (numberOfOptions > 0) {
            return hand.get(ThreadLocalRandom.current().nextInt(numberOfOptions));
        }
        return hand.get(0); // Default to the first option if no valid options found.
    }

    @Override
    public void notifyWhoWon(int id, Card winningApple){
        if (playerID == id) {
            points.add(playedCard);
        }
    }

    public static int countHashes(String input) {
        int count = 0;
        for (int i = 0; i < input.length(); i++) {
            if (input.charAt(i) == '#') {
                count++;
            }
        }
        return count;
    }

    @Override
    public void gamefinished(int winningPlayer) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'gamefinished'");
    }
}
