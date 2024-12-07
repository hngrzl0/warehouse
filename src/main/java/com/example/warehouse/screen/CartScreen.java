package com.example.warehouse.screen;

import com.example.warehouse.controller.BookTileController;
import com.example.warehouse.model.Book;
import com.example.warehouse.model.Cart;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

    private List<Book> books = new ArrayList<Book>(); // List of books
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
        // Add sample books

        for(com.example.warehouse.model.Book book : Cart.getInstance().getBooks()){
            Book tempBook = new Book(book.getTitle(), book.getPrice(), book.getPictureUrl());
            books.add(tempBook);
        }
    }

    private void displayBooks() {
        // Clear any existing tiles
        tiles.getChildren().clear();

        for (Book book : books) {
            try {
                // Load BookTile.fxml
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/warehouse/layout/tile_book.fxml"));
                Parent bookTile = loader.load();

                // Get the controller and set book details
                BookTileController controller = loader.getController();
                controller.setBookDetails(book.getTitle(), book.getPrice(), book.getImage());

                // Add listeners for quantity changes
                controller.incrementButton.setOnAction(e -> {
                    book.increaseQuantity();
                    updateTotalAmount();
                });
                controller.decrementButton.setOnAction(e -> {
                    if (book.getQuantity() > 1) {
                        book.decreaseQuantity();
                        updateTotalAmount();
                    }
                });

                // Add the tile to the VBox
                tiles.getChildren().add(bookTile);
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
            System.out.println("Please enter a delivery address.");
        } else {
            System.out.println("Proceeding to payment.");
            System.out.println("Delivery address: " + address);
            System.out.println("Total amount: " + totalAmount + "₮");
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
