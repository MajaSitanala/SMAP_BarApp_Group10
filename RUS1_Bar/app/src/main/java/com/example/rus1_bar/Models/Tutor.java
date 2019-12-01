package com.example.rus1_bar.Models;

import com.google.firebase.database.Exclude;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class Tutor {

    private String ID;
    private String tutorName;
    private String tutorNickname;
    private int phoneNr;
    private String mail;
    private int picture;
    private List<Purchase> purchases;

    public Tutor(String Name, String Nickname, int PhoneNumber, String Mail, int Picture)
    {
        this.setTutorName(Name);
        this.setNickname(Nickname);
        this.setPhoneNr(PhoneNumber);
        this.setMail(Mail);
        this.setPicture(Picture);
        purchases = new ArrayList<Purchase>();
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getID() {
        return ID;
    }

    public void setTutorName(String name) {
        tutorName = name;
    }

    public String getTutorName() {
        return tutorName;
    }

    public void setNickname(String nickname) {
        tutorNickname = nickname;
    }

    public String getNickname() {
        return tutorNickname;
    }

    public void setPhoneNr(int phoneNr) {
        this.phoneNr = phoneNr;
    }

    public int getPhoneNr() {
        return phoneNr;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getMail() {
        return mail;
    }

    public void setPicture(int picture) {
        this.picture = picture;
    }

    public int getPicture() {
        return picture;
    }

    public void addPurchase(Purchase purchase){ purchases.add(purchase); }

    public List<Purchase> getPurchases() {return purchases;}
}
