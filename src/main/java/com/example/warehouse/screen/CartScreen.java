package com.example.warehouse.screen;

import com.example.warehouse.controller.BookTileController;
import com.example.warehouse.model.Cart;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Controller for the cart screen, which manages the display and handling of books in the shopping cart.
 * <p>
 * The {@code CartScreen} class provides an interface for users to view, update, and manage their shopping cart. It allows users to add, remove, and modify
 * the quantity of books in the cart. The cart's total amount is updated dynamically, and users can proceed to payment or cancel the order.
 * </p>
 * <p>
 * The class interacts with the {@link Cart} model to retrieve the list of books and uses a {@link BookTileController} to display each book's details.
 * It also provides a UI for entering a delivery address and confirmation messages during the checkout process.
 * </p>
 * @author Khongorzul, Margad
 * @see BookTileController
 * @version 1.0
 * @since 2024-12-08
 */
public class CartScreen {

    @FXML
    private VBox tiles;

    @FXML
    private Text amount;

    @FXML
    private TextField addressTf;

    @FXML
    private Button deliveryBtn;

    @FXML
    private Button cancelBtn;

    @FXML
    private Button logoutButton;

    private List<Book> books = new ArrayList<Book>();
    private Map<String, BookTileController> bookTileMap = new HashMap<>();
    private double totalAmount = 0;

    /**
     * Initializes the cart screen by loading the books from the {@link Cart} instance and displaying them.
     * It sets up the UI elements and binds the buttons to their respective actions.
     */
    @FXML
    public void initialize() {
        loadBooks();
        displayBooks();
        updateTotalAmount();
        deliveryBtn.setOnAction(e -> proceedToPayment());
        cancelBtn.setOnAction(e -> cancelOrder());
    }

    /**
     * Loads books from the {@link Cart} into the screen.
     */
    private void loadBooks() {
        books.clear();
        List<com.example.warehouse.screen.CartScreen.Book> cartBooks = new ArrayList<>();
        for (com.example.warehouse.model.Book modelBook : Cart.getInstance().getBooks()) {
            com.example.warehouse.screen.CartScreen.Book screenBook =
                    new com.example.warehouse.screen.CartScreen.Book(modelBook.getTitle(), modelBook.getPrice(), modelBook.getPictureUrl());
            screenBook.setQuantity(modelBook.getCount());
            cartBooks.add(screenBook);
        }
        books.addAll(cartBooks);
    }


    /**
     * Displays the loaded books in the cart.
     */
    private void displayBooks() {
        tiles.getChildren().clear();
        bookTileMap.clear();
        for (Book book : books) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/warehouse/layout/tile_book.fxml"));
                Parent bookTile = loader.load();

                BookTileController controller = loader.getController();
                controller.setBookDetails(book.getTitle(), book.getPrice(), book.getImage());
                controller.setQuantity(book.getQuantity());
                controller.updateTotalPrice();

                // Add listeners for buttons
                controller.incrementButton.setOnAction(e -> {
                    book.increaseQuantity();
                    controller.setQuantity(book.getQuantity());
                    controller.updateTotalPrice();
                    updateTotalAmount();
                });

                controller.decrementButton.setOnAction(e -> {
                    if (book.getQuantity() > 1) {
                        book.decreaseQuantity();
                        controller.setQuantity(book.getQuantity());
                        controller.updateTotalPrice();
                    } else {
                        books.remove(book);
                        bookTileMap.remove(book.getTitle());
                        tiles.getChildren().remove(bookTile);
                    }
                    updateTotalAmount();
                });

                tiles.getChildren().add(bookTile);
                bookTileMap.put(book.getTitle(), controller);
            } catch (IOException e) {
                Logger logger = Logger.getLogger(getClass().getName());
                logger.log(Level.SEVERE, "Error occurred while performing IO operation", e);
            }
        }
    }

    /**
     * Updates the total amount of the cart based on the current list of books.
     */
    private void updateTotalAmount() {
        totalAmount = books.stream()
                .mapToDouble(book -> book.getPrice() * book.getQuantity())
                .sum();
        amount.setText(String.format("%.0f₮", totalAmount));
    }

    /**
     * Proceeds to the payment screen after validating the delivery address.
     */
    private void proceedToPayment() {
        String address = addressTf.getText();
        if (address.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Missing Address");
            alert.setHeaderText("Delivery Address Required");
            alert.setContentText("Please enter a delivery address to proceed.");
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Payment Confirmation");
            alert.setHeaderText("Proceeding to Payment");
            alert.setContentText("Delivery address: " + address + "\nTotal amount: " + totalAmount + "₮");
            alert.showAndWait();
        }
    }
    /**
     * Cancels the current order, resetting the cart to its initial state.
     */
    private void cancelOrder() {
        System.out.println("Order canceled.");
        books.forEach(book -> book.setQuantity(1));
        addressTf.clear();
        displayBooks();
        updateTotalAmount();
    }

    /**
     * Handles the cancel button click event, redirecting to the home screen.
     */
    public void handleCancel(MouseEvent mouseEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/example/warehouse/layout/screen_home.fxml")
            );
            Parent newRoot = loader.load();
            Stage stage = (Stage) tiles.getScene().getWindow();
            Scene scene = new Scene(newRoot);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            Logger logger = Logger.getLogger(getClass().getName());
            logger.log(Level.SEVERE, "Error occurred while performing IO operation", e);
        }
    }

    /**
     * Handles the logout button click event, redirecting to the login screen.
     */
    @FXML
    public void onLogoutButtonClick() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/example/warehouse/layout/screen_login.fxml")
            );
            Parent loginScreen = loader.load();
            Stage currentStage = (Stage) logoutButton.getScene().getWindow();
            Scene loginScene = new Scene(loginScreen);
            currentStage.setScene(loginScene);
            currentStage.show();
            System.out.println("User logged out and redirected to the login screen.");
        } catch (IOException e) {
            Logger logger = Logger.getLogger(getClass().getName());
            logger.log(Level.SEVERE, "Error occurred while navigating to the login screen", e);
        }
    }

    // Inner class to represent a book
    public static class Book {
        private final String title;
        private final double price;
        private final String image;
        private int quantity;

        public Book(String title, double price, String image) {
            this.title = title;
            this.price = price;
            this.image = image;
            this.quantity = 1;
        }

        public String getTitle() {
            return title;
        }

        public double getPrice() {
            return price;
        }

        public String getImage() {
            return image;
        }

        public int getQuantity() {
            return quantity;
        }

        public void increaseQuantity() {
            this.quantity++;
        }

        public void decreaseQuantity() {
            if (this.quantity > 1) {
                this.quantity--;
            }
        }
        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }
    }
}
