package com.iti.tictactoe.AIGame;

import com.iti.tictactoe.Single.ComputerGameBoard;
import com.iti.tictactoe.UIUtils;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;

public class SinglePlayerController {
    @FXML
    private Button easyButt;

    @FXML
    private Button medButt;

    @FXML
    private Button hardButt;

    @FXML
    private ImageView backImage;

    @FXML
    private Label backLabel;

    public static int flag = 0; ///1 FOR EASY - 2 FOR MED - 3 FOR HARD

    public void initialize() {
        UIUtils.addHoverAnimation(easyButt);
        UIUtils.addHoverAnimation(medButt);
        UIUtils.addHoverAnimation(hardButt);
        easyButt.setOnMouseClicked(this::handleEasy);
        medButt.setOnMouseClicked(this::handleMedium);
        hardButt.setOnMouseClicked(this::handleHard);
        backImage.setOnMouseClicked(this::handleBackImageClick);
        backLabel.setOnMouseClicked(this::handleBackImageClick);
    }

    private void handleEasy(javafx.scene.input.MouseEvent event) {
        UIUtils.playSoundEffect();
        flag = 1;
        navigateTONamePage(event);
    }

    private void handleMedium(javafx.scene.input.MouseEvent event) {
        UIUtils.playSoundEffect();
        flag = 2;
        navigateTONamePage(event);
    }

    private void handleHard(javafx.scene.input.MouseEvent event) {
        UIUtils.playSoundEffect();
        flag = 3;
        navigateTONamePage(event);
    }

    private void navigateTONamePage(javafx.scene.input.MouseEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/iti/tictactoe/NameOfUser.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = new Stage();
            stage.setTitle("Offline Mode");
            stage.setFullScreen(true);
            stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH); // Disable ESC to exit full-screen
            stage.setResizable(false);
            stage.setScene(scene);
            stage.show();

            // Close the current stage
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleBackImageClick(javafx.scene.input.MouseEvent event) {
        try {
            UIUtils.playSoundEffect();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/iti/tictactoe/offline-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = new Stage();
            stage.setTitle("Offline Mode");
            stage.setFullScreen(true);
            stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH); // Disable ESC to exit full-screen
            stage.setResizable(false);
            stage.setScene(scene);
            stage.show();

            // Close the current stage
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
