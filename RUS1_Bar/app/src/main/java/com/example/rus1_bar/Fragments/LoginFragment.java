package com.example.rus1_bar.Fragments;


import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.rus1_bar.Activities.MainActivity;
import com.example.rus1_bar.Models.Category;
import com.example.rus1_bar.Models.Product;
import com.example.rus1_bar.Models.Tutor;
import com.example.rus1_bar.R;
import com.example.rus1_bar.Service.ShoppingService;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment {

    private static final String SERVICE_CONNECTED_MAIN_ACTIVITY = "Service connected to the main Activity" ;

    private FirebaseAuth mAuth;

    Button buttonLogin;
    Button buttonCancel;
    TextInputEditText email;
    TextInputEditText password;

    DatabaseReference databaseTutors;
    DatabaseReference databaseProducts;
    DatabaseReference databaseCategory;
    FirebaseDatabase FireDB;

    private ShoppingService shoppingService;

    private View rootView;

    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_login, container, false);


        // Inflate the layout for this fragment
        return rootView;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onStart() {
        super.onStart();

        LocalBroadcastManager.getInstance(this.getActivity()).registerReceiver(ServiceConnected, new IntentFilter(SERVICE_CONNECTED_MAIN_ACTIVITY));

        if (((MainActivity)getActivity()).getShoppingService_fromMainActivity() != null)
        {
            initLoginFragment();
        }
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

    private void AddProduct(Product product, Category cat){
        String Id = databaseProducts.push().getKey();
        product.setProductID(Id);
        databaseCategory.child(cat.getCategoryName()).child(product.getProductName()).setValue(product);
        Toast.makeText(getContext(), "Added "+product.getProductName()+" to db",Toast.LENGTH_LONG).show();
    }

    private void AddCategory(Category category){
        String Id = databaseCategory.push().getKey();
        databaseCategory.child(category.getCategoryName()).setValue(category);
        Toast.makeText(getContext(), "Added "+category.getCategoryName()+" to db",Toast.LENGTH_LONG).show();
    }

    private void authenticate(String email,String pass,View view){
        try{
        mAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener((Activity) getContext(), new OnCompleteListener<AuthResult>() {
                     @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                         if(task.isSuccessful()){
                             //Authenticate
                             Toast.makeText(getContext(),"Authentication succeeded.",Toast.LENGTH_LONG).show();
                             FirebaseUser user = mAuth.getCurrentUser();
                             //Init Navigation
                             final NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
                             //Hide Keyboard
                             InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                             imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                             //Navigate
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

    private void initLoginFragment()
    {
        if (getActivity()!=null)
        {
            shoppingService = ((MainActivity)getActivity()).getShoppingService_fromMainActivity();

            mAuth = shoppingService.getFirebaseAuth_fromService();
            FireDB = shoppingService.getFirebaseDatabase_fromService();
            databaseTutors = FireDB.getReference("tutors");
            databaseProducts = FireDB.getReference("products");
            databaseCategory = FireDB.getReference("categories");

            View.OnClickListener s = Navigation.createNavigateOnClickListener(R.id.action_loginFragment_to_viewTutorsFragment);
            buttonCancel = rootView.findViewById(R.id.cancel_btn);
            buttonCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    NavController n = Navigation.findNavController(rootView);
                    n.navigate(R.id.action_loginFragment_to_viewTutorsFragment);
                }
            });

            email = rootView.findViewById(R.id.EmailEditText);
            password = rootView.findViewById(R.id.PasswordEditText);
            buttonLogin = rootView.findViewById(R.id.login_btn);

            buttonLogin.setOnClickListener(v -> {
                if(email.getText() != null && password.getText() != null){
                    authenticate(email.getText().toString(),password.getText().toString(),rootView);
                }
            });
        }

    }

    private BroadcastReceiver ServiceConnected = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            initLoginFragment();
        }
    };
}
