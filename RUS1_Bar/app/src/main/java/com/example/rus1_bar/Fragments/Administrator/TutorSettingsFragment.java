package com.example.rus1_bar.Fragments.Administrator;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.rus1_bar.Activities.MainActivity;
import com.example.rus1_bar.Adapters.ProductRecyclerAdapter;
import com.example.rus1_bar.Adapters.TutorDisplayAdapter;
import com.example.rus1_bar.Adapters.TutorRecyclerAdapter;
import com.example.rus1_bar.Models.Category;
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
public class TutorSettingsFragment extends Fragment {


    private List<Tutor> mTutorList = new ArrayList<>();

    Button addTutorBtn;
    Button editTutorBtn;
    Button cancelBtn;

    RecyclerView tutorRecyclerView;
    RecyclerView.Adapter tutorRecyclerAdapter;
    RecyclerView.LayoutManager tutorLayoutManager;

    // Database
    private DatabaseReference databaseTutorDisplay;
    private FirebaseDatabase FireDB;

    // Service reference
    ShoppingService shoppingService;


    public TutorSettingsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_tutor_settings, container, false);

        addTutorBtn = rootView.findViewById(R.id.tutorAddBtn);
        editTutorBtn = rootView.findViewById(R.id.tutorEditBtn);
        cancelBtn = rootView.findViewById(R.id.tutorListCancel);

        View.OnClickListener addTutorClick = Navigation.createNavigateOnClickListener(R.id.action_tutorSettingsFragment_to_addTutorFragment);
        addTutorBtn.setOnClickListener(addTutorClick);

        View.OnClickListener editTutorClick = Navigation.createNavigateOnClickListener(R.id.action_tutorSettingsFragment_to_editTutorFragment);
        editTutorBtn.setOnClickListener(editTutorClick);

        View.OnClickListener cancelBtnClick = Navigation.createNavigateOnClickListener(R.id.action_tutorSettingsFragment_to_settingsOverviewFragment);
        cancelBtn.setOnClickListener(cancelBtnClick);

        //Test data for the card view
        //fillTestTutorList();

        // Recycler View setup
        tutorRecyclerView = rootView.findViewById(R.id.tutorSettingsRecycleView);

        //Creates the grid layout
        tutorLayoutManager = new GridLayoutManager(getActivity(), 1);                                                                //https://youtu.be/SD2t75T5RdY?t=1302
        tutorRecyclerView.setLayoutManager(tutorLayoutManager);

        //Recycler adapter setup
        tutorRecyclerAdapter = new TutorDisplayAdapter(getActivity(), mTutorList);

        tutorRecyclerView.setAdapter(tutorRecyclerAdapter);

        //Get categories from db
        shoppingService = ((MainActivity)getActivity()).getShoppingService_fromMainActivity();
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



            // Inflate the layout for this fragment
        return rootView; // inflater.inflate(R.layout.fragment_view_tutors, container, false);
    }

    private void fillTestTutorList()
    {
        for (int i = 0; i<4; i++)
        {
            //Test data for the card view
            mTutorList.add(new Tutor("Christoffer Broberg", "Praktikant", 12345678, "123@enmail.com", R.drawable.praktikant));
            mTutorList.add(new Tutor("Andreas Blaabjerg", "Fl00b3r", 12345678, "123@enmail.com", R.drawable.flobber));
            mTutorList.add(new Tutor("Mikkel Bleeg", "Pampers", 12345678, "123@enmail.com", R.drawable.bleeg));
            mTutorList.add(new Tutor("Maja Andersen", "Crystal", 12345678, "123@enmail.com", R.drawable.crystal));
        }
    }
}
