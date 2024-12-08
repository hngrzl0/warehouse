package com.example.warehouse.controller;

import com.example.warehouse.model.Book;
import com.example.warehouse.service.FirestoreService;
import javafx.collections.FXCollections;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class HomeScreenController {
    private final FirestoreService firestoreService;

    public HomeScreenController(FirestoreService firestoreService) {
        this.firestoreService = firestoreService;
    }

    public List<Book> getBookList(){

        List<Book> allBooks = new ArrayList<Book>();

        try {
            List<Book> bookList = firestoreService.getAllBooks();
            allBooks = FXCollections.observableArrayList(bookList);
        } catch (InterruptedException | ExecutionException e) {
            Logger logger = Logger.getLogger(getClass().getName());
            logger.log(Level.SEVERE, "Error occurred while performing IO operation", e);
        }
        return allBooks;
    }

}