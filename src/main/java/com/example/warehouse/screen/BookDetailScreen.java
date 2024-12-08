package com.example.warehouse.screen;

import com.example.warehouse.model.Book;
import com.example.warehouse.model.Cart;
import com.example.warehouse.model.User;
import com.example.warehouse.model.UserSession;
import com.example.warehouse.service.FirestoreService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Controller class for the Book Detail screen in the warehouse application.
 * <p>
 * This class manages the display and interaction with the details of a selected book.
 * It allows users to view book details, add the book to their cart, purchase it,
 * and navigate between screens. Additionally, it includes user-specific functionalities
 * such as hiding buttons for admin users.
 * </p>
 *
 * @author Khongorzul, Margad
 * @version 1.0
 * @since 2024-11-28
 */
public class BookDetailScreen {
    @FXML
    public HBox buyBookHbox;
    @FXML
    private ImageView bookImage;
    @FXML
    private Text bookTitle;
    @FXML
    private Label bookDescription;
    @FXML
    private Text bookPrice;
    @FXML
    private Button logoutButton;
    @FXML
    private Label countLabel;
    @FXML
    private HBox btnCart;
    @FXML
    private Text loggedUser;
    @FXML
    private HBox addBookMenu;
    private String bookId;
    private Book book;
    private int count = 1;

    /**
     * Sets the selected book and loads its details into the UI.
     *
     * @param book The selected {@link Book} object.
     */
    public void setBookId(Book book) {
        this.book = book;
        this.bookId = book.getId();
        loadBookDetails();
    }


    /**
     * Loads the details of the selected book and updates the UI components.
     */
    private void loadBookDetails() {
        User currentUser = UserSession.getInstance().getUser();
        System.out.println("Logged in as a role of: " + currentUser.getRole());
        loggedUser.setText(currentUser.getName());

        if(Objects.equals(currentUser.getRole(), "user")){
            addBookMenu.setVisible(false);
        }
        else {
            addBookMenu.setVisible(Objects.equals(currentUser.getRole(), "admin"));
            btnCart.setVisible(false);
            buyBookHbox.setVisible(false);
        }

        addBookMenu.setVisible(false);
        if(Objects.equals(currentUser.getRole(), "admin")){
            addBookMenu.setVisible(true);
            btnCart.setVisible(false);
            buyBookHbox.setVisible(false);
        }

        try {
            // Fetch the full details of the book using the bookId
            System.out.println("Book Detail of: " + book.getId());

            // Set the UI components with the book details
            bookTitle.setText(book.getTitle());
            bookDescription.setText(book.getDescription());
            bookPrice.setText(book.getPrice() + "₮");
            bookImage.setImage(new Image(book.getPictureUrl() , true));  // Set the book image
            currentUser = UserSession.getInstance().getUser();
            if(Objects.equals(currentUser.getRole(), "admin")){
                btnCart.setVisible(false);
            }

        } catch (Exception e) {
            Logger logger = Logger.getLogger(getClass().getName());
            logger.log(Level.SEVERE, "Error occurred while performing IO operation", e);
        }
    }

    /**
     * Decreases the quantity count of the selected book.
     * Ensures the count does not go below 1.
     */
    @FXML
    private void handleDecreaseCount() {
        if (count > 1) {
            count--;
            countLabel.setText(String.valueOf(count));
        }
    }

    /**
     * Increases the quantity count of the selected book.
     */
    @FXML
    private void handleIncreaseCount() {
        count++;
        countLabel.setText(String.valueOf(count));
    }

    /**
     * Handles the purchase of the selected book.
     * Displays a confirmation alert and resets the quantity count.
     */
    @FXML
    private void handleBuy() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Purchase Successful");
        alert.setHeaderText(null);
        alert.setContentText("Successfully bought the book!");
        alert.showAndWait();
        count = 1;
        countLabel.setText(String.valueOf(count));
    }

    /**
     * Adds the selected book and quantity to the cart.
     * Displays a confirmation message and resets the quantity count.
     */
    @FXML
    private void handleAddToCart() {
        Cart.getInstance().addBookWithQuantity(book, count);
        System.out.println("Added to cart: " + book.getTitle());
        System.out.println(count + " copies of the book added to the cart!");
        count = 1;
        countLabel.setText(String.valueOf(count));
    }
    /**
     * Navigates to the Cart screen.
     */
    @FXML
    private void handleCartButton(){
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/example/warehouse/layout/screen_cart.fxml")
            );
            Parent newRoot = loader.load();
            Stage stage = (Stage) bookImage.getScene().getWindow();
            Scene scene = new Scene(newRoot);
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            Logger logger = Logger.getLogger(getClass().getName());
            logger.log(Level.SEVERE, "Error occurred while performing IO operation", e);
        }
    }


    /**
     * Navigates to the Book Forum screen.
     */
    public void handleAddBook(MouseEvent mouseEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/example/warehouse/layout/screen_forum.fxml")
            );
            Parent newRoot = loader.load();
            Stage stage = (Stage) bookImage.getScene().getWindow();
            Scene scene = new Scene(newRoot);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            Logger logger = Logger.getLogger(getClass().getName());
            logger.log(Level.SEVERE, "Error occurred while performing IO operation", e);
        }
    }

    /**
     * Navigates back to the Home screen.
     */
    public void handleBackButton(MouseEvent mouseEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/example/warehouse/layout/screen_home.fxml")
            );
            Parent newRoot = loader.load();
            Stage stage = (Stage) bookImage.getScene().getWindow();
            Scene scene = new Scene(newRoot);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            Logger logger = Logger.getLogger(getClass().getName());
            logger.log(Level.SEVERE, "Error occurred while performing IO operation", e);
        }
    }

    /**
     * Navigates back to the Home screen for search.
     */
    public void handleSearchBox(MouseEvent mouseEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/example/warehouse/layout/screen_home.fxml")
            );
            Parent newRoot = loader.load();
            Stage stage = (Stage) bookImage.getScene().getWindow();
            Scene scene = new Scene(newRoot);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            Logger logger = Logger.getLogger(getClass().getName());
            logger.log(Level.SEVERE, "Error occurred while performing IO operation", e);
        }
    }

    /**
     * Navigates to the Login screen.
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
            Logger logger = Logger.getLogger(getClass().getName());
            logger.log(Level.SEVERE, "Error occurred while performing IO operation", e);
        }
    }

    public void handleForumMenu(MouseEvent mouseEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/warehouse/layout/screen_forum.fxml"));
            Parent newRoot = loader.load();
            Stage stage = (Stage) bookImage.getScene().getWindow();
            Scene scene = new Scene(newRoot);
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            Logger logger = Logger.getLogger(getClass().getName());
            logger.log(Level.SEVERE, "Error occurred while performing IO operation", e);
        }
    }
}
