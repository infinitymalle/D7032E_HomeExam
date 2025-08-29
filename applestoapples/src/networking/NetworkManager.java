package networkingpackage;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;


public class NetworkManager {

    private Socket socket;
    private BufferedReader in;
    private DataOutputStream out;

    // When this instance is a online player connectiong to the server
    public NetworkManager(String ipAddress) {
        try {
            this.socket = new Socket(ipAddress, 2048);
            this.socket.setSoTimeout(60_000);
            this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.out = new DataOutputStream(socket.getOutputStream());
        } catch (Exception e) {
            System.out.println("Failed to setup client connection. Error " + e + "\nError message: " + e.getMessage());
        }
    }

    public String receiveMessage() {
        try {
            return in.readLine();
        } catch (IOException e) {
            System.out.println("Failed to receive message. Error " + e + "\nError message: " + e.getMessage());
            return null; 
        }
    }

    public void sendMessage(String message) {
        try {
            out.writeBytes(message + "\n");
            out.flush();
        } catch (IOException e) {
            System.out.println("Failed to send message. Error " + e + "\nError message: " + e.getMessage());
        }
    }
}