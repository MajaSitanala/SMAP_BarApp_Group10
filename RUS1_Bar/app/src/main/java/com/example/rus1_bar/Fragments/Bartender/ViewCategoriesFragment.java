package com.example.rus1_bar.Fragments.Bartender;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.example.rus1_bar.Activities.MainActivity;
import com.example.rus1_bar.Activities.ShoppingActivity;
import com.example.rus1_bar.Adapters.CategoryRecyclerAdapter;
import com.example.rus1_bar.Adapters.TutorRecyclerAdapter;
import com.example.rus1_bar.Models.Category;
import com.example.rus1_bar.Models.Tutor;
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
public class ViewCategoriesFragment extends Fragment {

    private static final String SERVICE_CONNECTED_SHOPPING_ACTIVITY = "Shopping service connected to Shopping Activity";

    private List<Category> testCategoryList = new ArrayList<>();
    RecyclerView categoryRecyclerView;
    RecyclerView.Adapter categoryRecyclerAdapter;
    RecyclerView.LayoutManager categoryLayoutManager;

    // Service reference
    private ShoppingService shoppingService;

    // Database
    private DatabaseReference databaseCategory;
    private FirebaseDatabase FireDB;

    public ViewCategoriesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // rootView is needed when using fingViewById because otherwise the views have not been created by the time the views are called.
        View rootView = inflater.inflate(R.layout.fragment_view_categories, container, false);                                              //https://stackoverflow.com/questions/26621060/display-a-recyclerview-in-fragment

        // Recycler View setup
        categoryRecyclerView = rootView.findViewById(R.id.categoryRecyclerView);

        //Creates the grid layout
        categoryLayoutManager = new GridLayoutManager(getActivity(), 3);                                                                //https://youtu.be/SD2t75T5RdY?t=1302
        categoryRecyclerView.setLayoutManager(categoryLayoutManager);

        //Recycler adapter setup
        categoryRecyclerAdapter = new CategoryRecyclerAdapter(getActivity(), testCategoryList);
        categoryRecyclerView.setAdapter(categoryRecyclerAdapter);

        // Inflate the layout for this fragment
        return rootView; // inflater.inflate(R.layout.fragment_view_tutors, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        // Local broadcast registration from main activity
        LocalBroadcastManager.getInstance(this.getActivity()).registerReceiver(ServiceConnected, new IntentFilter(SERVICE_CONNECTED_SHOPPING_ACTIVITY));

        if (((ShoppingActivity)getActivity()).getShoppingService_fromShoppingActivity() != null)
        {
            initViewCategoriesFragment();
        }
    }

    private void initViewCategoriesFragment()
    {
        if (getActivity()!=null)
        {
            // Service reference
            shoppingService = ((ShoppingActivity)getActivity()).getShoppingService_fromShoppingActivity();

            //Get categories from db
            FireDB = shoppingService.getFirebaseDatabase_fromService();
            databaseCategory = FireDB.getReference("categories");
            databaseCategory.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    testCategoryList.clear();
                    for (DataSnapshot categorySnapshot : dataSnapshot.getChildren()){
                        Category cat = categorySnapshot.getValue(Category.class);
                        testCategoryList.add(cat);
                        categoryRecyclerAdapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    private BroadcastReceiver ServiceConnected = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            initViewCategoriesFragment();
        }
    };

    private void fillTestCategoryList()
    {
        for (int i = 0; i<12; i++)
        {
            //Test data for the card view
            testCategoryList.add(new Category(i, "Drinks", R.drawable.drinks));
        }
    }
}



