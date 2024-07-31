package com.iti.tictactoe.auth;

import com.google.gson.JsonObject;
import com.iti.tictactoe.ListOfUsers;
import com.iti.tictactoe.ServerListener;
import com.iti.tictactoe.SocketManager;
import com.iti.tictactoe.models.AlertUtils;
import com.iti.tictactoe.models.UiUtils;
import com.iti.tictactoe.navigation.NavigationController;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

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
            emailTextField.setStyle("-fx-border-color: red; -fx-border-width: 3px; -fx-border-radius: 75; -fx-background-radius: 75;");
            passwordTextField.setStyle("-fx-border-color: red; -fx-border-width: 3px;-fx-border-radius: 75;  -fx-background-radius: 75;");
        } else {
            try {
                // Create JSON object for login request
                JsonObject jsonRequest = new JsonObject();
                jsonRequest.addProperty("action", "login");
                jsonRequest.addProperty("email", emailTextField.getText());
                jsonRequest.addProperty("password", passwordTextField.getText());
                SocketManager socketManager;

                socketManager = SocketManager.getInstance();
                socketManager.connectCheck();
                ListOfUsers.currentUserEmail = emailTextField.getText();
                // Send JSON request
                SocketManager.getInstance().sendJson(jsonRequest);

                // Read and parse the response
                JsonObject jsonResponse = SocketManager.getInstance().receiveJson(JsonObject.class);
                if (jsonResponse == null) {
                    AlertUtils.showInformationAlert("Login Failed", "No response from the server. Please try again later.", null);
                    return;
                }
                boolean success = jsonResponse.get("success").getAsBoolean();
                if (success) {
                    Platform.runLater(() -> {
                        System.out.println(jsonResponse);
                        System.out.println(jsonResponse);
                        new Thread(new ServerListener(socketManager.getSocket(), navController)).start();
                        AlertUtils.showInformationAlert("Login Successful", "Welcome back!", null);
                        // Navigate to the next screen if needed
                        if (navController != null) {
                            navController.pushScene("/com/iti/tictactoe/listOfUsers.fxml", controller -> {
                                if (controller instanceof ListOfUsers list) {
                                    list.setNavController(navController);
                                }
                            });
                            emailTextField.clear();
                            passwordTextField.clear();
                        }
                    });
                } else {
                    Platform.runLater(() -> AlertUtils.showInformationAlert("Login Failed", "Invalid email or password.", null));
                }
            } catch (IOException e) {
                SocketManager.getInstance();
                Platform.runLater(() -> AlertUtils.showInformationAlert("Connection Error", "Unable to connect to the server.", null));
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
}