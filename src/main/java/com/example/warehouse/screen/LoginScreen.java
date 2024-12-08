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
/**
 * The {@code LoginScreen} class represents the login interface of the application.
 * It handles user interactions, such as input validation and navigation.
 * <p>
 * This class interacts with {@code LoginScreenController} to validate users
 * using Firestore as the backend service.
 * </p>
 *
 * @author Khongorzul
 * @version 1.0
 * @since 2024-11-25
 */
public class LoginScreen {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button loginButton;

    private LoginScreenController controller;

    /**
     * Initializes the login screen. It sets up the {@link FirestoreService}
     * and links the {@link LoginScreenController} for validation logic.
     */
    @FXML
    public void initialize() {
        FirestoreService firestoreService = new FirestoreService();
        controller = new LoginScreenController(firestoreService);
    }

    /**
     * Handles the login button click event.
     * Validates the user credentials entered the username and password fields.
     * If the credentials are correct, and user role is admin it navigates to the Book Forum Page.
     * Or if the credentials are correct, and user role is user it navigates to the Home Screen Page.
     * Otherwise, it shows an error alert.
     */
    @FXML
    public void onLoginButtonClick() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        try {
            System.out.println("Validating username and password...");
            User user = controller.validateUser(username, password);
            if (user != null) {
                if(user.getRole().equals("admin")){
                    navigateToBookForum();
                }
                else if(user.getRole().equals("user")){
                    navigateToHomeScreen();
                }
            } else {
                showAlert(Alert.AlertType.ERROR, "Login Failed", "Wrong username or password!");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            showAlert(Alert.AlertType.ERROR, "Error", "An error occurred during login: " + e.getMessage());
        }
    }


    /**
     * Displays an alert with the given type, title, and message.
     *
     * @param alertType The type of the alert (e.g., INFORMATION, ERROR).
     * @param title     The title of the alert.
     * @param message   The content message of the alert.
     */
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Navigates the admin to the Book Forum Page.
     * This method loads the Book Forum FXML layout and updates the current stage scene.
     */
    private void navigateToBookForum() {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(Application.class.getResource("layout/screen_home.fxml")));
            Stage stage = (Stage) loginButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
            System.out.println("Navigating to Book Forum Page...");
        } catch (Exception e) {
            System.err.println("Error occurred while navigating to Book Forum Page: " + e.getMessage());

        }
    }

    /**
     * Navigates the user to the Home Screen Page.
     * This method loads the Home Screen FXML layout and updates the current stage scene.
     */
    public void navigateToHomeScreen(){
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(Application.class.getResource("layout/screen_home.fxml")));
            Stage stage = (Stage) loginButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
            System.out.println("Navigating to Home Page...");
        } catch (Exception e) {
            System.err.println("Error occurred while navigating to Book Forum Page: " + e.getMessage());

        }
    }
}
