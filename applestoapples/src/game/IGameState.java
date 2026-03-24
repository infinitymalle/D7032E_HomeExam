package game;

public interface IGameState {
    void handle(HostGame context);
    String getPhaseName();
}
