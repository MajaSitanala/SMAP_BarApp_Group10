package com.example.rus1_bar.Fragments.Administrator;


import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rus1_bar.Activities.MainActivity;
import com.example.rus1_bar.Adapters.RusturRecycleAdapter;
import com.example.rus1_bar.Models.Rustur;
import com.example.rus1_bar.R;
import com.example.rus1_bar.Service.ShoppingService;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
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

    List<Rustur> rusturList = new ArrayList<>();

    RecyclerView rusturRecyclerView;
    RecyclerView.Adapter rusturRecyclerAdapter;
    RecyclerView.LayoutManager rusturLayoutManager;

    // Database
    private DatabaseReference databaseRusturDisplay;
    private FirebaseDatabase FireDB;

    private ShoppingService shoppingService;


    public RusturSettingsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_rustur_settings, container, false);



        // Inflate the layout for this fragment
        return rootView;
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
            //Init Service
            shoppingService = ((MainActivity)getActivity()).getShoppingService_fromMainActivity();

            //Go back to settings overview
            cancelBtn = rootView.findViewById(R.id.rusturSettingsCancelBtn);
            cancelBtn.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_rusturSettingsFragment_to_settingsOverviewFragment));

            //show toast, deleting collection is not available from client device
            removeBtn = rootView.findViewById(R.id.rusturSettingsRemoveRusturBtn);
            removeBtn.setOnClickListener(v -> {
                Toast.makeText(this.getActivity(),R.string.rusturDeleteMsg, Toast.LENGTH_LONG).show();

            });

            //set Rycycler view
            rusturRecyclerView = rootView.findViewById(R.id.rusturList);
            rusturLayoutManager = new GridLayoutManager(getActivity(), 1);
            rusturRecyclerView.setLayoutManager(rusturLayoutManager);
            FireDB = shoppingService.getFirebaseDatabase_fromService();
            databaseRusturDisplay = FireDB.getReference("rustur");
            databaseRusturDisplay.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                {       rusturList.clear();
                    for (DataSnapshot RusturSnapshot : dataSnapshot.getChildren()){
                        Rustur rus = RusturSnapshot.getValue(Rustur.class);
                        rusturList.add(rus);
                        rusturRecyclerAdapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


            //Set Rustur as currentRustur
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
                        //Validate has to be 1 word and no '.', '#', '$', '[', or ']'
                        if(validateinput(input.getText().toString())){
                        //Add new Rustur here from firebaseRepofunc
                        Rustur rustur = new Rustur("");
                        rustur.setRusturName(input.getText().toString());
                        shoppingService.insertRustur(rustur);
                        rusturRecyclerAdapter.notifyDataSetChanged();
                        }else{Toast.makeText(getActivity(),R.string.validate, Toast.LENGTH_LONG).show();}
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

            rusturRecyclerAdapter = new RusturRecycleAdapter(getActivity(), rusturList, shoppingService);
            rusturRecyclerView.setAdapter(rusturRecyclerAdapter);
        }
    }

    private boolean validateinput(String string){
        //'.', '#', '$', '[', or ']'
        for (int i = 0; i < string.length(); i++){
            char c = string.charAt(i);
            switch (c){
                case '.':
                    return false;
                case '#':
                    return false;
                case '$':
                    return false;
                case '[':
                    return false;
                case ']':
                    return false;
                case ' ':
                    return false;
                case '@':
                    return false;
                case '{':
                    return false;
                case '}':
                    return false;
            }
        }
        return true;
    }

    private BroadcastReceiver ServiceConnected = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            initRusturSettingsFragment();
        }
    };

}
