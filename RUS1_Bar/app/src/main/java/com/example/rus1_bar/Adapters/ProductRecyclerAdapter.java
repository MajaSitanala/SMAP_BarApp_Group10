package com.example.rus1_bar.Adapters;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rus1_bar.Activities.ShoppingActivity;
import com.example.rus1_bar.Fragments.Bartender.ShoppingCardFragment;
import com.example.rus1_bar.Fragments.Bartender.ViewProductsFragment;
import com.example.rus1_bar.Models.Category;
import com.example.rus1_bar.Models.Product;
import com.example.rus1_bar.Models.Tutor;
import com.example.rus1_bar.R;
import com.example.rus1_bar.Repository.FirebaseRepository;
import com.google.android.gms.tasks.OnSuccessListener;
import com.squareup.picasso.Picasso;

import org.checkerframework.checker.units.qual.A;

import java.util.List;

public class ProductRecyclerAdapter extends RecyclerView.Adapter<ProductRecyclerAdapter.MyViewHolder> {

    private AdapterProductListner listner;

    private static final String CATEGORY_ID = "Category id";
    private static final int REQUEST_CATEGORY_ACTION = 102;

    private Context mContext;
    private List<Product> mProductList;
    private FirebaseRepository repository;

    private String categoryID;


    public ProductRecyclerAdapter(Context mContext, List<Product> mProductList,String CategoryId) {
        this.mContext = mContext;
        this.mProductList = mProductList;
        this.repository = new FirebaseRepository();
        this.categoryID = CategoryId;

        if (mContext instanceof AdapterProductListner)
        {
            listner = (AdapterProductListner) mContext;
        }
        else
        {
            throw new RuntimeException(mContext.toString() + ": must implement AdapterProductListner!");
        }
    }


    @NonNull
    @Override
    public ProductRecyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.cardwiev_item_product, parent, false);
        //final NavController navController = Navigation.findNavController(ShoppingActivity.this, R.id.nav_host_fragment);
        return new ProductRecyclerAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductRecyclerAdapter.MyViewHolder holder, int position) {

        holder.txt_productName.setText(mProductList.get(position).getProductName());
        holder.img_productImage.setImageResource(mProductList.get(position).getPicture());
        if(mProductList.get(position).getImageName() != null && categoryID !=  null){
            repository.getProductImage(mProductList.get(position).getImageName(), categoryID).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Picasso.with(mContext).load(uri).resize(600,600).centerInside().into(holder.img_productImage);
                }
            });

        }
        //Picasso.with(mContext).load(mTutorList.get(position).getPicture()).fit().centerInside().into(holder.img_tutorImage);



        holder.btn_minus.setOnClickListener(view -> {
            Product t = mProductList.get(position);
            //Toast.makeText(view.getContext(), "You tried to remove " + t.getProductName(), Toast.LENGTH_SHORT).show();
            listner.onClickRemoveProduct(t);
            this.notifyDataSetChanged();

        });

        holder.btn_plus.setOnClickListener(view -> {
            Product t = mProductList.get(position);
            //Toast.makeText(view.getContext(), "You tried to add " + t.getProductName(), Toast.LENGTH_SHORT).show();
            listner.onclickAddProduct(t);
            this.notifyDataSetChanged();
        });

        holder.cardViewProduct.setOnClickListener(view -> {
            Product t = mProductList.get(position);
            //Toast.makeText(view.getContext(), "You clicked " + t.getProductName(), Toast.LENGTH_SHORT).show();
            listner.onclickAddProduct(t);
            this.notifyDataSetChanged();
        });
    }

    @Override
    public int getItemCount() {
        return mProductList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView txt_productName;
        ImageView img_productImage;
        CardView cardViewProduct;
        ImageButton btn_minus;
        ImageButton btn_plus;

        public MyViewHolder(View itemView)
        {
            super(itemView);

            txt_productName = (TextView) itemView.findViewById(R.id.txt_productName);
            img_productImage = (ImageView) itemView.findViewById(R.id.img_productImage);
            cardViewProduct = (CardView) itemView.findViewById(R.id.cardview_product);
            btn_minus = itemView.findViewById(R.id.btn_remove_product);
            btn_plus = itemView.findViewById(R.id.btn_add_product);
        }

    }

    /**
     * Interface to comunicate with Shopping Activity
     *
     * Inspiration found at YouTube from Coding in Flow: https://youtu.be/i22INe14JUc?t=278
     */
    public interface AdapterProductListner
    {
        void onclickAddProduct(Product product);
        void onClickRemoveProduct(Product product);
    }
}
