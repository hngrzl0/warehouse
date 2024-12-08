package com.example.warehouse.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

/**
 * Controller class responsible for managing the display and interactions of a book tile in the cart or catalog.
 * <p>
 * This class provides functionality to display a book's details, including its title, price, and image,
 * and enables quantity adjustment (increment and decrement). It also calculates and displays the total price
 * based on the book's quantity.
 * </p>
 *
 * @author Margad
 * @version 1.0
 * @since 2024-11-28
 */
public class BookTileController {

    @FXML
    private Label bookTitle;

    @FXML
    private Label bookPrice;

    @FXML
    private Label quantityLabel;

    @FXML
    private Label totalPriceLabel;

    @FXML
    public Button incrementButton;

    @FXML
    public Button decrementButton;

    private int quantity;
    private double price = 10000;

    /**
     * Initializes the controller by setting up the default total price calculation.
     */
    @FXML
    public void initialize() {
        updateTotalPrice();
    }


    /**
     * Updates the total price label based on the book's quantity and unit price.
     */
    public void updateTotalPrice() {
        totalPriceLabel.setText(String.format("%.0f₮", quantity * price));
    }

    /**
     * Sets the details of the book, including its title, price, and image.
     *
     * @param title The title of the book.
     * @param price The unit price of the book.
     * @param image The URL of the book's image.
     */
    public void setBookDetails(String title, double price, String image) {
        this.bookTitle.setText(title);
        this.price = price;
        this.bookPrice.setText(String.format("%.0f₮", price));
        // This line can be used to set the image, ensure it's a valid image URL
//        this.bookImage.setImage(new Image(image));
    }

    /**
     * Sets the quantity of the book and updates the corresponding label.
     *
     * @param quantity The quantity to set.
     */
    public void setQuantity(int quantity) {
        this.quantity = quantity;
        quantityLabel.setText(String.valueOf(quantity));
    }
}
