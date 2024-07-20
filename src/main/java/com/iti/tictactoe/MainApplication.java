package com.iti.tictactoe;

import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;

public class MainApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        UIUtils.playBackgroundMusic();
        FXMLLoader splashLoader = new FXMLLoader(MainApplication.class.getResource("/com/iti/tictactoe/Splash.fxml"));
        FXMLLoader mainLoader = new FXMLLoader(MainApplication.class.getResource("/com/iti/tictactoe/home screen.fxml"));

        StackPane root = new StackPane();
        root.getChildren().add(splashLoader.load());

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setFullScreen(true);
        stage.show();

        PauseTransition pause = new PauseTransition(Duration.seconds(5));
        pause.setOnFinished(event -> {
            try {
                root.getChildren().clear(); // Clear the splash screen
                root.getChildren().add(mainLoader.load()); // Add the home screen
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        pause.play();
    }

    public static void main(String[] args) {
        launch();
    }
}
