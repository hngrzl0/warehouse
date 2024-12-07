package com.example.warehouse.screen;

import com.example.warehouse.controller.BookForumScreenController;
import com.example.warehouse.model.Book;
import com.example.warehouse.service.FirestoreService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.concurrent.Task;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Date;
import java.util.UUID;

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

    @FXML
    public void initialize() {
        // Populate the ChoiceBox with predefined categories
        categoryField.setItems(FXCollections.observableArrayList(
                "Adventure", "Fiction", "Non-Fiction", "History", "Romance", "Fantasy", "Children's Books"
        ));
        categoryField.setValue("Adventure");
        firestoreService = new FirestoreService();
        controller = new BookForumScreenController(firestoreService);
    }

    @FXML
    public void onSubmit() {
        try {
//             Create a Book object based on the input fields
            Book book = new Book(
                    titleField.getText(),
                    authorField.getText(),
                    publishedDatePicker.getValue().toString(),  // Convert LocalDate to Date
                    isbnField.getText(),
                    Double.parseDouble(priceField.getText()),
                    Integer.parseInt(countField.getText()),
                    descriptionField.getText(),
                    pictureUrlField.getText(),
                    categoryField.getValue()

            );

//            Book book = new Book(
//                    "Effective Java",
//                    "Joshua Bloch",
//                    new Date(),
//                    "9780134685991",
//                    45.99,
//                    10,
//                    "A comprehensive guide to Java best practices.",
//                    "http://example.com/cover.jpg",
//                    "Programming"
//            );

            // Create a background task for Firebase operation
            Task<Void> addBookTask = new Task<Void>() {
                @Override
                protected Void call() {
                    try {
                        System.out.println("Adding book to Firebase...");  // Log to check if the operation is triggered
                        controller.addBook(book);  // Firebase operation in background
                    } catch (Exception e) {
                        System.err.println("Error during Firebase operation: " + e.getMessage());
                    }
                    return null;
                }

                @Override
                protected void succeeded() {
                    System.out.println("Book added successfully!");
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

    public void handleHomeMenu(MouseEvent mouseEvent) {
        try {
            // Load the next screen FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/warehouse/layout/screen_home.fxml"));
            Parent newRoot = loader.load();

            // Get the current stage and set the new scene
            Stage stage = (Stage) titleField.getScene().getWindow(); // Get the current stage
            Scene scene = new Scene(newRoot);
            stage.setScene(scene); // Set the new scene
            stage.show(); // Show the new scene

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handleSearchBox(MouseEvent mouseEvent) {
        try {
            // Load the next screen FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/warehouse/layout/screen_home.fxml"));
            Parent newRoot = loader.load();

            // Get the current stage and set the new scene
            Stage stage = (Stage) titleField.getScene().getWindow(); // Get the current stage
            Scene scene = new Scene(newRoot);
            stage.setScene(scene); // Set the new scene
            stage.show(); // Show the new scene

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void onLogoutButtonClick(){
        try {
            // Load the login screen FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/warehouse/layout/screen_login.fxml"));
            Parent loginScreen = loader.load();

            // Get the current stage
            Stage currentStage = (Stage) logoutButton.getScene().getWindow();

            // Set the new scene to the login screen
            Scene loginScene = new Scene(loginScreen);
            currentStage.setScene(loginScene);

            // Show the new scene
            currentStage.show();

            System.out.println("User logged out and redirected to the login screen.");
        } catch (IOException e) {
            System.err.println("Error occurred while navigating to the login screen: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
