package com.iti.tictactoe;

import com.iti.tictactoe.models.UiUtils;
import com.iti.tictactoe.navigation.NavigationController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListOfUsers {
    private NavigationController navController;

    @FXML
    private Label backLabel;

    @FXML
    private Button inviteBtn;

    @FXML
    private ListView<?> PlayerList;

    @FXML
    private Label invitePlayerWarning;

    @FXML
    private Text playerName;

    List allFilesNames;
    public void initialize(){
        try {
            allFilesNames = Files.list(Path.of("src/main/resources/com/iti/tictactoe/Recordings")).map(Path::toString).toList();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        PlayerList.getItems().addAll(allFilesNames);

         System.out.println(PlayerList.getSelectionModel().getSelectedItem());
        UiUtils.addHoverAnimation(inviteBtn);
    }




    public void onBackImageClick(MouseEvent mouseEvent) {
        if (navController == null) {
            System.out.println("error in navController");
        } else {
            UiUtils.playSoundEffect();
            navController.popScene();
        }
    }

    public void inviteBtn(ActionEvent actionEvent) {

    }
    public void setNavController(NavigationController navController) {
        this.navController = navController;
    }

}
