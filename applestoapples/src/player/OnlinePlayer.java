package player;

import java.util.ArrayList;

import game.Card;

public class OnlinePlayer extends Player {

    public OnlinePlayer(int playerID) {
        super(playerID);
        isOnline = true;
    }

    @Override
    public Card judge(ArrayList<Card> playedApples){
        throw new UnsupportedOperationException("This method might be implemented in the future.");
    }

    @Override
    public Card playCard(Card playedApple){
        throw new UnsupportedOperationException("This method might be implemented in the future.");
    }

    @Override
    public void notifyWhoWon(int id, Card winningApple){
        throw new UnsupportedOperationException("This method might be implemented in the future.");
    }

    @Override
    public void gamefinished(int winningPlayer) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'gamefinished'");
    }
}