package com.iti.tictactoe;

import com.google.gson.Gson;

import java.io.*;
import java.net.Socket;

public class SocketManager {
    private static SocketManager instance;
    private Socket socket;
    private PrintWriter pw;
    private BufferedReader br;
    private final Gson gson = new Gson(); // Gson instance for JSON handling

    private SocketManager() {
        try {
            socket = new Socket("localhost", 12345);
            pw = new PrintWriter(socket.getOutputStream(), true);
            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static synchronized SocketManager getInstance() {
        if (instance == null) {
            instance = new SocketManager();
        }
        return instance;
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
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
