package com.iti.tictactoe.AIGame;

import com.iti.tictactoe.Single.NameOfUser;
import com.iti.tictactoe.muliplayerOffline.models.UiUtils;
import com.iti.tictactoe.navigation.NavigationController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;

public class SinglePlayerController {
    @FXML
    private Button easyButt;
    @FXML
    private Button medButt;
    @FXML
    private Button hardButt;

    private NavigationController navController;

    public static int flag = 0; ///1 FOR EASY - 2 FOR MED - 3 FOR HARD

    public void initialize() {
        UiUtils.addHoverAnimation(easyButt);
        UiUtils.addHoverAnimation(medButt);
        UiUtils.addHoverAnimation(hardButt);
    }

    @FXML
    private void handleEasy(javafx.scene.input.MouseEvent event) {
        UiUtils.playSoundEffect();
        flag = 1;
        navigateToGameLevelMenu();

    }

    @FXML
    private void handleMedium(javafx.scene.input.MouseEvent event) {
        UiUtils.playSoundEffect();
        flag = 2;
        navigateToGameLevelMenu();
    }

    @FXML
    private void handleHard(javafx.scene.input.MouseEvent event) {
        UiUtils.playSoundEffect();
        flag = 3;
        navigateToGameLevelMenu();
    }

    private void navigateToGameLevelMenu() {
        navController.pushScene("/com/iti/tictactoe/name-of-user.fxml", controller -> {
            if (controller instanceof NameOfUser nameOfUser) {
                nameOfUser.setNavController(navController);
            }

        });
    }

    public void onBackClick(MouseEvent mouseEvent) {
        if (navController == null) {
            System.out.println("error in navController");
        } else {
            UiUtils.playSoundEffect();
            navController.popScene();
        }
    }

    public void setNavController(NavigationController navController) {
        this.navController = navController;
    }
}
