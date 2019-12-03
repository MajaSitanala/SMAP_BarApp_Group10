package com.example.rus1_bar.Models;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class Purchase {

    private List<Product> boughtProducts;
    private double purchaseSum;

    public void setBoughtProducts(List<Product> boughtProducts) {
        this.boughtProducts = boughtProducts;
    }

    public Purchase(){
        boughtProducts = new ArrayList<Product>();
    }

    public void addproduct(Product product){
        boughtProducts.add(product);
        purchaseSum+=product.getPrice();

    }

    public List<Product> getBoughtProducts() {
        return boughtProducts;
    }

    private void setPurchaseSum(double purchaseSum) {
        this.purchaseSum = purchaseSum;
    }

    public double getPurchaseSum() {
        return purchaseSum;
    }
}
