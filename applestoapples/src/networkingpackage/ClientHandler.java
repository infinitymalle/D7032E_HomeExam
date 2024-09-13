package networkingpackage;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ClientHandler {
    private int id;
    private BufferedReader inFromClient;
    private DataOutputStream outToClient;


    ClientHandler(Socket socket, int id){
        this.id = id;
        try {
            this.inFromClient = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.outToClient = new DataOutputStream(socket.getOutputStream());
        } catch (Exception e) {
            System.out.println("ClientHandler failed to setup client connections. Error " + e + "\nError message: " + e.getMessage());
        }
        
    }

    public String receiveMessage() {
        try {
            return this.inFromClient.readLine();
        } catch (IOException e) {
            System.out.println("ClientHandler failed to receive message. Error " + e + "\nError message: " + e.getMessage());
            return null; 
        }
    }

    public void sendMessage(String message) {
        try {
            this.outToClient.writeBytes(message);
        } catch (IOException e) {
            System.out.println("Failed to send message. Error " + e + "\nError message: " + e.getMessage());
        }
    }

    public int getId(){
        return id;
    }
}
