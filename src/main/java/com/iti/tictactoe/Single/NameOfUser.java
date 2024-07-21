package com.iti.tictactoe.Single;

import com.iti.tictactoe.muliplayerOffline.models.AlertUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.AudioClip;
import javafx.stage.Stage;

import java.io.IOException;


public class NameOfUser {
    @FXML
    private ImageView backgroundImage;

    @FXML
    private TextField playerOne_txtField;

    private AudioClip buttonEffectPlayNow;


    public void initialize() {
        try {
            buttonEffectPlayNow = new AudioClip(getClass().getResource("/com/iti/tictactoe/Sounds/buttonSoundEffect.wav").toExternalForm());
            Image image = new Image(getClass().getResource("/com/iti/tictactoe/assets/HomeBackground.png").toExternalForm());
            backgroundImage.setImage(image);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void handlePlayNowButton(ActionEvent event) {
        String playerOne = playerOne_txtField.getText();
        if (!validatePlayerNames(playerOne)) {
            return;
        }
        PlayerName playerName = new PlayerName(playerOne);
        playButtonSound();

        // Switch from the current page to the game board page
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/iti/tictactoe/ComputerGameBoard.fxml"));
            Parent root = loader.load();

            // Get the controller and initialize it with the player names
            ComputerGameBoard gameBoardController = loader.getController();
            gameBoardController.initialize(playerName);

            // Get the current stage and set the new scene
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene newScene = new Scene(root);

            stage.setScene(newScene);
            stage.setFullScreen(true);  // Ensure the stage is in full screen mode
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            // Optionally show an alert or log the error
        }
    }


    private boolean validatePlayerNames(String playerOne) {
        //checking textfield empty or not
        if (playerOne_txtField.getText().isEmpty()) {
            AlertUtils.showWarningAlert("Check", "Write Players Names", null);
            return false;
        }

        // checking text field starting with number or not
        if (playerOne.matches("^[0-9].*")) {
            AlertUtils.showWarningAlert("Invalid Names", "Player names cannot start with a number.", null);
            return false;
        }
        return true;
    }

    private void playButtonSound() {
        // Check if the sound is initialized and play it
        if (buttonEffectPlayNow != null) {
            System.out.println("Playing button sound");
            buttonEffectPlayNow.play();
        } else {
            System.out.println("Button sound is not initialized");
        }
    }
}
