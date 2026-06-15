package networking;

import java.util.List;
import game.Card;
import game.IGameNotifier;
import player.Player;

/**
 * NetworkGameNotifier implements IGameNotifier to handle communication 
 * with players over the network or locally.
 */
public class NetworkGameNotifier implements IGameNotifier {
    private ServerNetworking serverNetworkManager;
    private List<Player> players;

    public NetworkGameNotifier(ServerNetworking serverNetworkManager, List<Player> players) {
        this.serverNetworkManager = serverNetworkManager;
        this.players = players;
    }

    @Override
    public void notifyDraw(Player player, List<Card> cards) {
        if (player.isOnline()) {
            StringBuilder applesDrawn = new StringBuilder();
            for (Card card : cards) {
                applesDrawn.append("#").append(card.getString());
            }
            serverNetworkManager.sendMessage(player.getId(), "DRAW_PHASE" + applesDrawn.toString());
        }
    }

    @Override
    public void askForCard(Player player, Card greenApple) {
        if (player.isOnline()) {
            serverNetworkManager.sendMessage(player.getId(), "PLAY_PHASE#" + greenApple.getString());
            String cardString = serverNetworkManager.receiveMessage(player.getId());
            if (cardString != null) {
                player.setPlayedCard(new Card(cardString.trim()));
            }
        } else {
            // For LocalPlayer and BotPlayer, they handle their own card selection.
            // BotPlayer selects automatically, LocalPlayer asks for input.
            player.playCard(greenApple);
        }
    }

    @Override
    public Card askToJudge(Player judge, List<Card> playedApples) {
        if (judge.isOnline()) {
            StringBuilder playedRedApplesString = new StringBuilder();
            for (Card card : playedApples) {
                playedRedApplesString.append("#").append(card.getString());
            }
            serverNetworkManager.sendMessage(judge.getId(), "JUDGE_PHASE" + playedRedApplesString.toString());
            String appleString = serverNetworkManager.receiveMessage(judge.getId());
            if (appleString != null) {
                return new Card(appleString.trim());
            }
        } else {
            return judge.judge((java.util.ArrayList<Card>) playedApples);
        }
        return null;
    }

    @Override
    public void broadcastWinner(int winnerId, Card winningApple) {
        for (Player p : players) {
            if (p.isOnline()) {
                serverNetworkManager.sendMessage(p.getId(), "WINNER#" + "Player " + winnerId + " won the round with apple: " + winningApple.getString());
            } else if (!p.isBot()) {
                if (p.getId() == winnerId) {
                    System.out.println("Congratulations! \n\nYou won with the red apple:\n" + winningApple.getString() + "!\n");
                } else {
                    System.out.println("The winner is player: " + winnerId + "\nWith the winning red apple: \n" + winningApple.getString() + "\n");
                }
            }
        }
    }

    @Override
    public void notifyGameFinished(int winningPlayerId) {
        for (Player p : players) {
            if (p.isOnline()) {
                serverNetworkManager.sendMessage(p.getId(), "FINISHED#" + winningPlayerId);
            } else if (!p.isBot()) {
                System.out.println("The player with player id " + winningPlayerId + " won the game!\n\n Thank you for playing Apples to Apples!");
            }
        }
    }
}
