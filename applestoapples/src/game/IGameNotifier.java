package game;

import player.Player;
import game.Card;
import java.util.List;

/**
 * IGameNotifier defines the communication strategy for the game.
 * It decouples the core game logic from the networking or console output,
 * allowing for easy testing via mock objects and extensibility for new clients.
 */
public interface IGameNotifier {
    void notifyDraw(Player player, List<Card> cards);
    void askForCard(Player player, Card greenApple);
    Card askToJudge(Player judge, List<Card> playedApples);
    void broadcastWinner(int winnerId, Card winningApple);
    void notifyGameFinished(int winningPlayerId);
}
