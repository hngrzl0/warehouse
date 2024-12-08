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
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Controller class responsible for managing the Home Screen of the warehouse application.
 * <p>
 * This class provides functionality to display books in a grid layout, search and filter books,
 * handle user role-based visibility, and navigate to other screens like the forum, cart, or login.
 * </p>
 *
 * @author Margad, Khongorzul
 * @version 1.0
 * @since 2024-12-08
 */
public class HomeScreenController {
    @FXML
    private GridPane books;

    @FXML
    private TextField searchField;

    @FXML
    private Text loggedUser;

    @FXML
    private HBox addBookMenu;

    @FXML
    private HBox btnCart;

    @FXML
    private ComboBox<String> genreComboBox;
    private User user;

    @FXML
    private Button logoutButton;
    private final FirestoreService firestoreService;
    private ObservableList<Book> allBooks;


    /**
     * Constructs a {@code HomeScreenController} and initializes the Firestore service.
     */
    public HomeScreenController() {
        this.firestoreService = new FirestoreService();
    }

    /**
     * Sets the logged-in user.
     *
     * @param user The currently logged-in {@link User}.
     */
    public void setUser(User user){
        this.user = user;
    }

    /**
     * Initializes the controller, loads books, sets up filtering functionality, and configures UI
     * elements based on the user's role.
     */
    public void initialize() {
        try {
            // Get all books from Firestore
            List<Book> bookList = firestoreService.getAllBooks();
            allBooks = FXCollections.observableArrayList(bookList);
            if(genreComboBox != null){
                genreComboBox.setItems(FXCollections.observableArrayList("All", "Fiction", "Non-Fiction", "Science", "Children's Books", "Adventure", "History", "Romance", "Fantasy"));
                genreComboBox.setValue("All");
            }else{
                System.out.println("home combo box null");
            }
            // Set up the search and filter functionality
            genreComboBox.setOnAction(event -> filterBooks());
            searchField.textProperty()
                    .addListener((observable, oldValue, newValue) -> filterBooks());
            // Load books into the GridPane
            loadBooks(allBooks);
        } catch (InterruptedException | ExecutionException e) {
            Logger logger = Logger.getLogger(getClass().getName());
            logger.log(Level.SEVERE, "Error occurred while performing IO operation", e);
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


    /**
     * Loads a list of books into the GridPane.
     *
     * @param bookList A list of {@link Book} objects to be displayed.
     */
    private void loadBooks(List<Book> bookList) {
        books.getChildren().clear();
        int column = 0;
        int row = 0;
        int maxColumns = 5;

        for (Book book : bookList) {
            try {
                FXMLLoader loader = new FXMLLoader(
                        getClass().getResource("/com/example/warehouse/layout/card_book.fxml")
                );
                Parent bookCard = loader.load();
                User currentUser = UserSession.getInstance().getUser();
                ImageView bookImageView = (ImageView) bookCard.lookup("#bookImage");
                Label bookTitleLabel = (Label) bookCard.lookup("#bookTitle");
                Label bookPriceLabel = (Label) bookCard.lookup("#bookPrice");
                Text bookAuthorText = (Text) bookCard.lookup("#author");
                Button addToCartButton = (Button) bookCard.lookup("#addToCartButton");
                if(Objects.equals(currentUser.getRole(), "admin")){
                    addToCartButton.setVisible(false);
                }
                bookImageView.setImage(new Image(book.getPictureUrl()));  // Set the book image

                bookTitleLabel.setText(book.getTitle());
                bookPriceLabel.setText("" + book.getPrice() + "₮");
                bookAuthorText.setText("" + book.getAuthor());

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
                Logger logger = Logger.getLogger(getClass().getName());
                logger.log(Level.SEVERE, "Error occurred while performing IO operation", e);
            }
        }
    }

    /**
     * Filters the books displayed in the GridPane based on search input and selected genre.
     */
    private void filterBooks() {
        String searchText = searchField.getText().toLowerCase();
        String selectedGenre = genreComboBox.getValue();

        List<Book> filteredBooks = allBooks.stream()
                .filter(book -> (selectedGenre.equals("All") || book.getCategory().equals(selectedGenre)) &&
                        (book.getTitle().toLowerCase().contains(searchText)))
                .collect(Collectors.toList());

        loadBooks(filteredBooks);
    }

    /**
     * Navigates to the forum screen.
     *
     * @param mouseEvent The event triggered by clicking the forum menu.
     */
    public void handleForumMenu(MouseEvent mouseEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/warehouse/layout/screen_forum.fxml"));
            Parent newRoot = loader.load();
            Stage stage = (Stage) books.getScene().getWindow();
            Scene scene = new Scene(newRoot);
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            Logger logger = Logger.getLogger(getClass().getName());
            logger.log(Level.SEVERE, "Error occurred while performing IO operation", e);
        }
    }

    /**
     * Logs out the current user and navigates to the login screen.
     */
    @FXML
    public void onLogoutButtonClick(){
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/example/warehouse/layout/screen_login.fxml")
            );
            Parent loginScreen = loader.load();
            Stage currentStage = (Stage) logoutButton.getScene().getWindow();
            Scene loginScene = new Scene(loginScreen);
            currentStage.setScene(loginScene);
            currentStage.show();
            System.out.println("User logged out and redirected to the login screen.");
        } catch (IOException e) {
            System.err.println("Error occurred while navigating to the login screen: " + e.getMessage());
            Logger logger = Logger.getLogger(getClass().getName());
            logger.log(Level.SEVERE, "Error occurred while performing IO operation", e);
        }
    }

    /**
     * Navigates to the cart screen.
     *
     * @param mouseEvent The event triggered by clicking the cart button.
     */
    public void handleGoToCart(MouseEvent mouseEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/warehouse/layout/screen_cart.fxml"));
            Parent newRoot = loader.load();
            Stage stage = (Stage) books.getScene().getWindow();
            Scene scene = new Scene(newRoot);
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            Logger logger = Logger.getLogger(getClass().getName());
            logger.log(Level.SEVERE, "Error occurred while performing IO operation", e);
        }
    }
}
