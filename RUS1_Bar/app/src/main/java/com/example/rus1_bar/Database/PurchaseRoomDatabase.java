
package com.example.rus1_bar.Database;


import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.rus1_bar.Models.Product;
import com.example.rus1_bar.R;

@Database(entities = Product.class, version = 2, exportSchema = false)
public abstract class PurchaseRoomDatabase extends androidx.room.RoomDatabase
{
    private static PurchaseRoomDatabase instance;

    public abstract PurchaseRoomDao purchaseRoomDao();

    public static synchronized PurchaseRoomDatabase getInstance(Context context)
    {
        if (instance == null)
        {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    PurchaseRoomDatabase.class, "room_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(purchaseRoomCallback)
                    .build();
        }

        return instance;
    }

    private static RoomDatabase.Callback purchaseRoomCallback = new RoomDatabase.Callback()
    {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
        }
    };

    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void>
    {
        private PurchaseRoomDao purchaseRoomDao;

        private PopulateDbAsyncTask(PurchaseRoomDatabase db)
        {
            purchaseRoomDao = db.purchaseRoomDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            Product testProduct = new Product("0",0, "Gin Hass", 20, R.drawable.ginhass);

            purchaseRoomDao.insertProducts(testProduct);

            return null;
        }
    }

}
