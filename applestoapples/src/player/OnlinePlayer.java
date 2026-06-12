package player;

import java.util.ArrayList;

import game.Card;

public class OnlinePlayer extends Player {

    public OnlinePlayer(int playerID) {
        super(playerID);
        isOnline = true;
        isBotPlayer = false;
    }

    @Override
    public Card judge(ArrayList<Card> playedApples){
        return null; // NetworkGameNotifier handles this externally.
    }

    @Override
    public Card playCard(Card playedApple){
        return null; // NetworkGameNotifier handles this externally.
    }
}