package com.example.rus1_bar.Fragments.Bartender;


import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.rus1_bar.Activities.MainActivity;
import com.example.rus1_bar.Activities.ShoppingActivity;
import com.example.rus1_bar.Adapters.TutorRecyclerAdapter;
import com.example.rus1_bar.Models.Product;
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
public class ViewTutorsFragment extends Fragment {

    private static final String SERVICE_CONNECTED_MAIN_ACTIVITY = "Service connected to the main Activity" ;

    private List<Tutor> testTutorList = new ArrayList<>();

    // Recycler view
    RecyclerView tutorRecyclerView;
    RecyclerView.Adapter tutorRecyclerAdapter;
    RecyclerView.LayoutManager tutorLayoutManager;

    // Database
    private DatabaseReference databaseTutor;
    private FirebaseDatabase FireDB;


    // Service reference
    private ShoppingService shoppingService;

    public ViewTutorsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // rootView is needed when using fingViewById because otherwise the views have not been created by the time the views are called.
        View rootView = inflater.inflate(R.layout.fragment_view_tutors, container, false);                                              //https://stackoverflow.com/questions/26621060/display-a-recyclerview-in-fragment
        // Recycler View setup
        tutorRecyclerView = rootView.findViewById(R.id.tutorRecyclerView);

        //Creates the grid layout
        tutorLayoutManager = new GridLayoutManager(getActivity(), 3);                                                                //https://youtu.be/SD2t75T5RdY?t=1302
        tutorRecyclerView.setLayoutManager(tutorLayoutManager);

        // Inflate the layout for this fragment
        return rootView; // inflater.inflate(R.layout.fragment_view_tutors, container, false);
    }

    @Override
    public void onStart()
    {
        super.onStart();
        LocalBroadcastManager.getInstance(this.getActivity()).registerReceiver(ServiceConnected, new IntentFilter(SERVICE_CONNECTED_MAIN_ACTIVITY));
    }

    private void initViewTutorsFragment()
    {
        if((MainActivity)getActivity() != null)
        {
            // Service reference
            shoppingService = ((MainActivity)getActivity()).getShoppingService_fromMainActivity();

            //Init Database ref
            FireDB = shoppingService.getFirebaseDatabase_fromService();//FirebaseDatabase.getInstance();//shoppingService.getFirebaseDatabase_fromService();  //TODO: maybe get this from service?

            databaseTutor = FireDB.getReference("tutors");
            databaseTutor.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    testTutorList.clear();
                    for (DataSnapshot tutorSnapshot : dataSnapshot.getChildren()){
                        Tutor tutor = tutorSnapshot.getValue(Tutor.class);
                        testTutorList.add(tutor);
                        tutorRecyclerAdapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            //Recycler adapter setup
            tutorRecyclerAdapter = new TutorRecyclerAdapter(getActivity(), testTutorList);

            tutorRecyclerView.setAdapter(tutorRecyclerAdapter);
        }

    }

    private BroadcastReceiver ServiceConnected = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            initViewTutorsFragment();
        }
    };

    private void fillTestTutorList()
    {
        //Test data for the card view
        testTutorList.add(new Tutor("Christoffer Broberg", "Praktikant", 12345678, "123@enmail.com", R.drawable.praktikant));
        testTutorList.add(new Tutor("Andreas Blaabjerg", "Fl00b3r", 12345678, "123@enmail.com", R.drawable.flobber));
        testTutorList.add(new Tutor("Mikkel Bleeg", "Pampers", 12345678, "123@enmail.com", R.drawable.bleeg));
        testTutorList.add(new Tutor("Maja Andersen", "Crystal", 12345678, "123@enmail.com", R.drawable.crystal));
        testTutorList.add(new Tutor("Christoffer Broberg", "Praktikant", 12345678, "123@enmail.com", R.drawable.praktikant));
        testTutorList.add(new Tutor("Andreas Blaabjerg", "Fl00b3r", 12345678, "123@enmail.com", R.drawable.flobber));
        testTutorList.add(new Tutor("Mikkel Bleeg", "Pampers", 12345678, "123@enmail.com", R.drawable.bleeg));
        testTutorList.add(new Tutor("Maja Andersen", "Crystal", 12345678, "123@enmail.com", R.drawable.crystal));
        testTutorList.add(new Tutor("Christoffer Broberg", "Praktikant", 12345678, "123@enmail.com", R.drawable.praktikant));
        testTutorList.add(new Tutor("Andreas Blaabjerg", "Fl00b3r", 12345678, "123@enmail.com", R.drawable.flobber));
        testTutorList.add(new Tutor("Mikkel Bleeg", "Pampers", 12345678, "123@enmail.com", R.drawable.bleeg));
        testTutorList.add(new Tutor("Maja Andersen", "Crystal", 12345678, "123@enmail.com", R.drawable.crystal));
        testTutorList.add(new Tutor("Christoffer Broberg", "Praktikant", 12345678, "123@enmail.com", R.drawable.praktikant));
        testTutorList.add(new Tutor("Andreas Blaabjerg", "Fl00b3r", 12345678, "123@enmail.com", R.drawable.flobber));
        testTutorList.add(new Tutor("Mikkel Bleeg", "Pambers", 12345678, "123@enmail.com", R.drawable.bleeg));
        testTutorList.add(new Tutor("Maja Andersen", "Crystal", 12345678, "123@enmail.com", R.drawable.crystal));
        testTutorList.add(new Tutor("Christoffer Broberg", "Praktikant", 12345678, "123@enmail.com", R.drawable.praktikant));
        testTutorList.add(new Tutor("Andreas Blaabjerg", "Fl00b3r", 12345678, "123@enmail.com", R.drawable.flobber));
        testTutorList.add(new Tutor("Mikkel Bleeg", "Pampers", 12345678, "123@enmail.com", R.drawable.bleeg));
        testTutorList.add(new Tutor("Maja Andersen", "Crystal", 12345678, "123@enmail.com", R.drawable.crystal));
    }

}
