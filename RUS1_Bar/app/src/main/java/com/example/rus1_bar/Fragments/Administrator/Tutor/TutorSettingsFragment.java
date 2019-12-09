package com.example.rus1_bar.Fragments.Administrator.Tutor;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rus1_bar.Activities.MainActivity;
import com.example.rus1_bar.Adapters.TutorDisplayAdapter;
import com.example.rus1_bar.Models.Tutor;
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
public class TutorSettingsFragment extends Fragment {

    private static final String SERVICE_CONNECTED_MAIN_ACTIVITY = "Service connected to the main Activity" ;

    private List<Tutor> mTutorList = new ArrayList<>();

    Button addTutorBtn;
    Button cancelBtn;

    RecyclerView tutorRecyclerView;
    RecyclerView.Adapter tutorRecyclerAdapter;
    RecyclerView.LayoutManager tutorLayoutManager;

    // Database
    private DatabaseReference databaseTutorDisplay;
    private FirebaseDatabase FireDB;

    // Service reference
    ShoppingService shoppingService;

    private View rootView;


    public TutorSettingsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_tutor_settings, container, false);

            // Inflate the layout for this fragment
        return rootView; // inflater.inflate(R.layout.fragment_view_tutors, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(this.getActivity()).registerReceiver(ServiceConnected, new IntentFilter(SERVICE_CONNECTED_MAIN_ACTIVITY));

        if (((MainActivity)getActivity()).getShoppingService_fromMainActivity() != null)
        {
            initTutorSettingsFragment();
        }
    }

    private void initTutorSettingsFragment()
    {
        if (getActivity()!=null)
        {
            shoppingService = ((MainActivity)getActivity()).getShoppingService_fromMainActivity();

            addTutorBtn = rootView.findViewById(R.id.tutorAddBtn);
            cancelBtn = rootView.findViewById(R.id.tutorListCancel);

            View.OnClickListener addTutorClick = Navigation.createNavigateOnClickListener(R.id.action_tutorSettingsFragment_to_addTutorFragment);
            addTutorBtn.setOnClickListener(addTutorClick);

            View.OnClickListener cancelBtnClick = Navigation.createNavigateOnClickListener(R.id.action_tutorSettingsFragment_to_settingsOverviewFragment);
            cancelBtn.setOnClickListener(cancelBtnClick);

            // Recycler View setup
            tutorRecyclerView = rootView.findViewById(R.id.tutorSettingsRecycleView);

            //Creates the grid layout
            tutorLayoutManager = new GridLayoutManager(getActivity(), 1);                                                                //https://youtu.be/SD2t75T5RdY?t=1302
            tutorRecyclerView.setLayoutManager(tutorLayoutManager);

            //Recycler adapter setup
            tutorRecyclerAdapter = new TutorDisplayAdapter(getActivity(), mTutorList, shoppingService);

            tutorRecyclerView.setAdapter(tutorRecyclerAdapter);

            //Get categories from db
            FireDB = shoppingService.getFirebaseDatabase_fromService();                                 //FirebaseDatabase.getInstance();
            databaseTutorDisplay = FireDB.getReference("tutors");
            databaseTutorDisplay.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    mTutorList.clear();
                    for (DataSnapshot tutorSnapshot : dataSnapshot.getChildren()){
                        Tutor t = tutorSnapshot.getValue(Tutor.class);
                        mTutorList.add(t);
                        tutorRecyclerAdapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

    }

    private BroadcastReceiver ServiceConnected = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            initTutorSettingsFragment();
        }
    };

}
