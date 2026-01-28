package org.praktikum.online;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

public class Server {

    // Colors
    public static final String RESET = "\u001B[0m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String BLUE = "\u001B[34m";
    public static final String CYAN = "\u001B[36m";

    // Thread-safe list of all clients
    private static final CopyOnWriteArrayList<ClientHandler> clients = new CopyOnWriteArrayList<>();

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(5000);
        System.out.println(GREEN + "Server started. Waiting for clients..." + RESET);

        while (true) {
            Socket socket = serverSocket.accept();
            ClientHandler handler = new ClientHandler(socket);
            clients.add(handler);
            new Thread(handler).start();
        }
    }

    // Timestamp
    public static String timestamp() {
        return "[" + java.time.LocalTime.now().withNano(0) + "]";
    }

    // Broadcast to all clients
    public static void broadcast(String message) {
        for (ClientHandler client : clients) {
            client.send(message);
        }
    }

    // Per-client handler
    static class ClientHandler implements Runnable {
        private Socket socket;
        private PrintWriter out;
        private BufferedReader in;
        private String username;
        private String color;

        public ClientHandler(Socket socket) {
            this.socket = socket;
            this.color = randomColor();
        }

        @Override
        public void run() {
            try {
                out = new PrintWriter(socket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                // Ask for username
                out.println("Enter username:");
                username = in.readLine();

                broadcast(YELLOW + timestamp() + " " + color + username + RESET + " joined the chat!" + RESET);
                System.out.println(username + " connected.");

                String msg;
                while ((msg = in.readLine()) != null) {
                    String formatted = CYAN + timestamp() + RESET + " " +
                            color + username + RESET + ": " + msg;
                    broadcast(formatted);
                }

            } catch (IOException e) {
                System.out.println("Client disconnected.");
            } finally {
                clients.remove(this);
                broadcast(YELLOW + timestamp() + " " + color + username + RESET + " left the chat." + RESET);
                try { socket.close(); } catch (IOException ignored) {}
            }
        }

        public void send(String msg) {
            out.println(msg);
        }

        private String randomColor() {
            String[] colors = {RED, GREEN, YELLOW, BLUE, CYAN};
            return colors[new Random().nextInt(colors.length)];
        }
    }
}






