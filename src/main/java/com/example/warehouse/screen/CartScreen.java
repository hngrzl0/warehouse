package com.example.warehouse.screen;

import com.example.warehouse.controller.BookTileController;
import com.example.warehouse.controller.CartScreenController;
import com.example.warehouse.model.Book;
import com.example.warehouse.model.Cart;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
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
    public TextField addressTf;

    @FXML
    private Button deliveryBtn;

    @FXML
    private Button cancelBtn;
    @FXML
    private Button logoutBtn;

    @FXML
    private Text homePageTxt;

    private List<BookWithQuantity> cartScreenBooks = new ArrayList<>();
    private Map<String, BookTileController> bookTileMap = new HashMap<>();
    private double totalAmount = 0;

    /**
     * Initializes the cart screen by loading the books from the {@link Cart} instance and displaying them.
     * It sets up the UI elements and binds the buttons to their respective actions.
     */
    @FXML
    public void initialize() {
        CartScreenController controller = new CartScreenController();
        cartScreenBooks = controller.loadBooks();
        displayBooks(cartScreenBooks);
        updateTotalAmount(cartScreenBooks);
        deliveryBtn.setOnAction(e -> controller.proceedToPayment(addressTf.getText(),amount.getText()));
        cancelBtn.setOnAction(e -> controller.cancelOrder(cartScreenBooks,this));
    }
    /**
     * Displays the loaded books in the cart.
     */
    public void displayBooks(List<BookWithQuantity> cartScreenBooks) {
        tiles.getChildren().clear();


        for (BookWithQuantity cartScreenBook : cartScreenBooks) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/warehouse/layout/tile_book.fxml"));
                Parent bookTile = loader.load();
                BookTileController controller = loader.getController();
                controller.setBookDetails(cartScreenBook.book.getTitle(), cartScreenBook.book.getPrice(), cartScreenBook.book.getPictureUrl());
                controller.setQuantity(cartScreenBook.getQuantity());
                controller.updateTotalPrice();

                // Add listeners for buttons
                controller.incrementButton.setOnAction(e -> {
                    cartScreenBook.increaseQuantity();
                    controller.setQuantity(cartScreenBook.getQuantity());
                    controller.updateTotalPrice();
                    updateTotalAmount(cartScreenBooks);
                });

                controller.decrementButton.setOnAction(e -> {
                    if (cartScreenBook.getQuantity() > 1) {
                        cartScreenBook.decreaseQuantity();
                        controller.setQuantity(cartScreenBook.getQuantity());
                        controller.updateTotalPrice();
                    } else {
                        cartScreenBooks.remove(cartScreenBook);
                        bookTileMap.remove(cartScreenBook.book.getTitle());
                        tiles.getChildren().remove(bookTile);
                        Cart.getInstance().deleteBook(cartScreenBook.book);
                    }
                    updateTotalAmount(cartScreenBooks);
                });

                tiles.getChildren().add(bookTile);
                //bookTileMap.put(cartScreenBook.book.getTitle(), controller);
            } catch (IOException e) {
                Logger logger = Logger.getLogger(getClass().getName());
                logger.log(Level.SEVERE, "Error occurred while performing IO operation", e);
            }
        }
    }

    /**
     * Updates the total amount of the cart based on the current list of books.
     */
    public void updateTotalAmount(List<BookWithQuantity> cartScreenBooks){
        double totalAmount = cartScreenBooks.stream()
                .mapToDouble(book -> book.getBook().getPrice() * book.getQuantity())
                .sum();
        amount.setText(String.format("%.0f₮", totalAmount));
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
            Stage currentStage = (Stage) logoutBtn.getScene().getWindow();
            Scene loginScene = new Scene(loginScreen);
            currentStage.setScene(loginScene);
            currentStage.show();
            System.out.println("User logged out and redirected to the login screen.");
        } catch (IOException e) {
            Logger logger = Logger.getLogger(getClass().getName());
            logger.log(Level.SEVERE, "Error occurred while navigating to the login screen", e);
        }
    }

    @FXML
    public void handleHomePage() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/example/warehouse/layout/screen_home.fxml")
            );
            Parent loginScreen = loader.load();
            Stage currentStage = (Stage) homePageTxt.getScene().getWindow();
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
    public static class BookWithQuantity {
        private Book book;
        private int quantity;

        public BookWithQuantity(Book book) {
            this.book = book;
            this.quantity = 1;
        }

        public Book getBook() {
            return book;
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