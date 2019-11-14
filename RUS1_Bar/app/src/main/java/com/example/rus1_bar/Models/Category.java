package com.example.rus1_bar.Models;

import java.util.ArrayList;
import java.util.List;

public class Category {

    private String categoryName;
    private List<Product> categoryProductList;
    private int picture;


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
}
