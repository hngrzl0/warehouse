package com.example.warehouse.model;

import java.util.ArrayList;
import java.util.List;

public class Cart {
    private List<Book> books;
    private static Cart instance;

    private Cart() {
        books = new ArrayList<>();
    }

    // Singleton instance
    public static Cart getInstance() {
        if (instance == null) {
            instance = new Cart();
        }
        return instance;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void addBook(Book book) {
        books.add(book);
    }

    public void removeBook(Book book) {
        books.remove(book);
    }

    public void clear() {
        books.clear();
    }
}
