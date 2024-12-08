package com.example.warehouse.controller;

import com.example.warehouse.model.Book;
import com.example.warehouse.model.Cart;

import com.example.warehouse.model.*;
import com.example.warehouse.screen.CartScreen;
import javafx.scene.control.Alert;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller class responsible for handling the business logic related to the cart screen.
 * It interacts with the {@link Cart} to update and manage the shopping cart.
 */
public class CartScreenController {
    public List<CartScreen.BookWithQuantity> loadBooks() {
        List<CartScreen.BookWithQuantity> cartScreenBooks = new ArrayList<>();
        for (Book cartBook : Cart.getInstance().getBooks()) {
            CartScreen.BookWithQuantity screenBook = new CartScreen.BookWithQuantity(cartBook);
            screenBook.setQuantity(cartBook.getCount());
            cartScreenBooks.add(screenBook);
        }
        return cartScreenBooks;
    }

    private double updateTotalAmount(List<CartScreen.BookWithQuantity> cartScreenBooks) {
        return cartScreenBooks.stream()
                .mapToDouble(book -> book.getBook().getPrice() * book.getQuantity())
                .sum();
    }

    public void proceedToPayment(String address, String totalAmount) {
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

    public void cancelOrder(List<CartScreen.BookWithQuantity> cartScreenBooks, CartScreen cartScreen) {
        cartScreenBooks.forEach(book -> book.setQuantity(1));
        cartScreen.addressTf.clear();
        cartScreen.displayBooks(cartScreenBooks);
        cartScreen.updateTotalAmount(cartScreenBooks);
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
