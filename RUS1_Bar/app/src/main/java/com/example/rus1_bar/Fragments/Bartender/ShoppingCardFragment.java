package com.example.rus1_bar.Fragments.Bartender;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.rus1_bar.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ShoppingCardFragment extends Fragment {


    public ShoppingCardFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_shopping_card, container, false);
    }
}
