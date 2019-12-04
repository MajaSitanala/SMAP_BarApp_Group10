package com.example.rus1_bar.Fragments.Bartender;


import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.rus1_bar.Activities.ShoppingActivity;
import com.example.rus1_bar.Adapters.ShoppingCardRecyclerAdapter;
import com.example.rus1_bar.Models.Product;
import com.example.rus1_bar.Models.ShoppingViewModel;
import com.example.rus1_bar.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ShoppingCardFragment extends Fragment{


    private FragmentViewShoppingCardListener listener;

    private ShoppingViewModel shoppingViewModel;

    private List<Product> mProductList = new ArrayList<>();

    RecyclerView shoppingCardRecyclerView;
    ShoppingCardRecyclerAdapter shoppingCardRecyclerAdapter;
    //RecyclerView.Adapter shoppingCardRecyclerAdapter;
    RecyclerView.LayoutManager shoppingCardLayoutManager;

    public ShoppingCardFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // rootView is needed when using fingViewById because otherwise the views have not been created by the time the views are called.
        View rootView = inflater.inflate(R.layout.fragment_shopping_card, container, false);                                              //https://stackoverflow.com/questions/26621060/display-a-recyclerview-in-fragment




        //Test data for the card view
        //fillTestShoppingCardList();

        // Getting data from the Activity.
        // mProductList = listener.fromActivity_getShoppingProducts();



        // Recycler View setup
        shoppingCardRecyclerView = rootView.findViewById(R.id.shoppingCardRecyclerView);


        // Creates the linear layout
        shoppingCardLayoutManager = new LinearLayoutManager(getActivity());                                                                //https://youtu.be/SD2t75T5RdY?t=1302
        shoppingCardRecyclerView.setLayoutManager(shoppingCardLayoutManager);

        // Recycler adapter setup
        shoppingCardRecyclerAdapter = new ShoppingCardRecyclerAdapter(getActivity(), mProductList);
        shoppingCardRecyclerView.setAdapter(shoppingCardRecyclerAdapter);


        // Using the view model from the Shopping activity
        shoppingViewModel = listener.fromActivity_GetShoppingViewModel();
        shoppingViewModel.getAllProductsinPurchase().observe(this, products ->
        {
            mProductList.clear();
            for (Product p : products)
            {
                mProductList.add(p);
            }
            shoppingCardRecyclerAdapter.notifyDataSetChanged();

        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                listener.swipeToDelete(shoppingCardRecyclerAdapter.getProductAt(viewHolder.getAdapterPosition()));
                //Toast.makeText(this, "Note deleted", Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(shoppingCardRecyclerView);


        // Inflate the layout for this fragment
        return rootView; // inflater.inflate(R.layout.fragment_view_tutors, container, false);
    }


    /**
     * onAttach is called when the fragment is attached to shoppingActivity
     *
     * Inspiration found at Youtube from Coding in Flow: https://youtu.be/i22INe14JUc?t=442
     */
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        // Checks if the activity implements the fragment interface. To avoid null refferences.
        if (context instanceof FragmentViewShoppingCardListener)
        {
            listener = (FragmentViewShoppingCardListener) context;
        }
        else
        {
            throw new RuntimeException(context.toString() + ": must implement FragmentViewShoppingCardListenter!");
        }
    }

    /**
     * When the fragment is detached from the activity
     *
     * Inspiration found at Youtube from Coding in Flow: https://youtu.be/i22INe14JUc?t=527
     */
    @Override
    public void onDetach() {
        super.onDetach();

        // Removes the refference to the activity.
        listener = null;
    }

    /**
     * Interface to comunicate with Shopping Activity
     *
     * Inspiration found at YouTube from Coding in Flow: https://youtu.be/i22INe14JUc?t=278
     */
    public interface FragmentViewShoppingCardListener
    {
        // Meathod to be overwritten that gets category data from the Activity that gets it from the database.
        List<Product> fromActivity_getShoppingProducts();

        ShoppingViewModel fromActivity_GetShoppingViewModel();
        void swipeToDelete(Product product);

    }


    private void fillTestShoppingCardList()
    {
        for (int i = 0; i<4; i++)
        {
            //Test data for the card view
            mProductList.add(new Product("i",i, "Gin Hass", 20, R.drawable.ginhass));
            mProductList.add(new Product("i",i, "Filur", 20, R.drawable.filur));
            mProductList.add(new Product("i",i, "White Russian", 20, R.drawable.whiterussianlang));
        }
    }
}
