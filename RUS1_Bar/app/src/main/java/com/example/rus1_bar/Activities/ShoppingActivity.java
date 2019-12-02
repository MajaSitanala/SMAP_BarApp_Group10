package com.example.rus1_bar.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import android.app.Notification;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.rus1_bar.Fragments.Bartender.ShoppingCardFragment;
import com.example.rus1_bar.Fragments.Bartender.ViewCategoriesFragment;
import com.example.rus1_bar.Models.Tutor;
import com.example.rus1_bar.R;
import com.example.rus1_bar.Service.ShoppingService;



public class ShoppingActivity extends AppCompatActivity {

    // Service
    ShoppingService shoppingService;
    boolean isBound = false;
    Button btn_cncl;
    Button btn_buy;
    TextView currentTutor;
    private static final String TUTOR_NICK= "Tutor nickname";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping);

        // Shopping card fragment (not navigation componment)
        getSupportFragmentManager().beginTransaction()                                                      //Inspiration and solution found https://codinginflow.com/tutorials/android/fragment-to-fragment-communication-with-shared-viewmodel
                .add(R.id.nav_shopping_cart_fragment, new ShoppingCardFragment())
                .commit();

        currentTutor = findViewById(R.id.tutorlabel_id);
        Intent mainIntent = getIntent();
        currentTutor.setText(mainIntent.getStringExtra(TUTOR_NICK));

        btn_cncl = findViewById(R.id.btn_shopping_cancle);
        btn_cncl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btn_buy = findViewById(R.id.btn_shopping_buy);
        btn_buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Køb her og tilføj til Cloud Firestore (service?)
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        startService();

        Intent intent = new Intent(this, ShoppingService.class);
        bindService(intent, connection, Context.BIND_AUTO_CREATE);

    }

    @Override
    protected void onStop() {
        unbindService(connection);
        stopService();
        super.onStop();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    /**
     * ServiceConnection that makes the ListActivity subscribe to the FourgroundService.
     */
    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            ShoppingService.LocalBinder binder = (ShoppingService.LocalBinder) iBinder;
            shoppingService = binder.getService();
            isBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            isBound = false;
        }
    };

    /**
     * Starts the service when called.
     * Inspitation from Code in flow at https://codinginflow.com/tutorials/android/foreground-service
     */
    public void startService() {
        Intent serviceIntent = new Intent(this, ShoppingService.class);
        //ContextCompat.startForegroundService(this, serviceIntent);
        startService(serviceIntent);
    }

    /**
     * Stops the service when called
     * Inspitation from Code in flow at https://codinginflow.com/tutorials/android/foreground-service
     */
    public void stopService() {
        Intent serviceIntent = new Intent(this, ShoppingService.class);
        stopService(serviceIntent);
    }
}
