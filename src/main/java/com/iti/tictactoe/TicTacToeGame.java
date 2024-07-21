package com.iti.tictactoe;

import com.iti.tictactoe.muliplayerOffline.models.UiUtils;
import com.iti.tictactoe.navigation.NavigationController;
import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;

public class TicTacToeGame extends Application {
    private NavigationController navController;
    @Override
    public void start(Stage stage) throws IOException {
        // Intialize Navigation Controller
        navController = new NavigationController(stage);
        UiUtils.playBackgroundMusic();

        FXMLLoader splashLoader = new FXMLLoader(TicTacToeGame.class.getResource("/com/iti/tictactoe/splash.fxml"));
        StackPane root = new StackPane();
        root.getChildren().add(splashLoader.load());

        Scene splashScene = new Scene(root);
        stage.setScene(splashScene);
        stage.setFullScreen(false);
        stage.show();

        PauseTransition pause = new PauseTransition(Duration.seconds(5));
        pause.setOnFinished(event -> {
            navController.pushScene("/com/iti/tictactoe/home-screen.fxml", controller -> {
                if (controller instanceof HomeScreenController homeScreenController) {
                    homeScreenController.setNavController(navController);
                }
            });
        });
        pause.play();
    }
    public static void main(String[] args) {
        launch();
    }
}
