package com.example.warehouse.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents a shopping cart model for a warehouse system.
 * <p>
 * The {@code Cart} class manages a collection of books added to a user's cart.
 * It uses a {@link HashMap} to store books with their quantities,
 * allowing for quick access and updates. The cart follows the singleton pattern
 * to ensure there is only one instance throughout the application.
 * </p>
 * <p>
 * You can add books with a specified quantity, retrieve the list of books in the cart,
 * and clear the cart when needed.
 * </p>
 * @author Margad, Khongorzul
 * @version 1.0
 * @since 2024-11-25
 */
public class Cart {
    private static Cart instance;

    // Use a map for unique titles
    private final Map<String, Book> cartBooks = new HashMap<>();

    private Cart() {
    }

    /**
     * Returns the singleton instance of the cart.
     *
     * @return the singleton instance of {@code Cart}
     */
    public static Cart getInstance() {
        if (instance == null) {
            instance = new Cart();
        }
        return instance;
    }

    /**
     * Adds a book to the cart with a specified quantity. If the book already exists in the cart,
     * its quantity will be updated.
     *
     * @param book     the book to add to the cart
     * @param quantity the quantity of the book to add
     */
    public void addBookWithQuantity(Book book, int quantity) {
        if (cartBooks.containsKey(book.getTitle())) {
            // If the book already exists, update its quantity
            Book existingBook = cartBooks.get(book.getTitle());
            existingBook.setCount(existingBook.getCount() + quantity);
        } else {
            book.setCount(quantity);
            cartBooks.put(book.getTitle(), book);
        }
    }

    /**
     * Retrieves a list of all books currently in the cart.
     *
     * @return a list of {@link Book} objects in the cart
     */
    public List<Book> getBooks() {
        return new ArrayList<>(cartBooks.values());
    }

    /**
     * Clears all books from the cart.
     */
    public void clearCart() {
        cartBooks.clear();
    }
}
