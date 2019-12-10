package com.example.rus1_bar.Fragments.Administrator.Product;


import android.content.BroadcastReceiver;
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
import androidx.navigation.Navigation;

import com.example.rus1_bar.Activities.MainActivity;
import com.example.rus1_bar.Models.Category;
import com.example.rus1_bar.Models.Product;
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
public class AddProductFragment extends Fragment {


    private static final String SERVICE_CONNECTED_MAIN_ACTIVITY = "Service connected to the main Activity" ;


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


    private ShoppingService shoppingService;
    private View rootView;


    public AddProductFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_add_product, container, false);

        addBtn = rootView.findViewById(R.id.addProductAddBtn);
        cancelBtn = rootView.findViewById(R.id.addProductCancelBtn);
        View.OnClickListener addProductCancelClick = Navigation.createNavigateOnClickListener(R.id.action_addProductFragment_to_productSettingsFragment);
        cancelBtn.setOnClickListener(addProductCancelClick);

        guid  = UUID.randomUUID().toString();

        newProduct = new Product();

        editName = rootView.findViewById(R.id.addProductEditName);
        editName.setText("");

        editCategory = rootView.findViewById(R.id.addProductEditCategory);
        editCategory.setText("");

        editPrice = rootView.findViewById(R.id.addProductEditPrice);
        editPrice.setText("");

        productImage = rootView.findViewById(R.id.addProductImage);

        productImage.setImageResource(R.drawable.defaultimg);
        productImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });

        return rootView;
    }

    @Override
    public void onStart()
    {
        super.onStart();

        LocalBroadcastManager.getInstance(this.getActivity()).registerReceiver(ServiceConnected, new IntentFilter(SERVICE_CONNECTED_MAIN_ACTIVITY));

        if (((MainActivity)getActivity()).getShoppingService_fromMainActivity() != null)
        {
            initAddProductFragment();
        }
    }


    private void initAddProductFragment()
    {
        if (getActivity() != null)
        {
            shoppingService = ((MainActivity)getActivity()).getShoppingService_fromMainActivity();

            firebaseRepo = shoppingService.getFirebaseRepository_fromService();//new FirebaseRepository();

            cancelBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    Navigation.findNavController(v).navigate(R.id.action_editProductFragment_to_productSettingsFragment);
                }
            });

            View.OnClickListener addProductCancelClick = Navigation.createNavigateOnClickListener(R.id.action_addProductFragment_to_productSettingsFragment);
            cancelBtn.setOnClickListener(addProductCancelClick);

            addBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Error handling for empty fields.

                    if ((editName.getText().toString().equals("")) || (editCategory.getText().toString().equals("")) || (editPrice.getText().toString().equals("")))
                    {
                        Toast.makeText(getContext(), "All fields must be filled out before proceeding.", Toast.LENGTH_LONG).show();
                    }
                    else
                        {

                        newProduct = new Product(editName.getText().toString(), Double.parseDouble(editPrice.getText().toString()));
                        newProduct.setImageName(guid);
                        if (imageUri != null){
                            firebaseRepo.saveProductImage(new Category(editCategory.getText().toString()),newProduct,cropResult.getUri());
                        }
                        firebaseRepo.insertProduct(newProduct, editCategory.getText().toString());
                        Navigation.findNavController(view).navigate(R.id.action_addProductFragment_to_productSettingsFragment);

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
            if(cropResult!= null){
                productImage.setImageURI(cropResult.getUri());
            } else {
                Toast.makeText(getActivity(), R.string.cropCancel, Toast.LENGTH_LONG).show();
            }

        }
    }


    private BroadcastReceiver ServiceConnected = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            initAddProductFragment();
        }
    };
}