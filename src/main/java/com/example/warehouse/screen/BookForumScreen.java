package com.example.warehouse.screen;

import com.example.warehouse.controller.BookForumScreenController;
import com.example.warehouse.model.Book;
import com.example.warehouse.service.FirestoreService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.concurrent.Task;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Controller for the book forum screen, which allows users to add new books to the inventory.
 * <p>
 * The {@code BookForumScreen} class provides a user interface for entering book details including title, author, ISBN, price, and category. It uses
 * a {@link FirestoreService} to persist book information to a Firebase Firestore database. The screen includes fields for entering these details and
 * offers functionality to submit the book, handle navigation to other screens, and log out.
 * </p>
 * <p>
 * The class leverages a background task to handle Firebase operations, allowing the UI to remain responsive during the process. Error handling is
 * implemented to log and display error messages in case of failures.
 * </p>
 * @author Margad, Khongorzul
 * @version 1.0
 * @since 2024-12-08
 */
public class BookForumScreen {

    @FXML
    private TextField titleField;
    @FXML
    private TextField authorField;
    @FXML
    private DatePicker publishedDatePicker;
    @FXML
    private TextField isbnField;
    @FXML
    private TextField priceField;
    @FXML
    private TextField countField;
    @FXML
    private TextField descriptionField;
    @FXML
    private TextField pictureUrlField;
    @FXML
    private ChoiceBox<String> categoryField;
    @FXML
    private Button logoutButton;

    private BookForumScreenController controller;
    private FirestoreService firestoreService;

    /**
     * Initializes the screen, setting up the category choice box with predefined categories.
     */
    @FXML
    public void initialize() {
        categoryField.setItems(FXCollections.observableArrayList(
                "All","Adventure", "Fiction", "Non-Fiction", "History", "Romance", "Fantasy", "Children's Books"
        ));
        categoryField.setValue("All");
        firestoreService = new FirestoreService();
        controller = new BookForumScreenController(firestoreService);
    }

    /**
     * Handles the submission of the book form. Creates a new {@link Book} from the form data and
     * triggers a background task to add the book to Firebase Firestore.
     */
    @FXML
    public void onSubmit() {
        try {
            Book book = new Book(
                    titleField.getText(),
                    authorField.getText(),
                    publishedDatePicker.getValue().toString(),
                    isbnField.getText(),
                    Double.parseDouble(priceField.getText()),
                    Integer.parseInt(countField.getText()),
                    descriptionField.getText(),
                    pictureUrlField.getText(),
                    categoryField.getValue()

            );
            // Create a background task for Firebase operation
            Task<Void> addBookTask = new Task<Void>() {
                @Override
                protected Void call() {
                    try {
                        // Log to check if the operation is triggered
                        System.out.println("Adding book to Firebase...");
                        // Firebase operation in background
                        controller.addBook(book);
                    } catch (Exception e) {
                        System.err.println("Error during Firebase operation: " + e.getMessage());
                    }
                    return null;
                }

                @Override
                protected void succeeded() {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Book Forum");
                    alert.setHeaderText("Adding the book");
                    alert.setContentText("Book added successfully to database!");
                    alert.showAndWait();
                    System.out.println("Book added successfully!");

                    titleField.clear();
                    authorField.clear();
                    publishedDatePicker.setValue(null);
                    isbnField.clear();
                    priceField.clear();
                    countField.clear();
                    descriptionField.clear();
                    pictureUrlField.clear();
                    categoryField.setValue(null);
                }

                @Override
                protected void failed() {
                    System.err.println("Error adding book: " + getException().getMessage());
                }
            };

            // Run the task in a background thread
            new Thread(addBookTask).start();
        } catch (Exception e) {
            System.err.println("Error creating book: " + e.getMessage());
        }
    }

    /**
     * Handles navigation to the home screen when the home menu is clicked.
     */
    public void handleHomeMenu(MouseEvent mouseEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/example/warehouse/layout/screen_home.fxml")
            );
            Parent newRoot = loader.load();
            Stage stage = (Stage) titleField.getScene().getWindow();
            Scene scene = new Scene(newRoot);
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            Logger logger = Logger.getLogger(getClass().getName());
            logger.log(Level.SEVERE, "Error occurred while performing IO operation", e);
        }
    }


    /**
     * Handles navigation to the home screen for search when the search box is clicked.
     */
    public void handleSearchBox(MouseEvent mouseEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/example/warehouse/layout/screen_home.fxml")
            );
            Parent newRoot = loader.load();
            Stage stage = (Stage) titleField.getScene().getWindow();
            Scene scene = new Scene(newRoot);
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            Logger logger = Logger.getLogger(getClass().getName());
            logger.log(Level.SEVERE, "Error occurred while performing IO operation", e);
        }
    }

    /**
     * Handles the logout button click event.
     */
    @FXML
    public void onLogoutButtonClick(){
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/example/warehouse/layout/screen_login.fxml"))
                    ;
            Parent loginScreen = loader.load();
            Stage currentStage = (Stage) logoutButton.getScene().getWindow();
            Scene loginScene = new Scene(loginScreen);
            currentStage.setScene(loginScene);
            currentStage.show();
            System.out.println("User logged out and redirected to the login screen.");
        } catch (IOException e) {
            System.err.println("Error occurred while navigating to the login screen: " + e.getMessage());
            Logger logger = Logger.getLogger(getClass().getName());
            logger.log(Level.SEVERE, "Error occurred while performing IO operation", e);
        }
    }
}
