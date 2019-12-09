package com.example.rus1_bar.Adapters;

import android.content.Context;
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

import com.example.rus1_bar.Models.Tutor;
import com.example.rus1_bar.R;
import com.example.rus1_bar.Repository.FirebaseRepository;
import com.example.rus1_bar.Service.ShoppingService;
import com.google.android.gms.tasks.OnSuccessListener;
import com.squareup.picasso.Picasso;

import java.util.List;

public class TutorDisplayAdapter extends RecyclerView.Adapter<TutorDisplayAdapter.MyViewHolder> {


    private static final String TUTOR_ID = "Tutor id";
    private Context mContext;
    private List<Tutor> mTutorList;
    private FirebaseRepository repository;


    public TutorDisplayAdapter(Context mContext, List<Tutor> mTutorList, ShoppingService shoppingService) {
        this.mContext = mContext;
        this.mTutorList = mTutorList;
        this.repository = shoppingService.getFirebaseRepository_fromService();
    }

    @NonNull
    @Override
    public TutorDisplayAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.cardview_item_tutor, parent, false);
        return new TutorDisplayAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TutorDisplayAdapter.MyViewHolder holder, int position) {
        holder.txt_tutorName.setText(mTutorList.get(position).getNickname());
        holder.img_tutorImage.setImageResource(mTutorList.get(position).getPicture());
        if(mTutorList.get(position).getImagename() != null) {
            repository.getTutorImage(mTutorList.get(position).getImagename()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Picasso.with(mContext).load(uri).resize(600, 600).centerInside().into(holder.img_tutorImage);
                }
            });
        }

        holder.cardView_tutorDisplay.setOnClickListener(view ->
        {
            Tutor t = mTutorList.get(position);

            Bundle bundle = new Bundle();
            bundle.putSerializable("Tutor", t);

            Navigation.findNavController(view).navigate(R.id.editTutorFragment,bundle);
        });


    }

    @Override
    public int getItemCount() {
        return mTutorList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView txt_tutorName;
        ImageView img_tutorImage;
        CardView cardView_tutorDisplay;

        public MyViewHolder(View itemView)
        {
            super(itemView);

            txt_tutorName = itemView.findViewById(R.id.txt_tutorName);
            img_tutorImage = itemView.findViewById(R.id.img_tutorImage);
            cardView_tutorDisplay = itemView.findViewById(R.id.cardview_tutor);

            txt_tutorName = itemView.findViewById(R.id.txt_tutorName);
            img_tutorImage = itemView.findViewById(R.id.img_tutorImage);
            cardView_tutorDisplay = itemView.findViewById(R.id.cardview_tutor);

            // https://stackoverflow.com/questions/19639691/android-getheight-and-getwidth
            DisplayMetrics viewMetrics = cardView_tutorDisplay.getResources().getDisplayMetrics();
            int displayWith = viewMetrics.widthPixels;
            int imagesize = ((displayWith-30)/3);

            img_tutorImage.getLayoutParams().width = imagesize;
            img_tutorImage.getLayoutParams().height = imagesize;     //30 is the collected the margin size
            img_tutorImage.requestLayout();

        }

    }

}
