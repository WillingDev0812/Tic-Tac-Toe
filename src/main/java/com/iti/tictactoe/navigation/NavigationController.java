package com.iti.tictactoe.navigation;


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

    // intialize the main stage
    public NavigationController(Stage stage) {
        this.stage = stage;
    }

    // Consumer : Functional interface that accepts the Controller as an argument and performs action with it
    //allow you to intialize the controller of the new scene
    public void pushScene(String filePath, Consumer<Object> action) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(filePath));
            Parent root = loader.load();
            Object controller = loader.getController();
            action.accept(controller);
            Scene scene = new Scene(root);
            navHistory.pushScene(scene); // push it to be saved in out stack
            stage.setScene(scene);
            stage.setResizable(true);
            stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH); // Disable ESC to exit full-screen
            stage.setFullScreen(true);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void popScene() {
        if (navHistory.size() > 1) { // Ensure there's more than one scene
            navHistory.popScene();
            Scene currentScene = navHistory.peekScene();
            if (currentScene != null) {
                stage.setScene(currentScene);
                stage.show();
                stage.setResizable(true);
                stage.setFullScreen(true);
            } else {
                System.out.println("No scene available to display."); // will be deleted in the master
            }
        } else {
            System.out.println("Navigation history has only one scene. Cannot pop the last scene."); // will be deleted in the master
        }
    }

}
