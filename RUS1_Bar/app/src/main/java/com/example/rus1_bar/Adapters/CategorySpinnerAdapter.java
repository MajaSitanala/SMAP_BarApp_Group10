package com.example.rus1_bar.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.rus1_bar.R;

import java.util.ArrayList;

public class CategorySpinnerAdapter extends ArrayAdapter<String> {

    public CategorySpinnerAdapter(Context context, ArrayList<String> categoryList){
        super(context, 0, categoryList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    private View initView(int position, View convertView, ViewGroup parent){
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.cardview_categoryname_spinner, parent, false
            );
        }
        TextView textViewName = convertView.findViewById(R.id.spinnerCategoryName);

        String currentCategory = getItem(position);

        if (currentCategory != null) {
            textViewName.setText(currentCategory);
        }

        return convertView;
    }
}
