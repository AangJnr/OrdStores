package com.shop.ordstore.userClasses;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.shop.ordstore.R;

/**
 * Created by aangjnr on 09/12/2016.
 */


public class Payments extends AppCompatActivity implements View.OnClickListener{

    LinearLayout visa, cash, mobile_money;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_layout);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setElevation(7);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        // Get access to the custom title view
        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);




        visa = (LinearLayout) findViewById(R.id.visa);
        mobile_money = (LinearLayout) findViewById(R.id.mobile_money);
        cash = (LinearLayout) findViewById(R.id.cash);

        visa.setOnClickListener(this);
        mobile_money.setOnClickListener(this);
        cash.setOnClickListener(this);

    }


    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case (R.id.visa):
                Toast.makeText(this, "Feature is under construction!", Toast.LENGTH_SHORT).show();
            break;

            case(R.id.cash):

                Toast.makeText(this, "Feature is under construction!", Toast.LENGTH_SHORT).show();
                break;


            case (R.id.mobile_money):
                Toast.makeText(this, "Feature is under construction!", Toast.LENGTH_SHORT).show();

                break;




        }


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // todo: goto back activity from here
                //super.onBackPressed();
                supportFinishAfterTransition();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
