package com.example.rus1_bar.Fragments.Administrator.Product;


import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ParseException;
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
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.rus1_bar.Activities.MainActivity;
import com.example.rus1_bar.Models.Product;
import com.example.rus1_bar.R;
import com.example.rus1_bar.Repository.FirebaseRepository;
import com.example.rus1_bar.Service.ShoppingService;
import com.google.android.gms.tasks.OnSuccessListener;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static android.app.Activity.RESULT_OK;
import static android.widget.Toast.makeText;
import static com.facebook.FacebookSdk.getApplicationContext;


/**
 * A simple {@link Fragment} subclass.
 */
public class EditProductFragment extends Fragment
{

    private static final String SERVICE_CONNECTED_MAIN_ACTIVITY = "Service connected to the main Activity" ;

    private Button cancelBtn;
    private Button deleteBtn;
    private Button editBtn;

    private Boolean categyNameCheck;

    private static final int PICK_IMAGE = 101;
    private ImageView productImage;
    private Uri imageUri;
    private CropImage.ActivityResult cropResult;
    private String guid;

    private FirebaseRepository firebaseRepo;
    private Product currentProduct;
    private String categoryName;

    private EditText editName;
    private EditText editCategoryOfproduct;
    private EditText editPrice;

    private AlertDialog diaBox;

    ShoppingService shoppingService;
    private List<String> mCategorynameList = new ArrayList<String>();

    private View rootView;
    private boolean newImageSat;

    public EditProductFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_edit_product, container, false);

        guid  = UUID.randomUUID().toString();
        currentProduct = (Product) getArguments().getSerializable("product");
        categoryName = (String) getArguments().getSerializable("category_name");
        mCategorynameList = (ArrayList<String>) getArguments().getStringArrayList("category_list");



        editName = rootView.findViewById(R.id.editProductEditName);
        editName.setText(currentProduct.getProductName());
        editCategoryOfproduct = rootView.findViewById(R.id.editProductEditCategory);
        editCategoryOfproduct.setText(categoryName);
        editPrice = rootView.findViewById(R.id.editProductEditPrice);
        editPrice.setText(Double.toString(currentProduct.getPrice()));

        cancelBtn = rootView.findViewById(R.id.editProductCancelBtn);
        deleteBtn = rootView.findViewById(R.id.editProductDeleteBtn);
        editBtn = rootView.findViewById(R.id.editProductEditBtn);

        productImage = rootView.findViewById(R.id.editProductImage);

        productImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                newImageSat = true;
                openGallery();
            }
        });


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
            firebaseRepo = shoppingService.getFirebaseRepository_fromService();

            if((currentProduct.getImageName() != null) && (newImageSat == false)){
                firebaseRepo.getProductImage(currentProduct.getImageName(), categoryName).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.with(rootView.getContext()).load(uri).resize(600,600).centerInside().into(productImage);
                    }
                });
            }

            NavController navController = Navigation.findNavController((this.getActivity()), R.id.nav_host_fragment);
            cancelBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    newImageSat = false;
                    navController.navigate(R.id.action_editProductFragment_to_productSettingsFragment);
                }
            });


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
                    for(String categoryName : mCategorynameList)
                    {
                        if (editCategoryOfproduct.getText().toString().equals(categoryName))
                        {
                            categyNameCheck = true;
                            if ((editName.getText().toString().equals("")) || (editCategoryOfproduct.getText().toString().equals("")) || (editPrice.getText().toString().equals("")))
                            {
                                Toast.makeText(getContext(), "All fields must be filled out before proceeding.", Toast.LENGTH_LONG).show();
                            }
                            else {


                                Product oldProduct = currentProduct;

                                currentProduct.setProductName(editName.getText().toString());
                                try {
                                    currentProduct.setPrice(Double.parseDouble(editPrice.getText().toString()));
                                }catch (ParseException e)
                                {
                                    Toast.makeText(getContext(),editPrice.getText().toString() + " Is not a valid price",Toast.LENGTH_LONG);
                                }

                                if(currentProduct.getImageName()!= null){firebaseRepo.insertProduct(currentProduct, categoryName);
                                    Navigation.findNavController(view).navigate(R.id.action_editProductFragment_to_productSettingsFragment);}
                                else {
                                    Toast.makeText(getContext(), "No image chosen",Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                        else
                        {
                            categyNameCheck = false;
                        }
                    }
                    if (categyNameCheck==false)
                    {
                        Toast.makeText(getContext(), "No catagoty named "+editCategoryOfproduct.getText().toString(),Toast.LENGTH_SHORT).show();
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
                .setMessage("Do you want to delete this Product?")
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        newImageSat = false;
                        firebaseRepo.deleteProduct(currentProduct, categoryName);
                        Navigation.findNavController(getView()).navigate(R.id.action_editProductFragment_to_productSettingsFragment);
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
            if (cropResult != null) {
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
            initEditCategoryFragment();
        }
    };
}
