package com.example.rus1_bar.Models;

import java.util.List;

public class Purchase {

    private List<Product> boughtProducts;
    private double purchaseSum;

    public void setBoughtProducts(List<Product> boughtProducts) {
        this.boughtProducts = boughtProducts;
    }

    public List<Product> getBoughtProducts() {
        return boughtProducts;
    }

    public void setPurchaseSum(double purchaseSum) {
        this.purchaseSum = purchaseSum;
    }

    public double getPurchaseSum() {
        return purchaseSum;
    }
}
