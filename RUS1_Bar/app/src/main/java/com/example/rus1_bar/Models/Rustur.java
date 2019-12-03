package com.example.rus1_bar.Models;

import java.util.List;

public class Rustur {

    private String rusturName;
    private List<Tutor> tutorList;
    private List<Category> categoryList;
    private int year;
    private String location;
    private String season;
    private int picture;

    public Rustur(String name){
        this.rusturName = name;
    }

    public void setRusturName(String rusturName) {
        this.rusturName = rusturName;
    }

    public String getRusturName() {
        return rusturName;
    }

    public void setTutorList(List<Tutor> tutorList) {
        this.tutorList = tutorList;
    }

    public List<Tutor> getTutorList() {
        return tutorList;
    }

    public void setCategoryList(List<Category> categoryList) {
        this.categoryList = categoryList;
    }

    public List<Category> getCategoryList() {
        return categoryList;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getYear() {
        return year;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLocation() {
        return location;
    }

    public void setSeason(String season) {
        this.season = season;
    }

    public String getSeason() {
        return season;
    }

    public void setPicture(int picture) {
        this.picture = picture;
    }

    public int getPicture() {
        return picture;
    }
}
