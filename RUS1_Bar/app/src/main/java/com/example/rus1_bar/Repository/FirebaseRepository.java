package com.example.rus1_bar.Repository;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.rus1_bar.Models.Category;
import com.example.rus1_bar.Models.Product;
import com.example.rus1_bar.Models.Purchase;
import com.example.rus1_bar.Models.Rustur;
import com.example.rus1_bar.Models.Tutor;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class FirebaseRepository {

    private MutableLiveData<List<Category>> allCategories;
    private MutableLiveData<List<Product>> allProducts;

    private FirebaseDatabase FireDB;
    private FirebaseFirestore fireStore;
    private DatabaseReference databaseTutors;
    private DatabaseReference databaseCategory;
    private StorageReference ImageDB;

    public FirebaseRepository()
    {
        FireDB = FirebaseDatabase.getInstance();
        databaseTutors = FireDB.getReference("tutors");
        databaseCategory = FireDB.getReference("categories");
        fireStore = FirebaseFirestore.getInstance();
        ImageDB = FirebaseStorage.getInstance().getReference();
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


    public void saveTutorImage(Tutor tutor, Uri imageUri){
        if(tutor.getImagename() != null){
            StorageReference pic = ImageDB.child("tutors/"+tutor.getImagename()+".jpg");
            pic.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Log.e("UPLOAD"," Completed for: "+taskSnapshot);
                }
            });
        }
    }

    public void saveCategoryImage(Category category, Uri imageUri){
        if(category.getImageName() != null){
            StorageReference pic = ImageDB.child("categories/"+category.getImageName()+".jpg");
            pic.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Log.e("UPLOAD"," Completed for: "+taskSnapshot);
                }
            });
        }
    }

    //TODO: Handle Exceptions when loading pictures
    public StorageReference getCategoryImage(String categoryName){
        StorageReference jpg = ImageDB.child("categories/"+categoryName+".jpg");
        StorageReference png = ImageDB.child("categories/"+categoryName+".png");
        if(jpg != null){return jpg;
        }else if(png != null){return png;}
        else {return ImageDB.child("categories/defaultprod.jpg");}
    }

    public StorageReference getProductImage(String productImageName, String categoryName){
        StorageReference jpg = ImageDB.child("categories/"+categoryName+"/"+productImageName+".jpg");
        StorageReference png = ImageDB.child("categories/"+categoryName+"/"+productImageName+".png");
        Log.e("PRODUCT:",productImageName);
        if(jpg != null){return jpg;
        }else if(png != null){return png;}
        else {return ImageDB.child("categories/defaultprod.jpg");}
    }

    public StorageReference getTutorImage(String tutorImageName){

        StorageReference jpg = ImageDB.child("tutors/"+tutorImageName+".jpg");
        StorageReference png = ImageDB.child("tutors/"+tutorImageName+".png");
        if(jpg != null){return jpg;
        }else if(png != null){return png;}
        else {return ImageDB.child("tutors/defaultimg.png");}
    }

    public void SaveAllPurchasesFromtutor(Rustur rustur, Tutor tutor, Context context){
        CollectionReference currentTutor = fireStore.collection(rustur.getRusturName()).document(tutor.getNickname()).collection("Purchases");
        List<Purchase> purchases = new ArrayList<>();
        currentTutor.get().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                for(QueryDocumentSnapshot doc : task.getResult()){
                    purchases.add(doc.toObject(Purchase.class));
                }
                //Purchases er listen med alle køb TODO FÅ en liste med alle products fra servicen
                List<Product> productList= GetAllProducts();
                for(Purchase p : purchases){
                    List<Product> boughtProducts = p.getBoughtProducts();
                    for(Product product : boughtProducts){
                        for(Product prod : productList){
                            if(prod.getProductName().equals(product.getProductName())){
                                prod.setQuantity(prod.getQuantity()+product.getQuantity());
                            }
                        }
                    }
                }
                //Take all Purchases and append to file
                try {
                    File file = new File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), tutor.getNickname());

                    WriteLineToFile(tutor.getNickname(),file);
                    WriteLineToFile("Produkt;Antal;Pris;",file);
                    double finalSum = 0;
                    for(Product finalprod : productList){
                        WriteLineToFile(finalprod.getProductName()+";"+finalprod.getQuantity()+";"+(finalprod.getPrice()*finalprod.getQuantity())+";",file);
                        finalSum +=(finalprod.getPrice()*finalprod.getQuantity());
                    }
                    WriteLineToFile("SUM IALT: "+Double.toString(finalSum),file);

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
    }
    //Få liste med alle produkter
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

    private void WriteLineToFile(String data,File file) throws IOException {
        OutputStream os = new FileOutputStream(file,true);
        file.getPath();
        try {
            os = new FileOutputStream(file,true);
            os.write(data.getBytes());
        } catch (IOException e) {
            Log.w("ExternalStorage", "Error writing " + file, e);
        } finally {
            os.flush();
            os.close();
        }
    }

    public FirebaseDatabase getFireDB_fromRepository()
    {
        return this.FireDB;
    }

    public FirebaseFirestore getFireStore_fromRepository()
    {
        return this.fireStore;
    }
}
