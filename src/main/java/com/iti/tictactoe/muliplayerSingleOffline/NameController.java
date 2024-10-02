package com.iti.tictactoe.muliplayerSingleOffline;

import com.iti.tictactoe.models.AlertUtils;
import com.iti.tictactoe.models.PlayerNames;
import com.iti.tictactoe.models.UiUtils;
import com.iti.tictactoe.navigation.NavigationController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

import static com.iti.tictactoe.AIGame.SinglePlayerMenuController.flag;

public class NameController {

    @FXML
    private ImageView backgroundImage;
    @FXML
    private TextField playerOne_txtField;
    @FXML
    private TextField playerTwo_txtField;
    @FXML
    private VBox playerTwoContainer; // Container holding playerTwo elements

    private NavigationController navController;
    private boolean isSinglePlayerMode;
    private int difficultyLevel;

    @FXML
    public void initialize() {
        try {
            Image image = new Image(getClass().getResource("/com/iti/tictactoe/assets/HomeBackground.png").toExternalForm());
            backgroundImage.setImage(image);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("playerTwoContainer is working Vbox ");
        updateUIForGameMode();
    }


    @FXML
    public void handlePlayNowButton(ActionEvent actionEvent) {
        String playerOne = playerOne_txtField.getText();
        String playerTwo = isSinglePlayerMode ? "Computer" : playerTwo_txtField.getText();
        if (!validatePlayerNames(playerOne, playerTwo)) {
            return;
        }
        PlayerNames playerNames = new PlayerNames(playerOne, playerTwo);
        UiUtils.playSoundEffect();
        if (isSinglePlayerMode) {
            navController.pushScene("/com/iti/tictactoe/board-game-view.fxml", controller -> {
                if (controller instanceof GameBoardController gameBoardController) {
                    gameBoardController.setNavController(navController);
                    gameBoardController.initialize(playerNames, true, difficultyLevel,0,0);
                }
            });
        } else {
            navController.pushScene("/com/iti/tictactoe/board-game-view.fxml", controller -> {
                if (controller instanceof GameBoardController gameBoardController) {
                    gameBoardController.setNavController(navController);
                    gameBoardController.initialize(playerNames, false, flag,0,0);
                }
            });
        }
    }

    private boolean validatePlayerNames(String playerOne, String playerTwo) {
        if (playerOne.isEmpty() || (!isSinglePlayerMode && playerTwo.isEmpty())) {
            AlertUtils.showWarningAlert("Check", "Write Players' Names", null);
            return false;
        }

        if (playerOne.matches("^[0-9].*") || (!isSinglePlayerMode && playerTwo.matches("^[0-9].*"))) {
            AlertUtils.showWarningAlert("Invalid Names", "Player names cannot start with a number.", null);
            return false;
        }

        return true;
    }

    public void setNavController(NavigationController navController) {
        this.navController = navController;
    }

    public void setSinglePlayerMode(boolean isSinglePlayerMode, int difficultyLevel) {
        this.isSinglePlayerMode = isSinglePlayerMode;
        this.difficultyLevel = difficultyLevel;
        updateUIForGameMode(); // Update UI based on the mode
    }

    private void updateUIForGameMode() {
        if (isSinglePlayerMode) {
            playerTwoContainer.setVisible(false); // Hide Player Two UI elements
        } else {
            playerTwoContainer.setVisible(true); // Show Player Two UI elements
        }
    }

    @FXML
    public void onBackClick(MouseEvent mouseEvent) {
        if (navController == null) {
            System.out.println("Error: navController is not set.");
        } else {
            UiUtils.playSoundEffect();
            navController.popScene();
        }
    }
}
