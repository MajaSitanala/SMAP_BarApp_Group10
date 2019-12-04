package com.example.rus1_bar.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rus1_bar.Activities.ShoppingActivity;
import com.example.rus1_bar.Models.Tutor;
import com.example.rus1_bar.R;

import java.util.List;

public class TutorDisplayAdapter extends RecyclerView.Adapter<TutorDisplayAdapter.MyViewHolder> {


    private static final String TUTOR_ID = "Tutor id";
    private Context mContext;
    private List<Tutor> mTutorList;


    public TutorDisplayAdapter(Context mContext, List<Tutor> mTutorList) {
        this.mContext = mContext;
        this.mTutorList = mTutorList;
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

        holder.cardView_tutorDisplay.setOnClickListener(view ->
        {
            Tutor t = mTutorList.get(position);


            Bundle bundle = new Bundle();
            bundle.putString("Tutor",t.getNickname());

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
        }

    }

}
