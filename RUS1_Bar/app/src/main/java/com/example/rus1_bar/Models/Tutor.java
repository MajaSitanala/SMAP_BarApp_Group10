package com.example.rus1_bar.Models;

/**
 *
 */
public class Tutor {

    private int ID;
    private String tutorName;
    private String tutorNickname;
    private int phoneNr;
    private String mail;
    private int picture;

    public Tutor(String Name, String Nickname, int PhoneNumber, String Mail, int Picture)
    {
        this.setTutorName(Name);
        this.setNickname(Nickname);
        this.setPhoneNr(PhoneNumber);
        this.setMail(Mail);
        this.setPicture(Picture);
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getID() {
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
}
