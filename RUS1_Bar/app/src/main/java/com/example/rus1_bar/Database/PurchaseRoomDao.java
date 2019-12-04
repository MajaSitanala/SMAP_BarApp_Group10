
package com.example.rus1_bar.Database;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.rus1_bar.Models.Product;
import com.example.rus1_bar.Models.Purchase;

import java.util.List;

@Dao
public interface PurchaseRoomDao
{
    @Insert
    void insertProducts(Product product);

    @Update
    void updateProducts(Product product);

    @Delete
    void deleteProducts(Product product);

    @Query("DELETE FROM Purchase_table")
    void deleteAllProducts();

    @Query("SELECT * FROM Purchase_table ORDER BY product")
    LiveData<List<Product>> getAllProducts();
}

