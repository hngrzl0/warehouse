package com.example.warehouse.controller;

import com.example.warehouse.model.Book;
import com.example.warehouse.model.User;
import com.example.warehouse.model.UserSession;
import com.example.warehouse.service.FirestoreService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class HomeScreenController {
    @FXML
    private GridPane books; // Reference to the GridPane in main.fxml

    @FXML
    private TextField searchField; // Reference to the search field

    @FXML
    private Text loggedUser;

    @FXML
    private HBox addBookMenu;

    @FXML
    private HBox btnCart;

    @FXML
    private ComboBox<String> genreComboBox; // Reference to the genre combo box
    private User user;

    @FXML
    private Button logoutButton;
    private final FirestoreService firestoreService;
    private ObservableList<Book> allBooks;

    public HomeScreenController() {
        this.firestoreService = new FirestoreService(); // Initialize Firestore service
    }

    public void setUser(User user){
        this.user = user;
    }

    public void initialize() {
        try {
            // Get all books from Firestore
            List<Book> bookList = firestoreService.getAllBooks();
            allBooks = FXCollections.observableArrayList(bookList);

            // Initialize ComboBox with genres (example, you can load these dynamically)
            if(genreComboBox != null){
                genreComboBox.setItems(FXCollections.observableArrayList("All", "Fiction", "Non-Fiction", "Science", "Children's Books", "Adventure", "History", "Romance", "Fantasy"));
                genreComboBox.setValue("All"); // Default value

            }else{
                System.out.println("home combo box null");
            }

            // Set up the search and filter functionality
            genreComboBox.setOnAction(event -> filterBooks());
            searchField.textProperty().addListener((observable, oldValue, newValue) -> filterBooks());

            // Load books into the GridPane
            loadBooks(allBooks);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        User currentUser = UserSession.getInstance().getUser();
        System.out.println("Logged in as a role of: " + currentUser.getRole());
        loggedUser.setText(currentUser.getName());
        if(Objects.equals(currentUser.getRole(), "user")){
            addBookMenu.setVisible(false);
        }
        else {
            addBookMenu.setVisible(Objects.equals(currentUser.getRole(), "admin"));
            btnCart.setVisible(false);
        }


    }

    private void loadBooks(List<Book> bookList) {
        books.getChildren().clear(); // Clear previous content
        int column = 0;
        int row = 0;
        int maxColumns = 5;

        for (Book book : bookList) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/warehouse/layout/card_book.fxml"));
                Parent bookCard = loader.load();

                ImageView bookImageView = (ImageView) bookCard.lookup("#bookImage");
                Label bookTitleLabel = (Label) bookCard.lookup("#bookTitle");
                Label bookPriceLabel = (Label) bookCard.lookup("#bookPrice");
                Label bookStockLabel = (Label) bookCard.lookup("#bookStock");
                Button addToCartButton = (Button) bookCard.lookup("#addToCartButton");

                InputStream imageStream = getClass().getResourceAsStream(book.getPictureUrl());
                if (imageStream == null) {
                    System.err.println("Image not found: " + book.getPictureUrl());
                    imageStream = getClass().getResourceAsStream("/com/example/warehouse/assets/book1.png");
                }
                bookImageView.setImage(new Image(imageStream));

                bookTitleLabel.setText(book.getTitle());
                bookPriceLabel.setText("" + book.getPrice());
                bookStockLabel.setText("" + book.getCount());

                addToCartButton.setOnAction(event -> {
                    System.out.println(book.getTitle() + " added to cart!");
                });

                BookCardController bookCardController = loader.getController();
                bookCardController.setBook(book);

                books.add(bookCard, column, row);

                column++;
                if (column == maxColumns) {
                    column = 0;
                    row++;
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private void filterBooks() {
        String searchText = searchField.getText().toLowerCase();
        String selectedGenre = genreComboBox.getValue();

        List<Book> filteredBooks = allBooks.stream()
                .filter(book -> (selectedGenre.equals("All") || book.getCategory().equals(selectedGenre)) &&
                        (book.getTitle().toLowerCase().contains(searchText)))
                .collect(Collectors.toList());

        loadBooks(filteredBooks); // Reload the filtered books into the GridPane
    }

    public void handleForumMenu(MouseEvent mouseEvent) {
        try {
            // Load the next screen FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/warehouse/layout/screen_forum.fxml"));
            Parent newRoot = loader.load();

            // Get the current stage and set the new scene
            Stage stage = (Stage) books.getScene().getWindow(); // Get the current stage
            Scene scene = new Scene(newRoot);
            stage.setScene(scene); // Set the new scene
            stage.show(); // Show the new scene

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void onLogoutButtonClick(){
        try {
            // Load the login screen FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/warehouse/layout/screen_login.fxml"));
            Parent loginScreen = loader.load();

            // Get the current stage
            Stage currentStage = (Stage) logoutButton.getScene().getWindow();

            // Set the new scene to the login screen
            Scene loginScene = new Scene(loginScreen);
            currentStage.setScene(loginScene);

            // Show the new scene
            currentStage.show();

            System.out.println("User logged out and redirected to the login screen.");
        } catch (IOException e) {
            System.err.println("Error occurred while navigating to the login screen: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void handleGoToCart(MouseEvent mouseEvent) {
        try {
            // Load the next screen FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/warehouse/layout/screen_cart.fxml"));
            Parent newRoot = loader.load();

            // Get the current stage and set the new scene
            Stage stage = (Stage) books.getScene().getWindow(); // Get the current stage
            Scene scene = new Scene(newRoot);
            stage.setScene(scene); // Set the new scene
            stage.show(); // Show the new scene

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
