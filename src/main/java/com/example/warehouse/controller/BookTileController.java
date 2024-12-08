package com.example.warehouse.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class BookTileController {

    @FXML
    private ImageView bookImage;

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
    private double price = 10000; // Example price (in your case, 10'000₮)

    @FXML
    public void initialize() {
        updateTotalPrice();
    }


    public void updateTotalPrice() {
        totalPriceLabel.setText(String.format("%.0f₮", quantity * price));
    }

    public void setBookDetails(String title, double price, String image) {
        this.bookTitle.setText(title);
        this.price = price;
        this.bookPrice.setText(String.format("%.0f₮", price));
        // This line can be used to set the image, ensure it's a valid image URL
//        this.bookImage.setImage(new Image(image));
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
        quantityLabel.setText(String.valueOf(quantity));
    }
}
