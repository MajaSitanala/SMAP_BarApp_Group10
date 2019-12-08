package com.example.rus1_bar.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ActionBar;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.MAtoolbar);
        setSupportActionBar(myToolbar);

        bundle = savedInstanceState;

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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService();
    }

    // Add functionality to AppBar's clickable objects here
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.appbar_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        // https://inducesmile.com/android-programming/how-to-get-current-visible-fragment-in-android-navigation-component/
        NavController navController = Navigation.findNavController(this,R.id.nav_host_fragment);
        String currentLabel = navController.getCurrentDestination().getLabel().toString();

        if(item.getTitle().toString().equals(getString(R.string.administrator)) && (currentLabel.equals("fragment_view_tutors")))
        {
            navController.navigate(R.id.action_viewTutorsFragment_to_loginFragment);
        }
        else if (item.getTitle().toString().equals(getString(R.string.bartender)) && (currentLabel.equals("fragment_login")))
        {
            navController.navigate(R.id.action_loginFragment_to_viewTutorsFragment);
        }
        return super.onOptionsItemSelected(item);
    }


    // https://stackoverflow.com/questions/3141996/android-how-to-override-the-back-button-so-it-doesnt-finish-my-activity
    @Override
    public void onBackPressed()
    {
        //super.onBackPressed();

        String currentLabel = navController.getCurrentDestination().getLabel().toString();

        switch (currentLabel) {
            case "fragment_login":
            {
                navController.navigate(R.id.action_loginFragment_to_viewTutorsFragment);
                break;
            }
            case "fragment_settings_overview":
            {
                navController.navigate(R.id.action_settingsOverviewFragment_to_loginFragment);
                break;
            }
            case "fragment_tutor_settings":
            {
                navController.navigate(R.id.action_tutorSettingsFragment_to_settingsOverviewFragment);
                break;
            }
            case "fragment_add_tutor":
            {
                navController.navigate(R.id.action_addTutorFragment_to_tutorSettingsFragment);
                break;
            }
            case "fragment_edit_tutor":
            {
                navController.navigate(R.id.action_editTutorFragment_to_tutorSettingsFragment);
                break;
            }
            case "fragment_category_settings":
            {
                navController.navigate(R.id.action_categorySettingsFragment_to_settingsOverviewFragment);
                break;
            }
            case "fragment_add_categories":
            {
                navController.navigate(R.id.action_addCategoriesFragment_to_categorySettingsFragment);
                break;
            }
            case "fragment_edit_category":
            {
                navController.navigate(R.id.action_editCategoryFragment_to_categorySettingsFragment);
                break;
            }
            case "fragment_product_settings":
            {
                navController.navigate(R.id.action_productSettingsFragment_to_settingsOverviewFragment);
                break;
            }
            case "fragment_add_product":
            {
                navController.navigate(R.id.action_addProductFragment_to_productSettingsFragment);
                break;
            }
            case "fragment_edit_product":
            {
                navController.navigate(R.id.action_editProductFragment_to_productSettingsFragment);
                break;
            }
            case "fragment_rustur_settings":
            {
                navController.navigate(R.id.action_rusturSettingsFragment_to_settingsOverviewFragment);
                break;
            }

            // TODO: Rustur's settings.

            case "fragment_bill_settings":
            {
                navController.navigate(R.id.action_billSettingsFragment_to_settingsOverviewFragment);
                break;
            }
            // TODO: Rustur's settings.

            case "fragment_view_tutors":
            {
                finish();
            }
        }
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

            if(shoppingViewModel!=null)
            {
                shoppingViewModel.deleteAllProductsInPurchase();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            isBound = false;

            if(shoppingViewModel!=null)
            {
                shoppingViewModel.deleteAllProductsInPurchase();
            }

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

    public void stopService() {
        Intent serviceIntent = new Intent(this, ShoppingService.class);
        stopService(serviceIntent);
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
