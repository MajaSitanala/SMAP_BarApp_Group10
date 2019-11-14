package com.example.rus1_bar.Models;

/**
 *
 */
public class Tutor {

    private String tutorName;
    private String nickname;
    private int phoneNr;
    private String mail;
    private int picture;

    public void setTutorName(String name) {
        tutorName = name;
    }

    public String getTutorName() {
        return tutorName;
    }

    public void setNickname(String nickname) {
        nickname = nickname;
    }

    public String getNickname() {
        return nickname;
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
}
