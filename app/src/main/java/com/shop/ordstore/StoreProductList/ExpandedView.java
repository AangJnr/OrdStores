package com.shop.ordstore.storeProductList;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.shop.ordstore.utilities.DatabaseHelper;
import com.shop.ordstore.utilities.DateUtil;
import com.shop.ordstore.R;
import com.shop.ordstore.userClasses.OrderTile;
import com.shop.ordstore.userClasses.OrdersFragment;
import com.squareup.picasso.Picasso;


/**
 * Created by AangJnr on 9/20/16.
 */
public class ExpandedView extends AppCompatActivity {
    String get_photo, get_product_code, get_product_details, get_product_name, get_price;
    DatabaseHelper ordersDataBaseHelper;
    private BottomSheetBehavior mBottomSheetBehavior;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();








        get_product_name = intent.getStringExtra("product_name");
        get_price = intent.getStringExtra("product_price");
        get_product_code = intent.getStringExtra("product_code");
        get_photo = intent.getStringExtra("product_photo");
        get_product_details = intent.getStringExtra("product_details");



        setContentView(R.layout.product_expand_layout);



        TextView _code = (TextView) findViewById(R.id.exp_product_code);
        ImageView _photo = (ImageView) findViewById(R.id.exp_product_photo);
        TextView _details = (TextView) findViewById(R.id.exp_product_details);

       /* Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);*/



        // set callback for changes
       /* mBottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                fab.animate().scaleX(1 - slideOffset).scaleY(1 - slideOffset).setDuration(0).start();
            }
        });*/








        _code.setText(get_product_code);
        _details.setText(get_product_details);

        Picasso.with(getApplicationContext()).load(get_photo).into(_photo);
        ordersDataBaseHelper = new DatabaseHelper(ExpandedView.this);











    }


    public void addOrderAlert(){
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(ExpandedView.this);
        View mView = layoutInflaterAndroid.inflate(R.layout.custom_dialog, null);
        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(ExpandedView.this);
        alertDialogBuilderUserInput.setView(mView);

        final EditText edittext = (EditText) mView.findViewById(R.id.userInputDialog);

        alertDialogBuilderUserInput
                .setCancelable(true);
        final AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
        alertDialogAndroid.show();

        mView.findViewById(R.id.positive_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String _quantity = edittext.getText().toString();

                try {
                    int quantity = Integer.parseInt(_quantity);

                    if(quantity != 0 && quantity < 100) {
                        addOrder(get_product_name, get_product_code, String.valueOf(quantity), get_price, "", "",
                                "", get_photo, StoreProductListActivity.merchantID(), DateUtil.getDateInMillisToString());
                        alertDialogAndroid.dismiss();
                        OrdersFragment.adapter.notifyDataSetChanged();
                    }else{
                        Toast.makeText(ExpandedView.this, "Quantity cannot be 0",
                                Toast.LENGTH_SHORT).show();

                    }
                } catch (NumberFormatException e) {

                    Toast.makeText(ExpandedView.this, "Please enter a valid quantity",
                            Toast.LENGTH_SHORT).show();
                }



            }
        });


        mView.findViewById(R.id.negative_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialogAndroid.dismiss();
            }
        });
    }


    public void addOrder(String product_name, String code, String quantity, String price, String size, String color,
                         String extraInfo, String photo, String merchant_uid, String date){


        OrderTile dataToAdd = new OrderTile(product_name, code, quantity, price, size, color,
                extraInfo, photo, merchant_uid, date);
        ordersDataBaseHelper.addOrdertile(dataToAdd);

        OrdersFragment.orderTile.add(dataToAdd);
        OrdersFragment.adapter.notifyDataSetChanged();

        Toast.makeText(ExpandedView.this, "Order added.",
                Toast.LENGTH_SHORT).show();

    }

}
