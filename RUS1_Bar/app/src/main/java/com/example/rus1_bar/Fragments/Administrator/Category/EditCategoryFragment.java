package com.example.rus1_bar.Fragments.Administrator.Category;


import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.navigation.Navigation;

import com.example.rus1_bar.Activities.MainActivity;
import com.example.rus1_bar.Models.Category;
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
public class EditCategoryFragment extends Fragment {

    private static final String SERVICE_CONNECTED_MAIN_ACTIVITY = "Service connected to the main Activity" ;

    private Button cancelBtn;
    private Button deleteBtn;
    private Button editBtn;

    private static final int PICK_IMAGE = 101;
    private ImageView categoryImage;
    private Uri imageUri;
    private CropImage.ActivityResult cropResult;
    private String guid;

    private FirebaseRepository firebaseRepo;
    private Category currentCategory;

    private EditText editName;

    private AlertDialog diaBox;

    ShoppingService shoppingService;

    private View rootView;

    public EditCategoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_edit_category, container, false);

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();

        LocalBroadcastManager.getInstance(this.getActivity()).registerReceiver(ServiceConnected, new IntentFilter(SERVICE_CONNECTED_MAIN_ACTIVITY));

        if (((MainActivity)getActivity()).getShoppingService_fromMainActivity() != null)
        {
            initEditCategoryFragment();
        }
    }

    private void initEditCategoryFragment()
    {
        if (getActivity()!=null)
        {
            shoppingService = ((MainActivity)getActivity()).getShoppingService_fromMainActivity();
            guid  = UUID.randomUUID().toString();
            firebaseRepo = shoppingService.getFirebaseRepository_fromService();
            currentCategory = (Category) getArguments().getSerializable("category");

            editName = rootView.findViewById(R.id.editCategoryEditName);
            editName.setText(currentCategory.getCategoryName());

            //TODO: Need to implement getImageName in Category
            categoryImage = rootView.findViewById(R.id.editCategoryImage);
            if(currentCategory.getImageName() != null){
                firebaseRepo.getCategoryImage(currentCategory.getImageName()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.with(rootView.getContext()).load(uri).resize(600,600).centerInside().into(categoryImage);
                    }
                });
            }
            categoryImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    openGallery();
                }
            });

            cancelBtn = rootView.findViewById(R.id.editCategoryCancelBtn);
            deleteBtn = rootView.findViewById(R.id.editCategoryDeleteBtn);
            editBtn = rootView.findViewById(R.id.editCategoryEditBtn);



            View.OnClickListener editCategoryCancelClick = Navigation.createNavigateOnClickListener(R.id.action_editCategoryFragment_to_categorySettingsFragment);
            cancelBtn.setOnClickListener(editCategoryCancelClick);



            deleteBtn.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view){
                    diaBox = AskOption();
                    diaBox.show();

                }
            });

            editBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Error handling for empty fields.
                    if (editName.toString().equals("")){
                        makeText(getApplicationContext(), "All fields must be filled out before proceeding.", Toast.LENGTH_LONG).show();
                    }
                    else {
                        currentCategory = new Category(editName.getText().toString());
                        currentCategory.setImageName(guid);
                        if (imageUri != null){
                            firebaseRepo.saveCategoryImage(currentCategory, cropResult.getUri());
                        }
                        firebaseRepo.insertCategory(currentCategory);
                        Navigation.findNavController(view).navigate(R.id.action_editCategoryFragment_to_categorySettingsFragment);
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
                .setMessage("Do you want to delete this Category?")
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        firebaseRepo.deleteCategory(currentCategory);
                        Navigation.findNavController(getView()).navigate(R.id.action_editCategoryFragment_to_categorySettingsFragment);
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
            categoryImage.setImageURI(cropResult.getUri());
        }
    }

    private BroadcastReceiver ServiceConnected = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            initEditCategoryFragment();
        }
    };


}
