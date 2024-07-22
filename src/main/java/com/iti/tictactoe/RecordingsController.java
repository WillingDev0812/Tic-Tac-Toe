package com.iti.tictactoe;

import com.iti.tictactoe.muliplayerOffline.models.UiUtils;
import com.iti.tictactoe.navigation.NavigationController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class RecordingsController {
    List<String> files;
    @FXML
    private Button deleteRecordingButton;
    @FXML
    private Button playRecordingButton;
    @FXML
    private Label DeleteRecordWarning;
    @FXML
    private ListView<String> RecordingListview;

    private NavigationController navController;

    List<String> allFilesNames;
    Map<String, String> locationNameMap =  new HashMap<>();
    public void initialize(){
       /* try {
            allFilesNames = Files.list(Path.of("src/main/resources/com/iti/tictactoe/Recordings")).map(Path::toString).toList();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }*/


        /*for (var file : allFilesNames) {
            locationNameMap.put("Recording game " + file.substring(file.lastIndexOf(System.getProperty("file.separator") ) + 1 , file.lastIndexOf('.') ),file);
        }*/
       /* RecordingListview.getItems().addAll(locationNameMap.keySet());*/
        // RecordingListview.getSelectionModel().getSelectedItem();
        // System.out.println(RecordingListview.getSelectionModel().getSelectedItem());
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
        String g= locationNameMap.get(RecordingListview.getSelectionModel().getSelectedItem());
        System.out.println(g);
    }

    public void onDeleteRecordingButton(ActionEvent actionEvent) {
        //  Platform.exit();
        try {
            if(RecordingListview.getSelectionModel().getSelectedItem() == null){
                DeleteRecordWarning.setOpacity(1.0);    //leh md5l4 3la catch???????
                return;
            }
            String g= locationNameMap.get(RecordingListview.getSelectionModel().getSelectedItem());
            Files.delete(Path.of(g));
            RecordingListview.getItems().remove(RecordingListview.getSelectionModel().getSelectedItem());

        } catch (IOException e) {
            // System.out.println("Error");
            // DeleteRecordWarning.setOpacity(1.0);
            throw new RuntimeException(e);
        }
    }

    public void setNavController(NavigationController navController) {
        this.navController = navController;
    }


}

