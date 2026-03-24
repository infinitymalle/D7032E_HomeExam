package game;

import player.Player;
import game.Card;
import java.util.List;

public interface IGameNotifier {
    void notifyDraw(Player player, List<Card> cards);
    void askForCard(Player player, Card greenApple);
    void broadcastWinner(int winnerId, Card winningApple);
    void notifyGameFinished(int winningPlayerId);
}
