package com.example.rus1_bar.Models;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;


public class Purchase {

    private String purchaseId;
    private List<Product> boughtProducts = new ArrayList<>();
    private double purchaseSum;

    public Purchase()
    {
    }

    public void setBoughtProducts(List<Product> boughtProducts) {
        this.boughtProducts = boughtProducts;
    }

    public String getPurchaseId() {
        return purchaseId;
    }

    public void setPurchaseId(String purchaseId) {
        this.purchaseId = purchaseId;
    }

    public void addProductListToPurchace(List<Product> productList)
    {
        boughtProducts.clear();
        purchaseSum = 0;
        boughtProducts = productList;

        for(Product product : productList)
        {
            purchaseSum = purchaseSum + product.getPrice();
        }
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
