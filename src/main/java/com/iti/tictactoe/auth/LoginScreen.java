package com.iti.tictactoe.auth;

import com.iti.tictactoe.ListOfUsers;
import com.iti.tictactoe.SocketManager;
import com.iti.tictactoe.models.UiUtils;
import com.iti.tictactoe.navigation.NavigationController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

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
    private Label warningTextLabel;
    private NavigationController navController;

    @FXML
    private void initialize() {
        UiUtils.addHoverAnimation(loginBtn);
        UiUtils.addHoverAnimation(signupBtn);
    }

    public void onBackImageClick(MouseEvent mouseEvent) {
        if (navController != null) {
            UiUtils.playSoundEffect();
            navController.popScene();
        } else {
            System.out.println("Error: NavigationController is not set.");
        }
    }

    public void onLoginBtn(ActionEvent actionEvent) {
        if (emailTextField.getText().isEmpty() || passwordTextField.getText().isEmpty()) {
            warningTextLabel.setOpacity(1.0);
            emailTextField.setStyle("-fx-border-color: red; -fx-border-width: 3px;");
            passwordTextField.setStyle("-fx-border-color: red; -fx-border-width: 3px;");
        } else {
            try {
                // Get SocketManager instance
                SocketManager socketManager = SocketManager.getInstance();
                DataOutputStream dos = socketManager.getDataOutputStream();
                DataInputStream dis = socketManager.getDataInputStream();

                dos.writeUTF("login");
                dos.writeUTF(emailTextField.getText());
                dos.writeUTF(passwordTextField.getText());
                dos.flush(); // Ensure data is sent immediately

                ListOfUsers.setCurrentUserEmail(emailTextField.getText());
                boolean success = dis.readBoolean();

                if (success) {
                    showAlert("Login Successful", "Welcome back!");
                    if (navController != null) {
                        navController.pushScene("/com/iti/tictactoe/listOfUsers.fxml", controller -> {
                            if (controller instanceof ListOfUsers list) {
                                list.setNavController(navController);
                            }
                        });
                        emailTextField.clear();
                        passwordTextField.clear();
                    }
                } else {
                    showAlert("Login Failed", "Invalid email or password.");
                }
            } catch (IOException e) {
                e.printStackTrace();
                showAlert("Connection Error", "Unable to connect to the server.");
            } finally {
                UiUtils.playSoundEffect();
            }
        }
    }

    public void onSignupBtn(ActionEvent actionEvent) {
        UiUtils.playSoundEffect();
        if (navController != null) {
            navController.pushScene("/com/iti/tictactoe/SignUp.fxml", controller -> {
                if (controller instanceof SignUp signUp) {
                    signUp.setNavController(navController);
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
