package com.example.rus1_bar.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rus1_bar.Activities.ShoppingActivity;
import com.example.rus1_bar.Models.Category;
import com.example.rus1_bar.R;

import java.util.List;

public class CategoryRecyclerAdapter extends RecyclerView.Adapter<CategoryRecyclerAdapter.MyViewHolder> {
    private static final String CATEGORY_ID = "Category id";
    private static final int REQUEST_CATEGORY_ACTION = 102;
    private Context mContext;
    private List<Category> mCategoryList;


    public CategoryRecyclerAdapter(Context mContext, List<Category> mCategoryList) {
        this.mContext = mContext;
        this.mCategoryList = mCategoryList;
    }


    @NonNull
    @Override
    public CategoryRecyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.cardwiev_item_category, parent, false);
        //final NavController navController = Navigation.findNavController(ShoppingActivity.this, R.id.nav_host_fragment);
        return new CategoryRecyclerAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryRecyclerAdapter.MyViewHolder holder, int position) {

        holder.txt_categoryName.setText(mCategoryList.get(position).getCategoryName());
        holder.img_categoryImage.setImageResource(mCategoryList.get(position).getPicture());
        //Picasso.with(mContext).load(mTutorList.get(position).getPicture()).fit().centerInside().into(holder.img_tutorImage);


        holder.cardViewCategory.setOnClickListener(view -> {
            Category t = mCategoryList.get(position);
            Toast.makeText(view.getContext(), "You clicked " + t.getCategoryName(), Toast.LENGTH_SHORT).show();

            // Navigation: navigates from ViewCategoriesFragment to ViewProducts fragment.
            Navigation.findNavController(view).navigate(R.id.viewProductsFragment);

        });
    }

    @Override
    public int getItemCount() {
        return mCategoryList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView txt_categoryName;
        ImageView img_categoryImage;
        CardView cardViewCategory;

        public MyViewHolder(View itemView)
        {
            super(itemView);

            txt_categoryName = (TextView) itemView.findViewById(R.id.txt_categoryName);
            img_categoryImage = (ImageView) itemView.findViewById(R.id.img_categoryImage);
            cardViewCategory = (CardView) itemView.findViewById(R.id.cardview_category);
        }
    }
}
