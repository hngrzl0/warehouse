package com.example.warehouse.model;

import java.util.Date;

/**
 * Represents a book model in the warehouse system.
 * <p>
 * The {@code Book} class contains information about a book, such as its title, author,
 * published year, ISBN, price, stock count, description, picture URL, category, and ID.
 * This class provides constructors, getters, and setters for managing book data.
 * </p>
 *
 * @author Khongorzul
 * @version 1.0
 * @since 2024-11-25
 */
public class Book {
    private String title;
    private String author;
    private String publishedYear;
    private String isbn;
    private double price;
    private int count;
    private String description;
    private String pictureUrl;
    private String category;
    private String id;

    /**
     * Constructs a {@code Book} with the specified details, excluding ID.
     *
     * @param title        the title of the book
     * @param author       the author of the book
     * @param publishedYear the year the book was published
     * @param isbn         the International Standard Book Number (ISBN) of the book
     * @param price        the price of the book
     * @param count        the stock count of the book
     * @param description  a brief description of the book
     * @param pictureUrl   the URL of the book's picture
     * @param category     the category or genre of the book
     */
    public Book(String title, String author, String publishedYear, String isbn, double price, int count, String description, String pictureUrl, String category) {
        this.title = title;
        this.author = author;
        this.publishedYear = publishedYear;
        this.isbn = isbn;
        this.price = price;
        this.count = count;
        this.description = description;
        this.pictureUrl = pictureUrl;
        this.category = category;
    }

    public Book(String title, String author, String publishedYear, String isbn, double price, int count, String description, String pictureUrl, String category, String id) {
        this.title = title;
        this.author = author;
        this.publishedYear = publishedYear;
        this.isbn = isbn;
        this.price = price;
        this.count = count;
        this.description = description;
        this.pictureUrl = pictureUrl;
        this.category = category;
        this.id = id;
    }

    // Getters and setters
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public String getPublishedYear() { return publishedYear; }
    public void setPublishedYear(Date publishedYear) { this.publishedYear = String.valueOf(publishedYear); }

    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public int getCount() { return count; }
    public void setCount(int count) { this.count = count; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getPictureUrl() { return pictureUrl; }
    public void setPictureUrl(String pictureUrl) { this.pictureUrl = pictureUrl; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getId() { return this.id; }
    public void setId(String id){ this.id = id; }
}
