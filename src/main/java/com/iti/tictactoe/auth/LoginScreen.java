package com.iti.tictactoe.auth;

import com.iti.tictactoe.models.UiUtils;
import com.iti.tictactoe.navigation.NavigationController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import com.iti.tictactoe.ListOfUsers;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class LoginScreen {

    @FXML
    private Button loginBtn;
    @FXML
    private Button signupBtn;
    @FXML
    private TextField emailTextField;
    @FXML
    private TextField passwordTextField;
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
        if (emailTextField.getText().isEmpty() || passwordTextField.getText().isEmpty()) {
            warningTextlabel.setOpacity(1.0);
            passwordTextField.setStyle("-fx-border-color: red ; -fx-border-width: 3px ; -fx-border-radius: 30; -fx-background-radius: 30; -fx-border-image-width: 5;");
            emailTextField.setStyle("-fx-border-color: red ; -fx-border-width: 3px ; -fx-border-radius: 30; -fx-background-radius: 30; -fx-border-image-width: 5;");
        } else {
            try (Socket socket = new Socket("localhost", 12345);
                 DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
                 DataInputStream dis = new DataInputStream(socket.getInputStream())) {

                dos.writeUTF("login");
                dos.writeUTF(emailTextField.getText()); // email
                dos.writeUTF(passwordTextField.getText());
                ListOfUsers.setCurrentUserEmail(emailTextField.getText());
                boolean success = dis.readBoolean();
                if (success) {
                    showAlert("Login Successful", "Welcome back!");
                    if (navController != null) {
                        navController.pushScene("/com/iti/tictactoe/listOfUsers.fxml", controller -> {
                            if (controller instanceof SignUp sighUp) {
                                sighUp.setNavController(navController);
                            }
                        });
                    }
                } else {
                    showAlert("Login Failed", "Invalid email or password.");
                }

            } catch (IOException e) {
                e.printStackTrace();
                showAlert("Connection Error", "Unable to connect to server.");
            }
            UiUtils.playSoundEffect();

        }
    }

    public void onSignupBtn(ActionEvent actionEvent) {
        UiUtils.playSoundEffect();
        if (navController != null) {
            navController.pushScene("/com/iti/tictactoe/SignUp.fxml", controller -> {
                if (controller instanceof SignUp sighUp) {
                    sighUp.setNavController(navController);
                }
            });
        }
    }

    public void setNavController(NavigationController navController) {
        this.navController = navController;
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
