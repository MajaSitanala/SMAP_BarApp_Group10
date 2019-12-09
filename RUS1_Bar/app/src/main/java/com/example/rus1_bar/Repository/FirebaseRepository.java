package com.example.rus1_bar.Repository;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.provider.ContactsContract;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.room.ColumnInfo;

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
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.checkerframework.checker.units.qual.A;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.List;

public class FirebaseRepository {

    private MutableLiveData<List<Category>> allCategories;
    private MutableLiveData<List<Product>> allProducts;

    private FirebaseDatabase FireDB;
    private FirebaseFirestore fireStore;
    private DatabaseReference databaseTutors;
    private DatabaseReference databaseCategory;
    private DatabaseReference databaseRustur;
    private StorageReference ImageDB;

    private List<Purchase> fullPurchaseList;

    public FirebaseRepository()
    {
        FireDB = FirebaseDatabase.getInstance();
        databaseTutors = FireDB.getReference("tutors");
        databaseCategory = FireDB.getReference("categories");
        databaseRustur = FireDB.getReference("rustur");
        fireStore = FirebaseFirestore.getInstance();
        ImageDB = FirebaseStorage.getInstance().getReference();
        fullPurchaseList = new ArrayList<>();
    }

    public void insertCategory(Category category) {
        databaseCategory.child(category.getCategoryName()).setValue(category);
    }

    public void insertProduct(Product product, String cat) {
        String Id = databaseCategory.push().getKey();
        product.setProductID(Id);
        databaseCategory.child(cat).child(product.getProductName()).setValue(product);
    }

    public void insertTutor(Tutor tutor){
        databaseTutors.child(tutor.getNickname()).setValue(tutor);
    }

    public void deleteCategory(Category category){
        databaseCategory.child(category.getCategoryName()).removeValue();
    }

    public void deleteProduct(Product product, String category){
        databaseCategory.child(category).child(product.getProductName()).removeValue();
    }

    public void deleteTutor(Tutor tutor){
        databaseTutors.child(tutor.getNickname()).removeValue();
    }

    public void insertFIRESTORERustur(Rustur rustur){
        fireStore.collection(rustur.getRusturName()).add(rustur);
    }

    public void insertRustur(Rustur rustur){
        databaseRustur.child(rustur.getRusturName()).setValue(rustur);
    }

