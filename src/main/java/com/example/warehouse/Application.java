package com.example.warehouse;

import com.example.warehouse.database.FirebaseConfig;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Entry point for the Warehouse application.
 * <p>
 * This class extends {@link javafx.application.Application} to initialize the JavaFX application.
 * It sets up the primary stage and loads the login screen as the initial user interface.
 * The class also ensures Firebase is initialized before the UI setup.
 * </p>
 *
 * <p><b>Usage:</b> Run the `main` method to launch the application.</p>
 *
 * @author Khongorzul, Margad
 * @version 1.0
 * @since 2024-11-25
 */
public class Application extends javafx.application.Application {

    /**
     * Starts the JavaFX application by initializing Firebase and setting up the main stage.
     *
     * @param stage The primary stage for the JavaFX application.
     * @throws IOException If there is an error loading the FXML layout.
     */
    @Override
    public void start(Stage stage) throws IOException {
        // Initialize Firebase before setting up the UI
        FirebaseConfig.initializeFirebase();

        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("layout/screen_forum.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Book forum");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Main entry point for the application.
     * <p>
     * This method is invoked when the application is launched.
     * It delegates to start the JavaFX runtime.
     * </p>
     *
     * @param args Command-line arguments passed during application startup.
     */
    public static void main(String[] args) {
        launch();
    }
}
