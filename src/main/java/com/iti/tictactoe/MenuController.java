package com.iti.tictactoe;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class MenuController {
    private Stage primaryStage;

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

    @FXML
    private void onBack() {
        System.exit(0);
    }

    @FXML
    public void initialize() {

        UIUtils.addHoverAnimation(onlineButt);
        UIUtils.addHoverAnimation(offlineButt);
        UIUtils.addHoverAnimation(recordsButt);
        onlineButt.setOnMouseClicked(event -> {
            UIUtils.playSoundEffect();
        });
        offlineButt.setOnMouseClicked(event -> UIUtils.playSoundEffect());
        recordsButt.setOnMouseClicked(event -> UIUtils.playSoundEffect());
        backLabel.setOnMouseClicked(event -> handleBackImageClick(event));
        backImage.setOnMouseClicked(event -> handleBackImageClick(event));

        offlineButt.setOnAction(event -> {
            try {
                UIUtils.playSoundEffect();
                FXMLLoader fxmlLoader = new FXMLLoader(MenuController.class.getResource("offline-view.fxml"));
                Scene scene = new Scene(fxmlLoader.load(), 2000, 1000);
                Stage stage = new Stage();
                stage.setTitle("Offline Mode");
                stage.setTitle("Neon TIC-TAC-TOE");
                stage.setFullScreen(true);
                stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH); // Disable ESC to exit full-screen
                stage.setResizable(false);
                stage.setScene(scene);
                stage.show();

                Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                currentStage.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

    }

    private void handleBackImageClick(MouseEvent event) {
        UIUtils.playSoundEffect();
        System.exit(0);
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }
}
