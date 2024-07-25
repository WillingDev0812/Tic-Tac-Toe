package com.iti.tictactoe;

import java.io.*;
import java.net.*;

public class TicTacToeClient {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 3060;
    private Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;

    public void start() {
        try {
            socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
            in = new ObjectInputStream(socket.getInputStream());
            out = new ObjectOutputStream(socket.getOutputStream());

            // Start a thread to listen for incoming messages
            new Thread(this::listenForMessages).start();

            // Example: Send and receive messages
            sendMessage("Hello from client!");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(Object message) {
        try {
            out.writeObject(message);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Object receiveMessage() {
        try {
            return in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void listenForMessages() {
        while (true) {
            try {
                Object message = receiveMessage();
                if (message != null) {
                    System.out.println("Received: " + message);
                    // Handle the received message
                }
            } catch (Exception e) {
                e.printStackTrace();
                break;
            }
        }
    }
}
