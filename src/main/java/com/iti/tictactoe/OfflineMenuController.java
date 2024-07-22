package com.iti.tictactoe;

import com.iti.tictactoe.AIGame.SinglePlayerMenuController;
import com.iti.tictactoe.models.UiUtils;
import com.iti.tictactoe.muliplayerSingleOffline.NameController;
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
            if (controller instanceof SinglePlayerMenuController singlePlayerMenuController) {
                singlePlayerMenuController.setNavController(navController);
            }
        });
    }

    public void multiPlayerClickedButton(MouseEvent mouseEvent) {
        UiUtils.playSoundEffect();
        navController.pushScene("/com/iti/tictactoe/name-offline-view.fxml", controller -> {
            if (controller instanceof NameController multiPlayerOfflineNameController) {
                multiPlayerOfflineNameController.setNavController(navController);
            }
        });
    }

    @FXML
    public void onBackClicked(MouseEvent mouseEvent) {
        if (navController == null) {
            System.out.println("Error with navController");
        } else {
            UiUtils.playSoundEffect();
            navController.popScene();
        }
    }

    public void setNavController(NavigationController navController) {
        this.navController = navController;
    }
}
