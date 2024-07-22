package com.iti.tictactoe;

import com.iti.tictactoe.muliplayerOffline.models.UiUtils;
import com.iti.tictactoe.navigation.NavigationController;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Border;
import javafx.scene.paint.Color;

public class LoginScreen {

    @FXML
    private Button loginBtn;
    @FXML
    private Button signupBtn;
    @FXML
    private TextField emailTextField;
    @FXML
    private TextField passwordTextField ;
       @FXML
   private Label warningTextlabel;
    private NavigationController navController;


    @FXML
    private void initialize() {
        UiUtils.addHoverAnimation(loginBtn);
        UiUtils.addHoverAnimation(signupBtn);
    }
    
    public void onBackImageClick(MouseEvent mouseEvent) {
        if (navController == null) {
            System.out.println("error in navController");
        } else {
            UiUtils.playSoundEffect();
            navController.popScene();
        }
    }

    public void onLoginBtn(ActionEvent actionEvent) {
       if(emailTextField.getText().isEmpty() || passwordTextField.getText().isEmpty()) {
           //  emailTextField.borderProperty();
           warningTextlabel.setOpacity(1.0);
           passwordTextField.setStyle("-fx-border-color: red ; -fx-border-width: 3px ; -fx-border-radius: 30; -fx-background-radius: 30; -fx-border-image-width: 5;" );
           emailTextField.setStyle("-fx-border-color: red ; -fx-border-width: 3px ; -fx-border-radius: 30; -fx-background-radius: 30; -fx-border-image-width: 5;" );
           //passwordTextField.setBorder(Border.stroke(Color.RED));
       }
        System.out.println(emailTextField.getText());
        System.out.println(passwordTextField.getText());
    }

    public void onSignupBtn(ActionEvent actionEvent) {
        // implements late ...
    }


    public void setNavController(NavigationController navController) {
        this.navController = navController;
    }

}
