package com.example.rus1_bar.Models;

public class Product {

    private int productID;
    private String productName;
    private double price;
    private int picture;
    private int quantity;

    public Product(int iD, int Quantity, String Name, int Price)
    {
        this.setProductID(iD);
        this.setQuantity(Quantity);
        this.setProductName(Name);
        this.setPrice(Price);
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }

    public int getProductID() {
        return productID;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductName() {
        return productName;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getPrice() {
        return price;
    }

    public void setPicture(int picture) {
        this.picture = picture;
    }

    public int getPicture() {
        return picture;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getQuantity() {
        return quantity;
    }
}
