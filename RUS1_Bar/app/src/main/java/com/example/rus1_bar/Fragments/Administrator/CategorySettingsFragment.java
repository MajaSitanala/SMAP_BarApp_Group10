package com.example.rus1_bar.Fragments.Administrator;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rus1_bar.Adapters.CategoryDisplayAdapter;
import com.example.rus1_bar.Models.Category;
import com.example.rus1_bar.R;
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

    private List<Category> mCategoryList = new ArrayList<>();

    Button cancelBtn;
    Button addCategoryBtn;

    RecyclerView categoryRecyclerView;
    RecyclerView.Adapter categoryRecyclerAdapter;
    RecyclerView.LayoutManager categoryLayoutManager;

    // Database
    private DatabaseReference databaseCategoryDisplay;
    private FirebaseDatabase FireDB;

    public CategorySettingsFragment() {
        // Required empty public constructor
    }

    //TODO: Make Correct recycler adapter - so it won't access the category
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_category_settings, container, false);

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
        FireDB = FirebaseDatabase.getInstance();
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

        categoryRecyclerAdapter = new CategoryDisplayAdapter(getActivity(), mCategoryList);
        categoryRecyclerView.setAdapter(categoryRecyclerAdapter);

        return rootView;
    }
}
