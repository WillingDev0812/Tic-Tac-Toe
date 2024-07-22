package com.iti.tictactoe;

import com.iti.tictactoe.models.UiUtils;
import com.iti.tictactoe.navigation.NavigationController;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class HomeScreenController {
    @FXML
    private Button startPlayingButton;
    @FXML
    private Button quitButton;
    private NavigationController navController;

    @FXML
    private void initialize() {
        UiUtils.addHoverAnimation(startPlayingButton);
        UiUtils.addHoverAnimation(quitButton);
    }

    @FXML
    protected void onQuitButton() {
        UiUtils.playSoundEffect();
        Platform.exit();
    }

    @FXML
    protected void onPlayButton() {
        UiUtils.playSoundEffect();
        showMenu();
        System.out.println("play");
    }

    private void showMenu() {
        if (navController == null) {
            System.out.println("error in navController \n");
        } else {
            navController.pushScene("/com/iti/tictactoe/menu-view.fxml", controller -> {
                if (controller instanceof FullMenuController fullMenuController) {
                    fullMenuController.setNavController(navController);
                }
            });
        }
    }

    public void setNavController(NavigationController navController) {
        this.navController = navController;
    }
}