    public void deleteRustur(Rustur rustur){
        databaseRustur.child(rustur.getRusturName()).removeValue();
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

    public void saveProductImage(Category category, Product product, Uri imageUri){
        if(product.getImageName()!= null){
            StorageReference pic = ImageDB.child("categories/"+category.getCategoryName()+"/"+product.getProductName()+".png");
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
    //TODO: Navigate correctly through DB
    public void saveProductImage(Product product, Uri imageUri){
        if(product.getImageName() != null){
            StorageReference pic = ImageDB.child("categories/"+product.getImageName()+".jpg");
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
        return jpg;
    }

    public StorageReference getProductImage(String productImageName, String categoryName){
        StorageReference png = ImageDB.child("categories").child(categoryName).child(productImageName+".png");
        Log.e("PRODUCT:",productImageName);
        return png;
    }

    public StorageReference getTutorImage(String tutorImageName){
        StorageReference jpg = ImageDB.child("tutors/"+tutorImageName+".jpg");
        return jpg;
    }

    public void SaveAllPurchases(Rustur rustur, Context context){
        CollectionReference allTutors = fireStore.collection(rustur.getRusturName());
        List<String> tutors = new ArrayList<>();
        allTutors.get().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                for(QueryDocumentSnapshot doc : task.getResult()){
                    tutors.add(doc.toObject(Tutor.class).getNickname());
                }

                //For each tutor, get their purchases from fireStore
                for (String tutor :  tutors){
                    CollectionReference t = fireStore.collection(rustur.getRusturName()).document(tutor).collection("Purchases");
                    List<Purchase> purchases = new ArrayList<>();
                    t.get().addOnCompleteListener(task1 -> {
                        if(task1.isSuccessful()){
                            for(QueryDocumentSnapshot doc : task1.getResult()){
                                purchases.add(doc.toObject(Purchase.class));
                            }

                            //For each product in purchases
                            List<Product> products = new ArrayList<>();
                            for(Purchase pur : purchases){
                                List<Product> boughtProds = pur.getBoughtProducts();
                                for(Product prod : boughtProds){
                                    products.add(prod);
                                }
                            }

                            //Products is now all products from each tutor
                            Purchase purchase = new Purchase();
                            purchase.setPurchaseId(tutor);
                            purchase.setBoughtProducts(products);
                            fullPurchaseList.add(purchase);
                        }
                    });
                }
                //Now ReadyToWrite
                //All tutors purchases is now in fullPurchaseList
                try{
                    File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),rustur.getRusturName()+".csv");
                    if(file.exists())
                    {
                        file.delete();
                        file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), rustur.getRusturName()+".csv");
                    }
                    //Overskrift
                    WriteLineToFile(rustur.getRusturName(),file);
                    //Række med alle produkter (første tom, sidste = "SUM"
                    List<String> ProductRow  = GetProductRow();
                    StringBuilder data = new StringBuilder();
                    for(String prods : ProductRow){
                        data.append(prods+";");
                    }
                    WriteLineToFile(data.toString(),file);
                    //Lister med tutornavn først og derefter antal køb af hvert prod

                    double totalSum = 0;
                    for(Purchase handel : fullPurchaseList){
                        StringBuilder row = new StringBuilder();
                        row.append(handel.getPurchaseId()+";");
                        for(String item : ProductRow){
                            int quantity = 0;
                            double price = 0;
                            for(Product vare : handel.getBoughtProducts()){
                                if(item.equals(vare.getProductName())){
                                    quantity+=vare.getQuantity();
                                    price += (quantity*vare.getPrice());
                                }
                                row.append((quantity)+";");
                                quantity=0;
                            }
                            row.append(price+";");
                            totalSum += price;
                        }
                        WriteLineToFile(row.toString(),file);

                    }
                    WriteLineToFile("SUM;;"+String.valueOf(totalSum),file);

                } catch (IOException e) {
                e.printStackTrace();
            }
            }
        });
    }

    private List<String> GetProductRow(){
        List<String> result = new ArrayList<>();
        result.add("");
        for(Purchase purchase : fullPurchaseList){
            List<Product> prods = purchase.getBoughtProducts();
            for(String p : result){
                for(Product ps: prods){
                    if(p.equals(ps.getProductName())){
                    }else {result.add(ps.getProductName());}
                }
            }
        }
        result.add("SUM");
        return result;
    }

    public void SaveAllPurchasesFromtutor(Rustur rustur, Tutor tutor, Context context){
        CollectionReference currentTutor = fireStore.collection(rustur.getRusturName()).document(tutor.getNickname()).collection("Purchases");
        List<Purchase> purchases = new ArrayList<>();
        currentTutor.get().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                for(QueryDocumentSnapshot doc : task.getResult()){
                    purchases.add(doc.toObject(Purchase.class));
                }
                //Purchases er listen med alle køb
                List<Product> productList= new ArrayList<>();
                for(Purchase p : purchases){
                    List<Product> boughtProducts = p.getBoughtProducts();
                    for(Product product : boughtProducts){
                       productList.add(product);
                    }
                }
                //Take all Purchases and append to file
                try {
                    File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), tutor.getNickname()+".csv");

                    if(file.exists())
                    {
                        file.delete();
                        file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), tutor.getNickname()+".csv");
                    }

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

    private void WriteLineToFile(String data,File file) throws IOException {
        OutputStream os = new FileOutputStream(file,true);
        file.getPath();
        try {
            //os = new FileOutputStream(file,true);
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
