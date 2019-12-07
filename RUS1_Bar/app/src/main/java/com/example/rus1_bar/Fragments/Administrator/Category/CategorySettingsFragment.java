package com.example.rus1_bar.Fragments.Administrator.Category;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
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
import com.example.rus1_bar.Adapters.CategoryRecyclerDisplayAdapter;
import com.example.rus1_bar.Models.Category;
import com.example.rus1_bar.R;
import com.example.rus1_bar.Service.ShoppingService;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class CategorySettingsFragment extends Fragment {

    private static final String SERVICE_CONNECTED_MAIN_ACTIVITY = "Service connected to the main Activity" ;

    private List<Category> mCategoryList = new ArrayList<>();

    Button cancelBtn;
    Button addCategoryBtn;

    RecyclerView categoryRecyclerView;
    RecyclerView.Adapter categoryRecyclerAdapter;
    RecyclerView.LayoutManager categoryLayoutManager;

    // Database
    private DatabaseReference databaseCategoryDisplay;
    private FirebaseDatabase FireDB;

    ShoppingService shoppingService;

    private View rootView;

    public CategorySettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_category_settings, container, false);

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();

        LocalBroadcastManager.getInstance(this.getActivity()).registerReceiver(ServiceConnected, new IntentFilter(SERVICE_CONNECTED_MAIN_ACTIVITY));

        if (((MainActivity)getActivity()).getShoppingService_fromMainActivity() != null)
        {
            initCategorySettingsFragment();
        }
    }

    private void initCategorySettingsFragment()
    {
        if (getActivity() != null)
        {
            shoppingService = ((MainActivity)getActivity()).getShoppingService_fromMainActivity();


            cancelBtn = rootView.findViewById(R.id.categorySettingsCancel);
            addCategoryBtn = rootView.findViewById(R.id.categorySettingsAddBtn);
            categoryRecyclerView = rootView.findViewById(R.id.categorySettingsRecyclerView);

            View.OnClickListener cancelClick = Navigation.createNavigateOnClickListener(R.id.action_categorySettingsFragment_to_settingsOverviewFragment);
            cancelBtn.setOnClickListener(cancelClick);

            View.OnClickListener addCategoryClick = Navigation.createNavigateOnClickListener(R.id.action_categorySettingsFragment_to_addCategoriesFragment);
            addCategoryBtn.setOnClickListener(addCategoryClick);

            categoryLayoutManager = new GridLayoutManager(getActivity(), 1);
            categoryRecyclerView.setLayoutManager(categoryLayoutManager);

            //Get categories from db
            FireDB = shoppingService.getFirebaseDatabase_fromService();
            databaseCategoryDisplay = FireDB.getReference("categories");
            databaseCategoryDisplay.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                {
                    for (DataSnapshot categorySnapshot : dataSnapshot.getChildren()){
                        Category cat = categorySnapshot.getValue(Category.class);
                        mCategoryList.add(cat);
                        categoryRecyclerAdapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            categoryRecyclerAdapter = new CategoryRecyclerDisplayAdapter(getActivity(), mCategoryList);
            categoryRecyclerView.setAdapter(categoryRecyclerAdapter);
        }
    }

    private BroadcastReceiver ServiceConnected = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            initCategorySettingsFragment();
        }
    };


}
