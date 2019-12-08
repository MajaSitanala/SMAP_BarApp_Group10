package com.example.rus1_bar.Fragments.Bartender;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import com.example.rus1_bar.Activities.MainActivity;
import com.example.rus1_bar.Activities.ShoppingActivity;
import com.example.rus1_bar.Adapters.ProductRecyclerAdapter;

import com.example.rus1_bar.Models.Category;
import com.example.rus1_bar.Models.Product;
import com.example.rus1_bar.Models.ShoppingViewModel;
import com.example.rus1_bar.Models.Tutor;
import com.example.rus1_bar.R;
import com.example.rus1_bar.Service.ShoppingService;
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

    private static final String SERVICE_CONNECTED_SHOPPING_ACTIVITY = "Shopping service connected to Shopping Activity";
    private static final String CATEGOTY_NAME = "category name from adapter";

    private List<Product> testProductList = new ArrayList<>();

    // Recycler view
    private RecyclerView productRecyclerView;
    private RecyclerView.Adapter productRecyclerAdapter;
    private RecyclerView.LayoutManager productLayoutManager;

    // Database
    private DatabaseReference databaseProduct;
    private FirebaseDatabase FireDB;

    // Service reference
    private ShoppingService shoppingService;
    private View rootView;
    private String cat;

    public ViewProductsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // RootView is needed when using fingViewById because otherwise the views have not been created by the time the views are called.
        rootView = inflater.inflate(R.layout.fragment_view_products, container, false);                                              //https://stackoverflow.com/questions/26621060/display-a-recyclerview-in-fragment

        // Broadcast Manager
        LocalBroadcastManager.getInstance(this.getActivity()).registerReceiver(ServiceConnected, new IntentFilter(SERVICE_CONNECTED_SHOPPING_ACTIVITY));

        //Chosen category
        cat = getArguments().getString("category");//listener.getCategoryString();//


        // Inflate the layout for this fragment
        return rootView; // inflater.inflate(R.layout.fragment_view_tutors, container, false);
    }


    @Override
    public void onStart() {
        super.onStart();

        // Service reference
        if (((ShoppingActivity)getActivity()).getShoppingService_fromShoppingActivity() != null)
        {
            initViewProductFragment();
        }
    }

    private void initViewProductFragment()
    {
        if (getActivity()!=null)
        {
            // Service
            shoppingService = ((ShoppingActivity)getActivity()).getShoppingService_fromShoppingActivity();

            // Recycler View setup
            productRecyclerView = rootView.findViewById(R.id.productRecyclerView);

            //Creates the grid layout
            if(rootView.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
            {
                productLayoutManager = new GridLayoutManager(getActivity(), 4);                                                                //https://youtu.be/SD2t75T5RdY?t=1302
                productRecyclerView.setLayoutManager(productLayoutManager);
            }
            else
            {
                productLayoutManager = new GridLayoutManager(getActivity(), 3);                                                                //https://youtu.be/SD2t75T5RdY?t=1302
                productRecyclerView.setLayoutManager(productLayoutManager);
            }


            //Init Database ref
            FireDB = shoppingService.getFirebaseDatabase_fromService();
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
            productRecyclerAdapter = new ProductRecyclerAdapter(getActivity(), testProductList,cat);
            productRecyclerView.setAdapter(productRecyclerAdapter);
        }
    }


    private BroadcastReceiver ServiceConnected = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent)
        {
             initViewProductFragment();
        }
    };


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



