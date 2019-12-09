package com.example.rus1_bar.Activities;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.rus1_bar.Adapters.ProductRecyclerAdapter;
import com.example.rus1_bar.Adapters.ShoppingCardRecyclerAdapter;
import com.example.rus1_bar.Fragments.Bartender.ShoppingCardFragment;
import com.example.rus1_bar.Models.Product;
import com.example.rus1_bar.Models.Purchase;
import com.example.rus1_bar.Models.Rustur;
import com.example.rus1_bar.Models.ShoppingViewModel;
import com.example.rus1_bar.Models.Tutor;
import com.example.rus1_bar.R;
import com.example.rus1_bar.Service.ShoppingService;
import com.facebook.stetho.Stetho;


public class ShoppingActivity extends AppCompatActivity implements ProductRecyclerAdapter.AdapterProductListner,
        ShoppingCardFragment.FragmentViewShoppingCardListener, ShoppingCardRecyclerAdapter.AdapterShoppingCardListner{

    private static final String SERVICE_CONNECTED_SHOPPING_ACTIVITY = "Shopping service connected to Shopping Activity";
    private ShoppingViewModel shoppingViewModel;

    // Service
    ShoppingService shoppingService;
    boolean isBound = false;

    Purchase mPurchace = new Purchase();

    Button btn_cncl;
    Button btn_buy;
    TextView currentTutor;
    private static final String TUTOR_OBJECT = "Current Tutor Object";
    private String currentTutorName;
    private Tutor currentTutorClicked;
    private Rustur rustur;

    private String mCategoryame;

    NavController navController;

    private TextView textView_itemsInCart;
    private TextView textView_totalSum;

    private int intemsInCart;
    private double totalSum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping);

        if(savedInstanceState != null)
        {
        }

        navController = Navigation.findNavController((this), R.id.nav_item_selection_fragment);

        //for debugging/viewing database
        enableStethos();

        // UI declarations
        textView_itemsInCart = findViewById(R.id.txt_items_in_cart);
        textView_totalSum = findViewById(R.id.txt_total_sum);
        updateCartUI(0, 0.0);

        // Intents from Main Activity
        Intent mainIntent = getIntent();
        currentTutorClicked = (Tutor) mainIntent.getSerializableExtra(TUTOR_OBJECT);
        currentTutorName = currentTutorClicked.getNickname();

        Toolbar shoppingToolbar = findViewById(R.id.MAtoolbar);
        setSupportActionBar(shoppingToolbar);

        TextView tutorNucknaleToolbar = findViewById(R.id.tekst_SA_Toolbar);
        tutorNucknaleToolbar.setText(currentTutorName);

    }

    @Override
    protected void onStart() {
        super.onStart();

        //startService();
        Intent intent = new Intent(this, ShoppingService.class);
        bindService(intent, connection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        unbindService(connection);
        super.onStop();
    }


    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume()
    {
        super.onResume();

    }

    /**
     * ServiceConnection that makes the ListActivity subscribe to the FourgroundService.
     */
    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            ShoppingService.LocalBinder binder = (ShoppingService.LocalBinder) iBinder;
            shoppingService = binder.getService();
            isBound = true;

            initViewModel();

            Intent intent = new Intent(SERVICE_CONNECTED_SHOPPING_ACTIVITY);
            LocalBroadcastManager.getInstance(shoppingService.getApplicationContext()).sendBroadcast(intent);

        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            isBound = false;
        }
    };


    private void initViewModel()
    {
        shoppingViewModel = shoppingService.getShoppingViewModel_fromService();

        rustur = shoppingService.getCurrentRustur();


        // Shopping card fragment (not navigation componment)
        ShoppingCardFragment shoppingCardFragment = new ShoppingCardFragment();

        getSupportFragmentManager().beginTransaction()            //Inspiration and solution found https://codinginflow.com/tutorials/android/fragment-to-fragment-communication-with-shared-viewmodel
                .add(R.id.nav_shopping_cart_fragment, shoppingCardFragment)// new ShoppingCardFragment())
                .commit();

        btn_cncl = findViewById(R.id.btn_shopping_cancle);
        btn_cncl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shoppingViewModel.deleteAllProductsInPurchase();

                finish();
            }
        });


        btn_buy = findViewById(R.id.btn_shopping_buy);
        btn_buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                if (shoppingViewModel.getAllProductsinPurchase().getValue().size()!=0)
                {
                    for (Product p : shoppingViewModel.getAllProductsinPurchase().getValue())
                    {
                        mPurchace.addProductToPurchace(p);
                    }

                    shoppingViewModel.insertPurchace_CloudFirestore(rustur, currentTutorClicked, mPurchace);
                    shoppingViewModel.deleteAllProductsInPurchase();
                    finish();
                }
                else
                {
                    Toast.makeText(v.getContext(), R.string.noProductsInBasket,Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onclickAddProduct(Product product)
    {
        for (Product p : shoppingViewModel.getAllProductsinPurchase().getValue())
        {
            if (p.getProductName().equals(product.getProductName()))
            {
                p.setQuantity(p.getQuantity()+1);
                p.setPrice(product.getPrice()*p.getQuantity());
                shoppingViewModel.updateProductInPurchase(p);
                updateCartUI(intemsInCart+1, totalSum+product.getPrice());
                return;
            }
        }
        shoppingViewModel.insertProductInPurchase(product);
        updateCartUI(intemsInCart+1, totalSum+product.getPrice());
    }

    @Override
    public void onClickRemoveProduct(Product product)
    {
        for (Product p : shoppingViewModel.getAllProductsinPurchase().getValue())
        {
            if (p.getProductName().equals(product.getProductName()) && p.getQuantity()>=2)
            {
                p.setQuantity(p.getQuantity()-1);
                p.setPrice(product.getPrice()*p.getQuantity());
                shoppingViewModel.updateProductInPurchase(p);
                updateCartUI(intemsInCart-1, totalSum-product.getPrice());
                return;
            }
            else if (p.getProductName().equals(product.getProductName()) && intemsInCart>=1)
            {
                updateCartUI(intemsInCart-product.getQuantity(), totalSum-product.getPrice());
            }
        }
        shoppingViewModel.deleteProductInPurchase(product);
    }


    @Override
    public void swipeToDelete(Product product) {
        shoppingViewModel.deleteProductInPurchase(product);
        updateCartUI(intemsInCart-product.getQuantity(), totalSum-product.getPrice());
    }


    @Override
    public void onclickTrashRemoveProduct(Product product)
    {
        shoppingViewModel.deleteProductInPurchase(product);
        updateCartUI(intemsInCart-product.getQuantity(), totalSum-product.getPrice());
    }

    private BroadcastReceiver RepositoriesDeclared = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent)
        {

        }
    };

    public ShoppingService getShoppingService_fromShoppingActivity()
    {
        return this.shoppingService;
    }

    @Override
    public void onBackPressed()
    {
        String currentLabel = navController.getCurrentDestination().getLabel().toString();

        switch (currentLabel) {
            case "fragment_view_products": {
                navController.navigate(R.id.action_viewProductsFragment_to_viewCategoriesFragment);
                break;
            }
            case "fragment_view_categories":
            {
                finish();
                break;
            }
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle saveInstanceState) {
        super.onSaveInstanceState(saveInstanceState);

        saveInstanceState.putSerializable(getString(R.string.savedInstanceState_ShoppingActivity), shoppingViewModel);
    }

    //enable stethos tool for inspecting database on device / emulator through chrome
    private void enableStethos()
    {

           /* Stetho initialization - allows for debugging features in Chrome browser
           See http://facebook.github.io/stetho/ for details
           1) Open chrome://inspect/ in a Chrome browse
           2) select 'inspect' on your app under the specific device/emulator
           3) select resources tab
           4) browse database tables under Web SQL
         */

        Stetho.initialize(Stetho.newInitializerBuilder(this)
                .enableDumpapp(
                        Stetho.defaultDumperPluginsProvider(this))
                .enableWebKitInspector(
                        Stetho.defaultInspectorModulesProvider(this))
                .build());

        /* end Stethos */
    }

    private void updateCartUI(int items, double sum)
    {
        this.intemsInCart = items;
        this.totalSum = sum;

        textView_itemsInCart.setText(getText(R.string.items_in_cart)+Integer.toString(intemsInCart));
        textView_totalSum.setText(getText(R.string.total_sum)+Double.toString(totalSum));
    }
}
