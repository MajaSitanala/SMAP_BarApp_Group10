package com.example.rus1_bar.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

// Refference: tutorial at https://www.youtube.com/watch?v=SD2t75T5RdY

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rus1_bar.Models.Tutor;
import com.example.rus1_bar.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class TutorRecyclerAdapter extends RecyclerView.Adapter<TutorRecyclerAdapter.MyViewHolder> {

    private Context mContext;
    private List<Tutor> mTutorList;

    public TutorRecyclerAdapter(Context mContext, List<Tutor> mTutorList) {
        this.mContext = mContext;
        this.mTutorList = mTutorList;
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
        //Picasso.with(mContext).load(mTutorList.get(position).getPicture()).fit().centerInside().into(holder.img_tutorImage);

        //TODO: use this onclick listenter to go to another fragment or activity
        holder.cardViewTutor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
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

            txt_tutorName = (TextView) itemView.findViewById(R.id.txt_tutorName);
            img_tutorImage = (ImageView) itemView.findViewById(R.id.img_tutorImage);
            cardViewTutor = (CardView) itemView.findViewById(R.id.cardview_tutor);
        }

    }
}
