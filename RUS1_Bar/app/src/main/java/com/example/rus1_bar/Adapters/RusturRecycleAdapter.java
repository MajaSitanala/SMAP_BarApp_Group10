package com.example.rus1_bar.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rus1_bar.Models.Rustur;
import com.example.rus1_bar.Models.Tutor;
import com.example.rus1_bar.R;
import com.example.rus1_bar.Repository.FirebaseRepository;

import java.util.ArrayList;
import java.util.List;

public class RusturRecycleAdapter extends RecyclerView.Adapter<RusturRecycleAdapter.MyViewHolder> {
    private Context mContext;
    private List<Rustur> mRusturList = new ArrayList<>();
    private FirebaseRepository repository;

    public RusturRecycleAdapter(Context mContext, List<Rustur> mTutorList) {
        this.mContext = mContext;
        this.mRusturList = mTutorList;
        this.repository = new FirebaseRepository();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.cardview_item_tutor, parent, false);
        return new RusturRecycleAdapter.MyViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return mRusturList.size();
    }

    @Override
    public void onBindViewHolder(@NonNull RusturRecycleAdapter.MyViewHolder holder, int position) {
        holder.img_rusturImage.setImageResource(R.drawable.com_facebook_button_like_icon_selected);
        holder.txt_rusturName.setText(mRusturList.get(position).getRusturName());
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView txt_rusturName;
        ImageView img_rusturImage;
        CardView cardViewrustur;

        public MyViewHolder(View itemView)
        {
            super(itemView);

            txt_rusturName = itemView.findViewById(R.id.txt_tutorName);
            img_rusturImage = itemView.findViewById(R.id.img_tutorImage);
            cardViewrustur = itemView.findViewById(R.id.cardview_tutor);


        }
    }
}
