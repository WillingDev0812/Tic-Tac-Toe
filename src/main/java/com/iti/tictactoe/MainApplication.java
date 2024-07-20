package com.iti.tictactoe;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        //  FXMLLoader splashLoader = new FXMLLoader(MainApplication.class.getResource("/com/iti/tictactoe/Splash.fxml"));
        //FXMLLoader mainLoader = new FXMLLoader(HelloApplication.class.getResource("/com/iti/tictacton_game/MainScreen.fxml"));
        FXMLLoader gameLoader = new FXMLLoader(MainApplication.class.getResource("/com/iti/tictactoe/name-offline-view.fxml"));
        StackPane root = new StackPane();
        //root.getChildren().add(splashLoader.load());
        //root.getChildren().add(mainLoader.load());
        root.getChildren().add(gameLoader.load());


        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setFullScreen(true);
        stage.show();

        // 3amlaly moshklla kan fl run 3shan el duration (need to handle it in your screen only )
    /*    PauseTransition pause = new PauseTransition(Duration.seconds(5));
        pause.setOnFinished(event -> root.getChildren().get(0).setVisible(false));
        pause.play();*/
    }

    public static void main(String[] args) {
        launch();
    }
}