package gamepackage;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

import networkingpackage.ServerNetworkManager;
import playerpackage.*;

public class HostGame {
    private String[] gamePhase = new String[]{"DRAW_PHASE", "PLAY_PHASE", "JUDGE_PHASE", "CHECKWIN"};

    private String greenApplesFile = "greenApples.txt";
    private String redApplesFile = "redApples.txt";
    private Deck greenApples = new Deck(greenApplesFile);
    private Deck redApples = new Deck(redApplesFile);

    private int currentPhaseIndex = 0;
    private int judge = 1;

    private ServerNetworkManager serverNetworkManager;
    private ArrayList<Player> players = new ArrayList<Player>();
    private ArrayList<Card> playedApples = new ArrayList<Card>();
    private int pointsToWin;
    private int victoriousPlayer;
    

    public HostGame(int numberOfOnlinePlayers){
        this.serverNetworkManager = new ServerNetworkManager(numberOfOnlinePlayers);

        players.add(new LocalPlayer(0));
        if(numberOfOnlinePlayers > 0){
            for(int i = 1; i <= numberOfOnlinePlayers+1; i++){
                players.add(new OnlinePlayer(i));
                serverNetworkManager.sendMessage(i, String.valueOf(i));
                System.out.println("Player with id: " + i + " joined the game!");
            }
        }
        
        if(numberOfOnlinePlayers+1 < 4){
            for(int i = players.size()+1 ;i<=4 ; i++){
                players.add(new BotPlayer(i));
                System.out.println("Bot with id: " + i + " joined the game!");
            }
            
        }
        if(players.size() < 8){
            this.pointsToWin = 12 - players.size();
        }else if(this.players.size() >= 8){
            this.pointsToWin = 4;
        }

        startGame();
    }

    private void startGame(){

        while(nextPhase()){}

        notifyGameFinished(victoriousPlayer);
        
    }

    private Boolean nextPhase() {
        // Perform actions based on the current phase
        switch (gamePhase[currentPhaseIndex]) {
            case "DRAW_PHASE":
                DrawPhase();
                break;
            case "PLAY_PHASE":
                PlayPhase();
                break;
            case "JUDGE_PHASE":
                JudgePhase();
                break;
            case "CHECKWIN":
                if(WinCheck()){
                    return false;
                }
                break;
        }

        // Move to the next phase
        currentPhaseIndex = (currentPhaseIndex + 1) % gamePhase.length;
        return true;
    }

    private void DrawPhase() {

        for(int i=0; i < players.size(); i++) {
            int numberOfCards = players.get(i).numberOfCards();
            
            //TODO servernetworkmanager
            
            ArrayList<Card> onlinePlayerCards = new ArrayList<>();
            for(int j = 0; j <= numberOfCards; j++){
                Card appleDrawn = redApples.drawCard();
                players.get(i).drawCard(appleDrawn);

                if(players.get(i).isOnline()){
                    onlinePlayerCards.add(appleDrawn);
                }
            }
            if(players.get(i).isOnline()){
                String applesDrawn = "";
                for(int j = 0;i< onlinePlayerCards.size() ;i++){
                    applesDrawn += onlinePlayerCards.get(j) + "#";
                }
                serverNetworkManager.sendMessage(i, applesDrawn);
            }
        }
    }

    private void PlayPhase() {
        Card greendApple = greenApples.drawCard();
        greenApples.playedCard(greendApple);
        
        ExecutorService threadpool = Executors.newFixedThreadPool(players.size()-1);
        CountDownLatch latch = new CountDownLatch(players.size()-1);  // excluding the judge

        for(int i=0; i < players.size(); i++) {
            if(i!=judge) {
                Player currentPlayer = players.get(i);

                //Make sure every player can answer at the same time
                Runnable task = new Runnable() {
                    @Override
                    public void run() {
                        try{
                            Card playedCard = null;
                            if (currentPlayer.isOnline()) {
                                serverNetworkManager.sendMessage(currentPlayer.getId(), "PLAY_PHASE#" + greendApple.toString());
                                String cardString = serverNetworkManager.receiveMessage(currentPlayer.getId());
                                playedCard = greenApples.creatCard(cardString);
                                
                            }else{
                                playedCard = currentPlayer.playCard(greendApple);
                                currentPlayer.setPlayedCard(playedCard);
                            }
                            
                            synchronized (playedApples) {
                                playedApples.add(playedCard);
                            }	
                            latch.countDown();
                        }catch (Exception e){
                            System.out.println("Error during player card submission: " + e.getMessage());
                        }
                    }
                };
                threadpool.execute(task);
            }
        }
        threadpool.shutdown();
        try {
            // Wait for all players to finish or timeout after 60 seconds
            if (!latch.await(60, TimeUnit.SECONDS)) {
                System.out.println("Not all players responded in time.");
                // Optionally cancel all running tasks if they are not responsive
                threadpool.shutdownNow();
                // Handle the situation where not all players have responded
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Interrupted while waiting for players to play cards");
        }
        
        
        
    }

    private void JudgePhase() {

        //Shuffle the answers
		ThreadLocalRandom rnd = ThreadLocalRandom.current();
		for(int i=playedApples.size()-1; i>0; i--) {
			int index = rnd.nextInt(i+1);
			Card a = playedApples.get(index); 
            playedApples.set(index, playedApples.get(i)); 
            playedApples.set(i, a); // SWAP
		}

        String playedRedApplesString = playedRedApplesToString();
        Card winningApple = null;
        if(players.get(judge).isOnline()){
            serverNetworkManager.sendMessage(judge, "JUDGE_PHASE#" + playedRedApplesString);
            String appleString = serverNetworkManager.receiveMessage(judge);
            winningApple = redApples.creatCard(appleString);
            
        }else if(!players.get(judge).isOnline()){
            winningApple = players.get(judge).judge(playedApples);
        }
        
        int winningPlayer = 0;
        // Who won
        for(int i=0; i < players.size(); i++){
            if(players.get(i).getPlayedCard() == winningApple){
                players.get(i).scorePoint(winningApple);
                winningPlayer = i;
            }
        }

        // notify who won
        for(int i=0; i < players.size(); i++){
            if(!players.get(i).isBot())
                if(players.get(i).isOnline()){
                    serverNetworkManager.sendMessage(i, winningApple.toString());
                }else{
                    players.get(i).notifyWhoWon(winningPlayer, winningApple);
                }
        }

        playedApples.clear();
    }

    private String playedRedApplesToString() {
        String applesString = "";
        for (int i = 0; i < playedApples.size(); i++) {
            applesString += playedApples.get(i).toString() + "#";
        }
        return applesString;
    }

    private boolean WinCheck() {
        for(int i=0; i < players.size(); i++){
            if(players.get(i).getPoints() == pointsToWin){
                victoriousPlayer = i;
                return true;
            }
        }
        return false;
    }

    private void notifyGameFinished(int winningPlayer) {
        for(int i = 0; i < players.size(); i++){
            if(players.get(i).isOnline()){
                serverNetworkManager.sendMessage(i, "FINISHED#" + winningPlayer); // TODO what palyer won?
            }else if(!players.get(i).isBot()){
                players.get(i).gamefinished(winningPlayer);
            }
        }
    }
}
