package com.example.rus1_bar.Fragments.Administrator.Tutor;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.rus1_bar.Activities.MainActivity;
import com.example.rus1_bar.Models.Tutor;
import com.example.rus1_bar.R;
import com.example.rus1_bar.Repository.FirebaseRepository;
import com.example.rus1_bar.Service.ShoppingService;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.UUID;

import static android.app.Activity.RESULT_OK;
import static android.widget.Toast.makeText;
import static com.facebook.FacebookSdk.getApplicationContext;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddTutorFragment extends Fragment {

    private static final String SERVICE_CONNECTED_MAIN_ACTIVITY = "Service connected to the main Activity" ;

    Button cancelBtn;
    Button addBtn;
    EditText editNick;
    EditText editName;
    EditText editEmail;
    EditText editPhone;

    FirebaseRepository firebaseRepo;
    Tutor newTutor;

    private static final int PICK_IMAGE = 100;
    ImageView tutorImage;
    Uri imageUri;
    CropImage.ActivityResult cropResult;

    String guid;

    private ShoppingService shoppingService;

    private View rootView;

    public AddTutorFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_add_tutor, container, false);

        editNick = rootView.findViewById(R.id.addTutorEditNick);
        editName = rootView.findViewById(R.id.addTutorEditName);
        editEmail = rootView.findViewById(R.id.addTutorEditEmail);
        editPhone = rootView.findViewById(R.id.addTutorEditPhone);

        cancelBtn = rootView.findViewById(R.id.addTutorCancelBtn);
        addBtn = rootView.findViewById(R.id.addTutorAddBtn);

        guid  = UUID.randomUUID().toString();
        newTutor = new Tutor();

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                Navigation.findNavController(v).navigate(R.id.action_addTutorFragment_to_tutorSettingsFragment);
            }
        });

        //Source: https://www.youtube.com/watch?v=OPnusBmMQTw
        tutorImage = rootView.findViewById(R.id.addTutorImage);
        tutorImage.setImageResource(R.drawable.defaultimg);
        tutorImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });

        LocalBroadcastManager.getInstance(this.getActivity()).registerReceiver(ServiceConnected, new IntentFilter(SERVICE_CONNECTED_MAIN_ACTIVITY));

        if (((MainActivity)getActivity()).getShoppingService_fromMainActivity() != null)
        {
            initAddTutorFragment();
        }

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();



    }

    private void initAddTutorFragment()
    {
        if (getActivity()!=null)
        {
            shoppingService = ((MainActivity)getActivity()).getShoppingService_fromMainActivity();

            firebaseRepo = shoppingService.getFirebaseRepository_fromService();//new FirebaseRepository();

            addBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Error handling for empty fields.
                    if (editName.toString().equals("") || editNick.toString().equals("") || editPhone.toString().equals("") || editEmail.toString().equals("")){
                        makeText(getApplicationContext(), "All fields must be filled out before proceeding.", Toast.LENGTH_LONG).show();
                    }
                    else {
                        newTutor = new Tutor(editName.getText().toString(), editNick.getText().toString(), Integer.parseInt(editPhone.getText().toString()), editEmail.getText().toString());
                        newTutor.setImagename(guid);
                        if (imageUri != null){
                            firebaseRepo.saveTutorImage(newTutor, cropResult.getUri());
                        }
                        firebaseRepo.insertTutor(newTutor);
                        Navigation.findNavController(view).navigate(R.id.action_addTutorFragment_to_tutorSettingsFragment);
                    }
                }
            });
        }
    }


    private void openGallery(){
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        gallery.setType("image/*");
        startActivityForResult(gallery, PICK_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE && data != null) {
            imageUri = data.getData();
            //Source: https://www.youtube.com/watch?v=buwyfcN1pLk
            CropImage.activity(imageUri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setMaxCropResultSize(1920, 1080)
                    .setAspectRatio(1, 1)
                    .start(getContext(), this);
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            cropResult = CropImage.getActivityResult(data);
            if (cropResult != null) {
                tutorImage.setImageURI(cropResult.getUri());
            } else {
                Toast.makeText(getActivity(), R.string.cropCancel, Toast.LENGTH_LONG).show();
            }
        }
    }

    private BroadcastReceiver ServiceConnected = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            initAddTutorFragment();
        }
    };
}
