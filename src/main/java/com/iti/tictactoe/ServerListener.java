package com.iti.tictactoe;

import com.iti.tictactoe.models.AlertUtils;
import com.iti.tictactoe.models.PlayerNames;
import com.iti.tictactoe.navigation.NavigationController;
import javafx.application.Platform;
import javafx.scene.control.ButtonType;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Optional;

import static com.iti.tictactoe.ListOfUsers.*;

public class ServerListener implements Runnable {

    private Socket socket;
    public static BufferedReader in;
    private NavigationController navController;
    public static String message;
    public static String invitedPlayer;
    private boolean exitNotificationDisplayed = false;


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
            while ((message = in.readLine()) != null) {
                System.out.println("ServerListener: " + message);
                if ("SERVER_STOPPED".equals(message)) {
                    keepRefreshing = false;
                    Platform.runLater(() -> {
                        if (navController != null) {
                            navController.handleServerStop();
                            message = null;
                        } else {
                            System.err.println("NavigationController is null in ServerListener.");
                        }
                    });
                    break;
                } else if (message.startsWith("INVITE")) {
                    String[] parts = message.split(" ", 3);
                    invitedPlayer = parts.length > 2 ? parts[2] : "No additional message";

                    Platform.runLater(() -> {
                        Optional<ButtonType> result = AlertUtils.showCustomConfirmationAlert(
                                "Invitation",
                                null,
                                invitedPlayer + " has invited you to play a game."
                        );

                        String response = "";
                        if (result.isPresent() && result.get().getText().equals("Accept")) {
                            response = "INVITE_ACCEPTED";
                            PlayerNames playerNames = new PlayerNames(username, invitedPlayer);
                            Platform.runLater(() -> {
                                navController.pushScene("/com/iti/tictactoe/OnlineController.fxml", controller -> {
                                    if (controller instanceof OnlineController onlinecont) {
                                        onlinecont.setNavController(navController);
                                        onlinecont.initialize(playerNames, 1, 1);
                                    }
                                });
                            });
                        } else if (result.isPresent() && result.get().getText().equals("Decline")) {
                            response = "INVITE_DECLINED";
                        }
                        SocketManager socketManager = SocketManager.getInstance();
                        socketManager.connectCheck();
                        com.google.gson.JsonObject jsonRequest = new com.google.gson.JsonObject();
                        jsonRequest.addProperty("action", response);
                        jsonRequest.addProperty("player", invitedPlayer);
                        jsonRequest.addProperty("player2", username);
                        try {
                            socketManager.sendJson(jsonRequest);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
                    message = null;
                } else if (message.startsWith("TMAM")) {
                    String[] parts = message.split(" ", 2);
                    invitedPlayer = parts.length > 1 ? parts[1] : "No additional message";
                    System.out.println("tmm");
                    PlayerNames playerNames = new PlayerNames(username, invitedPlayer);
                    Platform.runLater(() -> {
                        navController.pushScene("/com/iti/tictactoe/OnlineController.fxml", controller -> {
                            if (controller instanceof OnlineController onlinecont) {
                                onlinecont.setNavController(navController);
                                onlinecont.initialize(playerNames, 1, 1);
                            }
                        });
                    });
                } else if (message.startsWith("PlayerMoved")) {
                    // Handle player move messages

                } else if (message.startsWith("PLAYER_EXIT")) {
                    if (!exitNotificationDisplayed) {
                        String[] parts = message.split(" ", 2);
                        String username = parts.length > 1 ? parts[1] : "No username provided";

                        Platform.runLater(() -> {
                            AlertUtils.showInformationAlert("Exit Notification",
                                    null, username + " has exited the game");

                            navController.popScene();

                            // Retrieve current user's email
                            String userEmail = currentUserEmail; // Ensure this retrieves the user's email correctly

                            SocketManager socketManager = SocketManager.getInstance();
                            com.google.gson.JsonObject jsonRequest = new com.google.gson.JsonObject();
                            jsonRequest.addProperty("action", "online");
                            jsonRequest.addProperty("email", userEmail);
                            exitNotificationDisplayed = true; // Set flag to true to prevent repeated notifications
                            try {
                                socketManager.sendJson(jsonRequest);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        });
                    }
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
