package com.example.warehouse.controller;

import com.example.warehouse.model.User;
import com.example.warehouse.service.FirestoreService;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.util.concurrent.ExecutionException;

public class LoginScreenController {
    private final FirestoreService firestoreService;
    public LoginScreenController(FirestoreService firestoreService){
        this.firestoreService = firestoreService;
    }

    public User validateUser(String username, String password){
        try{
            return firestoreService.authenticateUser(username, password);
        }
        catch (InterruptedException | ExecutionException e){
            System.err.println("Error validating user: " + e.getMessage());
        }
        return null;

    }
}