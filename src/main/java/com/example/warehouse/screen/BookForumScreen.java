package com.example.warehouse.screen;

import com.example.warehouse.controller.BookForumScreenController;
import com.example.warehouse.model.Book;
import com.example.warehouse.service.FirestoreService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.concurrent.Task;

import java.util.Date;

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
                    java.sql.Date.valueOf(publishedDatePicker.getValue()),  // Convert LocalDate to Date
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
}
