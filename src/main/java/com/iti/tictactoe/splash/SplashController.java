package com.iti.tictactoe.splash;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class SplashController {

    @FXML
    private ImageView splashImage;

    @FXML
    public void initialize() {
        try {
            Image image = new Image(getClass().getResource("/com/iti/tictactoe/assets/Splash.png").toExternalForm());
            splashImage.setImage(image);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
