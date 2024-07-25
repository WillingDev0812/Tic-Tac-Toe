package com.iti.tictactoe;

import com.iti.tictactoe.auth.LoginScreen;
import com.iti.tictactoe.models.UiUtils;
import com.iti.tictactoe.navigation.NavigationController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;

public class FullMenuController {
    @FXML
    private Button onlineButt;

    @FXML
    private Button offlineButt;

    @FXML
    private Button recordsButt;


    public NavigationController navController;

    @FXML
    public void initialize() {
        UiUtils.addHoverAnimation(onlineButt);
        UiUtils.addHoverAnimation(offlineButt);
        UiUtils.addHoverAnimation(recordsButt);
    }

    public void handleOnOnlineButtonClicked(MouseEvent mouseEvent) {
        UiUtils.playSoundEffect();
        if (navController == null) {
            System.out.println("error in navController \n");
        } else {
            navController.pushScene("/com/iti/tictactoe/login-screen.fxml", controller -> {
                if (controller instanceof LoginScreen) {
                    LoginScreen r = (LoginScreen) controller;
                    r.setNavController(navController);
                }
            });
        }
    }

    public void handleOnRecordButtonClicked(MouseEvent mouseEvent) {
        UiUtils.playSoundEffect();
        if (navController != null) {
            navController.pushScene("/com/iti/tictactoe/recordings.fxml", controller -> {
                if (controller instanceof RecordingsController r) {
                    r.setNavController(navController);
                }
            });
        }
    }

    @FXML
    public void handleBackClick(MouseEvent event) {
        if (navController == null) {
            System.out.println("error in navController");
        } else {
            UiUtils.playSoundEffect();
            navController.popScene();
        }
    }


    // elly hy'rab lel zorar dh hafshakhoo
    public void handleOfflineButtonClicked() {
        UiUtils.playSoundEffect();
        if (navController == null) {
            System.out.println("error in navController \n");
        } else {
            navController.pushScene("/com/iti/tictactoe/offline-view.fxml", controller -> {
                if (controller instanceof OfflineMenuController) {
                    OfflineMenuController offlineMenuController = (OfflineMenuController) controller;
                    offlineMenuController.setNavController(navController);
                }
            });
        }
    }

    public void setNavController(NavigationController navController) {
        this.navController = navController;
    }


}