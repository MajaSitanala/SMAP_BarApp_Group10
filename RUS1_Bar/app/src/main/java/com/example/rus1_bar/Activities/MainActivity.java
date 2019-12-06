package com.example.rus1_bar.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.rus1_bar.Fragments.LoginFragment;
import com.example.rus1_bar.Models.Product;
import com.example.rus1_bar.Models.ShoppingViewModel;
import com.example.rus1_bar.R;
import com.example.rus1_bar.Service.ShoppingService;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private static final String SERVICE_CONNECTED_MAIN_ACTIVITY = "Service connected to the main Activity" ;
    // Firebase authentication
    private FirebaseAuth mAuth;

    // View Model
    private ShoppingViewModel shoppingViewModel;

    // Service
    ShoppingService shoppingService;
    boolean isBound = false;

    NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.MAtoolbar);
        setSupportActionBar(myToolbar);

        // Starting the service
        startService();

        navController = Navigation.findNavController((this), R.id.nav_host_fragment);

        if(savedInstanceState != null)
        {
            //Todo: Hvis der er noget data som skal gemmes ved rotation
        }

        // Initialize Firebase Auth
       //mAuth = shoppingService.getFirebaseAuth_fromService();// FirebaseAuth.getInstance();
    }

    /*
    public ShoppingService test()
    {
        return  this.shoppingService;

    }

     */


    @Override
    public void onStart() {
        super.onStart();

        // Check if user is signed in (non-null) and update UI accordingly.
        //FirebaseUser currentUser = mAuth.getCurrentUser();
        //Toast toast = Toast.makeText(getApplicationContext(),currentUser.getDisplayName(),Toast.LENGTH_LONG);

        Intent intent = new Intent(this, ShoppingService.class);
        bindService(intent, connection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unbindService(connection);
    }

    // Add functionality to AppBar's clickable objects here
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.appbar_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        boolean titleFlag = true;


        switch (item.getTitle().toString()) {
            case "Login":
                {
                    //Toast.makeText(getApplicationContext(),"hello",Toast.LENGTH_LONG).show();

                    // Two versions of shifting from tutorViewFragment to Loginfragment - couldnt workaround the first one - next one works.
                    //Navigation.findNavController(findViewById(R.id.MainActivity)).navigate(R.id.action_viewTutorsFragment_to_loginFragment);

                    titleFlag = true;
                    break;
                }
            case "Tutors":
                {
                    //NavController navController = Navigation.findNavController((this), R.id.nav_host_fragment);

                    //item.setTitle("Login");
                    titleFlag = false;

                    //case R.id.action_settings:
                    break;
                }
        }

        if(titleFlag) {
            item.setTitle("Tutors");
            navController.navigate(R.id.action_viewTutorsFragment_to_loginFragment);
        }
        else {
            item.setTitle("Login");
            navController.navigate(R.id.action_loginFragment_to_viewTutorsFragment);
        }

        return super.onOptionsItemSelected(item);
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

            initWhenServiceIsUp();

            Intent intent = new Intent(SERVICE_CONNECTED_MAIN_ACTIVITY);
            LocalBroadcastManager.getInstance(shoppingService.getApplicationContext()).sendBroadcast(intent);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            isBound = false;
        }
    };

    private void initWhenServiceIsUp()
    {
        // Initialize Firebase Auth
        mAuth = shoppingService.getFirebaseAuth_fromService();

        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        shoppingViewModel = shoppingService.getShoppingViewModel_fromService();
    }

    public void startService()
    {
        Intent serviceIntent = new Intent(this, ShoppingService.class);
        //ContextCompat.startForegroundService(this, serviceIntent);
        startService(serviceIntent);
    }

    public ShoppingService getShoppingService_fromMainActivity()
    {
        return this.shoppingService;
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle saveInstanceState) {
        super.onSaveInstanceState(saveInstanceState);
        //Todo: Hvis der er noget data som skal gemmes ved rotation
    }
}
