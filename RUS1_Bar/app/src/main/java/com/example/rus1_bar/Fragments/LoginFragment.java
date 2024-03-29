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
                    //dummyDataInit();
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

    private void dummyDataInit()
    {

        Tutor c = new Tutor("Chris Broberg","Prak10",30248747,"mail",R.drawable.praktikant);
        c.setImageUrl("p");

        Tutor ma = new Tutor("Maja Andersen","Crystal",12341234,"mail",R.drawable.crystal);
        ma.setImageUrl("crystal");

        Tutor mi = new Tutor("Mikkel Bleeg","Pampers",1234234,"mail",R.drawable.bleeg);
        mi.setImagename("ble");

        Tutor a = new Tutor("Andreas Blaabjerg","Flobber",1234234,"mail",R.drawable.flobber);
        a.setImagename("flobber");

        AddTutor(c);
        AddTutor(new Tutor("Maja Andersen","Crystal",12341234,"mail",R.drawable.crystal));
        AddTutor(new Tutor("Mikkel Bleeg","Pampers",1234234,"mail",R.drawable.ble));
        AddTutor(new Tutor("Andreas Blaabjerg","Flobber",1234234,"mail",R.drawable.defaultimg));

        AddCategory(new Category(1,"Drinks",R.drawable.drinks));
        AddCategory(new Category(2,"Snacks",R.drawable.snacks));
        AddCategory(new Category(3,"Shots",R.drawable.shots));
        AddCategory(new Category(4,"Sodavand",R.drawable.soda));
        AddCategory(new Category(5,"Kaffe",R.drawable.coffee));
        AddCategory(new Category(6,"Øl",R.drawable.tuborg));
        AddCategory(new Category(6,"RTD",R.drawable.breezer));

        AddProduct(new Product("0",1,"Filur",30,R.drawable.filur),new Category(1,"Drinks",R.drawable.drinks));
        AddProduct(new Product("1",1,"Gin Hass",30,R.drawable.ginhass),new Category(1,"Drinks",R.drawable.drinks));
        AddProduct(new Product("2",1,"White Russian",30,R.drawable.whiterussianlang),new Category(1,"Drinks",R.drawable.drinks));

        AddProduct(new Product("3",1,"Sort kaffe",5,R.drawable.coffee),new Category(5,"Kaffe",R.drawable.coffee));
        AddProduct(new Product("4",1,"Latte",5,R.drawable.latte),new Category(5,"Kaffe",R.drawable.coffee));
        AddProduct(new Product("5",1,"Espresso",5,R.drawable.espresso),new Category(5,"Kaffe",R.drawable.coffee));
        AddProduct(new Product("6",1,"Cappuccino",5,R.drawable.cappuccino),new Category(5,"Kaffe",R.drawable.coffee));
        AddProduct(new Product("7",1,"Choko-cino",5,R.drawable.chococino),new Category(5,"Kaffe",R.drawable.coffee));
        AddProduct(new Product("8",1,"Alm kaffe",5,R.drawable.blackcoffee),new Category(5,"Kaffe",R.drawable.coffee));

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

