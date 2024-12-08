package com.example.warehouse.screen;

import com.example.warehouse.controller.BookCardController;
import com.example.warehouse.controller.HomeScreenController;
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
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Controller class for the Home Screen in the warehouse application.
 */
public class HomeScreen {

    private static final Logger LOGGER = Logger.getLogger(HomeScreen.class.getName());
    private static final String ALL_GENRES = "All";
    private static final int MAX_COLUMNS = 5;

    @FXML
    private GridPane grdBooks;

    @FXML
    private TextField txtSearchField;

    @FXML
    private Text txtLoggedUser;

    @FXML
    private HBox hbxAddBookMenu;

    @FXML
    private HBox hbxBtnCart;

    @FXML
    private ComboBox<String> cmbGenre;

    @FXML
    private Button btnLogout;

    private ObservableList<Book> olstAllBooks;
    private HomeScreenController hscController;

    /**
     * Initializes the Home Screen UI and loads data.
     */
    public void initialize() {
        setupController();
        setupGenreComboBox();
        setupSearchAndFilter();
        setupUserUI();
        loadAllBooks();
    }

    /**
     * Sets up the HomeScreenController.
     */
    private void setupController() {
        FirestoreService fsFirestoreService = new FirestoreService();
        hscController = new HomeScreenController(fsFirestoreService);
    }

    /**
     * Configures the genre combo box with predefined options.
     */
    private void setupGenreComboBox() {
        if (cmbGenre == null) {
            LOGGER.warning("Genre combo box is null");
            return;
        }

        cmbGenre.setItems(FXCollections.observableArrayList(
                ALL_GENRES, "Fiction", "Non-Fiction", "Science", "Children's Books",
                "Adventure", "History", "Romance", "Fantasy"
        ));
        cmbGenre.setValue(ALL_GENRES);
    }

    /**
     * Sets up search and filter listeners.
     */
    private void setupSearchAndFilter() {
        try {
            olstAllBooks = FXCollections.observableArrayList(hscController.getBookList());
            cmbGenre.setOnAction(event -> filterBooks());
            txtSearchField.textProperty().addListener((observable, oldValue, newValue) -> filterBooks());
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error loading books", e);
        }
    }

    /**
     * Sets up the UI visibility and labels based on the logged-in user's role.
     */
    private void setupUserUI() {
        User currentUser = UserSession.getInstance().getUser();
        if (currentUser != null) {
            txtLoggedUser.setText(currentUser.getName());
            boolean isAdmin = isAdmin();
            hbxAddBookMenu.setVisible(isAdmin);
            hbxBtnCart.setVisible(!isAdmin);
        }
    }

    /**
     * Loads all books into the grid view.
     */
    private void loadAllBooks() {
        if (olstAllBooks != null) {
            loadBooks(olstAllBooks);
        }
    }

    /**
     * Filters books based on the search text and selected genre.
     */
    public void filterBooks() {
        String searchText = txtSearchField.getText().toLowerCase();
        String selectedGenre = cmbGenre.getValue();

        if (olstAllBooks == null || searchText == null || selectedGenre == null) {
            return; // Exit early if any critical field is null
        }

        List<Book> filteredBooks = olstAllBooks.stream()
                .filter(book -> (ALL_GENRES.equals(selectedGenre) || book.getCategory().equalsIgnoreCase(selectedGenre))
                        && book.getTitle().toLowerCase().contains(searchText))
                .collect(Collectors.toList());

        loadBooks(filteredBooks);
    }

    /**
     * Loads a list of books into the grid layout.
     *
     * @param books The list of books to display.
     */
    private void loadBooks(List<Book> books) {
        grdBooks.getChildren().clear();
        int column = 0, row = 0;

        for (Book book : books) {
            try {
                Parent bookCard = loadBookCard(book);
                grdBooks.add(bookCard, column, row);

                if (++column == MAX_COLUMNS) {
                    column = 0;
                    row++;
                }
            } catch (IOException e) {
                LOGGER.log(Level.SEVERE, "Error loading book card", e);
            }
        }
    }

    /**
     * Loads a book card for the given book.
     *
     * @param book The book to display.
     * @return The parent node of the loaded book card.
     * @throws IOException if the FXML file cannot be loaded.
     */
    private Parent loadBookCard(Book book) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/warehouse/layout/card_book.fxml"));
        Parent bookCard = fxmlLoader.load();

        BookCardController bookCardController = fxmlLoader.getController();
        bookCardController.setBook(book);

        populateBookCardDetails(bookCard, book);
        return bookCard;
    }

    /**
     * Populates the book card with details.
     *
     * @param bookCard The parent node of the book card.
     * @param book     The book details.
     */
    private void populateBookCardDetails(Parent bookCard, Book book) {
        ImageView imgBookImage = (ImageView) bookCard.lookup("#bookImage");
        Label lblBookTitle = (Label) bookCard.lookup("#bookTitle");
        Label lblBookPrice = (Label) bookCard.lookup("#bookPrice");
        Text txtBookAuthor = (Text) bookCard.lookup("#author");
        Button btnAddToCart = (Button) bookCard.lookup("#addToCartButton");

        imgBookImage.setImage(new Image(book.getPictureUrl(), true));
        lblBookTitle.setText(book.getTitle());
        lblBookPrice.setText(book.getPrice() + "₮");
        txtBookAuthor.setText(book.getAuthor());

        btnAddToCart.setVisible(!isAdmin());
        btnAddToCart.setOnAction(event -> LOGGER.info(book.getTitle() + " added to cart!"));
    }

    /**
     * Checks if the current user is an admin.
     *
     * @return True if the user is an admin, false otherwise.
     */
    private boolean isAdmin() {
        User currentUser = UserSession.getInstance().getUser();
        return currentUser != null && "admin".equals(currentUser.getRole());
    }

    @FXML
    public void onLogoutButtonClick() {
        navigateToScreen("/com/example/warehouse/layout/screen_login.fxml");
    }

    public void handleForumMenu(MouseEvent mouseEvent) {
        navigateToScreen("/com/example/warehouse/layout/screen_forum.fxml");
    }

    public void handleGoToCart(MouseEvent mouseEvent) {
        navigateToScreen("/com/example/warehouse/layout/screen_cart.fxml");
    }

    /**
     * Navigates to the specified screen.
     *
     * @param fxmlPath The path to the FXML file of the screen.
     */
    private void navigateToScreen(String fxmlPath) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
            Stage stage = (Stage) grdBooks.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Navigation error to " + fxmlPath, e);
        }
    }
}
