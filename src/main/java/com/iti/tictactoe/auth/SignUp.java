package com.iti.tictactoe.auth;

import com.iti.tictactoe.SocketManager;
import com.iti.tictactoe.models.UiUtils;
import com.iti.tictactoe.navigation.NavigationController;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
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
    private Label warningTextlabel;
    private NavigationController navController;

    @FXML
    private void handleSignUp() {
        // Get user input
        String username = usernameField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        // Validate input
        if (username.trim().isEmpty() || email.trim().isEmpty() || password.trim().isEmpty() || confirmPassword.trim().isEmpty()) {
            warningTextlabel.setText("All fields are required.");
            warningTextlabel.setOpacity(1.0);
            setFieldErrorStyles();
            return;
        }

        if (!isValidEmail(email)) {
            warningTextlabel.setText("Invalid email format.");
            warningTextlabel.setOpacity(1.0);
            emailField.setStyle("-fx-border-color: red ; -fx-border-width: 3px ; -fx-border-radius: 30; -fx-background-radius: 30; -fx-border-image-width: 5;");
            return;
        }

        String passwordFeedback = getPasswordFeedback(password);
        if (!passwordFeedback.isEmpty()) {
            warningTextlabel.setText("Password does not meet requirements: " + passwordFeedback);
            warningTextlabel.setOpacity(1.0);
            passwordField.setStyle("-fx-border-color: red ; -fx-border-width: 3px ; -fx-border-radius: 30; -fx-background-radius: 30; -fx-border-image-width: 5;");
            return;
        }

        if (!password.equals(confirmPassword)) {
            warningTextlabel.setText("Passwords do not match.");
            warningTextlabel.setOpacity(1.0);
            confirmPasswordField.setStyle("-fx-border-color: red ; -fx-border-width: 3px ; -fx-border-radius: 30; -fx-background-radius: 30; -fx-border-image-width: 5;");
            return;
        }

        // Use SocketManager for socket operations
        SocketManager socketManager = SocketManager.getInstance();
        DataOutputStream dos = null;
        DataInputStream dis = null;

        try {
            dos = socketManager.getDataOutputStream();
            dis = socketManager.getDataInputStream();

            dos.writeUTF("signup");
            dos.writeUTF(username);
            dos.writeUTF(email);
            dos.writeUTF(password);
            dos.flush();

            // Handle response
            boolean success = dis.readBoolean();
            if (success) {
                showAlert("Sign-Up Successful", "Your account has been created.");
                clearFields();
                if (navController != null) {
                    navController.pushScene("/com/iti/tictactoe/LoginScreen.fxml", controller -> {
                        if (controller instanceof LoginScreen loginScreen) {
                            loginScreen.setNavController(navController);
                        }
                    });
                }
            } else {
                showAlert("Sign-Up Failed", "Unable to create your account.");
            }

        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Connection Error", "Unable to connect to server. Please try again later.");
        } finally {
            try {
                if (dos != null) dos.close();
                if (dis != null) dis.close();
            } catch (IOException e) {
                e.printStackTrace();
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
            feedback.setLength(feedback.length() - 2); // Remove trailing comma and space
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

    @FXML
    private void handleShowLogin() {
        if (navController != null) {
            UiUtils.playSoundEffect();
            navController.popScene();
        }
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
