package com.example.rus1_bar.Fragments.Administrator.Product;


import android.content.Intent;
import android.media.Image;
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

import com.example.rus1_bar.Models.Product;
import com.example.rus1_bar.Models.Tutor;
import com.example.rus1_bar.R;
import com.example.rus1_bar.Repository.FirebaseRepository;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.text.DecimalFormat;
import java.util.UUID;

import static android.app.Activity.RESULT_OK;
import static android.widget.Toast.makeText;
import static com.facebook.FacebookSdk.getApplicationContext;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddProductFragment extends Fragment {

    Button addBtn;
    Button cancelBtn;


    EditText editName;
    EditText editCategory;
    EditText editPrice;

    FirebaseRepository firebaseRepo;
    Product newProduct;

    private static final int PICK_IMAGE = 102;

    ImageView productImage;
    Uri imageUri;
    CropImage.ActivityResult cropResult;

    String guid;



    public AddProductFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_add_product, container, false);

        addBtn = rootView.findViewById(R.id.addProductAddBtn);
        cancelBtn = rootView.findViewById(R.id.addProductCancelBtn);

        guid  = UUID.randomUUID().toString();
        firebaseRepo = new FirebaseRepository();
        newProduct = new Product();

        editName = rootView.findViewById(R.id.addProductEditName);
        editCategory = rootView.findViewById(R.id.addProductEditCategory);
        editPrice = rootView.findViewById(R.id.addProductEditPrice);

        productImage = rootView.findViewById(R.id.addProductImage);

        productImage.setImageResource(R.drawable.defaultimg);
        productImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });

        //TODO: Add Button Missing, need to figure out the best way to do it

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
            productImage.setImageURI(cropResult.getUri());
        }
    }
}
