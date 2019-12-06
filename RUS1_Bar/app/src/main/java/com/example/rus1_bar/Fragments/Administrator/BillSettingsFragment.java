package com.example.rus1_bar.Fragments.Administrator;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.rus1_bar.Activities.MainActivity;
import com.example.rus1_bar.R;
import com.example.rus1_bar.Service.ShoppingService;

/**
 * A simple {@link Fragment} subclass.
 */
public class BillSettingsFragment extends Fragment {

    Button seeBillBtn;
    Button sendBillBtn;
    Button exportBillBtn;
    Button cancelBtn;

    private ShoppingService shoppingService;

    public BillSettingsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_bill_settings, container, false);

        // Service
        shoppingService = ((MainActivity)getActivity()).getShoppingService_fromMainActivity();

        //Go to a tutorRecyclerView (same one with params to do diff stuff when pressed a tutor
        seeBillBtn = rootView.findViewById(R.id.billSettingsViewBillBtn);

        sendBillBtn = rootView.findViewById(R.id.billSettingsSendBillBtn);

        exportBillBtn = rootView.findViewById(R.id.billSettingsExportBillsBtn);

        cancelBtn = rootView.findViewById(R.id.billSettingsCancelBtn);
        cancelBtn.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_billSettingsFragment_to_settingsOverviewFragment));



        // Inflate the layout for this fragment
        return rootView;
    }

}
