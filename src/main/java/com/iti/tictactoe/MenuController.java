package com.iti.tictactoe;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class MenuController {
    @FXML
    private Button onlineButt;

    @FXML
    private Button offlineButt;

    @FXML
    private Button recordsButt;

    @FXML
    private ImageView backImage;

    @FXML
    private Label backLabel;

    @FXML
    public void initialize() {

        UIUtils.addHoverAnimation(onlineButt);
        UIUtils.addHoverAnimation(offlineButt);
        UIUtils.addHoverAnimation(recordsButt);
        onlineButt.setOnMouseClicked(event -> {
            UIUtils.playSoundEffect();
        });
        offlineButt.setOnMouseClicked(event -> UIUtils.playSoundEffect());
        recordsButt.setOnMouseClicked(event -> {
            try {
                UIUtils.playSoundEffect();
                FXMLLoader fxmlLoader = new FXMLLoader(MenuController.class.getResource("Recordings.fxml"));
                Scene scene = new Scene(fxmlLoader.load());
                Stage stage = new Stage();
                stage.setFullScreen(true);
                stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH); // Disable ESC to exit full-screen
                stage.setResizable(false);
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        });
        backLabel.setOnMouseClicked(this::handleBackImageClick);
        backImage.setOnMouseClicked(this::handleBackImageClick);

        offlineButt.setOnAction(MenuController::handleOffline);
    }
    private void handleBackImageClick(MouseEvent event) {
        UIUtils.playSoundEffect();
        try {
            FXMLLoader menuScreen = new FXMLLoader(getClass().getResource("home screen.fxml"));
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

    private static void handleOffline(ActionEvent event) {
        try {
            UIUtils.playSoundEffect();
            FXMLLoader fxmlLoader = new FXMLLoader(MenuController.class.getResource("offline-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = new Stage();
            stage.setFullScreen(true);
            stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH); // Disable ESC to exit full-screen
            stage.setResizable(false);
            stage.setScene(scene);
            stage.show();

            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onBack() {
        Platform.exit();
    }


}
