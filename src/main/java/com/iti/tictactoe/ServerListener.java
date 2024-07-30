package com.iti.tictactoe;

import com.iti.tictactoe.models.AlertUtils;
import com.iti.tictactoe.models.PlayerNames;
import com.iti.tictactoe.muliplayerSingleOffline.GameBoardController;
import com.iti.tictactoe.navigation.NavigationController;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import javax.json.JsonObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Optional;

import static com.iti.tictactoe.AIGame.SinglePlayerMenuController.flag;
import static com.iti.tictactoe.ListOfUsers.keepRefreshing;
import static com.iti.tictactoe.ListOfUsers.username;


public class ServerListener implements Runnable {


    private Socket socket;
    public static BufferedReader in;
    private NavigationController navController;
    public static String message;
    public static String invitedPlayer;
    public static volatile JsonObject gg;
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
            while ((message = in.readLine()) != null) {
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
                    //String[] parts = message.split(" ", 5); // Split into at most 3 parts
                    String[] parts = message.split(" ", 3); // Split into at most 3 parts
                    invitedPlayer = parts.length > 2 ? parts[2] : "No additional message";
//                    int score2 = parts.length > 3 ? Integer.parseInt(parts[3]) : 0;
//                    int score1 = parts.length > 4 ? Integer.parseInt(parts[4]) : 0;
                    Platform.runLater(() -> {
                        Optional<ButtonType> result = AlertUtils.showCustomConfirmationAlert(
                                "Invitation",
                                null,
                                invitedPlayer + " has invited you to play a game."
                        );

                        String response = "gg";
                        if (result.isPresent() && result.get().getText().equals("Accept")) {
                            response = "INVITE_ACCEPTED";
                            //plauer2 el et3mlo el invite
                            PlayerNames playerNames = new PlayerNames(username, invitedPlayer);
                            Platform.runLater(() -> {
                                navController.pushScene("/com/iti/tictactoe/board-game-view.fxml", controller -> {
                                    if (controller instanceof GameBoardController gameBoardController) {
                                        gameBoardController.setNavController(navController);
                                        //gameBoardController.initialize(playerNames, false, flag,score1,score2);
                                        gameBoardController.initialize(playerNames, false, flag,1,1);
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
                        jsonRequest.addProperty("player",invitedPlayer);
                        jsonRequest.addProperty("player2", username);
                        try {
                            socketManager.sendJson(jsonRequest);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
                    message = null;
                } else if(message.startsWith("TMAM"))
                {
                    String[] parts = message.split(" ", 2); // Split into at most 3 parts
                    invitedPlayer = parts.length > 1 ? parts[1] : "No additional message";
                    System.out.println("tmm");
                    PlayerNames playerNames = new PlayerNames(username, invitedPlayer);
                    Platform.runLater(() -> {
                        navController.pushScene("/com/iti/tictactoe/board-game-view.fxml", controller -> {
                            if (controller instanceof GameBoardController gameBoardController) {
                                gameBoardController.setNavController(navController);
                                gameBoardController.initialize(playerNames, false, flag,1,1);
                            }
                            else
                            {
                                System.out.println("a7a");
                            }
                        });
                    });
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
