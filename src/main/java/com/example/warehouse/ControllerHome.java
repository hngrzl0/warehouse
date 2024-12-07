package com.example.warehouse;

import com.example.warehouse.model.Book;
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

public class ControllerHome {
    @FXML
    private GridPane books; // Reference to the GridPane in main.fxml

    public void initialize() {
        List<Book> bookList = List.of(
                new Book("Ном 1", "10'000₮", 100, "/com/example/warehouse/assets/book1.png"),
                new Book("Ном 2", "12'000₮", 50, "/com/example/warehouse/assets/book1.png"),
                new Book("Ном 3", "15'000₮", 70, "/com/example/warehouse/assets/book1.png"),
                new Book("Ном 4", "8'000₮", 120, "/com/example/warehouse/assets/book1.png"),
                new Book("Ном 5", "5'000₮", 200, "/com/example/warehouse/assets/book1.png"),
                new Book("Ном 6", "18'000₮", 30, "/com/example/warehouse/assets/book1.png"),
                new Book("Ном 6", "18'000₮", 30, "/com/example/warehouse/assets/book1.png"),
                new Book("Ном 6", "18'000₮", 30, "/com/example/warehouse/assets/book1.png"),
                new Book("Ном 6", "18'000₮", 30, "/com/example/warehouse/assets/book1.png"),
                new Book("Ном 6", "18'000₮", 30, "/com/example/warehouse/assets/book1.png"),
                new Book("Ном 6", "18'000₮", 30, "/com/example/warehouse/assets/book1.png"),
                new Book("Ном 6", "18'000₮", 30, "/com/example/warehouse/assets/book1.png")

        );
        //aa

        loadBooks(bookList);
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

                InputStream imageStream = getClass().getResourceAsStream(book.getImagePath());
                if (imageStream == null) {
                    System.err.println("Image not found: " + book.getImagePath());
                    imageStream = getClass().getResourceAsStream("/com/example/warehouse/assets/default.png");
                }
                bookImageView.setImage(new Image(imageStream));

                bookTitleLabel.setText(book.getName());
                bookPriceLabel.setText(book.getPrice());
                bookStockLabel.setText("" + book.getStock());

                addToCartButton.setOnAction(event -> {
                    System.out.println(book.getName() + " added to cart!");
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
