package networking;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ClientHandler {
    private int id;
    private BufferedReader in;
    private DataOutputStream out;


    public ClientHandler(Socket socket, int id){
        this.id = id;
        try {
            this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.out = new DataOutputStream(socket.getOutputStream());
            System.out.print("Connected!");
        } catch (Exception e) {
            throw new RuntimeException("ClientHandler init failed for id=" + id, e);
        }
        
    }

    public String receiveMessage() {
        try {
            return this.in.readLine();
        } catch (IOException e) {
            System.out.println("ClientHandler failed to receive message. Error " + e + "\nError message: " + e.getMessage());
            return null; 
        }
    }

    public void sendMessage(String message) {
        try {
            this.out.writeBytes(message + "\n");
            this.out.flush();
        } catch (IOException e) {
            System.out.println("Failed to send message. Error " + e + "\nError message: " + e.getMessage());
        }
    }

    public int getId(){
        return id;
    }
}
