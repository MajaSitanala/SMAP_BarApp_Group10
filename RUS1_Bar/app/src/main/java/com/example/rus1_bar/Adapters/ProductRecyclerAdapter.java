package com.example.rus1_bar.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.example.rus1_bar.Fragments.Bartender.ViewProductsFragment;
import com.example.rus1_bar.Models.Category;
import com.example.rus1_bar.Models.Product;
import com.example.rus1_bar.R;

import java.util.List;

public class ProductRecyclerAdapter extends RecyclerView.Adapter<ProductRecyclerAdapter.MyViewHolder> {
    private static final String CATEGORY_ID = "Category id";
    private static final int REQUEST_CATEGORY_ACTION = 102;
    private Context mContext;
    private List<Product> mProductList;


    public ProductRecyclerAdapter(Context mContext, List<Product> mCategoryList) {
        this.mContext = mContext;
        this.mProductList = mCategoryList;
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
        //Picasso.with(mContext).load(mTutorList.get(position).getPicture()).fit().centerInside().into(holder.img_tutorImage);

        //TODO: use this onclick listenter to go to another fragment or activity
        holder.cardViewProduct.setOnClickListener(view -> {
            Product t = mProductList.get(position);
            Toast.makeText(view.getContext(), "You clicked " + t.getProductName(), Toast.LENGTH_SHORT).show();


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

        public MyViewHolder(View itemView)
        {
            super(itemView);

            txt_productName = (TextView) itemView.findViewById(R.id.txt_productName);
            img_productImage = (ImageView) itemView.findViewById(R.id.img_productImage);
            cardViewProduct = (CardView) itemView.findViewById(R.id.cardview_product);
        }

    }
}
