package com.iti.tictactoe;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;


public class Recordings {
    @FXML
    public ImageView imageView;
    @FXML
    private Button deleteRecordingbtn;
    @FXML
    private Button playRecordingbtn;
    List<String> files;

    public void initialize(){
        try {
            var files = Files.list(Path.of("src/main/resources/com/iti/tictactoe/Recordings")).map(Path::toString).toList();
            for (var file : files) {
                System.out.println(file);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //  imageView.setOnMouseClicked(this::goBack);
        UIUtils.addHoverAnimation(deleteRecordingbtn);
        UIUtils.addHoverAnimation(playRecordingbtn);
    }


    public void onBackImageClick(MouseEvent mouseEvent) {
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


    public void onplayrecordingbtn(ActionEvent actionEvent) {

    }

    public void ondeleterecordingbtn(ActionEvent actionEvent) {
       // Platform.exit();
    }


//    public  void goBack(MouseEvent event) {
//        try {
//            UIUtils.playSoundEffect();
//            FXMLLoader fxmlLoader = new FXMLLoader(MenuController.class.getResource("menu-view.fxml"));
//            Scene scene = new Scene(fxmlLoader.load());
//            Stage stage = new Stage();
//            stage.setFullScreen(true);
//            stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH); // Disable ESC to exit full-screen
//            stage.setResizable(false);
//            stage.setScene(scene);
//            stage.show();
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }

}

