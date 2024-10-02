package com.iti.tictactoe;

import com.iti.tictactoe.models.PlayerNames;
import com.iti.tictactoe.models.UiUtils;
import com.iti.tictactoe.navigation.NavigationController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;


public class RecordingsController {
    List<String> files;
    @FXML
    private Button deleteRecordingButton;
    @FXML
    private Button playRecordingButton;
    @FXML
    private Label DeleteRecordWarning;
    @FXML
    private Label PlayRecordWarning;
    @FXML
    private Label EmptyFileWarning;
    @FXML
    private ListView<String> RecordingListview;

    private NavigationController navController;

    List<String> allFilesNames;
    Map<String, String> locationNameMap =  new TreeMap<>();
    public void initialize(){
        try {
            allFilesNames = Files.list(Path.of("src/main/resources/com/iti/tictactoe/Recordings")).map(Path::toString).toList();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        for (var file : allFilesNames) {
            locationNameMap.put("Recording game " + file.substring(file.lastIndexOf(System.getProperty("file.separator") ) + 1 , file.lastIndexOf('.') ),file);
        }
        RecordingListview.getItems().addAll(locationNameMap.keySet());
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
        String fileLoc = locationNameMap.get(RecordingListview.getSelectionModel().getSelectedItem());
        UiUtils.playSoundEffect();

        if (fileLoc == null) {
            DeleteRecordWarning.setOpacity(0.0);
            PlayRecordWarning.setOpacity(1.0);
            EmptyFileWarning.setOpacity(0.0);
            System.out.println("Please select a path");
        } else {
            File file = new File(fileLoc);
            if (file.length() == 0) {
                // Handle empty file case
                DeleteRecordWarning.setOpacity(0.0);
                PlayRecordWarning.setOpacity(0.0);
                EmptyFileWarning.setOpacity(1.0);
                System.out.println("Selected file is empty");
            } else {
                PlayerNames playerNames = readPlayerNamesFromFile(fileLoc);
                List<int[]> moves = readMovesFromFile(fileLoc);
                navController.pushScene("/com/iti/tictactoe/recording-game-view.fxml", controller -> {
                    RecordingViewController recordingViewController = (RecordingViewController) controller;
                    recordingViewController.setNavController(navController);
                    recordingViewController.initialize(playerNames, moves);
                });
            }
        }
    }

    private List<int[]> readMovesFromFile(String filePath) {
        try {
            List<String> lines = Files.readAllLines(Path.of(filePath));
            return lines.stream()
                    .skip(1) // Skip the first line with player names
                    .map(line -> line.split(","))
                    .map(parts -> new int[]{Integer.parseInt(parts[0]), Integer.parseInt(parts[1])})
                    .toList(); ////////////////////LIST OF ARRAY OF INTEGERS ////// List<int[]> moves = List.of( new int[]{1, 1} new int[]{0, 1} >>>....)
        } catch (IOException e) {
            throw new RuntimeException("Error reading moves from file", e);
        }
    }

    private PlayerNames readPlayerNamesFromFile(String filePath) {
        try {
            List<String> lines = Files.readAllLines(Path.of(filePath));
            if (lines.size() >= 1) {
                String[] names = lines.get(0).split(",");
                if (names.length == 2) {
                    return new PlayerNames(names[0], names[1]);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Error reading player names from file", e);
        }
        return new PlayerNames("Player 1", "Player 2"); // Default names if reading fails
    }

    public void onDeleteRecordingButton(ActionEvent actionEvent) {
        UiUtils.playSoundEffect();
        try {
            if(RecordingListview.getSelectionModel().getSelectedItem() == null){
                PlayRecordWarning.setOpacity(0.0);
                EmptyFileWarning.setOpacity(0.0);
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