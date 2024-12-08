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
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

    /**
     * Controller class for the Home Screen in the warehouse application.
     * <p>
     * This class handles displaying books in a grid layout, providing search and filter functionalities,
     * and managing role-based UI visibility. It also provides navigation to other screens like cart and login.
     * </p>
     *
     * @version 1.0
     * @since 2024-12-08
     */
    public class HomeScreen {

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
         * Initializes the Home Screen by setting up the UI, loading books, and configuring event handlers.
         * This method is invoked automatically after the FXML file is loaded.
         */
        public void initialize() {
            FirestoreService fsFirestoreService = new FirestoreService();
            hscController = new HomeScreenController(fsFirestoreService);

            // Populate genre combo box
            if (cmbGenre != null) {
                cmbGenre.setItems(FXCollections.observableArrayList(
                        "All", "Fiction", "Non-Fiction", "Science", "Children's Books", "Adventure", "History", "Romance", "Fantasy"
                ));
                cmbGenre.setValue("All");
            } else {
                System.out.println("Genre combo box is null");
            }

            try {
                // Fetch and display all books
                olstAllBooks = (ObservableList<Book>) hscController.getBookList();
                cmbGenre.setOnAction(event -> filterBooks());
                txtSearchField.textProperty()
                        .addListener((observable, oldValue, newValue) -> filterBooks());
            } catch (Exception e) {
                System.out.println(e);
            }

            loadBooks(olstAllBooks);

            // Setup UI based on user role
            User usrCurrentUser = UserSession.getInstance().getUser();
            System.out.println("Logged in as a role of: " + usrCurrentUser.getRole());
            txtLoggedUser.setText(usrCurrentUser.getName());


            hbxAddBookMenu.setVisible(false);
            if(Objects.equals(usrCurrentUser.getRole(), "admin")){
                hbxAddBookMenu.setVisible(true);
                hbxBtnCart.setVisible(false);
            }
        }

        /**
         * Loads a list of books into the grid layout.
         *
         * @param lstBooks The list of books to display.
         */
        private void loadBooks(List<Book> lstBooks) {
            grdBooks.getChildren().clear();
            int iColumn = 0;
            int iRow = 0;
            int iMaxColumns = 5;

            for (Book bkBook : lstBooks) {
                try {
                    FXMLLoader fxmlLoader = new FXMLLoader(
                            getClass().getResource("/com/example/warehouse/layout/card_book.fxml")
                    );
                    Parent prtBookCard = fxmlLoader.load();
                    User usrCurrentUser = UserSession.getInstance().getUser();

                    // Populate book card details
                    ImageView imgBookImage = (ImageView) prtBookCard.lookup("#bookImage");
                    Label lblBookTitle = (Label) prtBookCard.lookup("#bookTitle");
                    Label lblBookPrice = (Label) prtBookCard.lookup("#bookPrice");
                    Text txtBookAuthor = (Text) prtBookCard.lookup("#author");
                    Button btnAddToCart = (Button) prtBookCard.lookup("#addToCartButton");

                    if (Objects.equals(usrCurrentUser.getRole(), "admin")) {
                        btnAddToCart.setVisible(false);
                    }
                    imgBookImage.setImage(new Image(bkBook.getPictureUrl(), true));
                    lblBookTitle.setText(bkBook.getTitle());
                    lblBookPrice.setText(bkBook.getPrice() + "₮");
                    txtBookAuthor.setText(bkBook.getAuthor());

                    btnAddToCart.setOnAction(event -> {
                        System.out.println(bkBook.getTitle() + " added to cart!");
                    });

                    BookCardController bccBookCardController = fxmlLoader.getController();
                    bccBookCardController.setBook(bkBook);

                    grdBooks.add(prtBookCard, iColumn, iRow);

                    iColumn++;
                    if (iColumn == iMaxColumns) {
                        iColumn = 0;
                        iRow++;
                    }

                } catch (IOException e) {
                    Logger lgrLogger = Logger.getLogger(getClass().getName());
                    lgrLogger.log(Level.SEVERE, "Error occurred while performing IO operation", e);
                }
            }
        }

    /**
     * Filters the displayed books based on the search text and selected genre.
     */
    public void filterBooks() {
        String strSearchText = txtSearchField.getText().toLowerCase();
        String strSelectedGenre = cmbGenre.getValue();

        List<Book> lstFilteredBooks = olstAllBooks.stream()
                .filter(book -> (strSelectedGenre.equals("All") || book.getCategory().equals(strSelectedGenre)) &&
                        (book.getTitle().toLowerCase().contains(strSearchText)))
                .collect(Collectors.toList());

        loadBooks(lstFilteredBooks);
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
            Stage stage = (Stage) grdBooks.getScene().getWindow();
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
            Stage currentStage = (Stage) btnLogout.getScene().getWindow();
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
            Stage stage = (Stage) grdBooks.getScene().getWindow();
            Scene scene = new Scene(newRoot);
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            Logger logger = Logger.getLogger(getClass().getName());
            logger.log(Level.SEVERE, "Error occurred while performing IO operation", e);
        }
    }
}

