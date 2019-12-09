
package com.example.rus1_bar.Repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.rus1_bar.Database.PurchaseRoomDao;
import com.example.rus1_bar.Database.PurchaseRoomDatabase;
import com.example.rus1_bar.Models.Product;

import java.util.List;

public class PurchaseRoomRepository
{
    private PurchaseRoomDao purchaseRoomDao;
    private LiveData<List<Product>> allProductsInPurchase;

    public PurchaseRoomRepository(Application aplication)
    {
        PurchaseRoomDatabase database = PurchaseRoomDatabase.getInstance(aplication);
        purchaseRoomDao = database.purchaseRoomDao();
        allProductsInPurchase = purchaseRoomDao.getAllProducts();
    }

    public void insertProduct(Product product)
    {
        new InsertProductAsyncTask(purchaseRoomDao).execute(product);
    }

    public void updateProduct(Product product)
    {
        new UpdateProductAsyncTask(purchaseRoomDao).execute(product);
    }

    public void deleteProduct(Product product)
    {
        new DeleteProductAsyncTask(purchaseRoomDao).execute(product);
    }

    public void deleteAllProducts()
    {
        new DeleteAllProductsAsyncTask(purchaseRoomDao).execute();
    }

    public LiveData<List<Product>> getAllPurchases() {
        return allProductsInPurchase;
    }

    private static class InsertProductAsyncTask extends AsyncTask<Product, Void, Void>
    {
        private PurchaseRoomDao purchaseRoomDao;

        private InsertProductAsyncTask(PurchaseRoomDao purchaseRoomDaos)
        {
            this.purchaseRoomDao = purchaseRoomDaos;
        }

        @Override
        protected Void doInBackground(Product... products)
        {
            purchaseRoomDao.insertProducts(products[0]);
            return null;
        }
    }

    private static class UpdateProductAsyncTask extends AsyncTask<Product, Void, Void>
    {
        private PurchaseRoomDao purchaseRoomDao;

        private UpdateProductAsyncTask(PurchaseRoomDao purchaseRoomDaos)
        {
            this.purchaseRoomDao = purchaseRoomDaos;
        }

        @Override
        protected Void doInBackground(Product... products)
        {
            purchaseRoomDao.updateProducts(products[0]);
            return null;
        }
    }

    private static class DeleteProductAsyncTask extends AsyncTask<Product, Void, Void>
    {
        private PurchaseRoomDao purchaseRoomDao;

        private DeleteProductAsyncTask(PurchaseRoomDao purchaseRoomDaos)
        {
            this.purchaseRoomDao = purchaseRoomDaos;
        }

        @Override
        protected Void doInBackground(Product... products)
        {
            purchaseRoomDao.deleteProducts(products[0]);
            return null;
        }
    }

    private static class DeleteAllProductsAsyncTask extends AsyncTask<Void, Void, Void>
    {
        private PurchaseRoomDao purchaseRoomDao;

        private DeleteAllProductsAsyncTask(PurchaseRoomDao purchaseRoomDaos)
        {
            this.purchaseRoomDao = purchaseRoomDaos;
        }

        @Override
        protected Void doInBackground(Void... v)
        {
            purchaseRoomDao.deleteAllProducts();
            return null;
        }
    }
}
