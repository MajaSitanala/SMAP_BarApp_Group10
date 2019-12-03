package com.example.rus1_bar.Models;

import java.util.List;

public class Bill {

    private int putchaceID;
    private List<Purchase> purchasesList;
    private String owner;

    public int getPutchaceID() {
        return putchaceID;
    }

    public void setPutchaceID(int putchaceID) {
        this.putchaceID = putchaceID;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public List<Purchase> getPurchasesList() {
        return purchasesList;
    }

    public void setPurchasesList(List<Purchase> purchasesList) {
        this.purchasesList = purchasesList;
    }
}
