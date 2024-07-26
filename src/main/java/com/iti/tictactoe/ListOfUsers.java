package com.iti.tictactoe;

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
                    Thread.sleep(1000); // Refresh every 3 seconds
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
            playerList.clear();
            for (int i = 0; i < size; i++) {
                String player = dis.readUTF();
                playerList.add(player);
            }

            // Update UI on the JavaFX Application Thread
            Platform.runLater(() -> PlayerListView.getItems().setAll(playerList));

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


    public void inviteBtn(ActionEvent actionEvent) {
        // Implement invite button functionality
    }

    public void setNavController(NavigationController navController) {
        this.navController = navController;
    }

    @Override
    public void run() {

    }

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
