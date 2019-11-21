package com.example.rus1_bar.Fragments.Bartender;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.rus1_bar.Adapters.CategoryRecyclerAdapter;
import com.example.rus1_bar.Adapters.ShoppingCardRecyclerAdapter;
import com.example.rus1_bar.Models.Category;
import com.example.rus1_bar.Models.Product;
import com.example.rus1_bar.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ShoppingCardFragment extends Fragment {



    private List<Product> testProductList = new ArrayList<>();
    RecyclerView shoppingCardRecyclerView;
    RecyclerView.Adapter shoppingCardRecyclerAdapter;
    RecyclerView.LayoutManager shoppingCardLayoutManager;

    public ShoppingCardFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // rootView is needed when using fingViewById because otherwise the views have not been created by the time the views are called.
        View rootView = inflater.inflate(R.layout.fragment_view_tutors, container, false);                                              //https://stackoverflow.com/questions/26621060/display-a-recyclerview-in-fragment


        //Test data for the card view
        fillTestShoppingCardList();

        // Recycler View setup
        shoppingCardRecyclerView = rootView.findViewById(R.id.tutorRecyclerView);

        //Creates the grid layout
        shoppingCardLayoutManager = new GridLayoutManager(getActivity(), 3);                                                                //https://youtu.be/SD2t75T5RdY?t=1302
        shoppingCardRecyclerView.setLayoutManager(shoppingCardLayoutManager);

        //Recycler adapter setup
        shoppingCardRecyclerAdapter = new ShoppingCardRecyclerAdapter(getActivity(), testProductList);

        shoppingCardRecyclerView.setAdapter(shoppingCardRecyclerAdapter);


        // Inflate the layout for this fragment
        return rootView; // inflater.inflate(R.layout.fragment_view_tutors, container, false);
    }

    private void fillTestShoppingCardList()
    {
        for (int i = 0; i<12; i++)
        {
            //Test data for the card view
            testProductList.add(new Product(i, i, "Gin Hass", 20));
        }

    }
}
