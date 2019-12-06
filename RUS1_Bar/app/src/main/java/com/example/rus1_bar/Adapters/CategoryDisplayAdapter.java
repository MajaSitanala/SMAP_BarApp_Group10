package com.example.rus1_bar.Adapters;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rus1_bar.Models.Category;
import com.example.rus1_bar.Models.Tutor;
import com.example.rus1_bar.R;
import com.example.rus1_bar.Repository.FirebaseRepository;
import com.google.android.gms.tasks.OnSuccessListener;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CategoryDisplayAdapter extends RecyclerView.Adapter<CategoryDisplayAdapter.MyViewHolder> {

    private static final String CATEGORY_ID = "Category id";
    private static final int REQUEST_CATEGORY_ACTION = 102;
    private Context mContext;
    private List<Category> mCategoryList;
    private FirebaseRepository repository;


    public CategoryDisplayAdapter(Context mContext, List<Category> mCategoryList) {
        this.mContext = mContext;
        this.mCategoryList = mCategoryList;
        this.repository = new FirebaseRepository();
    }


    @NonNull
    @Override
    public CategoryDisplayAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.cardwiev_item_category, parent, false);
        return new CategoryDisplayAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryDisplayAdapter.MyViewHolder holder, int position) {

        holder.txt_categoryName.setText(mCategoryList.get(position).getCategoryName());
        holder.img_categoryImage.setImageResource(mCategoryList.get(position).getPicture());
        if(mCategoryList.get(position).getImageName() != null){
            repository.getCategoryImage(mCategoryList.get(position).getImageName()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Picasso.with(mContext).load(uri).resize(600,600).centerInside().into(holder.img_categoryImage);
                }
            });

        }

        holder.cardViewCategory.setOnClickListener(view -> {
            Category t = mCategoryList.get(position);

            Bundle bundle = new Bundle();
            bundle.putSerializable("category", t);

            Navigation.findNavController(view).navigate(R.id.editCategoryFragment,bundle);

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
