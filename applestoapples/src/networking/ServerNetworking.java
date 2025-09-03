package networking;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ServerNetworking {
    private ServerSocket serverSocket;
    private ArrayList<ClientHandler> players = new ArrayList<>();

     // Server networksetup
    public ServerNetworking(int numberOfOnlinePlayers){
        try {
            serverSocket = new ServerSocket(2048);
            for(int i = 1; i <= numberOfOnlinePlayers; i++){
                Socket socket = serverSocket.accept();
                players.add(new ClientHandler(socket, i));
            }
        } catch (Exception e) {
            System.out.println("Failed to setup server connection. Error " + e + 
                               "\nError message: " + e.getMessage());
        }
    }

    public String receiveMessage(int id) {
        ClientHandler h = getHandler(id);
        if (h == null) {
            return null;
        }
        return h.receiveMessage();
    }
    
    public void sendMessage(int id, String message) {
        ClientHandler h = getHandler(id);
        if (h == null) {
            return;
        }
        h.sendMessage(message);
    }

    private ClientHandler getHandler(int id) {
        return players.get(id - 1);
    }
}
