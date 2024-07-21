package com.iti.tictactoe;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;

import java.io.IOException;

public class OfflineController {

    @FXML
    private ImageView backImage;

    @FXML
    private Button singleButt;

    @FXML
    private Button multiButt;

    @FXML
    private Label backLabel;

    @FXML
    public void initialize() {
        UIUtils.addHoverAnimation(singleButt);
        UIUtils.addHoverAnimation(multiButt);
        multiButt.setOnMouseClicked(event -> {
            UIUtils.playSoundEffect();
            offlineController();
        });
        backImage.setOnMouseClicked(this::handleBackImageClick);
        backLabel.setOnMouseClicked(this::handleBackImageClick);
        singleButt.setOnAction(event -> UIUtils.playSoundEffect());
    }

    private void offlineController() {
        try {
            UIUtils.playSoundEffect();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("name-offline-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = new Stage();
            stage.setFullScreen(true);
            stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH); // Disable ESC to exit full-screen
            stage.setResizable(false);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleBackImageClick(MouseEvent event) {
        try {
            UIUtils.playSoundEffect();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("menu-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = new Stage();
            stage.setTitle("Menu");
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
