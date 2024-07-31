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
import static com.iti.tictactoe.ListOfUsers.*;
import static com.iti.tictactoe.OnlineController.isPlayerOneTurn;


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


            while ((message = in.readLine()) != null&&!socket.isClosed()) {
                System.out.println("ServerListener: " + message);
                if ("SERVER_STOPPED".equals(message)) {
                    //keepRefreshing = false;
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
                    String[] parts = message.split(" ", 5); // Split into at most 3 parts
                    //String[] parts = message.split(" ", 3); // Split into at most 3 parts
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
                            isPlayerOneTurn=true;
                            Platform.runLater(() -> {
                                stopRefreshingPlayerList();
                                navController.pushScene("/com/iti/tictactoe/OnlineController.fxml", controller -> {
                                    if (controller instanceof OnlineController onlinecont) {
                                        onlinecont.setNavController(navController);
                                        //gameBoardController.initialize(playerNames, false, flag,score1,score2);
                                        onlinecont.initialize(playerNames,Integer.parseInt(parts[4]),Integer.parseInt(parts[3]));
                                    }
                                });
                            });
                        } else if (result.isPresent() && result.get().getText().equals("Decline")) {
                            response = "INVITE_DECLINED";
                            keepRefreshing=true;
                        }

                        SocketManager socketManager = SocketManager.getInstance();
                        socketManager.connectCheck();
                        com.google.gson.JsonObject jsonRequest = new com.google.gson.JsonObject();
                        jsonRequest.addProperty("action", response);
                        jsonRequest.addProperty("player",invitedPlayer);
                        jsonRequest.addProperty("player2", username);
                        keepRefreshing=false;
                        try {
                            socketManager.sendJson(jsonRequest);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
                    message = null;
                } else if(message.startsWith("TMAM"))
                {
                    String[] parts = message.split(" ", 4); // Split into at most 3 parts
                    invitedPlayer = parts.length > 1 ? parts[1] : "No additional message";
                    System.out.println("tmm");
                    PlayerNames playerNames = new PlayerNames(username, invitedPlayer);
                    stopRefreshingPlayerList();
                    isPlayerOneTurn=false;
                    Platform.runLater(() -> {
                        navController.pushScene("/com/iti/tictactoe/OnlineController.fxml", controller -> {
                            if (controller instanceof OnlineController onlinecont) {
                                onlinecont.setNavController(navController);
                                onlinecont.initialize(playerNames, Integer.parseInt(parts[2]),Integer.parseInt(parts[3]));
                            }

                        });
                    });
                }
                else if (message.startsWith("PlayerMoved")){

                }
             //   else if (message.startsWith(""))

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
