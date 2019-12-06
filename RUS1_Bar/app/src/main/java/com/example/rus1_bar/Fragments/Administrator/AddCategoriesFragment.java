package com.example.rus1_bar.Fragments.Administrator;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.rus1_bar.Models.Category;
import com.example.rus1_bar.Models.Tutor;
import com.example.rus1_bar.R;
import com.example.rus1_bar.Repository.FirebaseRepository;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.UUID;

import static android.app.Activity.RESULT_OK;
import static android.widget.Toast.makeText;
import static com.facebook.FacebookSdk.getApplicationContext;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddCategoriesFragment extends Fragment {


    private Button cancelBtn;
    private Button addBtn;
    private EditText editName;

    private FirebaseRepository firebaseRepo;
    private Category newCategory;

    private static final int PICK_IMAGE = 101;
    private ImageView categoryImage;
    private Uri imageUri;
    private CropImage.ActivityResult cropResult;

    private String guid;

    public AddCategoriesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_add_categories, container, false);

        editName = rootView.findViewById(R.id.addCategoryEditName);

        addBtn = rootView.findViewById(R.id.addCategoryAddBtn);
        cancelBtn = rootView.findViewById(R.id.addCategoryCancelBtn);

        guid  = UUID.randomUUID().toString();
        newCategory = new Category();
        firebaseRepo = new FirebaseRepository();

        //TODO: This needs to be fixed before it's used
        /*
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                Navigation.createNavigateOnClickListener(R.id.action_addCategoriesFragment_to_categorySettingsFragment);
            }
        });*/

        View.OnClickListener addCategoryCancelClick = Navigation.createNavigateOnClickListener(R.id.action_addCategoriesFragment_to_categorySettingsFragment);
        cancelBtn.setOnClickListener(addCategoryCancelClick);

        categoryImage = rootView.findViewById(R.id.addCategoryImage);
        categoryImage.setImageResource(R.drawable.defaultimg);
        categoryImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Error handling for empty fields.
                if (editName.toString().equals("")){
                    makeText(getApplicationContext(), "All fields must be filled out before proceeding.", Toast.LENGTH_LONG).show();
                }
                else {
                    newCategory = new Category(editName.getText().toString());
                    newCategory.setImageName(guid);
                    if (imageUri != null){
                        firebaseRepo.saveCategoryImage(newCategory, cropResult.getUri());
                    }
                    firebaseRepo.insertCategory(newCategory);
                    Navigation.findNavController(view).navigate(R.id.action_addCategoriesFragment_to_categorySettingsFragment);
                }
            }
        });

        return rootView;
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

}
