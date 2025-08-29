package player;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

import game.Card;

public class BotPlayer extends Player {
    public BotPlayer(int playerID) {
        super(playerID);
        this.isBotPlayer = true;
        this.isOnline = false;
    }

    @Override
    public Card judge(ArrayList<Card> playedApples) {
        int n = playedApples.size();
        if (n > 0) {
            return playedApples.get(ThreadLocalRandom.current().nextInt(n));
        }
        return playedApples.get(0);
    }
    
    @Override
    public Card playCard(Card playedGreenApple) {
        int n = hand.size();
        if (n == 0) return null;
        int i = ThreadLocalRandom.current().nextInt(n);
        return hand.remove(i);
    }

    @Override 
    public void notifyWhoWon(int id, Card winningApple){
        // bots doesn't need to change
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
        // Bots doesn't need to change
    }
}
