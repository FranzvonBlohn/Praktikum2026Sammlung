package org.praktikum.online;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client2 {

    public static final String RESET = "\u001B[0m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String BLUE = "\u001B[34m";
    public static final String CYAN = "\u001B[36m";

    public static void main(String[] args) throws IOException {

        Scanner scanner = new Scanner(System.in);

        //System.out.print("Enter server IP: ");
        //String ip = scanner.nextLine();
        String ip = "10.168.73.98";

        Socket socket = new Socket(ip, 5000);

        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

        // Server asks for username
        System.out.print(in.readLine());
        String username = scanner.nextLine();
        out.println(username);

        // Thread for receiving messages
        Thread reader = new Thread(() -> {
            try {
                String msg;
                while ((msg = in.readLine()) != null) {

                    // Move cursor to start of line
                    System.out.print("\r");

                    // Print the incoming message
                    System.out.println(msg);

                    // Redraw the prompt WITHOUT printing the user's input again
                    System.out.print(GREEN + username + ": " + RESET);
                }
            } catch (IOException e) {
                System.out.println(RED + "Disconnected from server." + RESET);
            }
        });



        // Thread for sending messages
        Thread writer = new Thread(() -> {
            while (true) {

                // Show prompt
                System.out.print(GREEN + username + ": " + RESET);

                // Read user input
                String msg = scanner.nextLine();

                // Send to server ONLY — do NOT print it locally
                out.println(msg);

                if (msg.equals("over")) break;
            }
        });




        reader.start();
        writer.start();
    }
}





