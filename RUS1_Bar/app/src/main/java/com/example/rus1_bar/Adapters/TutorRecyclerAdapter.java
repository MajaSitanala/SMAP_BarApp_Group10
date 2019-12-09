package com.example.rus1_bar.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rus1_bar.Activities.ShoppingActivity;
import com.example.rus1_bar.Models.Tutor;
import com.example.rus1_bar.R;
import com.example.rus1_bar.Repository.FirebaseRepository;
import com.google.android.gms.tasks.OnSuccessListener;
import com.squareup.picasso.Picasso;

import java.util.List;

// Refference: tutorial at https://www.youtube.com/watch?v=SD2t75T5RdY

public class TutorRecyclerAdapter extends RecyclerView.Adapter<TutorRecyclerAdapter.MyViewHolder> {

    private static final String TUTOR_NICK= "Tutor nickname";
    private static final int REQUEST_TUTOR_PURCHASE = 101;
    private static final String TUTOR_OBJECT = "Current Tutor Object";
    private Context mContext;
    private List<Tutor> mTutorList;
    private FirebaseRepository repository;



    public TutorRecyclerAdapter(Context mContext, List<Tutor> mTutorList) {
        this.mContext = mContext;
        this.mTutorList = mTutorList;
        this.repository = new FirebaseRepository();
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.cardview_item_tutor, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.txt_tutorName.setText(mTutorList.get(position).getNickname());
        holder.img_tutorImage.setImageResource(mTutorList.get(position).getPicture());
        if(mTutorList.get(position).getImagename() != null){
            repository.getTutorImage(mTutorList.get(position).getImagename()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Picasso.with(mContext).load(uri).resize(600,600).centerInside().into(holder.img_tutorImage);
                }
            });

        }

        holder.cardViewTutor.setOnClickListener(view -> {
            Tutor t = mTutorList.get(position);

            Intent listIntent = new Intent(mContext, ShoppingActivity.class);
            listIntent.putExtra(TUTOR_OBJECT, t);
            ((Activity)mContext).startActivityForResult(listIntent, REQUEST_TUTOR_PURCHASE);

        });
    }

    @Override
    public int getItemCount() {
        return mTutorList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView txt_tutorName;
        ImageView  img_tutorImage;
        CardView cardViewTutor;

        public MyViewHolder(View itemView)
        {
            super(itemView);

            txt_tutorName = itemView.findViewById(R.id.txt_tutorName);
            img_tutorImage = itemView.findViewById(R.id.img_tutorImage);
            cardViewTutor = itemView.findViewById(R.id.cardview_tutor);

            // https://stackoverflow.com/questions/19639691/android-getheight-and-getwidth
            DisplayMetrics viewMetrics = cardViewTutor.getResources().getDisplayMetrics();
            int displayWith = viewMetrics.widthPixels;

            // https://stackoverflow.com/questions/3674933/find-out-if-android-device-is-portrait-or-landscape-for-normal-usage
            if(itemView.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
            {
                int imagesize = ((displayWith-133)/4);   //133 is the collected the margin size

                cardViewTutor.getLayoutParams().width = imagesize;
                img_tutorImage.getLayoutParams().width = imagesize;
                img_tutorImage.getLayoutParams().height = imagesize;
                img_tutorImage.requestLayout();
            }
            else
            {
                int imagesize = ((displayWith-30)/3);   //30 is the collected the margin size

                img_tutorImage.getLayoutParams().width = imagesize;
                img_tutorImage.getLayoutParams().height = imagesize;
                img_tutorImage.requestLayout();
            }
        }
    }
}
