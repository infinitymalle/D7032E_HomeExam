package gamepackage;

import networkingpackage.NetworkManager;
import playerpackage.LocalPlayer;

public class JoinGame {
    private LocalPlayer localPlayer;
    private NetworkManager networkManager;
    private Deck mockDeck;

    public JoinGame(String ipAddress){

        this.networkManager = new NetworkManager(ipAddress);
        String message = networkManager.receiveMessage();
        int playerID = Integer.parseInt(message);
        this.localPlayer = new LocalPlayer(playerID);
        


        startGame();
    }

    private void startGame(){

        while(nextPhase()){}
    }


    private Boolean nextPhase() {
        String message = networkManager.receiveMessage();
        String[] serverMessage = message.split("#", 2);
        
        switch (serverMessage[0]) {
            case "DRAW_PHASE":
                DrawPhase();
                break;
            case "PLAY_PHASE":
                PlayPhase(serverMessage[1]);
                break;
            case "JUDGE_PHASE":
                JudgePhase(serverMessage[1]);
                break;
            case "FINISHED":
                
                break;
            default:
                System.out.print("Something went wrong!");
                break;
        
        
        }
        return true;
    }

    private void DrawPhase() {
        String numberOfCards = Integer.toString(localPlayer.numberOfCards());
        networkManager.sendMessage(numberOfCards);
        String message = networkManager.receiveMessage();
        String[] cards = message.split("#");

        for(int i = 0; i < cards.length; i++){
            Card card = mockDeck.creatCard(cards[i]);
            localPlayer.drawCard(card);
        }
    }

    private void PlayPhase(String playedAppleString) {
        Card playedGreenApple = mockDeck.creatCard(playedAppleString);
        Card playedRedApple = localPlayer.playCard(playedGreenApple);

        networkManager.sendMessage(playedRedApple.toString());
        
    }

    private void JudgePhase(String playedApplesString) {
        Card winningCard = localPlayer.judge(mockDeck.stringToCard(playedApplesString));

        networkManager.sendMessage(winningCard.toString());
        String winner = networkManager.receiveMessage();
        String[] idAndCard = winner.split("#");
        localPlayer.notifyWhoWon(Integer.valueOf(idAndCard[0]), mockDeck.creatCard(idAndCard[1]));
    }
}