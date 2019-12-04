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

    public void addProductToPurchace(Product product)
    {
        if(boughtProducts.size()!=0)
        {
            for(int i = 0; i<boughtProducts.size(); i++)
            {
                if (product.getProductName().equals(boughtProducts.get(i).getProductName()))
                {
                    boughtProducts.get(i).setQuantity(boughtProducts.get(i).getQuantity()+1);
                    purchaseSum+=product.getPrice();
                }
                else
                {
                    boughtProducts.add(product);
                    purchaseSum+=product.getPrice();
                }
            }
        }
        else
        {
            boughtProducts.add(product);
            purchaseSum+=product.getPrice();
        }
    }

    public void removeProductToPurchace(Product product)
    {
/*

        for(int i = 0; i<boughtProducts.size(); i++)
        {
            if (product.getProductName().equals(boughtProducts.get(i).getProductName()))
            {
                if (boughtProducts.get(i).getQuantity()>=2)
                {
                    boughtProducts.get(i).setQuantity(boughtProducts.get(i).getQuantity()-1);
                    purchaseSum-=product.getPrice();
                }
                else
                {
                    boughtProducts.remove(i);
                }
            }
            else
            {
                Log.e("PURCHACE removeproduct:", "Nothing to remove");
            }
        }

 */
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
