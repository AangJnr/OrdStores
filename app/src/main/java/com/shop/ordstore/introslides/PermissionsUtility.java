package com.shop.ordstore.introslides;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;

import com.shop.ordstore.R;

/**
 * Created by aangjnr on 30/11/2016.
 */

class PermissionsUtility extends AppCompatActivity{

    public Context context;
    String[] PERMISSIONS;
    int REQUEST_CODE;






    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permission, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permission, grantResults);



    if(grantResults[0] != PackageManager.PERMISSION_GRANTED) {


        AlertDialog.Builder builder1 = new AlertDialog.Builder(getApplicationContext(), R.style.AppTheme);
        builder1.setMessage("To get most out of OrdStores, you need to provide all permissions");
        builder1.setCancelable(false);

        builder1.setPositiveButton(
                "I'm in",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //launchMultiplePermsFlow();

                    }
                });

        builder1.setNegativeButton(
                "Nay",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        finish();


                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
        }

    }






}
