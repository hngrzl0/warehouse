package com.example.warehouse.database;

import java.io.FileInputStream;
import java.io.IOException;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.auth.oauth2.GoogleCredentials;

/**
 * Provides configuration and initialization for Firebase services in the application.
 * <p>
 * This class is responsible for initializing a Firebase application using the service account
 * credentials file and database URL.
 * </p>
 *
 * @author Khongorzul, Margad
 * @version 1.0
 * @since 2024-11-25
 */
public class FirebaseConfig {

    /**
     * Initializes the Firebase application.
     * <p>
     * This method loads the service account credentials from a JSON file and uses it to
     * configure Firebase services. It also sets the Firebase Realtime Database URL.
     * </p>
     *
     * @throws IOException if the service account credentials file cannot be read.
     */
    public static void initializeFirebase() {
        try {
            //service account private key directory
            FileInputStream serviceAccount = new FileInputStream("src/main/resources/google-services.json");

            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setDatabaseUrl("https://warehouse-b829d-default-rtdb.asia-southeast1.firebasedatabase.app/")
                    .build();

            FirebaseApp.initializeApp(options);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
