package com.example.rus1_bar.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.rus1_bar.R;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.FileInputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    public boolean loggedIn = false;

    private FirebaseAuth mAuth;
    NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.MAtoolbar);
        setSupportActionBar(myToolbar);

        navController = Navigation.findNavController((this), R.id.nav_host_fragment);



        // Initialize Firebase Auth
       mAuth = FirebaseAuth.getInstance();
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

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        //Toast toast = Toast.makeText(getApplicationContext(),currentUser.getDisplayName(),Toast.LENGTH_LONG);
    }

}
