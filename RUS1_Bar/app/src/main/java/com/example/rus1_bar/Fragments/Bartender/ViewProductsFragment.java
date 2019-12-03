package com.example.rus1_bar.Fragments.Bartender;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import com.example.rus1_bar.Adapters.ProductRecyclerAdapter;

import com.example.rus1_bar.Models.Category;
import com.example.rus1_bar.Models.Product;
import com.example.rus1_bar.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ViewProductsFragment extends Fragment {

    private List<Product> testProductList = new ArrayList<>();
    private RecyclerView productRecyclerView;
    private RecyclerView.Adapter productRecyclerAdapter;
    private RecyclerView.LayoutManager productLayoutManager;
    private DatabaseReference databaseProduct;
    private FirebaseDatabase FireDB;


    public ViewProductsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // rootView is needed when using fingViewById because otherwise the views have not been created by the time the views are called.
        View rootView = inflater.inflate(R.layout.fragment_view_products, container, false);                                              //https://stackoverflow.com/questions/26621060/display-a-recyclerview-in-fragment


        //Test data for the card view
        //fillTestProductList();

        // Recycler View setup
        productRecyclerView = rootView.findViewById(R.id.productRecyclerView);

        //Creates the grid layout
        productLayoutManager = new GridLayoutManager(getActivity(), 3);                                                                //https://youtu.be/SD2t75T5RdY?t=1302
        productRecyclerView.setLayoutManager(productLayoutManager);

        //Chosen category
        String cat = getArguments().getString("category");

        //Init Database ref
        FireDB = FirebaseDatabase.getInstance();
        databaseProduct = FireDB.getReference("categories").child(cat);
        databaseProduct.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                testProductList.clear();
                for (DataSnapshot productSnapshot : dataSnapshot.getChildren())
                {
                    try {
                        Product product = productSnapshot.getValue(Product.class);
                        testProductList.add(product);
                    }
                    catch (DatabaseException E)
                    {
                        Log.e("DB x: " + E, "Error fom try catch.");
                    }



                    productRecyclerAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        //Recycler adapter setup
        productRecyclerAdapter = new ProductRecyclerAdapter(getActivity(), testProductList);
        productRecyclerView.setAdapter(productRecyclerAdapter);


        // Inflate the layout for this fragment
        return rootView; // inflater.inflate(R.layout.fragment_view_tutors, container, false);
    }

    private void fillTestProductList()
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



