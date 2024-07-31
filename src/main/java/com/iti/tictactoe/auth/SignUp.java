package com.iti.tictactoe.auth;

import com.google.gson.JsonObject;
import com.iti.tictactoe.SocketManager;
import com.iti.tictactoe.models.UiUtils;
import com.iti.tictactoe.navigation.NavigationController;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.regex.Pattern;

public class SignUp {

    @FXML
    private TextField usernameField;
    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private PasswordField confirmPasswordField;
    @FXML
    private Label passwordFeedbackLabel;
    @FXML
    private Label warningTextLabel;
    private NavigationController navController;

    @FXML
    private void handleSignUp() {
        String username = usernameField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        // Validate input
        if (username.trim().isEmpty() || email.trim().isEmpty() || password.trim().isEmpty() || confirmPassword.trim().isEmpty()) {
            setFieldErrorStyles();
          //  warningTextLabel.setText("All fields are required.");
            warningTextLabel.setOpacity(1.0);
            setFieldErrorStyles();
            return;
        }
        if (!isValidEmail(email)) {
            warningTextLabel.setText("Invalid email format.");
            warningTextLabel.setOpacity(1.0);
            emailField.setStyle("-fx-border-color: red ; -fx-border-width: 3px ; -fx-border-radius: 30; -fx-background-radius: 30; -fx-border-image-width: 5;");
            return;
        }

        String passwordFeedback = getPasswordFeedback(password);
        if (!passwordFeedback.isEmpty()) {
            warningTextLabel.setText("Password does not meet requirements: " + passwordFeedback);
            warningTextLabel.setOpacity(1.0);
            passwordField.setStyle("-fx-border-color: red ; -fx-border-width: 3px ; -fx-border-radius: 30; -fx-background-radius: 30; -fx-border-image-width: 5;");
            return;
        }

        if (!password.equals(confirmPassword)) {
            warningTextLabel.setText("Passwords do not match.");
            warningTextLabel.setOpacity(1.0);
            confirmPasswordField.setStyle("-fx-border-color: red ; -fx-border-width: 3px ; -fx-border-radius: 30; -fx-background-radius: 30; -fx-border-image-width: 5;");
            return;
        }
        else{

        try {
            // Create JSON object for signup request
            JsonObject jsonRequest = new JsonObject();
            jsonRequest.addProperty("action", "signup");
            jsonRequest.addProperty("username", username);
            jsonRequest.addProperty("email", email);
            jsonRequest.addProperty("password", password);

            // Send JSON request
            SocketManager socketManager;

            socketManager = SocketManager.getInstance();
            socketManager.connectCheck();
            socketManager.sendJson(jsonRequest);

            // Read and parse the response
            JsonObject jsonResponse = socketManager.receiveJson(JsonObject.class);

//            if (jsonResponse == null) {
//                showAlert("Sign-Up Failed", "No response from the server. Please try again later.");
//                return;
//            }

            boolean success = jsonResponse.get("success").getAsBoolean();

            if (success) {
                showAlert("Sign-Up Successful", "Your account has been created.");
                clearFields();
                if (navController != null) {
                    navController.popScene();
                }
            } else {
                showAlert("Sign-Up Failed", jsonResponse.get("message").getAsString());
            }
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Connection Error", "Unable to connect to server. Please try again later.");
        }
        }

    }

    private boolean isValidEmail(String email) {
        // Regular expression for email validation
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        Pattern emailPattern = Pattern.compile(emailRegex);
        return emailPattern.matcher(email).matches();
    }

    private String getPasswordFeedback(String password) {
        StringBuilder feedback = new StringBuilder();

        if (!Pattern.compile("[A-Z]").matcher(password).find()) {
            feedback.append("an uppercase letter, ");
        }
        if (!Pattern.compile("[a-z]").matcher(password).find()) {
            feedback.append("a lowercase letter, ");
        }
        if (!Pattern.compile("\\d").matcher(password).find()) {
            feedback.append("a digit, ");
        }
        if (!Pattern.compile("[@#$%^&+=!]").matcher(password).find()) {
            feedback.append("a special character, ");
        }
        if (password.length() < 8) {
            feedback.append("at least 8 characters, ");
        }

        if (feedback.length() > 0) {
            feedback.setLength(feedback.length() - 2); // Remove comma and space
        }

        return feedback.toString();
    }

    private void setFieldErrorStyles() {
        usernameField.setStyle("-fx-border-color: red ; -fx-border-width: 3px ; -fx-border-radius: 30; -fx-background-radius: 30; -fx-border-image-width: 5;");
        emailField.setStyle("-fx-border-color: red ; -fx-border-width: 3px ; -fx-border-radius: 30; -fx-background-radius: 30; -fx-border-image-width: 5;");
        passwordField.setStyle("-fx-border-color: red ; -fx-border-width: 3px ; -fx-border-radius: 30; -fx-background-radius: 30; -fx-border-image-width: 5;");
        confirmPasswordField.setStyle("-fx-border-color: red ; -fx-border-width: 3px ; -fx-border-radius: 30; -fx-background-radius: 30; -fx-border-image-width: 5;");
    }

    private void clearFields() {
        usernameField.clear();
        emailField.clear();
        passwordField.clear();
        confirmPasswordField.clear();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }



    public void setNavController(NavigationController navController) {
        this.navController = navController;
    }

    @FXML
    private void onBackImageClick(MouseEvent mouseEvent) {
        if (navController != null) {
            UiUtils.playSoundEffect();
            navController.popScene();
        }
    }
}
