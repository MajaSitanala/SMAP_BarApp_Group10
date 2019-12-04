package com.example.rus1_bar.Service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.rus1_bar.Activities.ShoppingActivity;
import com.example.rus1_bar.R;
/**
 * Inspiration found from Code in flow at https://codinginflow.com/tutorials/android/foreground-service for the test notification.
 */
import static com.example.rus1_bar.Service.AppShopping.TEST_CHANNEL_ID;

public class ShoppingService extends Service {

    private final IBinder binder = new LocalBinder();

    @Override
    public void onCreate() {
        super.onCreate();
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


}
