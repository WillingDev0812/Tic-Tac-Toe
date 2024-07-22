package com.iti.tictactoe.AIGame;

import com.iti.tictactoe.models.UiUtils;
import com.iti.tictactoe.muliplayerSingleOffline.NameController;
import com.iti.tictactoe.navigation.NavigationController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;

public class SinglePlayerMenuController {
    @FXML
    private Button easyButt;
    @FXML
    private Button medButt;
    @FXML
    private Button hardButt;

    private NavigationController navController;

    public static int flag = 0; // 1 for easy, 2 for medium, 3 for hard

    public void initialize() {
        UiUtils.addHoverAnimation(easyButt);
        UiUtils.addHoverAnimation(medButt);
        UiUtils.addHoverAnimation(hardButt);
    }

    @FXML
    private void handleEasy(MouseEvent event) {
        UiUtils.playSoundEffect();
        flag = 1;
        navigateToGameLevelMenu();
    }

    @FXML
    private void handleMedium(MouseEvent event) {
        UiUtils.playSoundEffect();
        flag = 2;
        navigateToGameLevelMenu();
    }

    @FXML
    private void handleHard(MouseEvent event) {
        UiUtils.playSoundEffect();
        flag = 3;
        navigateToGameLevelMenu();
    }

    private void navigateToGameLevelMenu() {
        navController.pushScene("/com/iti/tictactoe/name-offline-view.fxml", controller -> {
            if (controller instanceof NameController nameController) {
                nameController.setNavController(navController);
                nameController.setSinglePlayerMode(true, flag); // Pass both parameters
            }
        });
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

    public void setNavController(NavigationController navController) {
        this.navController = navController;
    }
}
