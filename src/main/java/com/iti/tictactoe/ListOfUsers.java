package com.iti.tictactoe;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.iti.tictactoe.models.AlertUtils;
import com.iti.tictactoe.models.UiUtils;
import com.iti.tictactoe.navigation.NavigationController;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.text.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.iti.tictactoe.ServerListener.message;

public class ListOfUsers {
    @FXML
    private Button signOut;

    @FXML
    private Button inviteBtn;

    @FXML
    private ListView<String> PlayerListView;

    @FXML
    private Text playerName;

    private String invitedPlayerName;
    private List<String> playerList = new ArrayList<>();

    public static String currentUserEmail;
    private NavigationController navController;
    private final AtomicBoolean keepRefreshing = new AtomicBoolean(true);

    @FXML
    private void initialize() {
        UiUtils.addHoverAnimation(inviteBtn);
        UiUtils.addHoverAnimation(signOut);
        startRefreshingPlayerList();
    }

    private void startRefreshingPlayerList() {
        Thread refreshThread = new Thread(() -> {
            while (keepRefreshing.get()) {
                try {
                    refreshPlayerList();
                    Thread.sleep(4000); // Refresh every 3 seconds
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt(); // Restore interrupt status
                    System.out.println("Refresh thread interrupted: " + e.getMessage());
                }
            }
        });
        refreshThread.setDaemon(true);
        refreshThread.start();
    }

    private void stopRefreshingPlayerList() {
        keepRefreshing.set(false);
    }

    private void refreshPlayerList() {
        if (currentUserEmail == null) {
            System.out.println("username==null");
            return;
        }

        try {
             //Request username from the server
            SocketManager socketManager;
            socketManager = SocketManager.getInstance();
            JsonObject usernameRequest = new JsonObject();
            usernameRequest.addProperty("action", "getUsername");
            usernameRequest.addProperty("email", currentUserEmail);
            socketManager.sendJson(usernameRequest);
            JsonObject usernameResponse = socketManager.receiveJson(JsonObject.class);
            if (usernameResponse.get("success").getAsBoolean()) {
                String username = usernameResponse.get("message").getAsString();
                Platform.runLater(() -> playerName.setText("Hello " + username));
            } else {
                Platform.runLater(() -> playerName.setText("Hello Player")); // Fallback if username retrieval fails
            }

            // Create JSON object for showUsers request

            JsonObject jsonRequest = new JsonObject();
            jsonRequest.addProperty("action", "showUsers");
            jsonRequest.addProperty("email", currentUserEmail);

            // Send JSON request
            socketManager.sendJson(jsonRequest);
            System.out.println("the json request senttt =  " + jsonRequest);
            Thread.sleep(1000);

            // Read and parse the response
            Gson gson = new Gson();
            JsonArray jsonResponseArray = gson.fromJson(message, JsonArray.class);
            if (jsonResponseArray != null) {
                List<String> newPlayerList = new ArrayList<>();

                for (JsonElement element : jsonResponseArray) {
                    if (element.isJsonObject()) {
                        JsonObject userObject = element.getAsJsonObject();
                        String username = userObject.get("username").getAsString();
                        String status = userObject.get("status").getAsString();
                        newPlayerList.add(username + "                          " + status);
                    }
                }
                Platform.runLater(() -> {
                    String selectedItem = PlayerListView.getSelectionModel().getSelectedItem();
                    playerList = newPlayerList;
                    PlayerListView.getItems().setAll(newPlayerList);

                    if (selectedItem != null && newPlayerList.contains(selectedItem)) {
                        PlayerListView.getSelectionModel().select(selectedItem);
                    }
                });
            }
        } catch (IOException e) {
            System.err.println("Connection to server lost: " + e.getMessage());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


    @FXML
    public void inviteBtn(ActionEvent actionEvent) {
        String selectedPlayer = PlayerListView.getSelectionModel().getSelectedItem();
        if (selectedPlayer == null) {
            AlertUtils.showWarningAlert("No Player Selected", null, "Please select a player to invite.");
            return;
        }

        String[] parts = selectedPlayer.split(" ", 2);
        invitedPlayerName = parts[0];
        System.out.println("Invitingggg : " + invitedPlayerName);
        Optional<ButtonType> result = AlertUtils.showConfirmationAlert(
                "Send Invitation", "Do you want to send an invitation to " + selectedPlayer + "?",
                "Click 'Yes' to send the invitation or 'Cancel' to cancel."
        );
        if (result.isPresent() && result.get() == ButtonType.OK) {
            sendInvitation(invitedPlayerName);
        }
    }

    private void sendInvitation(String invitedPlayerName) {
        new Thread(() -> {
            SocketManager socketManager = SocketManager.getInstance();
            try {
                // Create JSON object for invite request
                JsonObject jsonRequest = new JsonObject();
                jsonRequest.addProperty("action", "invite");
                jsonRequest.addProperty("player", invitedPlayerName);
                System.out.println(jsonRequest + " the sent json request");
                // Send JSON request
                socketManager.sendJson(jsonRequest);

                // Read and parse the response
                JsonObject jsonResponse = socketManager.receiveJson(JsonObject.class);
                String response = jsonResponse.get("message").getAsString();
                System.out.println("Invitation response: " + response);
                Platform.runLater(() -> {
                    if ("online".equals(response)) {
                        AlertUtils.showInformationAlert("Invitation Status", "Invitation Sent", "The invitation has been successfully sent.");
                    } else if ("offline".equals(response)) {
                        AlertUtils.showInformationAlert("Invitation Status", "Invitation Not Sent", "The invited player is currently offline.");
                    } else {
                        AlertUtils.showInformationAlert("Invitation Status", "Invitation Error", "Failed to send invitation.");
                    }
                });
            } catch (IOException e) {
                System.out.printf("Error: %s\n", e.getMessage());
            }
        }).start();
    }

    @FXML
    public void signOut(ActionEvent actionEvent) {
        Optional<ButtonType> result = AlertUtils.showConfirmationAlert(
                "Sign Out",
                "Are you sure you want to sign out?",
                "Click 'OK' to proceed or 'Cancel' to stay logged in."
        );
        if (result.isPresent() && result.get() == ButtonType.OK) {
            stopRefreshingPlayerList();
            logout();
            if (navController != null) {
                navController.popScene();
                navController.popScene();
            }
        }
    }

    public void logout() {
        System.out.println("test");
        if (currentUserEmail == null) {
            return;
        }
        try {
            SocketManager socketManager = SocketManager.getInstance();
            if (socketManager == null) {
                System.out.println("SocketManager instance is null");
                return;
            }
            JsonObject logoutRequest = new JsonObject();
            logoutRequest.addProperty("action", "offline");
            logoutRequest.addProperty("email", currentUserEmail);
            System.out.println("Attempting to send logout request...");
            socketManager.sendJson(logoutRequest);
            System.out.println("Logout request sent: " + logoutRequest);
            socketManager.reinitializeConnection();
        } catch (IOException e) {
            e.printStackTrace(); // Print stack trace to help with debugging
            throw new RuntimeException("IOException during sign out: " + e.getMessage(), e);
        }
    }

    public void setNavController(NavigationController navController) {
        this.navController = navController;
    }

}
