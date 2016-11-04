package com.shop.ordstore.StoreProductList;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.shop.ordstore.R;
import com.shop.ordstore.Utils;
import com.squareup.picasso.Picasso;

/**
 * Created by AangJnr on 9/20/16.
 */
public class ExpandedView extends Activity {
    String get_photo, get_product_code, get_product_details;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_expand_layout);




        TextView _code = (TextView) findViewById(R.id.exp_product_code);
        ImageView _photo = (ImageView) findViewById(R.id.exp_product_photo);
        TextView _details = (TextView) findViewById(R.id.exp_product_details);
        Button _ok = (Button)  findViewById(R.id.exp_product_ok_button);


        Intent intent = getIntent();

        get_product_code = intent.getStringExtra("product_code");
        get_photo = intent.getStringExtra("product_photo");
        get_product_details = intent.getStringExtra("product_details");



        _code.setText(get_product_code);
        _details.setText(get_product_details);

        Picasso.with(getApplicationContext()).load(get_photo).into(_photo);




        _ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    finishAfterTransition();
                }else{
                    finish();

                }


            }
        });



    }
}
