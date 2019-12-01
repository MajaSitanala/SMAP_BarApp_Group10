package com.example.rus1_bar.Fragments;


import android.app.Activity;
import android.app.Application;
import android.app.PendingIntent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.rus1_bar.Models.Category;
import com.example.rus1_bar.Models.Product;
import com.example.rus1_bar.Models.Purchase;
import com.example.rus1_bar.Models.Tutor;
import com.example.rus1_bar.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.Executor;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment {

    private FirebaseAuth mAuth;

    Button buttonLogin;
    Button buttonCancel;
    TextInputEditText email;
    TextInputEditText password;

    DatabaseReference databaseTutors;
    DatabaseReference databaseProducts;
    DatabaseReference databaseCategory;
    FirebaseDatabase FireDB;

    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize Firebase Auth & Db
        mAuth = FirebaseAuth.getInstance();
        FireDB = FirebaseDatabase.getInstance();
        databaseTutors = FireDB.getReference("tutors");
        databaseProducts = FireDB.getReference("products");
        databaseCategory = FireDB.getReference("categories");

        View.OnClickListener s = Navigation.createNavigateOnClickListener(R.id.action_loginFragment_to_viewTutorsFragment);
        buttonCancel = view.findViewById(R.id.cancel_btn);
        buttonCancel.setOnClickListener(s);


        email = view.findViewById(R.id.EmailEditText);
        password = view.findViewById(R.id.PasswordEditText);
        buttonLogin = view.findViewById(R.id.login_btn);
        buttonLogin.setOnClickListener(v -> {

            //AddProduct(new Product("s",1,"Filur",30,R.drawable.filur));
            //AddTutor(new Tutor("Chris","Prak10",30248747,"mail",R.drawable.praktikant));
            //AddCategory(new Category(0,"Øl",R.drawable.drinks));
            if(email.getText() != null && password.getText() != null){
                //mAuth.createUserWithEmailAndPassword("Christoffer.broberg@hotmail.com","Pass@word0");
                authenticate(email.getText().toString(),password.getText().toString());

            }
        });

    }

    private void AddTutor(Tutor tutor){
        //Get unique Id from db
        //https://www.youtube.com/watch?v=EM2x33g4syY   save entity

        //https://www.youtube.com/watch?v=jEmq1B1gveM    get entity
        String Id = databaseTutors.push().getKey();
        tutor.setID(Id);
        databaseTutors.child(tutor.getNickname()).setValue(tutor);
        Toast.makeText(getContext(), "Added "+tutor.getNickname()+" to db",Toast.LENGTH_LONG).show();
    }

    private void AddProduct(Product product){
        String Id = databaseProducts.push().getKey();
        product.setProductID(Id);
        databaseProducts.child(product.getProductName()).setValue(product);
        Toast.makeText(getContext(), "Added "+product.getProductName()+" to db",Toast.LENGTH_LONG).show();
    }

    private void AddCategory(Category category){
        String Id = databaseCategory.push().getKey();
        databaseCategory.child(category.getCategoryName()).setValue(category);
        Toast.makeText(getContext(), "Added "+category.getCategoryName()+" to db",Toast.LENGTH_LONG).show();
    }

    private void authenticate(String email,String pass){
        try{
        mAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener((Activity) getContext(), new OnCompleteListener<AuthResult>() {
                     @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                         if(task.isSuccessful()){
                             Toast.makeText(getContext(),"Authentication succeeded.",Toast.LENGTH_LONG).show();
                             FirebaseUser user = mAuth.getCurrentUser();
                             final NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
                             navController.navigate(R.id.action_loginFragment_to_settingsOverviewFragment);
                        }else {
                            Toast.makeText(getContext(),"Authentication failed.",Toast.LENGTH_LONG).show();
                        }
                        if (!task.isSuccessful()) {
                            //password.setHint("Wrong password");
                            //Toast.makeText(getContext(),"Something went wrong in the login.",Toast.LENGTH_LONG).show();
                        }

                    }
                });
        }catch (Exception e){
            Snackbar.make(getActivity().findViewById(android.R.id.content),
                e.getLocalizedMessage(), Snackbar.LENGTH_LONG).show();}
    }
}
