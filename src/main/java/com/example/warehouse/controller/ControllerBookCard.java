package com.example.warehouse.controller;

import com.example.warehouse.model.Book;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.awt.event.MouseEvent;
import java.io.IOException;

public class ControllerBookCard {
    @FXML
    private AnchorPane bookCard; // Reference to the card container (AnchorPane)

    private Book book; // The book object for the current card

    public void setBook(Book book) {
        System.out.println(book.getId());
        this.book = book;
    }


    public void handleBookClick(javafx.scene.input.MouseEvent mouseEvent) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/warehouse/layout/screen_detail.fxml"));
        try {
            Parent bookDetailRoot = loader.load();

            ControllerBookDetail bookDetailController = loader.getController();
            bookDetailController.setBookId(book); // Pass the book ID to the next screen

            Stage stage = (Stage) bookCard.getScene().getWindow();
            Scene scene = new Scene(bookDetailRoot);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
