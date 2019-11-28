package com.example.rus1_bar.Fragments.Administrator;


import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.rus1_bar.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsOverviewFragment extends Fragment {

    Button tutorSettingsBtn;
    Button productSettingsBtn;
    Button rusturSettingsBtn;
    Button billSettingsBtn;
    Button logOffBtn;

    public SettingsOverviewFragment() {
        // Required empty public constructor
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tutorSettingsBtn = view.findViewById(R.id.tutorSettingsBtn);
        productSettingsBtn = view.findViewById(R.id.productSettingsBtn);
        rusturSettingsBtn = view.findViewById(R.id.rusturSettingsBtn);
        billSettingsBtn = view.findViewById(R.id.billSettingsBtn);
        logOffBtn = view.findViewById(R.id.logOffBtn);

        View.OnClickListener tutorSettingsClick = Navigation.createNavigateOnClickListener(R.id.action_settingsOverviewFragment_to_tutorSettingsFragment);
        tutorSettingsBtn.setOnClickListener(tutorSettingsClick);

        View.OnClickListener productSettingsClick = Navigation.createNavigateOnClickListener(R.id.action_settingsOverviewFragment_to_productSettingsFragment);
        productSettingsBtn.setOnClickListener(productSettingsClick);

        View.OnClickListener rusturSettingsClick = Navigation.createNavigateOnClickListener(R.id.action_settingsOverviewFragment_to_rusturSettingsFragment);
        rusturSettingsBtn.setOnClickListener(rusturSettingsClick);

        View.OnClickListener billSettingsClick = Navigation.createNavigateOnClickListener(R.id.action_settingsOverviewFragment_to_billSettingsFragment);
        billSettingsBtn.setOnClickListener(billSettingsClick);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings_overview, container, false);
    }



}
