package com.example.rus1_bar.Fragments.Administrator.Product;


import android.media.Image;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.rus1_bar.Models.Product;
import com.example.rus1_bar.R;
import com.example.rus1_bar.Repository.FirebaseRepository;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.UUID;


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


        return rootView;
    }
}
