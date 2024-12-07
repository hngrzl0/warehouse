package com.example.warehouse.controller;

import com.example.warehouse.model.User;
import com.example.warehouse.service.FirestoreService;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.util.concurrent.ExecutionException;

/**
 * Controller class responsible for handling the logic related to user login.
 * It interacts with the {@link FirestoreService} to authenticate users and
 * validate their credentials.
 *
 * <p>This class is typically used by the LoginScreen view to validate
 * username and password input and determine whether the user is authorized.</p>
 *
 * @author Khongorzul
 * @version 1.0
 * @since 2024-11-25
 */
public class LoginScreenController {
    private final FirestoreService firestoreService;

    /**
     * Constructs a {@code LoginScreenController} with a specified {@link FirestoreService}.
     *
     * @param firestoreService The service responsible for interacting with Firestore for authentication.
     */
    public LoginScreenController(FirestoreService firestoreService){
        this.firestoreService = firestoreService;
    }


    /**
     * Validates the username and password provided by the user.
     * <p>
     * This method checks the credentials against the Firestore database.
     * If the username and password match an existing user record, the corresponding
     * {@link User} object is returned. Otherwise, it returns {@code null}.
     * </p>
     *
     * @param username The username input provided by the user.
     * @param password The password input provided by the user.
     * @return A {@link User} object if the credentials are valid; {@code null} otherwise.
     */
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