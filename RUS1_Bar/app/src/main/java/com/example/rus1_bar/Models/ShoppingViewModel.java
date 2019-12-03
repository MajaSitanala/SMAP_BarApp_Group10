package com.example.rus1_bar.Models;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.rus1_bar.R;
import com.example.rus1_bar.Repository.FirebaseRepository;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class ShoppingViewModel extends AndroidViewModel {

    private FirebaseRepository firebaseRepository;
    private MutableLiveData<List<Category>> allCategories;
    private List<Category> dummyAllCategories;
    private MutableLiveData<List<Product>> allProductsInCart;

    public ShoppingViewModel(@NonNull Application application)
    {
        super(application);
        firebaseRepository = new FirebaseRepository();
    }

    /**
     * TODO: Fill this with a call to the database.
     * getAllCategories gets the categories from the database.
     */
    public void getAllCategories()
    {
        //allCategories = firebaseRepository.getAllCategories();


    }

    public void getAllProducts()
    {}



    public void dummyAllCategory()
    {
        //DUMMY DATA
        for (int i = 0; i<12; i++)
        {
            //Test data for the card view
            dummyAllCategories.add(new Category(i, "Drinks", R.drawable.drinks));
        }
    }
}
