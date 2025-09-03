package game;

import networking.ClientNetworking;
import player.LocalPlayer;

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
                DrawPhase(payload);
                break;
            case "PLAY_PHASE":
                PlayPhase(payload);
                break;
            case "JUDGE_PHASE":
                System.out.println("Time to judge!");
                JudgePhase(payload);
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

    private void DrawPhase(String cardsDrawn) {
        String[] cards = cardsDrawn.split("#");

        for(int i = 0; i < cards.length; i++){
            if (cards[i].isEmpty()) continue;
            Card card = mockDeck.creatCard(cards[i]);
            localPlayer.drawCard(card);
        }
    }

    private void PlayPhase(String playedAppleString) {
        Card GreenApple = mockDeck.creatCard(playedAppleString);
        Card RedApple = localPlayer.playCard(GreenApple);

        networkManager.sendMessage(RedApple.getString());
        
    }

    private void JudgePhase(String playedApplesString) {
        Card winningCard = localPlayer.judge(mockDeck.stringToCard(playedApplesString));

        networkManager.sendMessage(winningCard.getString());
    }
}