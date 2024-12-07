package com.example.warehouse.controller;

import com.example.warehouse.model.Book;
import com.example.warehouse.service.FirestoreService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class ControllerHome {
    @FXML
    private GridPane books; // Reference to the GridPane in main.fxml

    private final FirestoreService firestoreService;

    public ControllerHome() {
        this.firestoreService = new FirestoreService(); // Initialize Firestore service
    }

    public void initialize() {
        try {
            // Get all books from Firestore
            List<Book> bookList = firestoreService.getAllBooks();

            // Load books into the GridPane
            loadBooks(bookList);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    private void loadBooks(List<Book> bookList) {
        int column = 0;
        int row = 0;
        int maxColumns = 5;

        for (Book book : bookList) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/warehouse/layout/card_book.fxml"));
                Parent bookCard = loader.load();

                ImageView bookImageView = (ImageView) bookCard.lookup("#bookImage");
                Label bookTitleLabel = (Label) bookCard.lookup("#bookTitle");
                Label bookPriceLabel = (Label) bookCard.lookup("#bookPrice");
                Label bookStockLabel = (Label) bookCard.lookup("#bookStock");
                Button addToCartButton = (Button) bookCard.lookup("#addToCartButton");

                InputStream imageStream = getClass().getResourceAsStream(book.getPictureUrl());
                if (imageStream == null) {
                    System.err.println("Image not found: " + book.getPictureUrl());
                    imageStream = getClass().getResourceAsStream("/com/example/warehouse/assets/book1.png");
                }
                bookImageView.setImage(new Image(imageStream));

                bookTitleLabel.setText(book.getTitle());
                bookPriceLabel.setText("" + book.getPrice());
                bookStockLabel.setText("" + book.getCount());

                addToCartButton.setOnAction(event -> {
                    System.out.println(book.getTitle() + " added to cart!");
                });

                books.add(bookCard, column, row);

                column++;
                if (column == maxColumns) {
                    column = 0;
                    row++;
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
