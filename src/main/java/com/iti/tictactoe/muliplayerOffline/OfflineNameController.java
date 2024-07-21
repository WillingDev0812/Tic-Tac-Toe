package com.iti.tictactoe.muliplayerOffline;

import com.iti.tictactoe.muliplayerOffline.models.AlertUtils;
import com.iti.tictactoe.muliplayerOffline.models.PlayerNames;
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

public class OfflineNameController {

    @FXML
    ImageView backgroundImage;
    @FXML
    private TextField playerOne_txtField;
    @FXML
    private TextField playerTwo_txtField;
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

    public void handlePlayNowButton(ActionEvent actionEvent) {
        String playerOne = playerOne_txtField.getText();
        String playerTwo = playerTwo_txtField.getText();
        if (!validatePlayerNames(playerOne, playerTwo)) {
            return;
        }
        PlayerNames playerNames = new PlayerNames(playerOne, playerTwo);
        playButtonSound();
        System.out.println(playerOne_txtField.getText());
        System.out.println(playerTwo_txtField.getText());

        // to switch from page to page
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/iti/tictactoe/board-game-view.fxml"));
            Parent root = loader.load();
            GameBoardController gameBoardController = loader.getController();
            gameBoardController.initialize(playerNames);

            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            Scene newScene = new Scene(root);

            stage.setScene(newScene);
            stage.setFullScreen(true);  // Ensure the stage is in full screen mode
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            // Optionally show an alert or log the error
        }
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

    private boolean validatePlayerNames(String playerOne, String playerTwo) {
        //checking textfield empty or not
        if (playerOne_txtField.getText().isEmpty() || playerTwo_txtField.getText().isEmpty()) {
            AlertUtils.showWarningAlert("Check", "Write Players Names", null);
            return false;
        }

        // checking text field starting with number or not
        if (playerOne.matches("^[0-9].*") || playerTwo.matches("^[0-9].*")) {
            AlertUtils.showWarningAlert("Invalid Names", "Player names cannot start with a number.", null);
            return false;
        }


        // Checking both names are different
      /*  if (playerOne.equalsIgnoreCase(playerTwo)) {
            AlertUtils.showWarningAlert("Invalid Names", "Players names must be different.", null);
            return false;
        }*/

        return true;
    }
}



