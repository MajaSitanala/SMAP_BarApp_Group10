package com.example.rus1_bar.Fragments.Bartender;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.rus1_bar.Adapters.TutorRecyclerAdapter;
import com.example.rus1_bar.Models.Product;
import com.example.rus1_bar.Models.Tutor;
import com.example.rus1_bar.R;
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

    private List<Tutor> testTutorList = new ArrayList<>();
    RecyclerView tutorRecyclerView;
    RecyclerView.Adapter tutorRecyclerAdapter;
    RecyclerView.LayoutManager tutorLayoutManager;
    private DatabaseReference databaseTutor;
    private FirebaseDatabase FireDB;


    public ViewTutorsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // rootView is needed when using fingViewById because otherwise the views have not been created by the time the views are called.
        View rootView = inflater.inflate(R.layout.fragment_view_tutors, container, false);                                              //https://stackoverflow.com/questions/26621060/display-a-recyclerview-in-fragment


        //Test data for the card view
        //fillTestTutorList();

        // Recycler View setup
        tutorRecyclerView = rootView.findViewById(R.id.tutorRecyclerView);

        //Creates the grid layout
        tutorLayoutManager = new GridLayoutManager(getActivity(), 3);                                                                //https://youtu.be/SD2t75T5RdY?t=1302
        tutorRecyclerView.setLayoutManager(tutorLayoutManager);

        //Init Database ref
        FireDB = FirebaseDatabase.getInstance();
        databaseTutor = FireDB.getReference("tutors");
        databaseTutor.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //testTutorList.clear();
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


        // Inflate the layout for this fragment
        return rootView; // inflater.inflate(R.layout.fragment_view_tutors, container, false);
    }

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
