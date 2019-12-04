package com.example.rus1_bar.Fragments.Administrator;


import android.app.AlertDialog;
import android.content.DialogInterface;
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

import com.example.rus1_bar.Models.Tutor;
import com.example.rus1_bar.R;
import com.example.rus1_bar.Repository.FirebaseRepository;


/**
 * A simple {@link Fragment} subclass.
 */
public class EditTutorFragment extends Fragment {


    Button cancelBtn;
    Button deleteBtn;
    Button editBtn;

    private static final int PICK_IMAGE = 100;
    ImageView tutorImage;
    Uri imageUri;

    FirebaseRepository firebaseRepo;
    Tutor currentTutor;

    EditText editNick;
    EditText editName;
    EditText editEmail;
    EditText editPhone;

    public EditTutorFragment() {
        // Required empty public constructor
    }

     //TODO: Need to make it possible to access a specific tutor

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

        firebaseRepo = new FirebaseRepository();
        currentTutor = new Tutor();

        View.OnClickListener editTutorCancelClick = Navigation.createNavigateOnClickListener(R.id.action_editTutorFragment_to_tutorSettingsFragment);
        cancelBtn.setOnClickListener(editTutorCancelClick);

        deleteBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                AskOption();
            }
        });

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        return rootView;
    }

    //Source https://stackoverflow.com/questions/11740311/android-confirmation-message-for-delete
    private AlertDialog AskOption()
    {
        AlertDialog deleteDialogBox = new AlertDialog.Builder(getContext()) //Might have to be (this) instead of getContext
                .setTitle("Delete")
                .setMessage("Do you want to delete this Tutor?")
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        firebaseRepo.deleteTutor(currentTutor);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();
        return deleteDialogBox;
    }
}
