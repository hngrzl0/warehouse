package com.example.warehouse.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Cart {
    private static Cart instance;
    private final Map<String, Book> cartBooks = new HashMap<>(); // Use a map for unique titles

    private Cart() {
    }

    public static Cart getInstance() {
        if (instance == null) {
            instance = new Cart();
        }
        return instance;
    }

    public void addBookWithQuantity(Book book, int quantity) {
        if (cartBooks.containsKey(book.getTitle())) {
            // If the book already exists, update its quantity
            Book existingBook = cartBooks.get(book.getTitle());
            existingBook.setCount(existingBook.getCount() + quantity);
        } else {
            // Otherwise, add a new book to the cart
            book.setCount(quantity);
            cartBooks.put(book.getTitle(), book);
        }
    }

    public List<Book> getBooks() {
        return new ArrayList<>(cartBooks.values());
    }

    public void clearCart() {
        cartBooks.clear();
    }
}
