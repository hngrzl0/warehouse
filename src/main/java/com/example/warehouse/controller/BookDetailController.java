package com.example.warehouse.controller;

import com.example.warehouse.model.Book;
import com.example.warehouse.model.Cart;
import com.example.warehouse.model.User;
import com.example.warehouse.model.UserSession;
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
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.lang.System.out;

/**
 * Controller class for the Book Detail screen in the warehouse application.
 * <p>
 * This class manages the display and interaction with the details of a selected book.
 * It allows users to view book details, add the book to their cart, purchase it,
 * and navigate between screens. Additionally, it includes user-specific functionalities
 * such as hiding buttons for admin users.
 * </p>
 *
 * @author Khongorzul, Margad
 * @version 1.0
 * @since 2024-11-28
 */
public class BookDetailController {

    @FXML
    private ImageView bookImage;
    @FXML
    private Text bookTitle;
    @FXML
    private Label bookDescription;
    @FXML
    private Text bookPrice;
    @FXML
    private Button logoutButton;
    @FXML
    private Label countLabel;
    @FXML
    private HBox btnCart;
    private final FirestoreService firestoreService;
    private String bookId;
    private Book book;
    private int count = 1;

    /**
     * Constructs a {@code BookDetailController} and initializes the Firestore service.
     */
    public BookDetailController() {
        this.firestoreService = new FirestoreService(); // Initialize Firestore service
    }

    /**
     * Sets the selected book and loads its details into the UI.
     *
     * @param book The selected {@link Book} object.
     */
    public void setBookId(Book book) {
        this.book = book;
        this.bookId = book.getId();
        loadBookDetails();
    }


    /**
     * Loads the details of the selected book and updates the UI components.
     */
    private void loadBookDetails() {
        try {
            // Fetch the full details of the book using the bookId
            System.out.println("Book Detail of: " + book.getId());

            // Set the UI components with the book details
            bookTitle.setText(book.getTitle());
            bookDescription.setText(book.getDescription());
            bookPrice.setText(book.getPrice() + "₮");
            bookImage.setImage(new Image(book.getPictureUrl()));  // Set the book image
            User currentUser = UserSession.getInstance().getUser();
            if(Objects.equals(currentUser.getRole(), "admin")){
                btnCart.setVisible(false);
            }

        } catch (Exception e) {
            Logger logger = Logger.getLogger(getClass().getName());
            logger.log(Level.SEVERE, "Error occurred while performing IO operation", e);
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
        // Add the book with the desired count to the cart
        Cart.getInstance().addBookWithQuantity(book, count);

        // Show a confirmation message
        out.println("Added to cart: " + book.getTitle());
        out.println(count + " copies of the book added to the cart!");

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
            Logger logger = Logger.getLogger(getClass().getName());
            logger.log(Level.SEVERE, "Error occurred while performing IO operation", e);
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
            Logger logger = Logger.getLogger(getClass().getName());
            logger.log(Level.SEVERE, "Error occurred while performing IO operation", e);
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
            Logger logger = Logger.getLogger(getClass().getName());
            logger.log(Level.SEVERE, "Error occurred while performing IO operation", e);
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
            Logger logger = Logger.getLogger(getClass().getName());
            logger.log(Level.SEVERE, "Error occurred while performing IO operation", e);
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

            out.println("User logged out and redirected to the login screen.");
        } catch (IOException e) {
            Logger logger = Logger.getLogger(getClass().getName());
            logger.log(Level.SEVERE, "Error occurred while performing IO operation", e);
        }
    }
}
