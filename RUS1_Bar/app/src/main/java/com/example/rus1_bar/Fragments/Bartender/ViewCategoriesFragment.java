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
public class ViewCategoriesFragment extends Fragment {


    public ViewCategoriesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_view_categories, container, false);
    }

}
