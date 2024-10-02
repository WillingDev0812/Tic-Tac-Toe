package com.iti.tictactoe;

import com.iti.tictactoe.navigation.NavigationController;
import javafx.application.Platform;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ServerListener implements Runnable {


    private Socket socket;
    public static BufferedReader in;
    private NavigationController navController;
    public static String message;
    public static volatile String a7a;
    public ServerListener(Socket socket, NavigationController navController) {
        this.socket = socket;
        this.navController = navController;
        try {
            this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
        //    String message;
            while ((message = in.readLine())!= null) {
                if ("SERVER_STOPPED".equals(message)) {
                    Platform.runLater(() -> {
                        if (navController != null) {
                            navController.handleServerStop();
                        } else {
                            System.err.println("NavigationController is null in ServerListener.");
                        }
                    });
                    break;
                }else{

                  //   a7a=message;
                }
                // Handle other server messages here...
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
                if (socket != null && !socket.isClosed()) {
                    socket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
