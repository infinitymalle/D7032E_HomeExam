package game;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {
    public static void main(String[] args){
        System.out.println("Starting!");
        int choice = 0;
        BufferedReader keyboardInput = new BufferedReader(new InputStreamReader(System.in));
        do{
            System.out.println("Welcome to Apples to Apples!\n");
            System.out.println("What do you want to do?\n");
            System.out.println("1. Host game");
            System.out.println("2. Join game");
            System.out.println("3. Quit");

            try {
                String input = keyboardInput.readLine();
                choice = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                choice = 0;
            } catch (IOException e) {
                System.out.println("Input error: " + e.getMessage());
                choice = 0;
            }

            switch (choice) {
                case 1:
                    hostGame(keyboardInput);
                    break;
                case 2:
                    joinGame(keyboardInput);
                    break;
                case 3:
                    quit();
                    break;
                case 0:
                    System.out.println("Please choose a valid option!");
                    break;
            }
        } while(true);
    }

    private static void hostGame(BufferedReader keyboardInput){
        System.out.println("How many online players?\n");
        try {
            String input = keyboardInput.readLine();
            int numberOfOnlinePlayers = Integer.parseInt(input);
            new HostGame(numberOfOnlinePlayers);
        } catch (IOException e) {
            System.out.println("Input error: " + e.getMessage());
        }
    }

    private static void joinGame(BufferedReader keyboardInput){
        System.out.println("What is the ip adress?");
        try {
            String ipAddress = keyboardInput.readLine();
            new JoinGame(ipAddress);
        } catch (IOException e) {
            System.out.println("Input error: " + e.getMessage());
        }
    }

    private static void quit(){
        System.out.println("Bye Bye!");
        System.exit(0);
    }
}
