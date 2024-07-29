package com.iti.tictactoe.navigation;

import com.iti.tictactoe.SocketManager;
import com.iti.tictactoe.models.AlertUtils;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.function.Consumer;

public class NavigationController {
    private final Stage stage;
    private final NavigationHistory navHistory = new NavigationHistory();

    // Initialize the main stage
    public NavigationController(Stage stage) {
        this.stage = stage;
    }

    // Pushes a new scene onto the navigation stack
    public void pushScene(String filePath, Consumer<Object> action) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(filePath));
            Parent root = loader.load();
            Object controller = loader.getController();
            action.accept(controller);
            Scene scene = new Scene(root);
            navHistory.pushScene(scene); // Save the scene in our stack
            stage.setScene(scene);
            stage.setResizable(true);
            stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH); // Disable ESC to exit full-screen
            stage.setFullScreen(false);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Pops the top scene from the navigation stack
    public void popScene() {
        if (navHistory.size() > 1) { // Ensure there's more than one scene
            navHistory.popScene();
            Scene currentScene = navHistory.peekScene();
            if (currentScene != null) {
                stage.setScene(currentScene);
                stage.show();
                stage.setResizable(true);
                stage.setFullScreen(false);
            } else {
                System.out.println("No scene available to display.");
            }
        } else {
            System.out.println("Navigation history has only one scene. Cannot pop the last scene.");
        }
    }

    public void handleServerStop() {
        AlertUtils.showInformationAlert("Server Disconnected", "The server has been stopped.",
                "You have been logged out due to server disconnection.");
        // Pop all scenes until we reach the login page
        SocketManager.getInstance().reinitializeConnection();
        while (navHistory.size() > 2) {
            popScene();
        }

    }
}