package com.iti.tictactoe.signUp;

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
    private void handleSignUp() {
        // Get user input
        String username = usernameField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        // Validate that username is not empty
        if (username.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Username cannot be empty.");
            return;
        }

        // Validate email format
        if (!isValidEmail(email)) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Invalid email format.");
            return;
        }

        // Validate that password is not empty and meets complexity requirements
        if (password.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Password cannot be empty.");
            return;
        }

        String passwordFeedback = getPasswordFeedback(password);
        if (!passwordFeedback.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Password does not meet requirements: " + passwordFeedback);
            return;
        }

        // Validate that confirm password is not empty
        if (confirmPassword.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Confirm Password cannot be empty.");
            return;
        }

        // Validate passwords match
        if (!password.equals(confirmPassword)) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Passwords do not match.");
            return;
        }

        System.out.println("Sign-up successful for user: " + username);
    }

    private boolean isValidEmail(String email) {
        // Regular expression for email validation
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        Pattern emailPattern = Pattern.compile(emailRegex);

        // Check if the email matches the pattern
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

        // Remove the trailing comma and space if feedback is not empty
        if (!feedback.isEmpty()) {
            feedback.setLength(feedback.length() - 2); //Delete space and comma
        }

        return feedback.toString();
    }

    @FXML
    private void handlePasswordKeyReleased() {
        String password = passwordField.getText();
        String feedback = getPasswordFeedback(password);
        if (feedback.isEmpty()) {
            passwordFeedbackLabel.setText("Password meets requirements");
            passwordFeedbackLabel.setTextFill(javafx.scene.paint.Color.GREEN);
        } else {
            passwordFeedbackLabel.setText("Password is missing: " + feedback);
            passwordFeedbackLabel.setTextFill(javafx.scene.paint.Color.RED);
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void handleShowLogin() {
        // Handle showing login form
        System.out.println("Switching to login form.");
    }
}
