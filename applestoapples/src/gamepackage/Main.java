package gamepackage;

import java.util.Scanner;

public class Main {
    public static void main(String[] args){
        System.out.println("Starting!");
        int choice = 0;
        Scanner scanner = new Scanner(System.in);
        do{
            System.out.println("Welcome to Apples to Apples!\n");
            System.out.println("What do you want to do?\n");
            System.out.println("1. Host game");
            System.out.println("2. Join game");
            System.out.println("3. Quit");

            try {
                String input = scanner.nextLine();
                choice = Integer.parseInt(input);

            } catch (Exception e) {
                // TODO: handle exception
            }

            switch (choice) {
                case 1:
                    hostGame(scanner);
                    break;
                case 2:
                    joinGame(scanner);
                    break;
                case 3:
                    scanner.close();
                    quit();
                    break;
                case 0:
                System.out.println("Please choose a valid option!");
                    break;
            }
        } while(0 <= choice && 4 > choice);
    }

    private static void hostGame(Scanner scanner){

        System.out.println("How many online players?\n\nNumber of online players:");
        String input = scanner.nextLine();
        int numberOfOnlinePlayers = Integer.parseInt(input);

        scanner.close();
        new HostGame(numberOfOnlinePlayers);
    }

    private static void joinGame(Scanner scanner){
        System.out.println("What is the ip adress?");
        String ipAddress = scanner.nextLine();

        scanner.close();
        new JoinGame(ipAddress);
        
    }

    private static void quit(){
        System.out.println("Bye Bye!");
        System.exit(0);
    }
}
