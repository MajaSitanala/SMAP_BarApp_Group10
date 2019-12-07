package com.example.rus1_bar.Fragments.Administrator;


import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.navigation.Navigation;

import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.rus1_bar.Activities.MainActivity;
import com.example.rus1_bar.Models.Rustur;
import com.example.rus1_bar.R;
import com.example.rus1_bar.Service.ShoppingService;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class RusturSettingsFragment extends Fragment
{

    private static final String SERVICE_CONNECTED_MAIN_ACTIVITY = "Service connected to the main Activity" ;
    private View rootView;

    Button cancelBtn;
    Button removeBtn;
    Button setBtn;
    Button addBtn;

    List<Rustur> rusturList;

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
            //Init Service
            shoppingService = ((MainActivity)getActivity()).getShoppingService_fromMainActivity();

            //Go back to settings overview
            cancelBtn = rootView.findViewById(R.id.rusturSettingsCancelBtn);
            rootView.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_rusturSettingsFragment_to_settingsOverviewFragment));

            //show toast, deleting collection is not available from client device TODO TOAST
            removeBtn = rootView.findViewById(R.id.rusturSettingsRemoveRusturBtn);
            removeBtn.setOnClickListener(v -> Toast.makeText(this.getActivity(),R.string.rusturDeleteMsg, Toast.LENGTH_LONG).show());


            //Set Rustur as currentRustur TODO: Toast og highlight den aktive rsutur
            setBtn = rootView.findViewById(R.id.rusturSettingsSetCurrentBtn);
            setBtn.setOnClickListener(v->Toast.makeText(this.getActivity(),R.string.rusturSetMsg,Toast.LENGTH_LONG).show());


            //Start AlertDialog to enter new RusturName
            addBtn = rootView.findViewById(R.id.rusturSettingsAddRusturBtn);
            addBtn.setOnClickListener(v -> {
                AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());
                builder.setTitle(R.string.rusturTitle);
                EditText input = new EditText(getContext());
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);

                builder.setPositiveButton(R.string.Ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Add new Rustur here from firebaseRepofunc

                    }
                });
                builder.setNegativeButton(R.string.Cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
            });

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
