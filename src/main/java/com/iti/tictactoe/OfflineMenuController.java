package com.iti.tictactoe;

import com.iti.tictactoe.AIGame.SinglePlayerController;
import com.iti.tictactoe.muliplayerOffline.OfflineNameController;
import com.iti.tictactoe.muliplayerOffline.models.UiUtils;
import com.iti.tictactoe.navigation.NavigationController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;

public class OfflineMenuController {
    @FXML
    private Button singleButt;

    @FXML
    private Button multiButt;

    private NavigationController navController;

    @FXML
    public void initialize() {
        UiUtils.addHoverAnimation(singleButt);
        UiUtils.addHoverAnimation(multiButt);
    }

    public void singlePlayerClickedButton(MouseEvent mouseEvent) {
        UiUtils.playSoundEffect();
        navController.pushScene("/com/iti/tictactoe/single-player-view.fxml", controller -> {
            if (controller instanceof SinglePlayerController singlePlayerController) {
                singlePlayerController.setNavController(navController);
            }
        });
    }

    public void multiPlayerClickedButton(MouseEvent mouseEvent) {
        UiUtils.playSoundEffect();
        navController.pushScene("/com/iti/tictactoe/name-offline-view.fxml", controller -> {
            if (controller instanceof OfflineNameController) {
                OfflineNameController offlineNameController = (OfflineNameController) controller;
                offlineNameController.setNavController(navController);
            }
        });
    }

    @FXML
    public void onBackClicked(MouseEvent mouseEvent) {
        if (navController == null) {
            System.out.println("error fl navController");
        } else {
            UiUtils.playSoundEffect();
            navController.popScene();
        }
    }

    public void setNavController(NavigationController navController) {
        this.navController = navController;
    }

}
