package com.example.rus1_bar.Fragments.Administrator;


import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.rus1_bar.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class EditTutorFragment extends Fragment {


    Button cancelBtn;
    Button deleteBtn;
    Button editBtn;
    /*
    private static final int PICK_IMAGE = 100;
    ImageView tutorImage;
    Uri imageUri;*/


    EditText editNick;
    EditText editName;
    EditText editEmail;
    EditText editPhone;

    public EditTutorFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_edit_tutor, container, false);

        editName = rootView.findViewById(R.id.editTutorEditName);
        editNick = rootView.findViewById(R.id.editTutorEditNick);
        editEmail = rootView.findViewById(R.id.editTutorEditEmail);
        editPhone = rootView.findViewById(R.id.editTutorEditPhone);

        cancelBtn = rootView.findViewById(R.id.editTutorCancelBtn);
        deleteBtn = rootView.findViewById(R.id.editTutorDeleteBtn);
        editBtn = rootView.findViewById(R.id.editTutorEditBtn);


        View.OnClickListener editTutorCancelClick = Navigation.createNavigateOnClickListener(R.id.action_editTutorFragment_to_tutorSettingsFragment);
        cancelBtn.setOnClickListener(editTutorCancelClick);

        return rootView;
    }

}
