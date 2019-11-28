package com.example.rus1_bar.Fragments.Bartender;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.rus1_bar.Adapters.CategoryRecyclerAdapter;
import com.example.rus1_bar.Adapters.TutorRecyclerAdapter;
import com.example.rus1_bar.Models.Category;
import com.example.rus1_bar.Models.Tutor;
import com.example.rus1_bar.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ViewCategoriesFragment extends Fragment {

    private List<Category> testCategoryList = new ArrayList<>();
    RecyclerView categoryRecyclerView;
    RecyclerView.Adapter categoryRecyclerAdapter;
    RecyclerView.LayoutManager categoryLayoutManager;

    public ViewCategoriesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // rootView is needed when using fingViewById because otherwise the views have not been created by the time the views are called.
        View rootView = inflater.inflate(R.layout.fragment_view_categories, container, false);                                              //https://stackoverflow.com/questions/26621060/display-a-recyclerview-in-fragment

        //Test data for the card view
        fillTestCategoryList();

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

    private void fillTestCategoryList()
    {
        for (int i = 0; i<12; i++)
        {
            //Test data for the card view
            testCategoryList.add(new Category(i, "Drinks", R.drawable.drinks));
        }

    }

}



