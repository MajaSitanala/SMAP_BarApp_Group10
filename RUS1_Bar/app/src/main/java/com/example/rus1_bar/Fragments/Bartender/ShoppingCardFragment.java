package com.example.rus1_bar.Fragments.Bartender;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rus1_bar.Activities.ShoppingActivity;
import com.example.rus1_bar.Adapters.ShoppingCardRecyclerAdapter;
import com.example.rus1_bar.Models.Product;
import com.example.rus1_bar.Models.ShoppingViewModel;
import com.example.rus1_bar.R;
import com.example.rus1_bar.Service.ShoppingService;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ShoppingCardFragment extends Fragment{

    private static final String SERVICE_CONNECTED_MAIN_ACTIVITY = "Service connected to the main Activity";


    private FragmentViewShoppingCardListener listener;

    private ShoppingViewModel shoppingViewModel;

    private List<Product> mProductList = new ArrayList<>();

    RecyclerView shoppingCardRecyclerView;
    ShoppingCardRecyclerAdapter shoppingCardRecyclerAdapter;
    //RecyclerView.Adapter shoppingCardRecyclerAdapter;
    RecyclerView.LayoutManager shoppingCardLayoutManager;

    ShoppingService shoppingService;

    View rootView;

    public ShoppingCardFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // rootView is needed when using fingViewById because otherwise the views have not been created by the time the views are called.
        rootView = inflater.inflate(R.layout.fragment_shopping_card, container, false);                                              //https://stackoverflow.com/questions/26621060/display-a-recyclerview-in-fragment

        // Inflate the layout for this fragment
        return rootView; // inflater.inflate(R.layout.fragment_view_tutors, container, false);
    }

    @Override
    public void onStart()
    {
        super.onStart();
        LocalBroadcastManager.getInstance(this.getActivity()).registerReceiver(ServiceConnected, new IntentFilter(SERVICE_CONNECTED_MAIN_ACTIVITY));

        if (((ShoppingActivity)getActivity()).getShoppingService_fromShoppingActivity() != null)
        {
            initShoppingCardFragment();
        }
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


    private void initShoppingCardFragment()
    {
        if(getActivity() != null)
        {
            shoppingService = ((ShoppingActivity)getActivity()).getShoppingService_fromShoppingActivity();

            // Recycler View setup
            shoppingCardRecyclerView = rootView.findViewById(R.id.shoppingCardRecyclerView);


            // Creates the linear layout
            shoppingCardLayoutManager = new LinearLayoutManager(getActivity());                                                                //https://youtu.be/SD2t75T5RdY?t=1302
            shoppingCardRecyclerView.setLayoutManager(shoppingCardLayoutManager);

            // Recycler adapter setup
            shoppingCardRecyclerAdapter = new ShoppingCardRecyclerAdapter(getActivity(), mProductList);
            shoppingCardRecyclerView.setAdapter(shoppingCardRecyclerAdapter);


            // Using the view model from the Shopping activity
            shoppingViewModel = shoppingService.getShoppingViewModel_fromService();
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



        }
    }

    /**
     * Interface to comunicate with Shopping Activity
     *
     * Inspiration found at YouTube from Coding in Flow: https://youtu.be/i22INe14JUc?t=278
     */
    public interface FragmentViewShoppingCardListener
    {
        // Meathod to be overwritten that gets category data from the Activity that gets it from the database.
        void swipeToDelete(Product product);

    }

    private BroadcastReceiver ServiceConnected = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            initShoppingCardFragment();
        }
    };



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
