package networkingpackage;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ServerNetworkManager {
    private Socket socket;
    private ServerSocket serverSocket;
    private ArrayList<ClientHandler> players;

     // Server networksetup
    public ServerNetworkManager(int numberOfOnlinePlayers){
        try {
            serverSocket = new ServerSocket(2048);
        } catch (Exception e) {
            System.out.println("Failed to setup server connection. Error " + e + "\nError message: " + e.getMessage());
        }
        
        this.players = new ArrayList<ClientHandler>();
        for(int i = 0; i<numberOfOnlinePlayers; i++){
            try {
                socket = serverSocket.accept();
                players.add(new ClientHandler(socket, i));
                
            } catch (Exception e) {
                System.out.println("Failed to setup client connections. Error " + e + "\nError message: " + e.getMessage());
            }
        }
    }

    public String receiveMessage(int id) {
        for (ClientHandler player : players) {
            if (player.getId() == id) {
                try {
                    return player.receiveMessage();
                } catch (Exception e) {
                    System.out.println("Server networkmanager failed to receive message from player " + id + ". Error: " + e.getMessage());
                    return null;
                }
            }
        }
        System.out.println("Player with ID " + id + " not found.");
        return null;
    }
    

    public void sendMessage(int id, String message) {
        for (ClientHandler player : players) {
            if (player.getId() == id) {
                try {
                    player.sendMessage(message);
                } catch (Exception e) {
                    System.out.println("Server networkmanager failed to send message to player " + id + ". Error: " + e.getMessage());
                }
            }
        }
        System.out.println("Player with ID " + id + " not found.");
    }
}
