package com.example.warehouse.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class LoginScreenController {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }
}