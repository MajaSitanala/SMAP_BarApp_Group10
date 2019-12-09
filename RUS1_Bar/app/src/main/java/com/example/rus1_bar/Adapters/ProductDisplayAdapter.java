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

import com.example.rus1_bar.Models.Product;
import com.example.rus1_bar.R;
import com.example.rus1_bar.Repository.FirebaseRepository;
import com.example.rus1_bar.Service.ShoppingService;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ProductDisplayAdapter extends RecyclerView.Adapter<ProductDisplayAdapter.MyViewHolder>{

    private ProductRecyclerAdapter.AdapterProductListner listner;

    private static final String CATEGORY_ID = "Category id";
    private static final int REQUEST_CATEGORY_ACTION = 102;

    private Context mContext;
    private List<Product> mProductList;
    private FirebaseRepository repository;
    private List<StorageReference> mImageList;
    private List<String> mCategorynameList;



    public ProductDisplayAdapter(Context context, List<Product> productList,List<String> categorynameList, ShoppingService shoppingService) {
        this.mContext = context;
        this.mProductList = productList;
        this.repository = shoppingService.getFirebaseRepository_fromService();
        this.mCategorynameList = categorynameList;
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


        if(mProductList.get(position).getImageName() != null)
            {
                repository.getProductImage(mProductList.get(position).getImageName(),
                        mCategorynameList.get(position)).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>()
                {
                    @Override
                    public void onSuccess(Uri uri)
                    {
                        Picasso.with(mContext).load(uri).resize(600,600).centerInside().into(holder.img_productImage);
                    }
                });
        }

        holder.cardViewProduct.setOnClickListener(view -> {
            Product t = mProductList.get(position);

            Bundle bundle = new Bundle();
            bundle.putSerializable("product", t);
            bundle.putSerializable("category_name",mCategorynameList.get(position));

            Navigation.findNavController(view).navigate(R.id.editProductFragment,bundle);
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

            // https://stackoverflow.com/questions/19639691/android-getheight-and-getwidth
            DisplayMetrics viewMetrics = cardViewProduct.getResources().getDisplayMetrics();
            int displayWith = viewMetrics.widthPixels;

            // https://stackoverflow.com/questions/3674933/find-out-if-android-device-is-portrait-or-landscape-for-normal-usage
            if(itemView.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
            {
                int imagesize = ((displayWith/4)-200);   //133 is the collected the margin size

                cardViewProduct.getLayoutParams().width = imagesize;
                img_productImage.getLayoutParams().width = imagesize;
                img_productImage.getLayoutParams().height = imagesize;
                img_productImage.requestLayout();
            }
            else
            {
                int imagesize = ((displayWith-30)/3);   //30 is the collected the margin size

                img_productImage.getLayoutParams().width = imagesize;
                img_productImage.getLayoutParams().height = imagesize;
                img_productImage.requestLayout();
            }

        }

    }
}
