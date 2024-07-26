package com.iti.tictactoe;

import com.iti.tictactoe.models.AlertUtils;
import com.iti.tictactoe.models.UiUtils;
import com.iti.tictactoe.navigation.NavigationController;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.text.Text;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.iti.tictactoe.models.AlertUtils.showConfirmationAlert;

public class ListOfUsers implements Runnable {
    public Button signOut;

    private NavigationController navController;

    @FXML
    private Button inviteBtn;

    @FXML
    private ListView<String> PlayerListView;

    @FXML
    private Label invitePlayerWarning;

    @FXML
    private Text playerName;

    private DataOutputStream dos;
    private DataInputStream dis;
    private List<String> playerList = new ArrayList<>();

    public static String currentUserEmail;
    public String username;

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
            while (true) {
                refreshPlayerList();
                try {
                    Thread.sleep(3000); // Refresh every 1 second
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        refreshThread.setDaemon(true);  // to doesn't block the application
        refreshThread.start();
    }

    private void refreshPlayerList() {
        if (currentUserEmail == null) return;

        try (Socket socket = new Socket("localhost", 12345)) {
            dos = new DataOutputStream(socket.getOutputStream());
            dis = new DataInputStream(socket.getInputStream());

            dos.writeUTF("showUsers");
            dos.writeUTF(currentUserEmail);

            username = dis.readUTF();
            Platform.runLater(() -> playerName.setText("Hello " + username));

            int size = dis.readInt();
            List<String> newPlayerList = new ArrayList<>();
            for (int i = 0; i < size; i++) {
                String player = dis.readUTF();
                newPlayerList.add(player);
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
            e.printStackTrace();
        }
    }

    public void logout() {
        if (currentUserEmail == null) return;
        new Thread(() -> {
            try (Socket socket = new Socket("localhost", 12345)) {
                dos = new DataOutputStream(socket.getOutputStream());
                dos.writeUTF("offline");
                dos.writeUTF(currentUserEmail);
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
            try (Socket socket = new Socket("localhost", 12345)) {
                DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
                DataInputStream dis = new DataInputStream(socket.getInputStream());

                dos.writeUTF("invite");
                dos.writeUTF(currentUserEmail);
                dos.writeUTF(selectedPlayer);

                // Handle response from the server
                String response = dis.readUTF();

                Platform.runLater(() -> {
                    AlertUtils.showInformationAlert("Invitation Status", null, response);
                    if (response.startsWith("Invitation sent")) {
                        // Handle successful invitation (e.g., navigate to another scene or update UI)
                    } else {
                        // Handle failure (e.g., show an error message)
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
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
        Optional<ButtonType> result = showConfirmationAlert(
                "Sign Out",
                "Are you sure you want to sign out?",
                "Click 'OK' to proceed or 'Cancel' to stay logged in."
        );
        // Check if the user clicked the 'Sign Out' button
        if (result.isPresent() && result.get() == ButtonType.OK) {
            logout();
            navController.popScene();
        }
    }
}
