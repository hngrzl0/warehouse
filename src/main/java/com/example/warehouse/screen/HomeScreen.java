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

    private static final String ALL_GENRES = "All";

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
        if (cmbGenre != null) {
            cmbGenre.setItems(FXCollections.observableArrayList(
                    ALL_GENRES, "Fiction", "Non-Fiction", "Science", "Children's Books",
                    "Adventure", "History", "Romance", "Fantasy"
            ));
            cmbGenre.setValue(ALL_GENRES);
        } else {
            Logger.getLogger(getClass().getName()).warning("Genre combo box is null");
        }
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
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Error loading books", e);
        }
    }

    /**
     * Sets up the UI visibility and labels based on the logged-in user's role.
     */
    private void setupUserUI() {
        User usrCurrentUser = UserSession.getInstance().getUser();
        if (usrCurrentUser != null) {
            txtLoggedUser.setText(usrCurrentUser.getName());
            boolean isAdmin = Objects.equals(usrCurrentUser.getRole(), "admin");
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
        String strSearchText = txtSearchField.getText().toLowerCase();
        String strSelectedGenre = cmbGenre.getValue();

        List<Book> lstFilteredBooks = olstAllBooks.stream()
                .filter(book -> (ALL_GENRES.equals(strSelectedGenre) || book.getCategory().equalsIgnoreCase(strSelectedGenre)) &&
                        (book.getTitle().toLowerCase().contains(strSearchText)))
                .collect(Collectors.toList());

        loadBooks(lstFilteredBooks);
    }

    /**
     * Loads a list of books into the grid layout.
     *
     * @param lstBooks The list of books to display.
     */
    private void loadBooks(List<Book> lstBooks) {
        grdBooks.getChildren().clear();
        int iColumn = 0, iRow = 0, iMaxColumns = 5;

        for (Book bkBook : lstBooks) {
            try {
                Parent prtBookCard = loadBookCard(bkBook);
                grdBooks.add(prtBookCard, iColumn++, iRow);

                if (iColumn == iMaxColumns) {
                    iColumn = 0;
                    iRow++;
                }
            } catch (IOException e) {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Error loading book card", e);
            }
        }
    }

    /**
     * Loads a book card for the given book.
     *
     * @param bkBook The book to display.
     * @return The parent node of the loaded book card.
     * @throws IOException if the FXML file cannot be loaded.
     */
    private Parent loadBookCard(Book bkBook) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/warehouse/layout/card_book.fxml"));
        Parent prtBookCard = fxmlLoader.load();

        BookCardController bccBookCardController =  fxmlLoader.getController();
        bccBookCardController.setBook(bkBook);

        populateBookCardDetails(prtBookCard, bkBook);
        return prtBookCard;
    }

    /**
     * Populates the book card with details.
     *
     * @param prtBookCard The parent node of the book card.
     * @param bkBook      The book details.
     */
    private void populateBookCardDetails(Parent prtBookCard, Book bkBook) {
        ImageView imgBookImage = (ImageView) prtBookCard.lookup("#bookImage");
        Label lblBookTitle = (Label) prtBookCard.lookup("#bookTitle");
        Label lblBookPrice = (Label) prtBookCard.lookup("#bookPrice");
        Text txtBookAuthor = (Text) prtBookCard.lookup("#author");
        Button btnAddToCart = (Button) prtBookCard.lookup("#addToCartButton");

        imgBookImage.setImage(new Image(bkBook.getPictureUrl(), true));
        lblBookTitle.setText(bkBook.getTitle());
        lblBookPrice.setText(bkBook.getPrice() + "₮");
        txtBookAuthor.setText(bkBook.getAuthor());


        btnAddToCart.setOnAction(event -> Logger.getLogger(getClass().getName())
                .info(bkBook.getTitle() + " added to cart!"));
        btnAddToCart.setVisible(!isAdmin());
    }

    /**
     * Checks if the current user is an admin.
     *
     * @return True if the user is an admin, false otherwise.
     */
    private boolean isAdmin() {
        User usrCurrentUser = UserSession.getInstance().getUser();
        return usrCurrentUser != null && Objects.equals(usrCurrentUser.getRole(), "admin");
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent newRoot = loader.load();
            Stage stage = (Stage) grdBooks.getScene().getWindow();
            stage.setScene(new Scene(newRoot));
            stage.show();
        } catch (IOException e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Error navigating to screen: " + fxmlPath, e);
        }
    }
}
