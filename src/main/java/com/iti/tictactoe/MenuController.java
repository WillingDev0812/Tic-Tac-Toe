package com.iti.tictactoe;

import com.iti.tictactoe.navigation.NavigationController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

public class MenuController {
    @FXML
    private Button onlineButt;

    @FXML
    private Button offlineButt;

    @FXML
    private Button recordsButt;

    @FXML
    private ImageView backImage;

    @FXML
    private Label backLabel;

    public NavigationController navController;

    @FXML
    public void initialize() {
        UIUtils.addHoverAnimation(onlineButt);
        UIUtils.addHoverAnimation(offlineButt);
        UIUtils.addHoverAnimation(recordsButt);

        onlineButt.setOnMouseClicked(event -> {
            UIUtils.playSoundEffect();
            //implements later
        });

        recordsButt.setOnMouseClicked(event -> {
            UIUtils.playSoundEffect();
            if (navController == null) {
                System.out.println("error in navController \n");
            } else {
                navController.pushScene("/com/iti/tictactoe/Recordings.fxml", controller -> {
                });
            }

        });

        offlineButt.setOnMouseClicked(event -> handleOfflineButton());
        backLabel.setOnMouseClicked(this::handleBackImageClick);
        backImage.setOnMouseClicked(this::handleBackImageClick);

    }

    private void handleBackImageClick(MouseEvent event) {
        if (navController == null) {
            System.out.println("error in navController");
        } else {
            UIUtils.playSoundEffect();
            navController.popScene();
        }
    }


    // elly hy'rab lel zorar dh hafshakhoo
    public void handleOfflineButton() {
        UIUtils.playSoundEffect();
        if (navController == null) {
            System.out.println("error in navController \n");
        } else {
            navController.pushScene("/com/iti/tictactoe/offline-view.fxml", controller -> {
                if (controller instanceof OfflineController) {
                    OfflineController offlineController = (OfflineController) controller;
                    offlineController.setNavController(navController);
                }
            });
        }
    }

    public void setNavController(NavigationController navController) {
        this.navController = navController;
    }

}
