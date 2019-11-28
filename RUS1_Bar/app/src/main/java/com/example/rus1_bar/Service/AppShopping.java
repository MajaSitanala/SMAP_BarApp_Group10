package com.example.rus1_bar.Service;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

/**
 * Inspiration found from Code in flow at https://codinginflow.com/tutorials/android/foreground-service
 */

public class AppShopping extends Application {

    public static final String TEST_CHANNEL_ID = "testChannel";

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannels();
    }

    public void createNotificationChannels()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            NotificationChannel testChannel = new NotificationChannel(
                    TEST_CHANNEL_ID,
                    "test channel",
                    NotificationManager.IMPORTANCE_HIGH
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(testChannel);
        }
    }
}
