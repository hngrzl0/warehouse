package com.example.warehouse.service;

import com.example.warehouse.model.User;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.example.warehouse.model.Book;
import com.google.firebase.cloud.FirestoreClient;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * Service class for interacting with the Firestore database.
 * <p>
 * This class provides methods to perform CRUD operations on the Firestore database
 * for both {@link Book} and {@link User} objects. It handles Firestore interactions
 * such as creating, reading, updating, and deleting records, as well as authenticating users.
 * </p>
 *
 * <p><b>Usage:</b> Instantiate this class and call its methods to interact with Firestore.</p>
 *
 * @author Khongorzul, Margad
 * @version 1.0
 * @since 2024-11-25
 */
public class FirestoreService {

    private final Firestore db;

    /**
     * Initializes a new instance of {@code FirestoreService} and connects to Firestore.
     */
    public FirestoreService() {
        this.db = FirestoreClient.getFirestore();
    }

    /**
     * Creates a new {@link Book} record in Firestore.
     *
     * @param book The {@code Book} object to be added.
     * @throws InterruptedException If the thread is interrupted while waiting.
     * @throws ExecutionException   If the computation threw an exception.
     */
    public void createBook(Book book) throws InterruptedException, ExecutionException {
        CollectionReference books = db.collection("book");

        // Convert Book object to Map for Firestore
        Map<String, Object> bookData = new HashMap<>();
        bookData.put("title", book.getTitle());
        bookData.put("author", book.getAuthor());
        bookData.put("publishedYear", book.getPublishedYear());
        bookData.put("isbn", book.getIsbn());
        bookData.put("price", book.getPrice());
        bookData.put("count", book.getCount());
        bookData.put("description", book.getDescription());
        bookData.put("pictureUrl", book.getPictureUrl());
        bookData.put("category", book.getCategory());


        // Add the book document to Firestore
        DocumentReference result = books.add(bookData).get();  // Blocking call
        System.out.println("Book added with ID: " + result.getId());
    }

    /**
     * Retrieves a {@link Book} by its unique ID.
     *
     * @param bookId The ID of the book to retrieve.
     * @return A {@code Book} object if found; otherwise {@code null}.
     * @throws InterruptedException If the thread is interrupted while waiting.
     * @throws ExecutionException   If the computation threw an exception.
     */
    public Book getBookById(String bookId) throws InterruptedException, ExecutionException {
        DocumentReference bookRef = db.collection("book").document(bookId);
        QueryDocumentSnapshot document = (QueryDocumentSnapshot) bookRef.get().get();

        if (document.exists()) {
            // Convert Firestore document to Book object
            Map<String, Object> bookData = document.getData();
            Book book = new Book(
                    (String) bookData.get("title"),
                    (String) bookData.get("author"),
                    (String) bookData.get("publishedYear"),
                    (String) bookData.get("isbn"),
                    (Double) bookData.get("price"),
                    ((Long) bookData.get("count")).intValue(),
                    (String) bookData.get("description"),
                    (String) bookData.get("pictureUrl"),
                    (String) bookData.get("category"),
                    (String) document.getId()

            );
            return book;
        } else {
            System.out.println("No such book found.");
            return null;
        }
    }

    /**
     * Retrieves all {@link Book} records from Firestore.
     *
     * @return A list of {@code Book} objects.
     * @throws InterruptedException If the thread is interrupted while waiting.
     * @throws ExecutionException   If the computation threw an exception.
     */
    public List<Book> getAllBooks() throws InterruptedException, ExecutionException {
        CollectionReference booksCollection = db.collection("book");
        List<Book> booksList = new ArrayList<>();

        // Get all books from the collection
        Iterable<QueryDocumentSnapshot> documents = booksCollection.get().get().getDocuments();

        for (QueryDocumentSnapshot document : documents) {
            // Convert Firestore document to Book object
            Map<String, Object> bookData = document.getData();
            Book book = new Book(
                    (String) bookData.get("title"),
                    (String) bookData.get("author"),
                    (String) bookData.get("publishedYear"),
                    (String) bookData.get("isbn"),
                    (Double) bookData.get("price"),
                    ((Long) bookData.get("count")).intValue(),
                    (String) bookData.get("description"),
                    (String) bookData.get("pictureUrl"),
                    (String) bookData.get("category"),
                    (String) document.getId()

            );
            booksList.add(book);
        }

        return booksList;
    }

    /**
     * Updates an existing {@link Book} record in Firestore.
     *
     * @param bookId      The ID of the book to update.
     * @param updatedBook A {@code Book} object containing updated information.
     * @throws InterruptedException If the thread is interrupted while waiting.
     * @throws ExecutionException   If the computation threw an exception.
     */
    public void updateBook(String bookId, Book updatedBook) throws InterruptedException, ExecutionException {
        DocumentReference bookRef = db.collection("book").document(bookId);

        // Convert updated Book object to Map for Firestore
        Map<String, Object> updatedData = new HashMap<>();
        updatedData.put("title", updatedBook.getTitle());
        updatedData.put("author", updatedBook.getAuthor());
        updatedData.put("publishedYear", updatedBook.getPublishedYear());
        updatedData.put("isbn", updatedBook.getIsbn());
        updatedData.put("price", updatedBook.getPrice());
        updatedData.put("count", updatedBook.getCount());
        updatedData.put("description", updatedBook.getDescription());
        updatedData.put("pictureUrl", updatedBook.getPictureUrl());
        updatedData.put("category", updatedBook.getCategory());

        // Update book document in Firestore
        WriteResult result = bookRef.update(updatedData).get();
        System.out.println("Updated book at " + result.getUpdateTime());
    }

    /**
     * Deletes a {@link Book} record from Firestore.
     *
     * @param bookId The ID of the book to delete.
     * @throws InterruptedException If the thread is interrupted while waiting.
     * @throws ExecutionException   If the computation threw an exception.
     */
    public void deleteBook(String bookId) throws InterruptedException, ExecutionException {
        DocumentReference bookRef = db.collection("book").document(bookId);
        WriteResult result = bookRef.delete().get();
        System.out.println("Deleted book at " + result.getUpdateTime());
    }

    /**
     * Authenticates a {@link User} using their username and password.
     *
     * @param username The username of the user.
     * @param password The password of the user.
     * @return A {@code User} object if authentication is successful; otherwise {@code null}.
     * @throws InterruptedException If the thread is interrupted while waiting.
     * @throws ExecutionException   If the computation threw an exception.
     */
    public User authenticateUser(String username, String password) throws InterruptedException, ExecutionException {
        CollectionReference usersCollection = db.collection("user");
        Query query = usersCollection.whereEqualTo("username", username).whereEqualTo("password", password);
        ApiFuture<QuerySnapshot> querySnapshot = query.get();

        if (!querySnapshot.get().isEmpty()) {
            // Convert Firestore document to User object
            QueryDocumentSnapshot document = querySnapshot.get().getDocuments().get(0);
            return document.toObject(User.class);
        } else {
            return null;
        }
    }
}
