package com.example.rus1_bar.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

// Refference: tutorial at https://www.youtube.com/watch?v=SD2t75T5RdY

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rus1_bar.Activities.ShoppingActivity;
import com.example.rus1_bar.Models.Tutor;
import com.example.rus1_bar.R;

import java.util.List;

public class TutorRecyclerAdapter extends RecyclerView.Adapter<TutorRecyclerAdapter.MyViewHolder> {

    private static final String TUTOR_ID = "Tutor id";
    private static final int REQUEST_TUTOR_PURCHASE = 101;
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
        //final NavController navController = Navigation.findNavController(MainActivity.this, R.id.nav_host_fragment);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.txt_tutorName.setText(mTutorList.get(position).getNickname());
        holder.img_tutorImage.setImageResource(mTutorList.get(position).getPicture());
        //Picasso.with(mContext).load(mTutorList.get(position).getPicture()).fit().centerInside().into(holder.img_tutorImage);



        //TODO: use this onclick listenter to go to another fragment or activity
        holder.cardViewTutor.setOnClickListener(view -> {
            Tutor t = mTutorList.get(position);
            Toast.makeText(view.getContext(), "You clicked " + t.getNickname(), Toast.LENGTH_SHORT).show();

            Intent listIntent = new Intent(mContext, ShoppingActivity.class);
            listIntent.putExtra(TUTOR_ID, t.getID());
            ((Activity)mContext).startActivityForResult(listIntent, REQUEST_TUTOR_PURCHASE);


            //navController = Navigation.findNavController(getActivity(),R.id.nav_host_fragment);
            //Navigation.createNavigateOnClickListener(R.id.action_viewTutorsFragment_to_viewCategoriesFragment);
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

        }

    }

}
