package com.example.warehouse;

import com.example.warehouse.database.FirebaseConfig;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Application extends javafx.application.Application {
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

    public static void main(String[] args) {
        launch();
    }
}
