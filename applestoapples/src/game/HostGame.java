package game;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import networking.NetworkGameNotifier;
import networking.ServerNetworking;
import player.*;
import game.states.DrawState;

public class HostGame {

    private String greenApplesFile = "greenApples.txt";
    private String redApplesFile = "redApples.txt";
    private Deck greenApples = new Deck(greenApplesFile);
    private Deck redApples = new Deck(redApplesFile);
    private Card currentGreenApple;

    private int judge;

    private ServerNetworking serverNetworkManager;
    private ArrayList<Player> players = new ArrayList<Player>();
    private ArrayList<Card> playedApples = new ArrayList<Card>();
    private int pointsToWin;
    
    private IGameState currentGameState;
    private IGameNotifier notifier;
    

    public HostGame(int numberOfOnlinePlayers){
        this.serverNetworkManager = new ServerNetworking(numberOfOnlinePlayers);

        players.add(new LocalPlayer(0));
        if(numberOfOnlinePlayers > 0){
            for(int i = 1; i <= numberOfOnlinePlayers; i++){
                players.add(new OnlinePlayer(i));
                this.serverNetworkManager.sendMessage(i, String.valueOf(i));
                System.out.println("Player with id: " + i + " joined the game!");
            }
        }
        
        if(players.size() < 4){
            for(int i = players.size() ;i <= 3 ; i++){
                players.add(new BotPlayer(i));
                System.out.println("Bot with id: " + i + " joined the game!");
            }
            
        }
        if(players.size() < 8){
            this.pointsToWin = 12 - players.size();
        }else if(this.players.size() >= 8){
            this.pointsToWin = 4;
        }

        judge = ThreadLocalRandom.current().nextInt(players.size());
        
        // Initialize notifier and initial state
        this.notifier = new NetworkGameNotifier(serverNetworkManager, players);
        this.currentGameState = new DrawState();
        
        System.out.println("Starting the game");
        startGame();
    }

    public HostGame(List<Player> players, Deck redApples, Deck greenApples, IGameNotifier notifier, int pointsToWin) {
        this.players = new ArrayList<>(players);
        this.redApples = redApples;
        this.greenApples = greenApples;
        this.notifier = notifier;
        this.pointsToWin = pointsToWin;
        this.judge = 0;
        this.currentGameState = new DrawState();
    }

    public List<Player> getPlayers() { return players; }
    public Deck getRedApples() { return redApples; }
    public Deck getGreenApples() { return greenApples; }
    public ArrayList<Card> getPlayedApples() { return playedApples; }
    public int getJudge() { return judge; }
    public void setJudge(int judge) { this.judge = judge; }
    public int getPointsToWin() { return pointsToWin; }
    public Card getCurrentGreenApple() { return currentGreenApple; }
    public void setCurrentGreenApple(Card card) { this.currentGreenApple = card; }
    public IGameNotifier getNotifier() { return notifier; }
    public void setGameState(IGameState state) { this.currentGameState = state; }

    private void startGame(){
        while(nextPhase()){}
    }

    private boolean nextPhase() {
        if (currentGameState == null) {
            return false;
        }
        currentGameState.handle(this);
        return currentGameState != null;
    }

}
