package com.example.rus1_bar.Models;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.rus1_bar.R;
import com.example.rus1_bar.Repository.FirebaseRepository;
import com.example.rus1_bar.Repository.PurchaseRoomRepository;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class ShoppingViewModel extends AndroidViewModel
{
    // Firebase database
    private FirebaseRepository firebaseRepository;

    // Room database
    private PurchaseRoomRepository purchaseRoomRepository;
    private LiveData<List<Product>> allProductsinPurchase;
    private MutableLiveData<List<Product>> changedProductList = new MutableLiveData();

    public ShoppingViewModel(@NonNull Application application)
    {
        super(application);
        firebaseRepository = new FirebaseRepository();
        purchaseRoomRepository = new PurchaseRoomRepository(application);
    }

    public void insertProductInPurchase(Product product)
    {
        purchaseRoomRepository.insertProduct(product);
    }

    public void updateProductInPurchase(Product product)
    {
        purchaseRoomRepository.updateProduct(product);
    }

    public void deleteProductInPurchase(Product product)
    {
        purchaseRoomRepository.deleteProduct(product);
    }

    public void deleteAllProductsInPurchase(Product product)
    {
        purchaseRoomRepository.deleteAllProducts();
    }

    public LiveData<List<Product>> getAllProductsinPurchase()
    {

        allProductsinPurchase = purchaseRoomRepository.getAllPurchases();
        List<Product> localChangedProduct = new ArrayList<>();
        changedProductList.postValue(localChangedProduct);

        return allProductsinPurchase;
    }



/*

    MutableLiveData<List<CountryOnRankingList>> changedRankings = new MutableLiveData();

    public LiveData<List<CountryOnRankingList>> getCountries() {


                        List<CountryOnRankingList> localChangedRankings = new ArrayList<>();


                            changedRankings.postValue(localChangedRankings);

        return changedRankings;
    }

 */
}




