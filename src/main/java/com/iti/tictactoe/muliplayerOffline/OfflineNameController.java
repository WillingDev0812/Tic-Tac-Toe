package com.iti.tictactoe.muliplayerOffline;

import com.iti.tictactoe.muliplayerOffline.models.AlertUtils;
import com.iti.tictactoe.muliplayerOffline.models.PlayerNames;
import com.iti.tictactoe.muliplayerOffline.models.UiUtils;
import com.iti.tictactoe.navigation.NavigationController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

public class OfflineNameController {
    @FXML
    ImageView backgroundImage;
    @FXML
    private TextField playerOne_txtField;
    @FXML
    private TextField playerTwo_txtField;

    private NavigationController navController;


    @FXML
    public void initialize() {
        try {
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
        UiUtils.playSoundEffect();

        System.out.println(playerOne_txtField.getText());
        System.out.println(playerTwo_txtField.getText());

        navController.pushScene("/com/iti/tictactoe/board-game-view.fxml", controller -> {
            GameBoardController gameBoardController = (GameBoardController) controller;
            gameBoardController.setNavController(navController);
            gameBoardController.initialize(playerNames);
        });
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

    public void setNavController(NavigationController navController) {
        this.navController = navController;
        System.out.println("NavigationController set in OfflineNameController");
    }

    @FXML
    public void onBackClick(MouseEvent mouseEvent) {
        System.out.println("Back button clicked");
        if (navController == null) {
            System.out.println("Error fl navController kal3ada");
        } else {
            UiUtils.playSoundEffect();
            navController.popScene();
        }
    }
}



