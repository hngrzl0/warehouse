package com.example.warehouse.screen;

import com.example.warehouse.controller.BookTileController;
import com.example.warehouse.model.Book;
import com.example.warehouse.model.Cart;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CartScreen {

    @FXML
    private VBox tiles; // VBox to display book tiles

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
    // List of books
    private double totalAmount = 0; // Total cart amount

    @FXML
    public void initialize() {
        // Initialize the list of books
        loadBooks();
        displayBooks();
        updateTotalAmount();


        deliveryBtn.setOnAction(e -> proceedToPayment());
        cancelBtn.setOnAction(e -> cancelOrder());
    }

    private void loadBooks() {
        books.clear();
        List<com.example.warehouse.screen.CartScreen.Book> cartBooks = new ArrayList<>();
        for (com.example.warehouse.model.Book modelBook : Cart.getInstance().getBooks()) {
            com.example.warehouse.screen.CartScreen.Book screenBook =
                    new com.example.warehouse.screen.CartScreen.Book(modelBook.getTitle(), modelBook.getPrice(), modelBook.getPictureUrl());
            screenBook.setQuantity(modelBook.getCount());
            cartBooks.add(screenBook);
        }
        books.addAll(cartBooks); // Now it will work correctly
    }

    private void displayBooks() {
        tiles.getChildren().clear(); // Clear previous tiles to avoid duplication
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
                e.printStackTrace();
            }
        }
    }

    private void updateTotalAmount() {
        totalAmount = books.stream()
                .mapToDouble(book -> book.getPrice() * book.getQuantity())
                .sum();
        amount.setText(String.format("%.0f₮", totalAmount));
    }

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

    private void cancelOrder() {
        System.out.println("Order canceled.");
        books.forEach(book -> book.setQuantity(1));
        addressTf.clear();
        displayBooks();
        updateTotalAmount();
    }

    public void handleCancel(MouseEvent mouseEvent) {
        try {
            // Load the next screen FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/warehouse/layout/screen_home.fxml"));
            Parent newRoot = loader.load();

            // Get the current stage and set the new scene
            Stage stage = (Stage) tiles.getScene().getWindow(); // Get the current stage
            Scene scene = new Scene(newRoot);
            stage.setScene(scene); // Set the new scene
            stage.show(); // Show the new scene

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void onLogoutButtonClick() {
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
