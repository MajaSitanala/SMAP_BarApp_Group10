package com.example.rus1_bar.Repository;

import android.app.Application;
import android.os.AsyncTask;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.rus1_bar.Models.Category;
import com.example.rus1_bar.Models.Product;
import com.example.rus1_bar.Models.Tutor;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class FirebaseRepository {

    private MutableLiveData<List<Category>> allCategories;
    private MutableLiveData<List<Product>> allProducts;
    private FirebaseDatabase FireDB;
    private DatabaseReference databaseTutors;
    private DatabaseReference databaseCategory;

    public FirebaseRepository(Application application)
    {
        FireDB = FirebaseDatabase.getInstance();
        databaseTutors = FireDB.getReference("tutors");
        databaseCategory = FireDB.getReference("categories");

    }

    public void insertCategory(Category category) {
        databaseCategory.child(category.getCategoryName()).setValue(category);
    }

    public void insertProduct(Product product, Category cat) {
        String Id = databaseCategory.push().getKey();
        product.setProductID(Id);
        databaseCategory.child(cat.getCategoryName()).child(product.getProductName()).setValue(product);
    }

    public void insertTutor(Tutor tutor){
        databaseTutors.child(tutor.getNickname()).setValue(tutor);
    }

    public void deleteCategory(Category category){
        databaseCategory.child(category.getCategoryName()).removeValue();
    }

    public void deleteProduct(Product product, Category category){
        databaseCategory.child(category.getCategoryName()).child(product.getProductName()).removeValue();
    }

    public void deleteTutor(Tutor tutor){
        databaseTutors.child(tutor.getNickname()).removeValue();
    }



}
