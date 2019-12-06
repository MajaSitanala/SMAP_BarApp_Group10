package com.example.rus1_bar.Fragments.Administrator;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.rus1_bar.Activities.MainActivity;
import com.example.rus1_bar.R;
import com.example.rus1_bar.Service.ShoppingService;

/**
 * A simple {@link Fragment} subclass.
 */
public class RusturSettingsFragment extends Fragment
{
    private ShoppingService shoppingService;


    public RusturSettingsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Service
        shoppingService = ((MainActivity)getActivity()).getShoppingService_fromMainActivity();


        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_rustur_settings, container, false);
    }

}
