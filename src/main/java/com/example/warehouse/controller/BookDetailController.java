package com.example.warehouse.controller;

import com.example.warehouse.model.Book;
import com.example.warehouse.model.Cart;
import com.example.warehouse.service.FirestoreService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;

public class BookDetailController {

    @FXML
    private ImageView bookImage;         // ImageView for displaying book image
    @FXML
    private Text bookTitle;              // Text for book title
    @FXML
    private Label bookDescription;       // Label for book description
    @FXML
    private Text bookPrice;
    @FXML
    private Button logoutButton;// Text for book price
    @FXML
    private Label countLabel;
    @FXML
    private Button decreaseButton;
    @FXML
    private Button increaseButton;
    @FXML
    private Button buyButton;
    @FXML
    private Button addToCartButton;
    @FXML
    private HBox btnCart;
    private FirestoreService firestoreService;
    private String bookId;
    private Book book;
    private int count = 1;

    public BookDetailController() {
        this.firestoreService = new FirestoreService(); // Initialize Firestore service
    }

    // Set the bookId from the previous screen (book card click)
    public void setBookId(Book book) {
        this.book = book;
        this.bookId = book.getId();
        loadBookDetails();
    }

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
    private void handleDecreaseCount() {
        if (count > 1) {
            count--;
            countLabel.setText(String.valueOf(count));
        }
    }
    @FXML
    private void handleIncreaseCount() {
        count++;
        countLabel.setText(String.valueOf(count));
    }

    @FXML
    private void handleBuy() {
        // Show an alert indicating a successful purchase
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Purchase Successful");
        alert.setHeaderText(null);
        alert.setContentText("Successfully bought the book!");
        alert.showAndWait();

        //reset the count
        count = 1;
        countLabel.setText(String.valueOf(count));
    }
    @FXML
    private void handleAddToCart() {
        for (int i = 0; i < count; i++) {
            Cart.getInstance().addBook(book); // Add the book to the cart multiple times based on count
        }

        // Show a confirmation message
        System.out.println("Added to cart: " + bookId);
        System.out.println(count + " copies of the book added to the cart!");

        // Reset the count after adding to the cart
        count = 1;
        countLabel.setText(String.valueOf(count));
    }
    @FXML
    private void handleCartButton(){
        try {
            // Load the next screen FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/warehouse/layout/screen_cart.fxml"));
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
