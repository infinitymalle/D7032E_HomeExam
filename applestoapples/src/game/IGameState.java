package game;

/**
 * IGameState defines the interface for the State Pattern.
 * Each specific phase of the game implements this interface to handle
 * its own logic and safely transition the Context (HostGame) to the next state.
 */
public interface IGameState {
    void handle(HostGame context);
    String getPhaseName();
}
