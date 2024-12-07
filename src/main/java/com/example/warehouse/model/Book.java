package com.example.warehouse.model;

public class Book {
    private String name;
    private String price;
    private int stock;
    private String imagePath;

    public Book(String name, String price, int stock, String imagePath) {
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.imagePath = imagePath;
    }

    public String getName() { return name; }
    public String getPrice() { return price; }
    public int getStock() { return stock; }
    public String getImagePath() { return imagePath; }
}
