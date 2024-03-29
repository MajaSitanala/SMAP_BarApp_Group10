package com.example.rus1_bar.Service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.rus1_bar.Models.Category;
import com.example.rus1_bar.Models.Product;
import com.example.rus1_bar.Models.Rustur;
import com.example.rus1_bar.Models.ShoppingViewModel;
import com.example.rus1_bar.Repository.FirebaseRepository;
import com.example.rus1_bar.Repository.PurchaseRoomRepository;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Inspiration found from Code in flow at https://codinginflow.com/tutorials/android/foreground-service for the test notification.
 */

public class ShoppingService extends Service implements Serializable {

    static final String ACTION_DATABASE_INSTANCING_DONE = "The repositories have been declared";

    private final IBinder binder = new LocalBinder();


    private FirebaseRepository firebaseRepository;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth firebaseAuth;
    private PurchaseRoomRepository purchaseRoomRepository;

    public Rustur currentRustur;

    private ShoppingViewModel shoppingViewModel;

    @Override
    public void onCreate() {
        super.onCreate();

        //This is the current set Rustur, change this name when setting new Rustur, and use this when writing to FireStore
        currentRustur = new Rustur("Test_RUSTUR_From_Shopping");

        firebaseRepository = new FirebaseRepository();
        firebaseFirestore = firebaseRepository.getFireStore_fromRepository();//FirebaseFirestore.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();//firebaseRepository.getFireDB_fromRepository();
        firebaseAuth = FirebaseAuth.getInstance();
        purchaseRoomRepository = new PurchaseRoomRepository(this.getApplication());
        shoppingViewModel = new ShoppingViewModel(this.getApplication(), this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }


    /**
     * Binder class that lets the activities and fragments bind to the service.
     */
    public class LocalBinder extends Binder
    {
        public ShoppingService getService()
        {
            return ShoppingService.this;
        }
    }

    public List<Product> getAllProductsInDatabase()
    {
        List<Category> allCategoriesInDatabase = new ArrayList<>();
        List<Product> allProductsInDatabase = new ArrayList<>();

        //Get categories from db
        DatabaseReference databaseCategory = this.firebaseDatabase.getReference("categories");
        databaseCategory.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.e("Category fromService ", "inside");
                allCategoriesInDatabase.clear();
                for (DataSnapshot categorySnapshot : dataSnapshot.getChildren())
                {
                    Category cat = categorySnapshot.getValue(Category.class);
                    allCategoriesInDatabase.add(cat);

                    //Init Database ref
                    DatabaseReference databaseProduct = firebaseDatabase.getReference("categories").child(cat.getCategoryName());
                    databaseProduct.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            allProductsInDatabase.clear();
                            for (DataSnapshot productSnapshot : dataSnapshot.getChildren())
                            {
                                try {
                                    Product product = productSnapshot.getValue(Product.class);
                                    allProductsInDatabase.add(product);
                                }
                                catch (DatabaseException E)
                                {
                                    Log.e("DB x: " + E, "Error fom try catch.");
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError)
                        {
                            Log.e("PRODUCT fromService ", "Error did not load the products");
                        }
                    });

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {
                Log.e("Category fromService ", "Error did not load the categories");
            }
        });

        return allProductsInDatabase;
    }

    public void insertRustur(Rustur rustur){
        this.firebaseRepository.insertRustur(rustur);
        this.firebaseRepository.insertFIRESTORERustur(rustur);
    }

    public void deleteRustur(Rustur rustur){
        this.firebaseRepository.deleteRustur(rustur);
    }

    public void setCurrentRustur(Rustur rustur){
        Rustur temp = currentRustur;
        temp.setisActive(false);
        this.firebaseRepository.insertRustur(temp);
        rustur.setisActive(true);
        this.firebaseRepository.insertRustur(rustur);
        currentRustur = rustur;
    }

    public FirebaseRepository getFirebaseRepository_fromService()
    {
        return this.firebaseRepository;
    }

    public void setFirebaseRepository_inService(FirebaseRepository firebaseRepository)
    {
        this.firebaseRepository = firebaseRepository;
    }

    public FirebaseFirestore getFirebaseFirestore_fromService()
    {
        return this.firebaseFirestore;
    }

    public void setFirebaseFirestore_inService(FirebaseFirestore firebaseFirestore)
    {
        this.firebaseFirestore = firebaseFirestore;
    }

    public void setFirebaseDatabase_inService(FirebaseDatabase firebaseDatabase)
    {
        this.firebaseDatabase = firebaseDatabase;
    }

    public FirebaseDatabase getFirebaseDatabase_fromService()
    {
        return this.firebaseDatabase;
    }

    public FirebaseAuth getFirebaseAuth_fromService()
    {
        return this.firebaseAuth;
    }

    public void setFirebaseAuth_inService(FirebaseAuth firebaseAuth)
    {
        this.firebaseAuth = firebaseAuth;
    }

    public PurchaseRoomRepository getPurchaseRoomRepository_fromService()
    {
        return this.purchaseRoomRepository;
    }

    public void setPurchaseRoomRepository_inService(PurchaseRoomRepository purchaseRoomRepository)
    {
        this.purchaseRoomRepository = purchaseRoomRepository;
    }

    public ShoppingViewModel getShoppingViewModel_fromService()
    {
        return this.shoppingViewModel;
    }

    public void setShoppingViewModel_inService(ShoppingViewModel shoppingViewModel)
    {
        this.shoppingViewModel = shoppingViewModel;
    }

    public Rustur getCurrentRustur()
    {
        return this.currentRustur;
    }
}
