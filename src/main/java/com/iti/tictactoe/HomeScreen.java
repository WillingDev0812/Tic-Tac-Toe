package com.iti.tictactoe;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;

public class HomeScreen {


    @FXML
    private Button startplayingbtn;
    @FXML
    private Button quitbtn;

    @FXML
    private void initialize() {
        addHoverAnimation(startplayingbtn);
        addHoverAnimation(quitbtn);
    }

    @FXML
    protected void onquitbtn() {
        Platform.exit();
    }
    @FXML
    protected void onplaybtn() {
        UIUtils.playSoundEffect();
        showMenu();
        System.out.println("play");
    }
    private void addHoverAnimation(Button button) {
        button.setOnMouseEntered(e -> {
            Timeline timeline = new Timeline(
                    new KeyFrame(Duration.ZERO, new KeyValue(button.translateXProperty(), 0)),
                    new KeyFrame(Duration.seconds(0.2), new KeyValue(button.translateXProperty(), 10)),
                    new KeyFrame(Duration.seconds(0.4), new KeyValue(button.translateXProperty(), 0))
            );
           timeline.setCycleCount(Timeline.INDEFINITE);
           timeline.setAutoReverse(true);
           timeline.play();
            button.setUserData(timeline); // Store timeline in button's user data
        });

        button.setOnMouseExited(e -> {
            Timeline timeline = (Timeline) button.getUserData();
            if (timeline != null) {
                timeline.stop();
                button.setTranslateX(0); // Reset position
            }
        });
    }

    private  void showMenu() {
        try {
            FXMLLoader menuScreen = new FXMLLoader(getClass().getResource("menu-view.fxml"));
            Scene scene = new Scene(menuScreen.load());
            Stage stage = new Stage();
            stage.setFullScreen(true);
            stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH); // Disable ESC to exit full-screen
            stage.setResizable(false);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }
}
