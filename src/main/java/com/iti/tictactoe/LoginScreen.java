package com.iti.tictactoe;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;
import org.w3c.dom.Text;

public class LoginScreen {

    @FXML
    private Button loginbtn;
    @FXML
    private Button signupbtn;
    @FXML
    private TextField emailTextField;
    @FXML
    private TextField passwordTextField ;
       @FXML
   private Label warningTextlabel;


    @FXML
    private void initialize() {
        addHoverAnimation(loginbtn);
        addHoverAnimation(signupbtn);
    }
    private void addHoverAnimation(Button button) {
        button.setOnMouseEntered(e -> {
            Timeline timeline = new Timeline(
                    new KeyFrame(Duration.ZERO, new KeyValue(button.translateXProperty(), 0)),
                    new KeyFrame(Duration.seconds(0.2), new KeyValue(button.translateXProperty(), 10)),
                    new KeyFrame(Duration.seconds(0.4), new KeyValue(button.translateXProperty(), 0))
            );
            timeline.setCycleCount(Timeline.INDEFINITE);
            timeline.setAutoReverse(true);
            timeline.play();
            button.setUserData(timeline); // Store timeline in button's user data
        });

        button.setOnMouseExited(e -> {
            Timeline timeline = (Timeline) button.getUserData();
            if (timeline != null) {
                timeline.stop();
                button.setTranslateX(0); // Reset position
            }
        });
    }
    
    public void onBackImageClick(MouseEvent mouseEvent) {
        Platform.exit();
    }

    public void onLoginBtn(ActionEvent actionEvent) {
       if(emailTextField.getText().isEmpty() || passwordTextField.getText().isEmpty()) {
           //  emailTextField.borderProperty();
           warningTextlabel.setOpacity(1.0);
       }
        System.out.println(emailTextField.getText());
        System.out.println(passwordTextField.getText());
    }

    public void onSignupBtn(ActionEvent actionEvent) {
    }
}
