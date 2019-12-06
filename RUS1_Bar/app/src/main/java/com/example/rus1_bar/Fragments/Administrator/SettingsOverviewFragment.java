package com.example.rus1_bar.Fragments.Administrator;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.navigation.Navigation;

import com.example.rus1_bar.Activities.MainActivity;
import com.example.rus1_bar.R;
import com.example.rus1_bar.Service.ShoppingService;
import com.google.firebase.auth.FirebaseAuth;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsOverviewFragment extends Fragment {

    private static final String SERVICE_CONNECTED_MAIN_ACTIVITY = "Service connected to the main Activity" ;


    Button tutorSettingsBtn;
    Button categorySettingsBtn;
    Button productSettingsBtn;
    Button rusturSettingsBtn;
    Button billSettingsBtn;
    Button logOffBtn;


    FirebaseAuth FAuth;

    ShoppingService shoppingService;
    private View rootView;

    public SettingsOverviewFragment() {
        // Required empty public constructor
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_settings_overview, container, false);
        // Inflate the layout for this fragment
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();

        LocalBroadcastManager.getInstance(this.getActivity()).registerReceiver(ServiceConnected, new IntentFilter(SERVICE_CONNECTED_MAIN_ACTIVITY));

        if (((MainActivity)getActivity()).getShoppingService_fromMainActivity() != null)
        {
            initSettingsFragment();
        }
    }

    private void initSettingsFragment()
    {
        if (getActivity()!=null)
        {
            shoppingService = ((MainActivity)getActivity()).getShoppingService_fromMainActivity();

            FAuth = shoppingService.getFirebaseAuth_fromService();
            tutorSettingsBtn = rootView.findViewById(R.id.tutorSettingsBtn);
            categorySettingsBtn = rootView.findViewById(R.id.categorySettingsBtn);
            productSettingsBtn = rootView.findViewById(R.id.productSettingsBtn);
            rusturSettingsBtn = rootView.findViewById(R.id.rusturSettingsBtn);
            billSettingsBtn = rootView.findViewById(R.id.billSettingsBtn);
            logOffBtn = rootView.findViewById(R.id.logOffBtn);

            logOffBtn.setOnClickListener(v -> {
                FAuth.signOut();
                Navigation.createNavigateOnClickListener(R.id.action_settingsOverviewFragment_to_loginFragment).onClick(v);
                Toast.makeText(getContext(),"Succesfully logged out",Toast.LENGTH_LONG).show();
            });

            View.OnClickListener tutorSettingsClick = Navigation.createNavigateOnClickListener(R.id.action_settingsOverviewFragment_to_tutorSettingsFragment);
            tutorSettingsBtn.setOnClickListener(tutorSettingsClick);

            View.OnClickListener categorySettingsClick = Navigation.createNavigateOnClickListener(R.id.action_settingsOverviewFragment_to_categorySettingsFragment);
            categorySettingsBtn.setOnClickListener(categorySettingsClick);

            View.OnClickListener productSettingsClick = Navigation.createNavigateOnClickListener(R.id.action_settingsOverviewFragment_to_productSettingsFragment);
            productSettingsBtn.setOnClickListener(productSettingsClick);

            View.OnClickListener rusturSettingsClick = Navigation.createNavigateOnClickListener(R.id.action_settingsOverviewFragment_to_rusturSettingsFragment);
            rusturSettingsBtn.setOnClickListener(rusturSettingsClick);

            View.OnClickListener billSettingsClick = Navigation.createNavigateOnClickListener(R.id.action_settingsOverviewFragment_to_billSettingsFragment);
            billSettingsBtn.setOnClickListener(billSettingsClick);
        }

    }

    private BroadcastReceiver ServiceConnected = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            initSettingsFragment();
        }
    };
}
