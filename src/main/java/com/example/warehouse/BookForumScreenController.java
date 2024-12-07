//package com.example.warehouse;
//
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.example.warehouse.model.Book;
//import com.google.api.core.ApiFuture;
//
//public class BookForumScreenController {
//
//    private final DatabaseReference databaseReference;
//
//    public BookForumScreenController() {
//        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
//        this.databaseReference = firebaseDatabase.getReference("book");
//    }
//
//    public void addBook(Book book) {
//        String bookId = databaseReference.push().getKey();
//        if (bookId != null) {
//            ApiFuture<Void> future = databaseReference.child(bookId).setValueAsync(book);
//            try {
//                // Blocking call to wait for the result
//                future.get();
//                System.out.println("Book added successfully!");
//            } catch (Exception e) {
//                System.err.println("Error adding book: " + e.getMessage());
//            }
//        }
//    }
//}

package com.example.warehouse;

import com.example.warehouse.model.Book;
import com.example.warehouse.service.FirestoreService;
import java.util.concurrent.ExecutionException;

public class BookForumScreenController {

    private final FirestoreService firestoreService;

    public BookForumScreenController(FirestoreService firestoreService) {
//        this.firestoreService = firestoreService;
        this.firestoreService = firestoreService;
    }

    // Method to add a book to Firestore
    public void addBook(Book book) {
        try {
            firestoreService.createBook(book);  // Add book to Firestore
            System.out.println("Book added successfully!");
        } catch (InterruptedException | ExecutionException e) {
            System.err.println("Error adding book: " + e.getMessage());
        }
    }
}
