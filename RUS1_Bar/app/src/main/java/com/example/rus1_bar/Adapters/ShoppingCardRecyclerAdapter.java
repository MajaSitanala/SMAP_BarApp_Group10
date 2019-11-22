package com.example.rus1_bar.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rus1_bar.Activities.ShoppingActivity;
import com.example.rus1_bar.Models.Product;
import com.example.rus1_bar.R;

import java.util.List;

public class ShoppingCardRecyclerAdapter extends RecyclerView.Adapter<ShoppingCardRecyclerAdapter.MyViewHolder> {

    private Context mContext;
    private List<Product> mProductList;


    public ShoppingCardRecyclerAdapter(Context mContext, List<Product> mProductList) {
        this.mContext = mContext;
        this.mProductList = mProductList;
    }


    @NonNull
    @Override
    public ShoppingCardRecyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.cardview_item_shoppingcard, parent, false);
        //final NavController navController = Navigation.findNavController(MainActivity.this, R.id.nav_host_fragment);
        return new ShoppingCardRecyclerAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ShoppingCardRecyclerAdapter.MyViewHolder holder, int position) {

        holder.txt_itemQuantity.setText(String.valueOf(mProductList.get(position).getQuantity()));
        holder.txt_itemName.setText(mProductList.get(position).getProductName());
        holder.txt_itemPrice.setText(String.valueOf(mProductList.get(position).getPrice()));

        //Picasso.with(mContext).load(mTutorList.get(position).getPicture()).fit().centerInside().into(holder.img_tutorImage);

        //TODO: use this onclick listenter to go to another fragment or activity
        holder.cardViewShoppingCard.setOnClickListener(view -> {
            Product t = mProductList.get(position);
            Toast.makeText(view.getContext(), "You clicked " + t.getProductName(), Toast.LENGTH_SHORT).show();

            //navController = Navigation.findNavController(getActivity(),R.id.nav_host_fragment);
            //Navigation.createNavigateOnClickListener(R.id.action_viewTutorsFragment_to_viewCategoriesFragment);
        });
    }

    @Override
    public int getItemCount() {
        return mProductList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView txt_itemQuantity;
        TextView txt_itemName;
        TextView txt_itemPrice;
        ImageButton btn_deleteButton;
        CardView cardViewShoppingCard;

        public MyViewHolder(View itemView)
        {
            super(itemView);

            txt_itemQuantity = (TextView) itemView.findViewById(R.id.txt_itemQuantity);
            txt_itemName = (TextView) itemView.findViewById(R.id.txt_itemName);
            txt_itemPrice = (TextView) itemView.findViewById(R.id.txt_itemPrice);
            btn_deleteButton = (ImageButton) itemView.findViewById(R.id.btn_deleteButton);
            cardViewShoppingCard = (CardView) itemView.findViewById(R.id.cardview_shoppingcard);
        }
    }
}