package com.iti.tictactoe.Single;

import com.iti.tictactoe.muliplayerOffline.models.AlertUtils;
import com.iti.tictactoe.muliplayerOffline.models.UiUtils;
import com.iti.tictactoe.navigation.NavigationController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;


public class NameOfUser {
    @FXML
    private ImageView backgroundImage;
    @FXML
    private TextField playerOne_txtField;

    private NavigationController navController;

    public void initialize() {
        try {
            Image image = new Image(getClass().getResource("/com/iti/tictactoe/assets/HomeBackground.png").toExternalForm());
            backgroundImage.setImage(image);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setNavController(NavigationController navController) {
        this.navController = navController;
    }

    public void handlePlayNowButton(ActionEvent event) {
        String playerOne = playerOne_txtField.getText();
        if (!validatePlayerNames(playerOne)) {
            return;
        }
        PlayerName playerName = new PlayerName(playerOne);
        UiUtils.playSoundEffect();
        navController.pushScene("/com/iti/tictactoe/board-game-computer.fxml", controller -> {
            ComputerGameBoard gameBoardController = (ComputerGameBoard) controller;
            gameBoardController.setNavController(navController);
            gameBoardController.initialize(playerName);
        });
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


    public void onBackClick(MouseEvent mouseEvent) {
        if (navController == null) {
            System.out.println("error in navController");
        } else {
            UiUtils.playSoundEffect();
            navController.popScene();
        }
    }
}
