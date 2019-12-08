package com.example.rus1_bar.Fragments.Administrator;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.rus1_bar.Models.Rustur;
import com.example.rus1_bar.Models.Tutor;
import com.example.rus1_bar.Activities.MainActivity;
import com.example.rus1_bar.R;
import com.example.rus1_bar.Repository.FirebaseRepository;
import com.example.rus1_bar.Service.ShoppingService;

import java.io.File;
import java.net.URI;

/**
 * A simple {@link Fragment} subclass.
 */
public class BillSettingsFragment extends Fragment {

    private static final String SERVICE_CONNECTED_MAIN_ACTIVITY = "Service connected to the main Activity" ;

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



            sendBillBtn = rootView.findViewById(R.id.billSettingsSendBillBtn);
            sendBillBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getActivity(),"Comming soon.",Toast.LENGTH_LONG);
                }
            });

            exportBillBtn = rootView.findViewById(R.id.billSettingsExportBillsBtn);
            exportBillBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FirebaseRepository rep = new FirebaseRepository();
                    rep.SaveAllPurchases(shoppingService.currentRustur,getContext());
                    Toast.makeText(getActivity(),R.string.exporting,Toast.LENGTH_LONG).show();
                }
            });
            //exportBillBtn.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.ac));

            cancelBtn = rootView.findViewById(R.id.billSettingsCancelBtn);
            cancelBtn.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_billSettingsFragment_to_settingsOverviewFragment));

            sendBillBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    /** // https://developer.android.com/training/sharing/send

                    Toast.makeText(getActivity(),R.string.ExportingToMail,Toast.LENGTH_LONG).show();

                    //Uri uriToBillCSV = Uri.parse(""); //
                    Uri uriToBillCSV = Uri.fromFile(new File(""));    //TODO: Insert link to Bar Bill CSV from Broberg

                    Intent mailIntent = new Intent();
                    mailIntent.setAction(Intent.ACTION_SEND);
                    //mailIntent.putExtra(String, ); TODO: Might have to add string for certain mail applications (for extra mail recipients)
                    mailIntent.putExtra(Intent.EXTRA_SUBJECT, "RUS-1 Bar Bill:");
                    mailIntent.putExtra(Intent.EXTRA_TEXT, "Sent from Android");
                    mailIntent.setType("text/plain");

                    Intent attachIntent = new Intent();
                    attachIntent.setAction(Intent.ACTION_SEND);
                    attachIntent.putExtra(Intent.EXTRA_STREAM, uriToBillCSV);
                    attachIntent.setType("csv");
                    startActivity(Intent.createChooser(mailIntent, getResources().getText(R.string.send_to))); // TODO: send_to midlertidig mail - nemt at hardcode extra mailrecipients. måske også hente fra database?

                    Toast.makeText(getActivity(),R.string.youregood,Toast.LENGTH_LONG).show(); */

                }
            });
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
