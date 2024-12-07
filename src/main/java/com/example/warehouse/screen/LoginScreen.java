package com.example.warehouse.screen;


import com.example.warehouse.Application;
import com.example.warehouse.controller.LoginScreenController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import com.example.warehouse.model.User;
import com.example.warehouse.service.FirestoreService;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.util.Objects;

public class LoginScreen {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button loginButton;
    private LoginScreenController controller;

    @FXML
    public void initialize() {
        FirestoreService firestoreService = new FirestoreService();
        controller = new LoginScreenController(firestoreService);
    }
    @FXML
    public void onLoginButtonClick() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        try {
            System.out.println("Validating username and password...");
            User user = controller.validateUser(username, password);
            if (user != null) {
                // Show success alert and navigate to Book Forum Page
                showAlert(Alert.AlertType.INFORMATION, "Login Successful", "Welcome, " + user.getName());
                if(user.getRole().equals("admin")){
                    navigateToBookForum();
                }
            } else {
                // Show error alert
                showAlert(Alert.AlertType.ERROR, "Login Failed", "Wrong username or password!");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            showAlert(Alert.AlertType.ERROR, "Error", "An error occurred during login: " + e.getMessage());
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void navigateToBookForum() {
        try {
            Parent root = FXMLLoader.load(Application.class.getResource("layout/screen_forum.fxml"));
            Stage stage = (Stage) loginButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
            System.out.println("Navigating to Book Forum Page...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
