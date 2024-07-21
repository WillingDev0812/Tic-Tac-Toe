package com.iti.tictactoe;

import com.iti.tictactoe.muliplayerOffline.models.UiUtils;
import com.iti.tictactoe.navigation.NavigationController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;

import java.util.List;


public class RecordingsController {
    List<String> files;
    @FXML
    private Button deleteRecordingButton;
    @FXML
    private Button playRecordingButton;
    private NavigationController navController;


    public void initialize() {
        /*try {
            var files = Files.list(Path.of("src/main/resources/com/iti/tictactoe/RecordingsController")).map(Path::toString).toList();
            for (var file : files) {
                System.out.println(file);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }*/
        //  imageView.setOnMouseClicked(this::goBack);

        UiUtils.addHoverAnimation(deleteRecordingButton);
        UiUtils.addHoverAnimation(playRecordingButton);
    }

    public void onBackImageClick(MouseEvent mouseEvent) {
        if (navController == null) {
            System.out.println("error in navController");
        } else {
            UiUtils.playSoundEffect();
            navController.popScene();
        }
    }

    public void onPlayRecordingButton(ActionEvent actionEvent) {
        // implements later ...
    }

    public void onDeleteRecordingButton(ActionEvent actionEvent) {
        // Platform.exit();
    }

    public void setNavController(NavigationController navController) {
        this.navController = navController;
    }


}

