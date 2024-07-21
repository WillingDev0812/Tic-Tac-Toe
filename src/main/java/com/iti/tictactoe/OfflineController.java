package com.iti.tictactoe;

import com.iti.tictactoe.Single.NameOfUser;
import com.iti.tictactoe.muliplayerOffline.OfflineNameController;
import com.iti.tictactoe.navigation.NavigationController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

public class OfflineController {

    @FXML
    private ImageView backImage;

    @FXML
    private Button singleButt;

    @FXML
    private Button multiButt;

    @FXML
    private Label backLabel;

    private NavigationController navController;

    @FXML
    public void initialize() {
        UIUtils.addHoverAnimation(singleButt);
        UIUtils.addHoverAnimation(multiButt);
        multiButt.setOnMouseClicked(event -> {
            UIUtils.playSoundEffect();
            multiPlayerOfflineModeButton();
        });
        backImage.setOnMouseClicked(this::handleBackImageClick);
        backLabel.setOnMouseClicked(this::handleBackImageClick);
        singleButt.setOnAction(event -> {
            singlePlayerModeButton();
        });
    }

    public void setNavController(NavigationController navController) {
        this.navController = navController;
    }

    private void singlePlayerModeButton() {
        navController.pushScene("/com/iti/tictactoe/NameOfUser.fxml", controller -> {
            if (controller instanceof NameOfUser) {
                NameOfUser nameOfUser = (NameOfUser) controller;
                nameOfUser.setNavController(navController);
            }
        });
    }

    private void multiPlayerOfflineModeButton() {
        navController.pushScene("/com/iti/tictactoe/name-offline-view.fxml", controller -> {
            if (controller instanceof OfflineNameController) {
                OfflineNameController offlineNameController = (OfflineNameController) controller;
                offlineNameController.setNavController(navController);
            }
        });

    }

    private void handleBackImageClick(MouseEvent event) {
        if (navController == null) {
            System.out.println("error in navController");
        } else {
            UIUtils.playSoundEffect();
            navController.popScene();
        }
    }
}
