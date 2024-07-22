package com.iti.tictactoe.auth;

import com.iti.tictactoe.RecordingsController;
import com.iti.tictactoe.muliplayerOffline.models.UiUtils;
import com.iti.tictactoe.navigation.NavigationController;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

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

        // Check if any field is empty
        if (username.trim().isEmpty() || email.trim().isEmpty() || password.trim().isEmpty() || confirmPassword.trim().isEmpty()) {
            warningTextlabel.setText("All fields are required.");
            warningTextlabel.setOpacity(1.0);
            setFieldErrorStyles();
            return;
        }

        // Validate email format
        if (!isValidEmail(email)) {
            warningTextlabel.setText("Invalid email format.");
            warningTextlabel.setOpacity(1.0);
            emailField.setStyle("-fx-border-color: red ; -fx-border-width: 3px ; -fx-border-radius: 30; -fx-background-radius: 30; -fx-border-image-width: 5;");
            return;
        }

        // Validate password complexity
        String passwordFeedback = getPasswordFeedback(password);
        if (!passwordFeedback.isEmpty()) {
            warningTextlabel.setText("Password does not meet requirements: " + passwordFeedback);
            warningTextlabel.setOpacity(1.0);
            passwordField.setStyle("-fx-border-color: red ; -fx-border-width: 3px ; -fx-border-radius: 30; -fx-background-radius: 30; -fx-border-image-width: 5;");
            return;
        }

        // Validate passwords match
        if (!password.equals(confirmPassword)) {
            warningTextlabel.setText("Passwords do not match.");
            warningTextlabel.setOpacity(1.0);
            confirmPasswordField.setStyle("-fx-border-color: red ; -fx-border-width: 3px ; -fx-border-radius: 30; -fx-background-radius: 30; -fx-border-image-width: 5;");
            return;
        }

        System.out.println("Sign-up successful for user: " + username);
        clearFieldErrorStyles();
        warningTextlabel.setText("Sign-up successful!");
        warningTextlabel.setOpacity(1.0);
        // Handle successful signup logic here (e.g., save user data)
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

    private void clearFieldErrorStyles() {
        usernameField.setStyle("");
        emailField.setStyle("");
        passwordField.setStyle("");
        confirmPasswordField.setStyle("");
    }

    @FXML
    private void handleShowLogin() {
        UiUtils.playSoundEffect();
        if (navController != null) {
            UiUtils.playSoundEffect();
            navController.popScene();
        }
    }

    public void setNavController(NavigationController navController) {
        this.navController = navController;
    }
}
