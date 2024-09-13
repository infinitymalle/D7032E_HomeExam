package networkingpackage;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;


public class NetworkManager {

    private Socket socket;
    private BufferedReader streamIn;
    private DataOutputStream streamOut;

    // When this instance is a online player connectiong to the server
    public NetworkManager(String ipAddress) {
        try {
            this.socket = new Socket(ipAddress, 2048);
            this.streamIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.streamOut = new DataOutputStream(socket.getOutputStream());
        } catch (Exception e) {
            System.out.println("Failed to setup client connection. Error " + e + "\nError message: " + e.getMessage());
        }
    }

    public String receiveMessage() {
        try {
            return streamIn.readLine();
        } catch (IOException e) {
            System.out.println("Failed to receive message. Error " + e + "\nError message: " + e.getMessage());
            return null; 
        }
    }

    public void sendMessage(String message) {
        try {
            streamOut.writeBytes(message + "\n");
        } catch (IOException e) {
            System.out.println("Failed to send message. Error " + e + "\nError message: " + e.getMessage());
        }
    }
}