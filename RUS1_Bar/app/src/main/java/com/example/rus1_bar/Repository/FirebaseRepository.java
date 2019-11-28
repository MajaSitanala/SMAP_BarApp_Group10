package com.example.rus1_bar.Repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.rus1_bar.Models.Category;
import com.example.rus1_bar.Models.Product;

import java.util.List;

public class FirebaseRepository {

    private MutableLiveData<List<Category>> allCategories;
    private MutableLiveData<List<Product>> allProducts;

    public FirebaseRepository(Application application)
    {}

    public void insertCategory(Category category)
    {}

    public void insertProduct(Product product)
    {}

    public void updateCategory(Category category)
    {}

    public void updateProduct(Product product)
    {}

    public void deleteCategory(Category category)
    {}

    public void deleteProduct(Product product)
    {}

    public void deleteAllCategories()
    {}

    public void deleteAllProducts()
    {}

    public MutableLiveData<List<Category>> getAllCategories()
    {
        return allCategories;
    }

    public MutableLiveData<List<Product>> getAllProducts()
    {
        return allProducts;
    }


    private static class InsertCategoryAsyncTask extends AsyncTask<Category, Void, Void>
    {
        private InsertCategoryAsyncTask()
        {}

        @Override
        protected Void doInBackground(Category... categories) {
            return null;
        }
    }


    private static class InsertProductAsyncTask extends AsyncTask<Product, Void, Void>
    {
        private InsertProductAsyncTask()
        {}

        @Override
        protected Void doInBackground(Product... products) {
            return null;
        }
    }

    //TODO: lav acynkrone tasks til at alle databasekald.




}
