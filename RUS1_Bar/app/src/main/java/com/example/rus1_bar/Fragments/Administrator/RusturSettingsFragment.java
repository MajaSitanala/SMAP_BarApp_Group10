package com.example.rus1_bar.Fragments.Administrator;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

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

    private static final String SERVICE_CONNECTED_MAIN_ACTIVITY = "Service connected to the main Activity" ;
    private View rootView;

    private ShoppingService shoppingService;


    public RusturSettingsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_rustur_settings, container, false);

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_rustur_settings, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        LocalBroadcastManager.getInstance(this.getActivity()).registerReceiver(ServiceConnected, new IntentFilter(SERVICE_CONNECTED_MAIN_ACTIVITY));

        if (((MainActivity)getActivity()).getShoppingService_fromMainActivity() != null)
        {
            initRusturSettingsFragment();
        }
    }


    private void initRusturSettingsFragment()
    {
        if (getActivity()!=null)
        {

            //TODO: add logic for fragment here!

        }
    }

    private BroadcastReceiver ServiceConnected = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            initRusturSettingsFragment();
        }
    };

}
