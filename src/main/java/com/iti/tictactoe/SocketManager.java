package com.iti.tictactoe;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class SocketManager {
    private static SocketManager instance;
    private Socket socket;
    private DataOutputStream dos;
    private DataInputStream dis;

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
