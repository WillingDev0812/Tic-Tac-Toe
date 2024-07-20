package com.iti.tictactoe;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;


public class Recordings {
    @FXML
    public ImageView imageView;


    public void initialize(){
        imageView.setOnMouseClicked(this::goBack);
    }

    public void onplaybtn(ActionEvent actionEvent) {
    }

    public void onquitbtn(ActionEvent actionEvent) {
        Platform.exit();
    }

    public  void goBack(MouseEvent event) {
        try {
            UIUtils.playSoundEffect();
            FXMLLoader fxmlLoader = new FXMLLoader(MenuController.class.getResource("menu-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = new Stage();
            stage.setFullScreen(true);
            stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH); // Disable ESC to exit full-screen
            stage.setResizable(false);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}

