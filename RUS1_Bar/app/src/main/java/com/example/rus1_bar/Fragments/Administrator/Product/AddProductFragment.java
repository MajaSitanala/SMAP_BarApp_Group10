package com.example.rus1_bar.Fragments.Administrator.Product;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rus1_bar.Activities.MainActivity;
import com.example.rus1_bar.Adapters.CategoryRecyclerAdapter;
import com.example.rus1_bar.Adapters.CategorySpinnerAdapter;
import com.example.rus1_bar.Models.Category;
import com.example.rus1_bar.Models.Product;
import com.example.rus1_bar.R;
import com.example.rus1_bar.Repository.FirebaseRepository;
import com.example.rus1_bar.Service.ShoppingService;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
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
public class AddProductFragment extends Fragment {


    private static final String SERVICE_CONNECTED_MAIN_ACTIVITY = "Service connected to the main Activity" ;


    private Button addBtn;
    private Button cancelBtn;

    private EditText editName;
    private EditText editPrice;

    private FirebaseRepository firebaseRepo;
    private Product newProduct;

    private List<String> mCategoryStringList = new ArrayList<>();
    private CategorySpinnerAdapter sAdapter;

    private static final int PICK_IMAGE = 102;

    // Database
    private DatabaseReference databaseCategoryDisplay;
    private FirebaseDatabase FireDB;

    private ImageView productImage;
    private Uri imageUri;
    private CropImage.ActivityResult cropResult;

    private String guid;
    private Spinner categoryNameSpinner;

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

        mCategoryStringList = (ArrayList<String>) getArguments().getSerializable("category_name_list");

        categoryNameSpinner = rootView.findViewById(R.id.categoryNameSpinner);

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

            sAdapter = new CategorySpinnerAdapter(getContext(), (ArrayList<String>) mCategoryStringList);

            categoryNameSpinner.setAdapter(sAdapter);

            categoryNameSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String clickedItem = (String) parent.getItemAtPosition(position);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

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

                    if ((editName.getText().toString().equals("")) || (categoryNameSpinner.getSelectedItem().toString().equals("")) || (editPrice.getText().toString().equals("")))
                    {
                        Toast.makeText(getContext(), "All fields must be filled out before proceeding.", Toast.LENGTH_LONG).show();
                    }
                    else
                        {

                        newProduct = new Product(editName.getText().toString(), Double.parseDouble(editPrice.getText().toString()));
                        newProduct.setImageName(guid);
                        if (imageUri != null){
                            firebaseRepo.saveProductImage(new Category(categoryNameSpinner.getSelectedItem().toString()),newProduct,cropResult.getUri());
                        }
                        firebaseRepo.insertProduct(newProduct, categoryNameSpinner.getSelectedItem().toString());
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