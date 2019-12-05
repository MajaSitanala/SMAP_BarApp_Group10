package com.example.rus1_bar.Models;

import java.util.ArrayList;
import java.util.List;

public class Category {

    private int categoryID;
    private String categoryName;
    private List<Product> categoryProductList;
    private int picture;
    private String imageName;

    public Category(int iD, String Name, int Picture)
    {
        this.setCategoryID(iD);
        this.setCategoryName(Name);
        this.setPicture(Picture);
    }

    public Category(){}

    public void setCategoryID(int categoryID) {
        this.categoryID = categoryID;
    }

    public int getCategoryID() {
        return categoryID;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setProductList(List<Product> productList) {
        this.categoryProductList = productList;
    }

    public List<Product> getProductList() {
        return categoryProductList;
    }

    public void setPicture(int picture) {
        this.picture = picture;
    }

    public int getPicture() {
        return picture;
    }

    public String getImageName() {return imageName; }

    public void setImageName(String imageName) { this.imageName = imageName; }
}
