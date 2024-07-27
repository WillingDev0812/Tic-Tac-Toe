package com.iti.tictactoe;

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

public class ListOfUsers implements Runnable {
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
    private volatile boolean disconnectionHandled = false;
    private AtomicBoolean keepRefreshing = new AtomicBoolean(true);


    public static void setCurrentUserEmail(String email) {
        currentUserEmail = email;
    }

    public void initialize() {
        UiUtils.addHoverAnimation(inviteBtn);
        UiUtils.addHoverAnimation(signOut);
        startRefreshingPlayerList();
    }

    private void startRefreshingPlayerList() {
        Thread refreshThread = new Thread(() -> {
            try {
                while (keepRefreshing.get()) {
                    refreshPlayerList();
                    Thread.sleep(3000); // Refresh every 3 seconds
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                System.out.println("Refresh thread interrupted: " + e.getMessage());
            }
        });
        refreshThread.setDaemon(true);
        refreshThread.start();
    }

    private void refreshPlayerList() {
        if (currentUserEmail == null) return;

        SocketManager socketManager = SocketManager.getInstance();

        try {
            // Create JSON object for showUsers request
            JsonObject jsonRequest = new JsonObject();
            jsonRequest.addProperty("action", "showUsers");
            jsonRequest.addProperty("email", currentUserEmail);

            // Send JSON request
            socketManager.sendJson(jsonRequest);

            // Read and parse the response
            JsonArray jsonResponseArray = socketManager.receiveJson(JsonArray.class);
            List<String> newPlayerList = new ArrayList<>();

            for (JsonElement element : jsonResponseArray) {
                if (element.isJsonObject()) {
                    JsonObject userObject = element.getAsJsonObject();
                    String username = userObject.get("username").getAsString();
                    String status = userObject.get("status").getAsString();
                    newPlayerList.add(username + "      " + status);
                } else {
                    // Handle unexpected element types if necessary
                    System.err.println("Unexpected JSON element type: " + element.toString());
                }
            }

            // Update UI if needed
            Platform.runLater(() -> {
                String selectedItem = PlayerListView.getSelectionModel().getSelectedItem();
                playerList = newPlayerList;
                PlayerListView.getItems().setAll(newPlayerList);

                if (selectedItem != null && newPlayerList.contains(selectedItem)) {
                    PlayerListView.getSelectionModel().select(selectedItem);
                }
                playerName.setText("Hello " + currentUserEmail);
            });

        } catch (IOException e) {
            System.err.println("Connection to server lost: " + e.getMessage());
            // Handle server disconnection
        }
    }

    private void handleServerDisconnection() {
        if (disconnectionHandled) {
            return;
        }
        disconnectionHandled = true;
        keepRefreshing.set(false);

        Platform.runLater(() -> {
            AlertUtils.showWarningAlert("Server Disconnected", "Connection to the server was lost.", "You will be returned to the main menu.");
            navController.popScene();
        });
    }
    public void logout() {
        // Implementation of logout logic
        sendLogoutRequest();
    }

    private void sendLogoutRequest() {
        SocketManager socketManager = SocketManager.getInstance();

        try {
            // Create JSON object for logout request
            JsonObject jsonRequest = new JsonObject();
            jsonRequest.addProperty("action", "offline");
            jsonRequest.addProperty("email", currentUserEmail);

            // Send JSON request
            socketManager.sendJson(jsonRequest);

        } catch (IOException e) {
            e.printStackTrace();
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

        Optional<ButtonType> result = AlertUtils.showConfirmationAlert(
                "Send Invitation", "Do you want to send an invitation to " + selectedPlayer + "?",
                "Click 'Yes' to send the invitation or 'Cancel' to cancel."
        );
        if (result.isPresent() && result.get() == ButtonType.OK) {
            sendInvitation(selectedPlayer);
        }
    }

    private void sendInvitation(String selectedPlayer) {
        new Thread(() -> {
            SocketManager socketManager = SocketManager.getInstance();

            try {
                // Create JSON object for invite request
                JsonObject jsonRequest = new JsonObject();
                jsonRequest.addProperty("action", "invite");
                jsonRequest.addProperty("player", selectedPlayer);

                // Send JSON request
                socketManager.sendJson(jsonRequest);

                // Read and parse the response
                JsonObject jsonResponse = socketManager.receiveJson(JsonObject.class);
                String response = jsonResponse.get("status").getAsString();

                Platform.runLater(() -> {
                    if (response.equals("online")) {
                        AlertUtils.showInformationAlert("Invitation Status", "Invitation Sent", "The invitation has been successfully sent.");
                    } else if (response.equals("offline")) {
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

    public void setNavController(NavigationController navController) {
        this.navController = navController;
    }

    @Override
    public void run() {
        // Implementation for Runnable if needed
    }

    @FXML
    public void signOut(ActionEvent actionEvent) {
        Optional<ButtonType> result = AlertUtils.showConfirmationAlert(
                "Sign Out",
                "Are you sure you want to sign out?",
                "Click 'OK' to proceed or 'Cancel' to stay logged in."
        );
        if (result.isPresent() && result.get() == ButtonType.OK) {
            sendLogoutRequest();
            navController.popScene();
        }
    }
}
