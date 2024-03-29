package com.example.rus1_bar.Adapters;

import android.content.Context;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
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
import com.example.rus1_bar.R;
import com.example.rus1_bar.Repository.FirebaseRepository;
import com.example.rus1_bar.Service.ShoppingService;
import com.google.android.gms.tasks.OnSuccessListener;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CategoryRecyclerAdapter extends RecyclerView.Adapter<CategoryRecyclerAdapter.MyViewHolder> {
    private static final String CATEGORY_ID = "Category id";
    private static final int REQUEST_CATEGORY_ACTION = 102;
    private static final String CATEGOTY_NAME = "category name from adapter";
    private Context mContext;
    private List<Category> mCategoryList;
    private FirebaseRepository repository;

    public CategoryRecyclerAdapter(Context mContext, List<Category> mCategoryList, ShoppingService shoppingService) {
        this.mContext = mContext;
        this.mCategoryList = mCategoryList;
        this.repository = shoppingService.getFirebaseRepository_fromService();
    }


    @NonNull
    @Override
    public CategoryRecyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.cardwiev_item_category, parent, false);
        return new CategoryRecyclerAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryRecyclerAdapter.MyViewHolder holder, int position) {

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
            bundle.putString("category",t.getCategoryName());

            // Navigation: navigates from ViewCategoriesFragment to ViewProducts fragment.
            Navigation.findNavController(view).navigate(R.id.viewProductsFragment,bundle);

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

            // https://stackoverflow.com/questions/19639691/android-getheight-and-getwidth
            DisplayMetrics viewMetrics = cardViewCategory.getResources().getDisplayMetrics();
            int displayWith = viewMetrics.widthPixels;

            // https://stackoverflow.com/questions/3674933/find-out-if-android-device-is-portrait-or-landscape-for-normal-usage
            if(itemView.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
            {
                int imagesize = ((displayWith/4)-200);   //133 is the collected the margin size

                cardViewCategory.getLayoutParams().width = imagesize;
                img_categoryImage.getLayoutParams().width = imagesize;
                img_categoryImage.getLayoutParams().height = imagesize;
                img_categoryImage.requestLayout();
            }
            else
            {
                int imagesize = ((displayWith-30)/3);   //30 is the collected the margin size

                img_categoryImage.getLayoutParams().width = imagesize;
                img_categoryImage.getLayoutParams().height = imagesize;
                img_categoryImage.requestLayout();
            }
        }
    }
}
