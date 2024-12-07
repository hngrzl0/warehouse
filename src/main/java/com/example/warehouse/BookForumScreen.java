package com.example.warehouse;

import com.example.warehouse.model.Book;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;


public class BookForumScreen {

    @FXML
    private TextField titleField;
    @FXML
    private TextField authorField;
    @FXML
    private DatePicker publishedDatePicker;
    @FXML
    private TextField isbnField;
    @FXML
    private TextField priceField;
    @FXML
    private TextField countField;
    @FXML
    private TextField descriptionField;
    @FXML
    private TextField pictureUrlField;
    @FXML
    private ChoiceBox<String> categoryField;

    private final BookForumScreenController controller = new BookForumScreenController();

    @FXML
    public void initialize() {
        // Populate the ChoiceBox with predefined categories
        categoryField.setItems(FXCollections.observableArrayList(
                "Adventure", "Fiction", "Non-Fiction", "History", "Romance", "Fantasy", "Children's Books"
        ));
        categoryField.setValue("Adventure");
    }
    @FXML
    public void onSubmit() {
        try {
            Book book = new Book(
                    titleField.getText(),
                    authorField.getText(),
                    java.sql.Date.valueOf(publishedDatePicker.getValue()), // Convert LocalDate to Date
                    isbnField.getText(),
                    Double.parseDouble(priceField.getText()),
                    Integer.parseInt(countField.getText()),
                    descriptionField.getText(),
                    pictureUrlField.getText(),
                    categoryField.getValue() // Retrieve the selected category
            );
            controller.addBook(book);
        } catch (Exception e) {
            System.err.println("Error creating book: " + e.getMessage());
        }
    }

}