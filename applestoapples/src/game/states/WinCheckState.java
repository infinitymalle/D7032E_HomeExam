package game.states;

import game.HostGame;
import game.IGameState;
import player.Player;
import java.util.List;

/**
 * WinCheckState checks if any player has reached the required points to win.
 * If so, it notifies that the game is finished.
 * Otherwise, it transitions to the DrawState for the next round.
 */
public class WinCheckState implements IGameState {

    @Override
    public void handle(HostGame context) {
        List<Player> players = context.getPlayers();
        int pointsToWin = context.getPointsToWin();
        int winnerId = -1;

        for (Player p : players) {
            if (p.getPoints() >= pointsToWin) {
                winnerId = p.getId();
                break;
            }
        }

        if (winnerId != -1) {
            if (context.getNotifier() != null) {
                context.getNotifier().notifyGameFinished(winnerId);
            }
            context.setGameState(null); // End the game
        } else {
            // No winner yet, prepare for next round
            // 1. Increment judge index (round robin)
            int currentJudge = context.getJudge();
            context.setJudge((currentJudge + 1) % players.size());

            // 2. Transition back to DrawState
            context.setGameState(new DrawState());
        }
    }

    @Override
    public String getPhaseName() {
        return "WIN_CHECK_PHASE";
    }
}
