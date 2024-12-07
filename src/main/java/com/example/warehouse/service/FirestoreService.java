package com.example.warehouse.service;

import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.WriteResult;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.example.warehouse.model.Book;
import com.google.firebase.cloud.FirestoreClient;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class FirestoreService {

    private final Firestore db;

    public FirestoreService() {
        this.db = FirestoreClient.getFirestore();
    }

    // Create Book
    public void createBook(Book book) throws InterruptedException, ExecutionException {
        CollectionReference books = db.collection("books");

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

    // Read Book by ID
    public Book getBookById(String bookId) throws InterruptedException, ExecutionException {
        DocumentReference bookRef = db.collection("books").document(bookId);
        QueryDocumentSnapshot document = (QueryDocumentSnapshot) bookRef.get().get();

        if (document.exists()) {
            // Convert Firestore document to Book object
            Map<String, Object> bookData = document.getData();
            Book book = new Book(
                    (String) bookData.get("title"),
                    (String) bookData.get("author"),
                    (java.util.Date) bookData.get("publishedYear"),
                    (String) bookData.get("isbn"),
                    (Double) bookData.get("price"),
                    ((Long) bookData.get("count")).intValue(),
                    (String) bookData.get("description"),
                    (String) bookData.get("pictureUrl"),
                    (String) bookData.get("category")
            );
            return book;
        } else {
            System.out.println("No such book found.");
            return null;
        }
    }

    // Update Book
    public void updateBook(String bookId, Book updatedBook) throws InterruptedException, ExecutionException {
        DocumentReference bookRef = db.collection("books").document(bookId);

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

    // Delete Book
    public void deleteBook(String bookId) throws InterruptedException, ExecutionException {
        DocumentReference bookRef = db.collection("books").document(bookId);
        WriteResult result = bookRef.delete().get();
        System.out.println("Deleted book at " + result.getUpdateTime());
    }
}
