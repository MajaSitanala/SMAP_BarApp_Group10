package com.example.rus1_bar.Fragments.Administrator;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.rus1_bar.Activities.MainActivity;
import com.example.rus1_bar.Adapters.ProductDisplayAdapter;
import com.example.rus1_bar.Adapters.ProductRecyclerAdapter;
import com.example.rus1_bar.Models.Product;
import com.example.rus1_bar.R;
import com.example.rus1_bar.Service.ShoppingService;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProductSettingsFragment extends Fragment {

    private List<Product> testProductList = new ArrayList<>();

    Button addProductBtn;
    Button editProductBtn;
    Button addCategoryBtn;
    Button editCategoryBtn;
    Button cancelBtn;

    RecyclerView productRecyclerView;
    RecyclerView.Adapter productRecyclerAdapter;
    RecyclerView.LayoutManager productLayoutManager;

    private ShoppingService shoppingService;

    public ProductSettingsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_product_settings, container, false);

        // Shopping service
        shoppingService = ((MainActivity)getActivity()).getShoppingService_fromMainActivity();

        addProductBtn = rootView.findViewById(R.id.productAddBtn);

        View.OnClickListener addProductClick = Navigation.createNavigateOnClickListener(R.id.action_productSettingsFragment_to_addProductFragment);
        addProductBtn.setOnClickListener(addProductClick);

        //Test data for the card view
        fillTestShoppingCardList();

        //Cancel button go back
        View.OnClickListener cancelClick = Navigation.createNavigateOnClickListener(R.id.action_productSettingsFragment_to_settingsOverviewFragment);
        cancelBtn = rootView.findViewById(R.id.productsettingsCancelBtn);

        // Recycler View setup
        productRecyclerView = rootView.findViewById(R.id.productSettingsRecycleView);

        //Creates the grid layout
        productLayoutManager = new GridLayoutManager(getActivity(), 1);                                                                //https://youtu.be/SD2t75T5RdY?t=1302
        productRecyclerView.setLayoutManager(productLayoutManager);

        //Recycler adapter setup
        productRecyclerAdapter = new ProductDisplayAdapter(getActivity(), testProductList);

        productRecyclerView.setAdapter(productRecyclerAdapter);


        // Inflate the layout for this fragment
        return rootView; // inflater.inflate(R.layout.fragment_view_tutors, container, false);
    }

    private void fillTestShoppingCardList()
    {
        for (int i = 0; i<4; i++)
        {
            //Test data for the card view
            testProductList.add(new Product("i",i, "Gin Hass", 20, R.drawable.ginhass));
            testProductList.add(new Product("i",i, "Filur", 20, R.drawable.filur));
            testProductList.add(new Product("i",i, "White Russian", 20, R.drawable.whiterussianlang));
        }
    }
}
