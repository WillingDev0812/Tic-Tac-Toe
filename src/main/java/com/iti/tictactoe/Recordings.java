package com.iti.tictactoe;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;


public class Recordings {
    @FXML
    private ImageView imageView;

    public void onplaybtn(ActionEvent actionEvent) {
    }

    public void onquitbtn(ActionEvent actionEvent) {
        Platform.exit();
    }
}
