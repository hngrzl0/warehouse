package com.example.warehouse.controller;

import com.example.warehouse.model.Book;
import com.example.warehouse.service.FirestoreService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;

public class ControllerBookDetail {

    @FXML
    private ImageView bookImage;         // ImageView for displaying book image
    @FXML
    private Text bookTitle;              // Text for book title
    @FXML
    private Label bookDescription;       // Label for book description
    @FXML
    private Text bookPrice;              // Text for book price
    @FXML
    private HBox addToCartButton;        // Button for adding book to the cart

    private FirestoreService firestoreService;
    private String bookId;
    private Book book;// Book ID for fetching the book details

    public ControllerBookDetail() {
        this.firestoreService = new FirestoreService(); // Initialize Firestore service
    }

    // Set the bookId from the previous screen (book card click)
    public void setBookId(Book book) {
        this.book = book;
        this.bookId = book.getId();
        loadBookDetails();
    }

    // Load the book details from the Firestore or other data source
//    private void loadBookDetails() {
//        try {
//            // Fetch the full details of the book using the bookId
//            Book book = firestoreService.getBookById(bookId);
//            System.out.println("Book Detail of: " + book.getId());// Replace this with your actual method for fetching book by ID
//
//            // Set the UI components with the book details
//            bookTitle.setText(book.getTitle());
//            bookDescription.setText(book.getDescription());
//            bookPrice.setText(String.valueOf(book.getPrice()));
//            bookImage.setImage(new Image(book.getPictureUrl()));  // Set the book image
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    private void loadBookDetails() {
        try {
            // Fetch the full details of the book using the bookId
            System.out.println("Book Detail of: " + book.getId());// Replace this with your actual method for fetching book by ID

            // Set the UI components with the book details
            bookTitle.setText(book.getTitle());
            bookDescription.setText(book.getDescription());
            bookPrice.setText(String.valueOf(book.getPrice()));
            bookImage.setImage(new Image(book.getPictureUrl()));  // Set the book image

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleAddToCart() {
        System.out.println("Added to cart: " + bookId);
    }

    public void handleAddBook(MouseEvent mouseEvent) {
        try {
            // Load the next screen FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/warehouse/layout/screen_forum.fxml"));
            Parent newRoot = loader.load();

            // Get the current stage and set the new scene
            Stage stage = (Stage) bookImage.getScene().getWindow(); // Get the current stage
            Scene scene = new Scene(newRoot);
            stage.setScene(scene); // Set the new scene
            stage.show(); // Show the new scene



        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handleBackButton(MouseEvent mouseEvent) {
        try {
            // Load the next screen FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/warehouse/layout/screen_home.fxml"));
            Parent newRoot = loader.load();

            // Get the current stage and set the new scene
            Stage stage = (Stage) bookImage.getScene().getWindow(); // Get the current stage
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
            Stage stage = (Stage) bookImage.getScene().getWindow(); // Get the current stage
            Scene scene = new Scene(newRoot);
            stage.setScene(scene); // Set the new scene
            stage.show(); // Show the new scene

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
