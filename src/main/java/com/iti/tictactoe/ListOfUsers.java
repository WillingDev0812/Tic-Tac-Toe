package com.iti.tictactoe;

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

import java.io.DataInputStream;
import java.io.DataOutputStream;
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
    private AtomicBoolean keepRefreshing = new AtomicBoolean(true); // Add this flag



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
                System.out.println("Refresh thread interrupted: " + e.getMessage());
            }
        });
        refreshThread.setDaemon(true);  // Ensure the thread doesn't block application termination
        refreshThread.start();
    }

    private void refreshPlayerList() {
        if (currentUserEmail == null || !keepRefreshing.get()) return; // Check the flag

        try {
            SocketManager socketManager = SocketManager.getInstance();
            DataOutputStream dos = socketManager.getDataOutputStream();
            DataInputStream dis = socketManager.getDataInputStream();

            dos.writeUTF("showUsers");
            dos.writeUTF(currentUserEmail);
            dos.flush(); // Ensure data is sent immediately

            String username = dis.readUTF();
            Platform.runLater(() -> playerName.setText("Hello " + username));

            int size = dis.readInt();
            List<String> newPlayerList = new ArrayList<>();
            for (int i = 0; i < size; i++) {
                newPlayerList.add(dis.readUTF());
            }

            Platform.runLater(() -> {
                if (!playerList.equals(newPlayerList)) {
                    // Preserve the selected item
                    String selectedItem = PlayerListView.getSelectionModel().getSelectedItem();
                    playerList = newPlayerList;
                    PlayerListView.getItems().setAll(newPlayerList);

                    // Restore the selected item
                    if (selectedItem != null && newPlayerList.contains(selectedItem)) {
                        PlayerListView.getSelectionModel().select(selectedItem);
                    }
                }
            });

        } catch (IOException e) {
            System.err.println("Connection to server lost: " + e.getMessage());
            handleServerDisconnection();
        }
    }

    private void handleServerDisconnection() {
        if (disconnectionHandled) {
            return;
        }
        disconnectionHandled = true;
        keepRefreshing.set(false); // Stop the refresh thread

        Platform.runLater(() -> {
            AlertUtils.showWarningAlert("Server Disconnected", "Connection to the server was lost.", "You will be returned to the main menu.");
            navController.popScene();
            // Add any additional navigation actions needed on disconnection
        });
    }

    public void logout() {
        if (currentUserEmail == null) return;
        new Thread(() -> {
            try {
                SocketManager socketManager = SocketManager.getInstance();
                DataOutputStream dos = socketManager.getDataOutputStream();
                dos.writeUTF("offline");
                dos.writeUTF(currentUserEmail);
                dos.flush(); // Ensure data is sent immediately
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    @FXML
    public void inviteBtn(ActionEvent actionEvent) {
        String selectedPlayer = PlayerListView.getSelectionModel().getSelectedItem();
        if (selectedPlayer == null) {
            AlertUtils.showWarningAlert("No Player Selected", null, "Please select a player to invite.");
            return;
        }

        String[] parts = selectedPlayer.split(" ", 2); // Split into at most 2 parts
        invitedPlayerName = parts[0]; // This will be the username part

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
            try {
                SocketManager socketManager = SocketManager.getInstance();
                DataOutputStream dos = socketManager.getDataOutputStream();
                DataInputStream dis = socketManager.getDataInputStream();

                // Send invitation command to the server
                dos.writeUTF("invite");
                dos.writeUTF(invitedPlayerName);
                dos.flush(); // Ensure data is sent immediately

                // Wait for the server response
                String response = dis.readUTF();
                Platform.runLater(() -> {
                    if (response.equals("online")) {
                        AlertUtils.showInformationAlert("Invitation Status", "Invitation Sent", "The invitation has been successfully sent.");
                        // Handle additional UI updates or game logic here
                    } else if (response.equals("offline")) {
                        AlertUtils.showInformationAlert("Invitation Status", "Invitation Not Sent", "The invited player is currently offline.");
                    } else {
                        AlertUtils.showInformationAlert("Invitation Status", "Invitation Error", "Failed to send invitation.");
                    }
                });
            } catch (IOException e) {
                System.out.printf("error: %s\n", e.getMessage());
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
        // Show confirmation alert
        Optional<ButtonType> result = AlertUtils.showConfirmationAlert(
                "Sign Out",
                "Are you sure you want to sign out?",
                "Click 'OK' to proceed or 'Cancel' to stay logged in."
        );
        if (result.isPresent() && result.get() == ButtonType.OK) {
            logout();
            navController.popScene();
        }
    }
}