//TODO: When in LoginFragment and one presses Cancel then it opens the next fragment instead of navigating back.
//TODO: When pressing the messenger icon two times then it crashes.

package com.example.rus1_bar.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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

import com.example.rus1_bar.Models.ShoppingViewModel;
import com.example.rus1_bar.R;
import com.example.rus1_bar.Service.ShoppingService;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.FileInputStream;
import java.io.IOException;


public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;


    // View Model
    private ShoppingViewModel shoppingViewModel;

    // Service
    ShoppingService shoppingService;
    boolean isBound = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.MAtoolbar);
        setSupportActionBar(myToolbar);

        // Initialize Firebase Auth
       mAuth = FirebaseAuth.getInstance();
    }


    @Override
    public void onStart() {
        super.onStart();

        // Starting the service
        startService();

        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        //Toast toast = Toast.makeText(getApplicationContext(),currentUser.getDisplayName(),Toast.LENGTH_LONG);

        Intent intent = new Intent(this, ShoppingService.class);
        bindService(intent, connection, Context.BIND_AUTO_CREATE);
    }

    // Add functionality to AppBar's clickable objects here
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.appbar_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_login:

                //Toast.makeText(getApplicationContext(),"hello",Toast.LENGTH_LONG).show();

                //Two versions of shifting from tutorViewFragment to Loginfragment - couldnt workaround the first one - next one works.
                //Navigation.findNavController(findViewById(R.id.MainActivity)).navigate(R.id.action_viewTutorsFragment_to_loginFragment);

                final NavController navController = Navigation.findNavController((this), R.id.nav_host_fragment);
                navController.navigate(R.id.action_viewTutorsFragment_to_loginFragment);
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

            initViewModel();

        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            isBound = false;
        }
    };

    public void startService()
    {
        Intent serviceIntent = new Intent(this, ShoppingService.class);
        //ContextCompat.startForegroundService(this, serviceIntent);
        startService(serviceIntent);
    }

    private void initViewModel()
    {
        shoppingViewModel = shoppingService.getShoppingViewModel_fromService();
    }
}
