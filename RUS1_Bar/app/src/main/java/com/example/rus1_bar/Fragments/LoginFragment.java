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
            dummyDataInit();

            if(email.getText() != null && password.getText() != null){
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


    private void dummyDataInit()
    {

        AddTutor(new Tutor("Chris Broberg","Prak10",30248747,"mail",R.drawable.praktikant));
        AddTutor(new Tutor("Maja Andersen","Crystal",12341234,"mail",R.drawable.crystal));
        AddTutor(new Tutor("Mikkel Bleeg","Pampers",1234234,"mail",R.drawable.bleeg));
        AddTutor(new Tutor("Andreas Blaabjerg","Flobber",1234234,"mail",R.drawable.flobber));


        AddCategory(new Category(1,"Drinks",R.drawable.drinks));
        AddCategory(new Category(2,"Snacks",R.drawable.snacks));
        AddCategory(new Category(3,"Shots",R.drawable.shots));
        AddCategory(new Category(4,"Sodavand",R.drawable.soda));
        AddCategory(new Category(5,"Kaffe",R.drawable.coffe));
        AddCategory(new Category(6,"Øl",R.drawable.tuborg));
        AddCategory(new Category(6,"RTD",R.drawable.breezer));

        AddProduct(new Product("0",1,"Filur",30,R.drawable.filur),new Category(1,"Drinks",R.drawable.drinks));
        AddProduct(new Product("1",1,"Gin Hass",30,R.drawable.ginhass),new Category(1,"Drinks",R.drawable.drinks));
        AddProduct(new Product("2",1,"White Russian",30,R.drawable.whiterussianlang),new Category(1,"Drinks",R.drawable.drinks));

        AddProduct(new Product("3",1,"Sort kaffe",5,R.drawable.coffe),new Category(5,"Kaffe",R.drawable.coffe));
        AddProduct(new Product("4",1,"Latte",5,R.drawable.latte),new Category(5,"Kaffe",R.drawable.coffe));
        AddProduct(new Product("5",1,"Espresso",5,R.drawable.espresso),new Category(5,"Kaffe",R.drawable.coffe));
        AddProduct(new Product("6",1,"Cappuccino",5,R.drawable.cappuccino),new Category(5,"Kaffe",R.drawable.coffe));
        AddProduct(new Product("7",1,"Choko-cino",5,R.drawable.chococino),new Category(5,"Kaffe",R.drawable.coffe));
        AddProduct(new Product("8",1,"Alm kaffe",5,R.drawable.blackcoffee),new Category(5,"Kaffe",R.drawable.coffe));

        AddProduct(new Product("9",1,"Cashews",5,R.drawable.cashews),new Category(2,"Snacks",R.drawable.snacks));
        AddProduct(new Product("10",1,"Kims HavSalt",5,R.drawable.havsalt),new Category(2,"Snacks",R.drawable.snacks));
        AddProduct(new Product("11",1,"Peanuts",5,R.drawable.peanuts),new Category(2,"Snacks",R.drawable.snacks));
        AddProduct(new Product("12",1,"Saltmandler",5,R.drawable.saltalmonds),new Category(2,"Snacks",R.drawable.snacks));
        AddProduct(new Product("13",1,"Kims SnackChips",5,R.drawable.snackchips),new Category(2,"Snacks",R.drawable.snacks));
        AddProduct(new Product("14",1,"Kims SnackChips tomat",5,R.drawable.snacktomat),new Category(2,"Snacks",R.drawable.snacks));
        AddProduct(new Product("15",1,"Kims SourCream",5,R.drawable.sourcream),new Category(2,"Snacks",R.drawable.snacks));
        AddProduct(new Product("16",1,"Kims Sour&dild",5,R.drawable.sourdild),new Category(2,"Snacks",R.drawable.snacks));
        AddProduct(new Product("17",1,"popcorn",5,R.drawable.popcorn),new Category(2,"Snacks",R.drawable.snacks));

        AddProduct(new Product("18",1,"Gajol Shot",5,R.drawable.gajol),new Category(3,"Shots",R.drawable.shots));
        AddProduct(new Product("19",1,"Pure Shot",5,R.drawable.pure),new Category(3,"Shots",R.drawable.shots));
        AddProduct(new Product("20",1,"Rom Shot",5,R.drawable.rumshots),new Category(3,"Shots",R.drawable.shots));
        AddProduct(new Product("21",1,"Tequila Shot",5,R.drawable.tequila),new Category(3,"Shots",R.drawable.shots));
        AddProduct(new Product("22",1,"Whiskey Shot",5,R.drawable.whiskey),new Category(3,"Shots",R.drawable.shots));
        AddProduct(new Product("23",1,"Vodka Shot",5,R.drawable.shots),new Category(3,"Shots",R.drawable.shots));

        AddProduct(new Product("24",1,"Cola",5,R.drawable.cola),new Category(3,"Sodavand",R.drawable.soda));
        AddProduct(new Product("25",1,"Cult",5,R.drawable.cult),new Category(3,"Sodavand",R.drawable.soda));
        AddProduct(new Product("26",1,"Fanta",5,R.drawable.fanta),new Category(3,"Sodavand",R.drawable.soda));
        AddProduct(new Product("27",1,"Fanta Exotic",5,R.drawable.fantaexotic),new Category(3,"Sodavand",R.drawable.soda));
        AddProduct(new Product("28",1,"Mountain Dew",5,R.drawable.mountaindew),new Category(3,"Sodavand",R.drawable.soda));
        AddProduct(new Product("29",1,"Redbull",5,R.drawable.redbull),new Category(3,"Sodavand",R.drawable.soda));
        AddProduct(new Product("30",1,"Sprite",5,R.drawable.sprite),new Category(3,"Sodavand",R.drawable.soda));

        AddProduct(new Product("31",1,"Tuborg",5,R.drawable.tuborg),new Category(6,"Øl",R.drawable.tuborg));
        AddProduct(new Product("32",1,"Carlsberg",5,R.drawable.carlsberg),new Category(6,"Øl",R.drawable.tuborg));
        AddProduct(new Product("33",1,"Ceres Top",5,R.drawable.ceres),new Category(6,"Øl",R.drawable.tuborg));
        AddProduct(new Product("34",1,"Slots",5,R.drawable.slots),new Category(6,"Øl",R.drawable.tuborg));
        AddProduct(new Product("35",1,"VestFyn",5,R.drawable.vestfyn),new Category(6,"Øl",R.drawable.tuborg));

        AddProduct(new Product("36",1,"Breezer",5,R.drawable.breezer),new Category(6,"RTD",R.drawable.breezer));
        AddProduct(new Product("37",1,"Breezer Lime",5,R.drawable.breezerlime),new Category(6,"RTD",R.drawable.breezer));
        AddProduct(new Product("38",1,"Breezer Strawberry",5,R.drawable.breezerstrawberry),new Category(6,"RTD",R.drawable.breezer));
        AddProduct(new Product("39",1,"BreezerLime",5,R.drawable.breezerlime),new Category(6,"RTD",R.drawable.breezer));
        AddProduct(new Product("40",1,"Mokai",5,R.drawable.mokai),new Category(6,"RTD",R.drawable.breezer));
        AddProduct(new Product("41",1,"Mokai Forrest",5,R.drawable.mokaiforrest),new Category(6,"RTD",R.drawable.breezer));
        AddProduct(new Product("42",1,"Mokai Lime",5,R.drawable.mokailime),new Category(6,"RTD",R.drawable.breezer));
        AddProduct(new Product("43",1,"Mokai Pink",5,R.drawable.mokaipink),new Category(6,"RTD",R.drawable.breezer));
        AddProduct(new Product("44",1,"Shaker",5,R.drawable.shaker),new Category(6,"RTD",R.drawable.breezer));
        AddProduct(new Product("45",1,"Shaker Pineapple",5,R.drawable.shakerpineapple),new Category(6,"RTD",R.drawable.breezer));

    }


}
