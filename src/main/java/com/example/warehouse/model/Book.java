package com.example.warehouse.model;

import java.util.Date;

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

    // Constructor
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
}
