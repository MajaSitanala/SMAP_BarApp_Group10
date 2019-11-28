package com.example.rus1_bar.Fragments;


import android.app.Activity;
import android.app.Application;
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

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        //FirebaseDatabase database = FirebaseDatabase.getInstance();
        //DatabaseReference DBRef = database.getReference("message");

        View.OnClickListener s = Navigation.createNavigateOnClickListener(R.id.action_loginFragment_to_viewTutorsFragment);
        buttonCancel = view.findViewById(R.id.cancel_btn);
        buttonCancel.setOnClickListener(s);


        email = view.findViewById(R.id.EmailEditText);
        password = view.findViewById(R.id.PasswordEditText);
        buttonLogin = view.findViewById(R.id.login_btn);
        buttonLogin.setOnClickListener(v -> {

            if(email.getText() != null && password.getText() != null){
                //mAuth.createUserWithEmailAndPassword("Christoffer.broberg@hotmail.com","Pass@word0");
                authenticate(email.getText().toString(),password.getText().toString());
            }
        });

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
