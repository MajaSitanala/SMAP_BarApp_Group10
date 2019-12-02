package com.example.rus1_bar.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rus1_bar.Models.Product;
import com.example.rus1_bar.R;

import java.util.List;

//TODO: Needs to use the cardview_item_product_display layout - this needs to be completed first.

public class ProductDisplayAdapter extends RecyclerView.Adapter<ProductDisplayAdapter.MyViewHolder>{

    private static final String CATEGORY_ID = "Category id";
    private Context mContext;
    private List<Product> mProductList;


    public ProductDisplayAdapter(Context mContext, List<Product> mProductList) {
        this.mContext = mContext;
        this.mProductList = mProductList;
    }


    @NonNull
    @Override
    public ProductDisplayAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.cardwiev_item_product, parent, false);
        return new ProductDisplayAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductDisplayAdapter.MyViewHolder holder, int position) {

        holder.txt_productName.setText(mProductList.get(position).getProductName());
        holder.img_productImage.setImageResource(mProductList.get(position).getPicture());

    }

    @Override
    public int getItemCount() {
        return mProductList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView txt_productName;
        ImageView img_productImage;

        public MyViewHolder(View itemView)
        {
            super(itemView);

            txt_productName = (TextView) itemView.findViewById(R.id.txt_productName);
            img_productImage = (ImageView) itemView.findViewById(R.id.img_productImage);
        }

    }
}
