package networking;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ServerNetworking {
    private Socket socket;
    private ServerSocket serverSocket;
    private ArrayList<ClientHandler> players;

     // Server networksetup
    public ServerNetworking(int numberOfOnlinePlayers){
        try {
            serverSocket = new ServerSocket(2048);
            for(int i = 0; i<numberOfOnlinePlayers; i++){
                socket = serverSocket.accept();
                players.add(new ClientHandler(socket, i));
            }
        } catch (Exception e) {
            System.out.println("Failed to setup server connection. Error " + e + "\nError message: " + e.getMessage());
        }
    }

    public String receiveMessage(int id) {
        ClientHandler h = players.get(id);
        if (h == null) {
            System.out.println("Player with ID " + id + " not found.");
            return null;
        }
        return h.receiveMessage();
    }
    

    public void sendMessage(int id, String message) {
        ClientHandler h = players.get(id);
        if (h == null) {
            System.out.println("Player with ID " + id + " not found.");
            return;
        }
        h.sendMessage(message);
    }
}
