package com.example.rus1_bar.Adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rus1_bar.Models.Product;
import com.example.rus1_bar.R;
import com.example.rus1_bar.Repository.FirebaseRepository;
import com.google.android.gms.tasks.OnSuccessListener;
import com.squareup.picasso.Picasso;

import java.util.List;

//TODO: Needs to use the cardview_item_product_display layout - this needs to be completed first.

public class ProductDisplayAdapter extends RecyclerView.Adapter<ProductDisplayAdapter.MyViewHolder>{

    private ProductRecyclerAdapter.AdapterProductListner listner;

    private static final String CATEGORY_ID = "Category id";
    private static final int REQUEST_CATEGORY_ACTION = 102;

    private Context mContext;
    private List<Product> mProductList;
    private FirebaseRepository repository;

    private String categoryID;


    public ProductDisplayAdapter(Context mContext, List<Product> mProductList,String CategoryId) {
        this.mContext = mContext;
        this.mProductList = mProductList;
        this.repository = new FirebaseRepository();
        this.categoryID = CategoryId;
    }


    @NonNull
    @Override
    public ProductDisplayAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.cardwiev_item_product, parent, false);
        //final NavController navController = Navigation.findNavController(ShoppingActivity.this, R.id.nav_host_fragment);
        return new ProductDisplayAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductDisplayAdapter.MyViewHolder holder, int position) {

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

        public MyViewHolder(View itemView)
        {
            super(itemView);

            txt_productName = (TextView) itemView.findViewById(R.id.txt_productName);
            img_productImage = (ImageView) itemView.findViewById(R.id.img_productImage);
            cardViewProduct = (CardView) itemView.findViewById(R.id.cardview_product);
        }

    }
}
