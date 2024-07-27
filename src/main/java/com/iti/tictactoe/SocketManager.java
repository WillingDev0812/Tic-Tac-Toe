package com.iti.tictactoe;

import com.google.gson.Gson;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class SocketManager {
    private static SocketManager instance;
    private Socket socket;
    private DataOutputStream dos;
    private DataInputStream dis;
    private final Gson gson = new Gson(); // Gson instance for JSON handling

    private SocketManager() {
        try {
            socket = new Socket("localhost", 12345);
            dos = new DataOutputStream(socket.getOutputStream());
            dis = new DataInputStream(socket.getInputStream());
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

    public DataOutputStream getDataOutputStream() {
        return dos;
    }

    public DataInputStream getDataInputStream() {
        return dis;
    }

    public void sendJson(Object object) throws IOException {
        // Serialize the object to JSON and send it
        String json = gson.toJson(object);
        dos.writeUTF(json);
        dos.flush();
    }

    public <T> T receiveJson(Class<T> clazz) throws IOException {
        // Read JSON from the stream and deserialize it
        String json = dis.readUTF();
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
