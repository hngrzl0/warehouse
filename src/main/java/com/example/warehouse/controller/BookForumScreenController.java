package com.example.warehouse.controller;

import com.example.warehouse.model.Book;
import com.example.warehouse.service.FirestoreService;
import java.util.concurrent.ExecutionException;

public class BookForumScreenController {

    private final FirestoreService firestoreService;

    public BookForumScreenController(FirestoreService firestoreService) {
        this.firestoreService = firestoreService;
    }

    public void addBook(Book book) {
        try {
            firestoreService.createBook(book);
        } catch (InterruptedException | ExecutionException e) {
            System.err.println("Error adding book: " + e.getMessage());
        }
    }
}
