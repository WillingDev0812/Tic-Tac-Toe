package com.iti.tictactoe;

import javafx.application.Platform;
import javafx.scene.control.Alert;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ServerListener implements Runnable {
    private Socket socket;

    public ServerListener(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            String message;
            while ((message = in.readLine()) != null) {
                handleServerMessage(message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleServerMessage(String message) {
        if (message.equals("SERVER_STOPPED")) {
            Platform.runLater(() -> showAlert("Server Disconnected", "The server has been stopped."));
        } else {
            // Handle other types of server messages if needed
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public void connectToServer(String host, int port) {
        try {
            SocketManager.getInstance().reinitializeConnection();
            // Start the listener thread
            new Thread(new ServerListener(SocketManager.getInstance().getSocket())).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
