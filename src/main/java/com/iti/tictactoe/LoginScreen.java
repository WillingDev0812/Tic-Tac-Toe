package com.iti.tictactoe;

import com.iti.tictactoe.muliplayerOffline.models.UiUtils;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

public class LoginScreen {

    @FXML
    private Button loginButton;
    @FXML
    private Button signUpButton;
    @FXML
    private TextField emailTextField;
    @FXML
    private TextField passwordTextField ;
       @FXML
   private Label warningTextlabel;


    @FXML
    private void initialize() {
        UiUtils.addHoverAnimation(loginButton);
        UiUtils.addHoverAnimation(signUpButton);
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
        // implements late ...
    }
}
