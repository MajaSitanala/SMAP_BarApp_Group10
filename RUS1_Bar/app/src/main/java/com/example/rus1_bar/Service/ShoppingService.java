package com.example.rus1_bar.Service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.rus1_bar.Activities.ShoppingActivity;
import com.example.rus1_bar.Models.ShoppingViewModel;
import com.example.rus1_bar.R;
import com.example.rus1_bar.Repository.FirebaseRepository;
import com.example.rus1_bar.Repository.PurchaseRoomRepository;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
/**
 * Inspiration found from Code in flow at https://codinginflow.com/tutorials/android/foreground-service for the test notification.
 */
import static com.example.rus1_bar.Service.AppShopping.TEST_CHANNEL_ID;

public class ShoppingService extends Service {

    static final String ACTION_DATABASE_INSTANCING_DONE = "The repositories have been declared";

    private final IBinder binder = new LocalBinder();


    private FirebaseRepository firebaseRepository;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth firebaseAuth;
    private PurchaseRoomRepository purchaseRoomRepository;

    private ShoppingViewModel shoppingViewModel;

    @Override
    public void onCreate() {
        super.onCreate();

        firebaseRepository = new FirebaseRepository();
        firebaseFirestore = firebaseRepository.getFireStore_fromRepository();//FirebaseFirestore.getInstance();
        firebaseDatabase = firebaseRepository.getFireDB_fromRepository();
        firebaseAuth = FirebaseAuth.getInstance();
        purchaseRoomRepository = new PurchaseRoomRepository(this.getApplication());

        shoppingViewModel = new ShoppingViewModel(this.getApplication(), this);


        // Notification intent
        Intent notificationIntent = new Intent(this, ShoppingActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, 0);

        //Notification creation
        Notification notification = new NotificationCompat.Builder(this, TEST_CHANNEL_ID)
                .setContentTitle("Test Service Running")
                .setContentText("Yo yo yo yo")
                .setSmallIcon(R.drawable.rusicon)
                .setContentIntent(pendingIntent)
                .build();

        //Notification starts in the forground
        startForeground(1, notification);

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
}
