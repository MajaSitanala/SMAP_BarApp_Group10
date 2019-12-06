package com.example.rus1_bar.Fragments.Administrator;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.rus1_bar.Models.Rustur;
import com.example.rus1_bar.Models.Tutor;
import com.example.rus1_bar.Activities.MainActivity;
import com.example.rus1_bar.R;
import com.example.rus1_bar.Repository.FirebaseRepository;
import com.example.rus1_bar.Service.ShoppingService;

/**
 * A simple {@link Fragment} subclass.
 */
public class BillSettingsFragment extends Fragment {

    private static final String SERVICE_CONNECTED_MAIN_ACTIVITY = "Service connected to the main Activity" ;

    Button seeBillBtn;
    Button sendBillBtn;
    Button exportBillBtn;
    Button cancelBtn;

    private ShoppingService shoppingService;
    private View rootView;

    public BillSettingsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_bill_settings, container, false);

        // Inflate the layout for this fragment
        return rootView;
    }


    @Override
    public void onStart() {
        super.onStart();

        LocalBroadcastManager.getInstance(this.getActivity()).registerReceiver(ServiceConnected, new IntentFilter(SERVICE_CONNECTED_MAIN_ACTIVITY));

        if (((MainActivity)getActivity()).getShoppingService_fromMainActivity() != null)
        {
            initBillSettingsFragment();
        }
    }

    private void initBillSettingsFragment()
    {
        if (getActivity() != null)
        {
            // Service
            shoppingService = ((MainActivity)getActivity()).getShoppingService_fromMainActivity();

            //Go to a tutorRecyclerView (same one with params to do diff stuff when pressed a tutor
            seeBillBtn = rootView.findViewById(R.id.billSettingsViewBillBtn);

            sendBillBtn = rootView.findViewById(R.id.billSettingsSendBillBtn);

            exportBillBtn = rootView.findViewById(R.id.billSettingsExportBillsBtn);
            exportBillBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FirebaseRepository rep = new FirebaseRepository();
                    rep.SaveAllPurchases(new Rustur("TestTur"),getContext());
                }
            });
            //exportBillBtn.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.ac));

            cancelBtn = rootView.findViewById(R.id.billSettingsCancelBtn);
            cancelBtn.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_billSettingsFragment_to_settingsOverviewFragment));
        }
    }


    private BroadcastReceiver ServiceConnected = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            initBillSettingsFragment();
        }
    };

}
