package com.iti.tictactoe.auth;

import com.google.gson.JsonObject;
import com.iti.tictactoe.ListOfUsers;
import com.iti.tictactoe.SocketManager;
import com.iti.tictactoe.models.UiUtils;
import com.iti.tictactoe.navigation.NavigationController;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

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
            SocketManager socketManager = SocketManager.getInstance();
            PrintWriter pw = socketManager.getPrintWriter();
            BufferedReader br = socketManager.getBufferedReader();

            try {
                // Create JSON object for login request
                ListOfUsers.setCurrentUserEmail(emailTextField.getText());
                JsonObject jsonRequest = new JsonObject();
                jsonRequest.addProperty("action", "login");
                jsonRequest.addProperty("email", emailTextField.getText());
                jsonRequest.addProperty("password", passwordTextField.getText());

                // Send JSON request
                socketManager.sendJson(jsonRequest);

                // Read and parse the response
                JsonObject jsonResponse = socketManager.receiveJson(JsonObject.class);
                if (jsonResponse == null) {
                    showAlert("Login Failed", "No response from the server. Please try again later.");
                    return;
                }

                boolean success = jsonResponse.get("success").getAsBoolean();
                if (success) {
                    Platform.runLater(() -> {
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
                    });
                } else {
                    Platform.runLater(() -> showAlert("Login Failed", "Invalid email or password."));
                }
            } catch (IOException e) {
                e.printStackTrace();
                Platform.runLater(() -> showAlert("Connection Error", "Unable to connect to the server."));
            }
        }
    }

    private void showAlert(String title, String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        });
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

    /*private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }*/
}