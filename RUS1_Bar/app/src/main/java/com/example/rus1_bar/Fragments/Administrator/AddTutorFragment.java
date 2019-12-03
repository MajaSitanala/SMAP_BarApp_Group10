package com.example.rus1_bar.Fragments.Administrator;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.rus1_bar.R;

import static com.facebook.FacebookSdk.getApplicationContext;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddTutorFragment extends Fragment {

    Button cancelBtn;
    Button addBtn;

    EditText editNick;
    EditText editName;
    EditText editEmail;
    EditText editPhone;


    public AddTutorFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_add_tutor, container, false);

        editNick = rootView.findViewById(R.id.addTutorEditNick);
        editName = rootView.findViewById(R.id.addTutorEditName);
        editEmail = rootView.findViewById(R.id.addTutorEditEmail);
        editPhone = rootView.findViewById(R.id.addTutorEditPhone);




        return rootView;
    }

}
