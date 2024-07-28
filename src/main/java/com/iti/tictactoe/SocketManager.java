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
        // Private constructor to prevent instantiation
    }

    public Socket getSocket() {
        return socket;
    }

    public static synchronized SocketManager getInstance() throws Exception {
        if (instance == null) {
            instance = new SocketManager();
            instance.initializeSocket(); // Initialize on first access
        }
        return instance;
    }

    private void initializeSocket() throws Exception {
        try {
            // Close existing socket and streams if they are not closed
            if (socket != null && !socket.isClosed()) {
                close();
            }

            // Reinitialize the socket and streams
            socket = new Socket("localhost", 12345);
            pw = new PrintWriter(socket.getOutputStream(), true);
            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            throw new RuntimeException("Failed to initialize socket: " + e.getMessage(), e);
        }
    }

    public PrintWriter getPrintWriter() {
        return pw;
    }

    public BufferedReader getBufferedReader() {
        return br;
    }

    public void sendJson(Object object) throws IOException {
        // Serialize the object to JSON and send it
        String json = gson.toJson(object);
        pw.println(json);
        pw.flush();
    }

    public <T> T receiveJson(Class<T> clazz) throws IOException {
        // Read JSON from the stream and deserialize it
        String json = br.readLine();
        return gson.fromJson(json, clazz);
    }

    public void close() {
        try {
            if (br != null) {
                br.close();
            }
            if (pw != null) {
                pw.close();
            }
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        } catch (IOException e) {
            System.out.println("Server is down");
        }
    }

    public void reinitializeConnection() {
        close();
        try {
            initializeSocket();
        } catch (Exception e) {
            throw new RuntimeException("Failed to reinitialize connection: " + e.getMessage(), e);
        }
    }
}
