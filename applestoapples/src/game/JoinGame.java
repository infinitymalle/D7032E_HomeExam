package game;

import networking.ClientNetworking;
import player.LocalPlayer;

/**
 * Manages the client-side game loop for an online player.
 * Communicates with the HostGame server to receive phase instructions
 * and submit played or judged cards over the network.
 */
public class JoinGame {
    private LocalPlayer localPlayer;
    private ClientNetworking networkManager;
    private Deck mockDeck = new Deck();

    public JoinGame(String ipAddress){

        this.networkManager = new ClientNetworking(ipAddress);
        String message = networkManager.receiveMessage();
        int playerID = Integer.parseInt(message);
        System.out.println("Your are player: " + playerID);
        this.localPlayer = new LocalPlayer(playerID);
        
        startGame();
    }

    private void startGame(){

        while(nextPhase()){}
    }


    private Boolean nextPhase() {
        String message = networkManager.receiveMessage();
        String[] serverMessage = message.split("#", 2);
        String payload = (serverMessage.length > 1) ? serverMessage[1] : "";
        switch (serverMessage[0]) {
            case "DRAW_PHASE":
                drawPhase(payload);
                break;
            case "PLAY_PHASE":
                playPhase(payload);
                break;
            case "JUDGE_PHASE":
                System.out.println("Time to judge!");
                judgePhase(payload);
                break;
            case "WINNER":
                System.out.println(payload);;
                break;
            case "FINISHED":
                if(localPlayer.getId() == Integer.parseInt(payload)){
                    System.out.println("You won the game!");
                }
                System.out.println("Player " + payload + "has won the game!");
                return false;

            default:
                System.out.print("Something went wrong!");
                break;
        }
        return true;
    }

    private void drawPhase(String cardsDrawn) {
        String[] cards = cardsDrawn.split("#");

        for(int i = 0; i < cards.length; i++){
            if (cards[i].isEmpty()) continue;
            Card card = mockDeck.createCard(cards[i]);
            localPlayer.drawCard(card);
        }
    }

    private void playPhase(String playedAppleString) {
        Card greenApple = mockDeck.createCard(playedAppleString);
        Card redApple = localPlayer.playCard(greenApple);

        networkManager.sendMessage(redApple.getString());
        
    }

    private void judgePhase(String playedApplesString) {
        Card winningCard = localPlayer.judge(mockDeck.stringToCard(playedApplesString));

        networkManager.sendMessage(winningCard.getString());
    }
}