package com.example.rus1_bar.Models;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Purchase_table")
public class Product {

    @NonNull
    @PrimaryKey(autoGenerate = false)
    private String productID;

    @ColumnInfo(name = "product")
    private String productName;

    @ColumnInfo(name = "price")
    private double price;

    @ColumnInfo(name = "picture")
    private int picture;

    @ColumnInfo(name = "quantity")
    private int quantity;

    public Product(String iD, int Quantity, String Name, int Price, int Picture)
    {
        this.setQuantity(Quantity);
        this.setProductName(Name);
        this.setPrice(Price);

        //To avoid null error in adapter in holder
        this.setProductID(iD);
        this.setPicture(Picture);
    }

    public Product(){}

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public String getProductID() {
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
