package com.iti.tictactoe;

import com.iti.tictactoe.navigation.NavigationController;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.util.Duration;

public class HomeScreen {


    @FXML
    private Button startplayingbtn;
    @FXML
    private Button quitbtn;


    private NavigationController navController;

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

    private void showMenu() {
        if (navController == null) {
            System.out.println("error in navController \n");
        } else {
            navController.pushScene("/com/iti/tictactoe/menu-view.fxml", controller -> {
                if (controller instanceof MenuController menuController) {
                    menuController.setNavController(navController);
                }
            });
        }
    }

    public void setNavController(NavigationController navController) {
        this.navController = navController;
    }
}
