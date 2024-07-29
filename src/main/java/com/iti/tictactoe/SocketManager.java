package com.iti.tictactoe;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class SocketManager {
    private static SocketManager instance;
    private Socket socket;
    private PrintWriter pw;
    private BufferedReader br;
    private final Gson gson = new Gson(); // Gson instance for JSON handling

    private SocketManager() {
        // Constructor is private to enforce singleton pattern
    }

    public static synchronized SocketManager getInstance() {
        if (instance == null) {
            instance = new SocketManager();
        }
        return instance;
    }

    public synchronized void connectCheck() {
        if (socket == null || socket.isClosed()) {
            initializeSocket();
        } else {
            System.out.println("Socket already connected.");
        }
    }

    private void initializeSocket() {
        close(); // Ensure previous resources are closed
        try {
            // Initialize the socket and streams
            socket = new Socket("localhost", 12345);
            pw = new PrintWriter(socket.getOutputStream(), true);
            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            System.out.println("Socket initialized: " + socket);
        } catch (IOException e) {
            e.printStackTrace();
            // Consider throwing a custom exception or handling it more gracefully
        }
    }

    public synchronized PrintWriter getPrintWriter() {
        return pw;
    }

    public synchronized BufferedReader getBufferedReader() {
        return br;
    }

    public synchronized void sendJson(Object object) throws IOException {
        if (pw == null) {
            throw new IOException("PrintWriter is not initialized.");
        }
        // Serialize the object to JSON and send it
        String json = gson.toJson(object);
        pw.println(json);
        pw.flush();
        System.out.println("Sent JSON: " + json);
    }

    public synchronized <T> T receiveJson(Class<T> clazz) throws IOException {
        if (br == null) {
            throw new IOException("BufferedReader is not initialized.");
        }
        // Read JSON from the stream and deserialize it
        String json = br.readLine();
        if (json == null) {
            throw new IOException("Received null JSON.");
        }
        System.out.println("Received JSON: " + json);
        return gson.fromJson(json, clazz);
    }

    public synchronized void close() {
        try {
            if (br != null) {
                br.close();
            }
            if (pw != null) {
                pw.close();
            }
            if (socket != null && !socket.isClosed()) {
                socket.close();
                System.out.println("Socket closed.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized void reinitializeConnection() {
        close();
        initializeSocket();
    }

    public synchronized Socket getSocket() {
        return socket;
    }

    public boolean connect(String host, int port) {
        try {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
            socket = new Socket(host, port);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public void disconnect() {
        try {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
