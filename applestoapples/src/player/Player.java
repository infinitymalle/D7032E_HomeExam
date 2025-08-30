package player;

import java.util.ArrayList;

import game.Card;

public abstract class Player {
    protected int playerID;
    protected Card playedCard;
    protected ArrayList<Card> hand = new ArrayList<Card>();
    protected ArrayList<Card> points = new ArrayList<Card>();;
    protected boolean isBotPlayer = false;
    protected boolean isOnline = false;
    

    public Player(int playerID) {
        this.playerID = playerID;
    }

    abstract public Card judge(ArrayList<Card> playedApples);

    abstract public Card playCard(Card playedApple);

    abstract public void notifyWhoWon(int id, Card winningApple);

    abstract public void gamefinished(int winningPlayer);

    public String printHand(){
        String returnString = "";
        for(int i = 1; i<hand.size(); i++){
            returnString += "#\t["+i+"] " + hand.get(i).getString() + "\n\n";
        }
        return returnString;
    }

    public String newroundString(String judgeString){
        if(judgeString == "JUDGE"){
            return "*****************************************************\n\n**                 NEW ROUND - JUDGE               **\n\n*****************************************************";

        }
        return "*****************************************************\n\n**                    NEW ROUND                    **\n\n*****************************************************";
    }

    public String playedApplesToString(ArrayList<Card> playedApples){
        String returnString = "";
        for(int i = 0; i<playedApples.size(); i++){
            returnString += playedApples.get(i) + "#";
        }
        return returnString;
    }

    public int getPoints(){
        return points.size();
    }

    public void scorePoint(Card card){
        points.add(card);
    }

    public void drawCard(Card card){
        hand.add(card);
    }
    
    public void setPlayedCard(Card card){
        playedCard = card;
        this.hand.remove(card);
    }

    public Card getPlayedCard(){
        return playedCard;
    }

    public boolean isBot(){
        return this.isBotPlayer;
    }

    public boolean isOnline(){
        return this.isOnline;
    }

    public int numberOfCards(){
        return this.hand.size();
    }

    public int getId(){
        return this.playerID;
    }
}