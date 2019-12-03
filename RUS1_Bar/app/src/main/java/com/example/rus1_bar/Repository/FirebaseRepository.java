package com.example.rus1_bar.Repository;

import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.rus1_bar.Models.Category;
import com.example.rus1_bar.Models.Product;
import com.example.rus1_bar.Models.Purchase;
import com.example.rus1_bar.Models.Rustur;
import com.example.rus1_bar.Models.Tutor;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class FirebaseRepository {


    private FirebaseDatabase FireDB;
    private FirebaseFirestore fireStore;
    private DatabaseReference databaseTutors;
    private DatabaseReference databaseCategory;

    public FirebaseRepository()
    {
        FireDB = FirebaseDatabase.getInstance();
        databaseTutors = FireDB.getReference("tutors");
        databaseCategory = FireDB.getReference("categories");
        fireStore = FirebaseFirestore.getInstance();

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

    public void insertPurchase(Rustur rustur, Tutor tutor, Purchase purchase){
        fireStore.collection(rustur.getRusturName()).document(tutor.getNickname())
                .collection("Purchases").add(purchase);
    }

    public void SaveAllPurchasesFromtutor(Rustur rustur, Tutor tutor){
        CollectionReference currentTutor = fireStore.collection(rustur.getRusturName()).document(tutor.getNickname()).collection("Purchases");
        List<Purchase> purchases = new ArrayList<>();
        currentTutor.get().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                for(QueryDocumentSnapshot doc : task.getResult()){
                    purchases.add(doc.toObject(Purchase.class));
                }
                //Purchases er listen med alle køb

            }
        });
    }

    private List<Product> GetAllProducts(){
        List<Product> products = new ArrayList<>();
        List<String> categories = new ArrayList<>();
        databaseCategory.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot categorySnapshot : dataSnapshot.getChildren()){
                    Category category = categorySnapshot.getValue(Category.class);
                    categories.add(category.getCategoryName());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        for (String cat:categories ){
            databaseCategory.child(cat).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot prodSnap : dataSnapshot.getChildren()){
                        Product product = prodSnap.getValue(Product.class);
                        product.setQuantity(0);
                        products.add(product);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

        return products;
    }

    
}
