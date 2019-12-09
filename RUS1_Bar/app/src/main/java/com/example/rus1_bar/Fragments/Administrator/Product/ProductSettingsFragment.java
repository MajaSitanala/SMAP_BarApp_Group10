package com.example.rus1_bar.Fragments.Administrator.Product;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rus1_bar.Activities.MainActivity;
import com.example.rus1_bar.Adapters.ProductDisplayAdapter;
import com.example.rus1_bar.Models.Category;
import com.example.rus1_bar.Models.Product;
import com.example.rus1_bar.R;
import com.example.rus1_bar.Repository.FirebaseRepository;
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
public class ProductSettingsFragment extends Fragment {

    private static final String SERVICE_CONNECTED_MAIN_ACTIVITY = "Service connected to the main Activity" ;

    private List<Category> mCategoryList = new ArrayList<>();
    private List<String> mCategoryStringList = new ArrayList<>();
    private List<Product> mProductList = new ArrayList<>();
    private List<StorageReference> mProductImageList = new ArrayList<>();
    private List<String> categoryNameList = new ArrayList<>();

    Button cancelBtn;
    Button addProductBtn;

    RecyclerView productRecyclerView;
    RecyclerView.Adapter productRecyclerAdapter;
    RecyclerView.LayoutManager productLayoutManager;
    FirebaseRepository firebaseRepository;

    // Database
    private DatabaseReference databaseCategoryDisplay;
    private FirebaseDatabase FireDB;

    private ShoppingService shoppingService;
    private View rootView;
    private Category cat;

    public ProductSettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_product_settings, container, false);

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();

        LocalBroadcastManager.getInstance(this.getActivity()).registerReceiver(ServiceConnected, new IntentFilter(SERVICE_CONNECTED_MAIN_ACTIVITY));

        if (((MainActivity)getActivity()).getShoppingService_fromMainActivity() != null)
        {
            initProductSettingsFragment();
        }
    }

    private void initProductSettingsFragment()
    {
        if (getActivity() != null)
        {
            shoppingService = ((MainActivity)getActivity()).getShoppingService_fromMainActivity();

            firebaseRepository = shoppingService.getFirebaseRepository_fromService();

            cancelBtn = rootView.findViewById(R.id.productsettingsCancelBtn);
            addProductBtn = rootView.findViewById(R.id.productSettingsAddBtn);
            productRecyclerView = rootView.findViewById(R.id.productSettingsRecycleView);

            View.OnClickListener cancelClick = Navigation.createNavigateOnClickListener(R.id.action_productSettingsFragment_to_settingsOverviewFragment);
            cancelBtn.setOnClickListener(cancelClick);

            View.OnClickListener addProductClick = Navigation.createNavigateOnClickListener(R.id.action_productSettingsFragment_to_addProductFragment);
            addProductBtn.setOnClickListener(addProductClick);

            productLayoutManager = new GridLayoutManager(getActivity(), 1);
            productRecyclerView.setLayoutManager(productLayoutManager);

            //Get categories from db
            FireDB = shoppingService.getFirebaseDatabase_fromService();
            databaseCategoryDisplay = FireDB.getReference("categories");
            databaseCategoryDisplay.addValueEventListener(new ValueEventListener()
            {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                {

                    for (DataSnapshot categorySnapshot : dataSnapshot.getChildren()){
                        cat = categorySnapshot.getValue(Category.class);
                        mCategoryList.add(cat);
                        mCategoryStringList.add(cat.getCategoryName());
                        //productRecyclerAdapter.notifyDataSetChanged();
                    }


                   for (Category category : mCategoryList)
                    {
                        DatabaseReference databaseProduct = FireDB.getReference("categories").child(category.getCategoryName());
                        ValueEventListener productListenter = new ValueEventListener()
                        {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                            {
                                    for (DataSnapshot productSnapshot : dataSnapshot.getChildren())
                                    {

                                        try {
                                            Product product = productSnapshot.getValue(Product.class);
                                            mProductList.add(product);
                                            categoryNameList.add(category.getCategoryName());

                                            productRecyclerAdapter.notifyDataSetChanged();
                                        }
                                        catch (DatabaseException E)
                                        {
                                            Log.e("DB x: " + E, "Error fom try catch.");
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError)
                            {
                                Log.e("PRODUCT fromService ", "Error did not load the products");
                            }
                        };
                        databaseProduct.addValueEventListener(productListenter);
                    }

                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            productRecyclerAdapter = new ProductDisplayAdapter(getActivity(), mProductList, categoryNameList, shoppingService);
            productRecyclerView.setAdapter(productRecyclerAdapter);
        }
    }

    private BroadcastReceiver ServiceConnected = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            initProductSettingsFragment();
        }
    };
}
