package com.example.warehouse;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class CartScreenController {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }
}