package com.example.rus1_bar.Fragments.Administrator.Tutor;


import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.rus1_bar.Activities.MainActivity;
import com.example.rus1_bar.Models.Tutor;
import com.example.rus1_bar.R;
import com.example.rus1_bar.Repository.FirebaseRepository;
import com.example.rus1_bar.Service.ShoppingService;
import com.google.android.gms.tasks.OnSuccessListener;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.UUID;

import static android.app.Activity.RESULT_OK;
import static android.widget.Toast.makeText;
import static com.facebook.FacebookSdk.getApplicationContext;


/**
 * A simple {@link Fragment} subclass.
 */
public class EditTutorFragment extends Fragment {

    private static final String SERVICE_CONNECTED_MAIN_ACTIVITY = "Service connected to the main Activity";


    Button cancelBtn;
    Button deleteBtn;
    Button editBtn;

    private static final int PICK_IMAGE = 100;
    ImageView tutorImage;
    Uri imageUri;
    CropImage.ActivityResult cropResult;
    String guid;

    FirebaseRepository firebaseRepo;
    Tutor currentTutor;

    EditText editNick;
    EditText editName;
    EditText editEmail;
    EditText editPhone;

    private ShoppingService shoppingService;

    AlertDialog diaBox;
    private View rootView;
    private boolean newImageSat;

    public EditTutorFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_edit_tutor, container, false);

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();

        LocalBroadcastManager.getInstance(this.getActivity()).registerReceiver(ServiceConnected, new IntentFilter(SERVICE_CONNECTED_MAIN_ACTIVITY));

        if (((MainActivity) getActivity()).getShoppingService_fromMainActivity() != null) {
            initEditTutorFragment();
        }

    }

    private void initEditTutorFragment()
    {
        if (getActivity() != null)
        {
            // Service
            shoppingService = ((MainActivity) getActivity()).getShoppingService_fromMainActivity();

            guid = UUID.randomUUID().toString();
            firebaseRepo = shoppingService.getFirebaseRepository_fromService();
            currentTutor = (Tutor) getArguments().getSerializable("Tutor");

            editName = rootView.findViewById(R.id.editTutorEditName);
            editName.setText(currentTutor.getTutorName());

            editNick = rootView.findViewById(R.id.editTutorEditNick);
            editNick.setText(currentTutor.getNickname());

            editEmail = rootView.findViewById(R.id.editTutorEditEmail);
            editEmail.setText(currentTutor.getMail());

            editPhone = rootView.findViewById(R.id.editTutorEditPhone);
            editPhone.setText(Integer.toString(currentTutor.getPhoneNr()));

            tutorImage = rootView.findViewById(R.id.editTutorImage);
            if ((currentTutor.getImagename() != null) && (newImageSat==false)) {
                firebaseRepo.getTutorImage(currentTutor.getImagename()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {

                        Picasso.with(rootView.getContext()).load(uri).resize(600, 600).centerInside().into(tutorImage);
                    }
                });
            }

            tutorImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view)
                {
                    newImageSat = true;
                    openGallery();
                }
            });

            cancelBtn = rootView.findViewById(R.id.editTutorCancelBtn);
            deleteBtn = rootView.findViewById(R.id.editTutorDeleteBtn);
            editBtn = rootView.findViewById(R.id.editTutorEditBtn);

            NavController navController = Navigation.findNavController((this.getActivity()), R.id.nav_host_fragment);
            cancelBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    newImageSat = false;
                    navController.navigate(R.id.action_editTutorFragment_to_tutorSettingsFragment);
                }
            });


            deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    diaBox = AskOption();
                    diaBox.show();

                }
            });

            editBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Error handling for empty fields.
                    if (editName.getText().toString().equals("") || editNick.getText().toString().equals("") || editPhone.getText().toString().equals("") || editEmail.getText().toString().equals("")){
                        Toast.makeText(getContext(), "All fields must be filled out before proceeding.", Toast.LENGTH_LONG).show();
                    }
                    else {

                        Tutor oldTutor = currentTutor;

                        currentTutor = new Tutor(editName.getText().toString(), editNick.getText().toString(), Integer.parseInt(editPhone.getText().toString()), editEmail.getText().toString());

                        if (newImageSat == false)
                        {
                            currentTutor = oldTutor;
                            currentTutor.setTutorName(editName.getText().toString());
                            currentTutor.setNickname(editNick.getText().toString());
                            currentTutor.setPhoneNr(Integer.parseInt(editPhone.getText().toString()));
                            currentTutor.setMail(editEmail.getText().toString());
                        }
                        else
                        {
                            currentTutor.setImagename(guid);
                            if (imageUri != null) {
                                firebaseRepo.saveTutorImage(currentTutor, cropResult.getUri());
                            }
                        }

                        newImageSat = false;
                        firebaseRepo.insertTutor(currentTutor);
                        Navigation.findNavController(view).navigate(R.id.tutorSettingsFragment);
                    }
                }
            });
        }

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
                        newImageSat = false;
                        firebaseRepo.deleteTutor(currentTutor);
                        Navigation.findNavController(getView()).navigate(R.id.action_editTutorFragment_to_tutorSettingsFragment);
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

    private void openGallery(){
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        gallery.setType("image/*");
        startActivityForResult(gallery, PICK_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && requestCode == PICK_IMAGE && data!=null){
            imageUri = data.getData();
            //Source: https://www.youtube.com/watch?v=buwyfcN1pLk
            CropImage.activity(imageUri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setMaxCropResultSize(1920, 1080)
                    .setAspectRatio(1,1)
                    .start(getContext(), this);
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            cropResult = CropImage.getActivityResult(data);
            if(cropResult!=null){
                tutorImage.setImageURI(cropResult.getUri());
            }else {Toast.makeText(getActivity(),R.string.cropCancel, Toast.LENGTH_LONG).show();}
        }
    }

    private BroadcastReceiver ServiceConnected = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            initEditTutorFragment();
        }
    };
}
