package game;

import networking.ClientNetworking;
import player.LocalPlayer;

public class JoinGame {
    private LocalPlayer localPlayer;
    private ClientNetworking networkManager;
    private Deck mockDeck = new Deck(null);

    public JoinGame(String ipAddress){

        this.networkManager = new ClientNetworking(ipAddress);
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
                DrawPhase(serverMessage[1]);
                break;
            case "PLAY_PHASE":
                PlayPhase(serverMessage[1]);
                break;
            case "JUDGE_PHASE":
                JudgePhase(serverMessage[1]);
                break;
            case "WINNER":
                System.out.println(serverMessage[1]);;
                break;
            case "FINISHED":
                if(localPlayer.getId() == Integer.parseInt(serverMessage[1])){
                    System.out.println("You won the game!");
                }
                System.out.println("Player " + serverMessage[1] + "has won the game!");
                return false;

            default:
                System.out.print("Something went wrong!");
                break;
        }
        return true;
    }

    private void DrawPhase(String cardsDrawn) {
        String[] cards = cardsDrawn.split("#");

        for(int i = 0; i < cards.length; i++){
            Card card = mockDeck.creatCard(cards[i]);
            localPlayer.drawCard(card);
        }
    }

    private void PlayPhase(String playedAppleString) {
        Card GreenApple = mockDeck.creatCard(playedAppleString);
        Card RedApple = localPlayer.playCard(GreenApple);

        networkManager.sendMessage(RedApple.toString());
        
    }

    private void JudgePhase(String playedApplesString) {
        Card winningCard = localPlayer.judge(mockDeck.stringToCard(playedApplesString));

        networkManager.sendMessage(winningCard.toString());
        String winner = networkManager.receiveMessage();
        String[] idAndCard = winner.split("#");
        localPlayer.notifyWhoWon(Integer.valueOf(idAndCard[0]), mockDeck.creatCard(idAndCard[1]));
    }
}