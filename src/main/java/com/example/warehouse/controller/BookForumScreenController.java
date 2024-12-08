package com.example.warehouse.controller;

import com.example.warehouse.model.Book;
import com.example.warehouse.service.FirestoreService;
import java.util.concurrent.ExecutionException;

/**
 * Controller class responsible for handling the logic of the Book Forum screen.
 * <p>
 * This class manages the functionality for adding books to the Firestore database.
 * It interacts with the {@link FirestoreService} to perform create operations
 * for books.
 * </p>
 *
 * @author Margad
 * @version 1.0
 * @since 2024-12-08
 */
public class BookForumScreenController {

    private final FirestoreService firestoreService;

    /**
     * Constructs a {@code BookForumScreenController} with the specified {@link FirestoreService}.
     *
     * @param firestoreService The service responsible for interacting with Firestore.
     */
    public BookForumScreenController(FirestoreService firestoreService) {

        this.firestoreService = firestoreService;
    }

    /**
     * Adds a new book to the Firestore database.
     * <p>
     * This method calls the {@link FirestoreService#createBook(Book)} method to add the
     * provided {@link Book} object to the database. If an exception occurs during the operation,
     * an error message is printed to the console.
     * </p>
     *
     * @param book The {@link Book} object to be added to the database.
     */
    public void addBook(Book book) {
        try {
            firestoreService.createBook(book);
        } catch (InterruptedException | ExecutionException e) {
            System.err.println("Error adding book: " + e.getMessage());
        }
    }
}
