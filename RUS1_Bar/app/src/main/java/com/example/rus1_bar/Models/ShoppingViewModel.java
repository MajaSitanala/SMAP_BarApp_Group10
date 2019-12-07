package com.example.rus1_bar.Models;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.rus1_bar.R;
import com.example.rus1_bar.Repository.FirebaseRepository;
import com.example.rus1_bar.Repository.PurchaseRoomRepository;
import com.example.rus1_bar.Service.ShoppingService;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class ShoppingViewModel extends AndroidViewModel implements Serializable
{
    // Firebase database
    private FirebaseRepository firebaseRepository;
    private FirebaseFirestore firebaseFirestore;
    private DatabaseReference databaseRustur;

    // Room database
    private PurchaseRoomRepository purchaseRoomRepository;
    private LiveData<List<Product>> allProductsinPurchase;
    private MutableLiveData<List<Product>> changedProductList = new MutableLiveData();

    // Service
    private ShoppingService shoppingService;

    public ShoppingViewModel(@NonNull Application application, ShoppingService shoppingService)
    {
        super(application);
        firebaseRepository = shoppingService.getFirebaseRepository_fromService();
        firebaseFirestore = shoppingService.getFirebaseFirestore_fromService();
        purchaseRoomRepository = shoppingService.getPurchaseRoomRepository_fromService();

        //firebaseRepository = new FirebaseRepository();
        //firebaseFirestore = FirebaseFirestore.getInstance();
        //purchaseRoomRepository = new PurchaseRoomRepository(application);
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

    public void deleteAllProductsInPurchase()
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

    public void insertPurchace_CloudFirestore(Tutor tutor, Purchase purchase)
    {
        firebaseRepository.insertPurchase(shoppingService.currentRustur, tutor, purchase);
    }


}




