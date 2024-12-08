package com.example.warehouse.controller;

import com.example.warehouse.model.Book;
import com.example.warehouse.model.Cart;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
/**
 * Controller class responsible for handling interactions with individual book cards in the UI.
 * It is like single book component.
 * It provides functionality for viewing book details and adding books to the cart.
 *
 * <p>This class is typically used in the context of a book browsing or catalog screen.
 * Users can click on a book card to view its details or add the book to their shopping cart.</p>
 *
 * @author Margad
 * @version 1.0
 * @since 2024-11-28
 */
public class BookCardController {
    @FXML
    private VBox bookCard;
    private Book book;

    /**
     * Associates a specific {@link Book} object with this book card.
     *
     * @param book The {@link Book} object to associate with this card.
     */
    public void setBook(Book book) {
        System.out.println(book.getId());
        this.book = book;
    }

    /**
     * Handles the event triggered when a user clicks on the book card.
     * <p>
     * This method navigates to the book detail screen and passes the book details to it.
     * </p>
     *
     * @param mouseEvent The {@link MouseEvent} triggered by clicking on the book card.
     */
    public void handleBookClick(javafx.scene.input.MouseEvent mouseEvent) {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/com/example/warehouse/layout/screen_detail.fxml")
        );
        try {
            Parent bookDetailRoot = loader.load();

            BookDetailController bookDetailController = loader.getController();
            // Pass the book ID to the next screen
            bookDetailController.setBookId(book);

            Stage stage = (Stage) bookCard.getScene().getWindow();
            Scene scene = new Scene(bookDetailRoot);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Handles the event triggered when a user clicks the "Add to Cart" button on the book card.
     * <p>
     * This method adds the current book to the user's shopping cart with a default quantity of 1.
     * </p>
     *
     * @param mouseEvent The {@link MouseEvent} triggered by clicking the "Add to Cart" button.
     */
    public void handleAddToCart(MouseEvent mouseEvent) {
        Cart cart = Cart.getInstance();
        cart.addBookWithQuantity(book, 1);
    }
}
