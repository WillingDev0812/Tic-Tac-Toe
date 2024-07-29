package com.iti.tictactoe;

import com.iti.tictactoe.models.UiUtils;
import com.iti.tictactoe.navigation.NavigationController;
import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;


public class TicTacToeGame extends Application {
    private NavigationController navController;

  /*  @Override
    public void stop() throws Exception {
        SocketManager socketManager = SocketManager.getInstance();
        new ListOfUsers().logout();
        socketManager.close();
        super.stop();
    }*/

    @Override
    public void start(Stage stage) {
        try {
            // Initialize Navigation Controller
            navController = new NavigationController(stage);
            UiUtils.playBackgroundMusic();

            // Load and display the splash screen
            FXMLLoader splashLoader = new FXMLLoader(TicTacToeGame.class.getResource("/com/iti/tictactoe/splash.fxml"));
            StackPane root = new StackPane();
            root.getChildren().add(splashLoader.load());

            Scene splashScene = new Scene(root);
            stage.setScene(splashScene);

            stage.setFullScreen(false);
            stage.setResizable(true);
            stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH); // Disable ESC to exit full-screen

            stage.show();

            // Transition to the home screen after a delay
            PauseTransition pause = new PauseTransition(Duration.seconds(0.1));
            pause.setOnFinished(event -> {
                navController.pushScene("/com/iti/tictactoe/home-screen.fxml", controller -> {
                    if (controller instanceof HomeScreenController homeScreenController) {
                        homeScreenController.setNavController(navController);
                    }
                });
            });
            pause.play();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch();
    }
}
