package com.example.rus1_bar.Fragments.Administrator;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.rus1_bar.R;

import java.net.URI;

import static android.app.Activity.RESULT_OK;
import static com.facebook.FacebookSdk.getApplicationContext;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddTutorFragment extends Fragment {

    Button cancelBtn;
    Button addBtn;


    private static final int PICK_IMAGE = 100;
    ImageView tutorImage;
    Uri imageUri;

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
        cancelBtn = rootView.findViewById(R.id.addTutorCancelBtn);


        View.OnClickListener addTutorCancelClick = Navigation.createNavigateOnClickListener(R.id.action_addTutorFragment_to_tutorSettingsFragment);
        cancelBtn.setOnClickListener(addTutorCancelClick);

        //Source: https://www.youtube.com/watch?v=OPnusBmMQTw
        tutorImage = rootView.findViewById(R.id.addTutorImage);
        tutorImage.setImageResource(R.drawable.defaultimg);
        tutorImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });

        addBtn = rootView.findViewById(R.id.addTutorAddBtn);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        return rootView;
    }

    private void openGallery(){
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && requestCode == PICK_IMAGE){
            imageUri = data.getData();
            tutorImage.setImageURI(imageUri);
        }
    }
}
