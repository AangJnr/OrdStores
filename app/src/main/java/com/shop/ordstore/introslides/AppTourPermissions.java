package com.shop.ordstore.introslides;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;

import com.amqtech.permissions.helper.objects.Permission;
import com.amqtech.permissions.helper.objects.Permissions;
import com.amqtech.permissions.helper.objects.PermissionsActivity;
import com.github.paolorotolo.appintro.AppIntro2;
import com.shop.ordstore.R;
import com.shop.ordstore.SignUpClasses.SignUpActivity;


/**
 * Created by AangJnr on 7/24/16.
 */


public class AppTourPermissions extends AppIntro2 {


    public static int OVERLAY_PERMISSION_REQ_CODE = 1234;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addSlide(SampleSlide.newInstance(R.layout.slide_1));

        addSlide(SampleSlide.newInstance(R.layout.slide_2));

        addSlide(SampleSlide.newInstance(R.layout.slide_3));

        addSlide(SampleSlide.newInstance(R.layout.slide_4));


        showStatusBar(false);
        setZoomAnimation();

    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        // Do something when users tap on Skip button.
        currentFragment.onDestroy();

        if (android.os.Build.VERSION.SDK_INT >= 23) {
            //Do something
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                    checkSelfPermission(Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED &&
                    checkSelfPermission(Manifest.permission.WRITE_CONTACTS) == PackageManager.PERMISSION_GRANTED &&
                    checkSelfPermission(Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED &&
                    checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                    Settings.canDrawOverlays(this)
                    ) {

                loadNextActivity();
            } else {

                launchMultiplePermsFlow();

            }

        } else {


            loadNextActivity();
        }
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        currentFragment.onDestroy();
        // Do something when users tap on Done button.
        if (android.os.Build.VERSION.SDK_INT >= 23) {
            //Do something
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                    checkSelfPermission(Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED &&
                    checkSelfPermission(Manifest.permission.WRITE_CONTACTS) == PackageManager.PERMISSION_GRANTED &&
                    checkSelfPermission(Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED &&
                    checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                    Settings.canDrawOverlays(this)
                    ) {

                loadNextActivity();
            } else {

                launchMultiplePermsFlow();

            }

        } else {


            loadNextActivity();
        }
    }

    @Override
    public void onSlideChanged(@Nullable Fragment oldFragment, @Nullable Fragment newFragment) {
        super.onSlideChanged(oldFragment, newFragment);
//        oldFragment.onDestroy();
        // Do something when the slide changes.
    }

    public void launchMultiplePermsFlow() {


        Permission writeContacts = new Permission(Permissions.WRITE_CONTACTS,
                Permissions.WRITE_CONTACTS_EXPLANATION);
        Permission readContacts = new Permission(Permissions.READ_CONTACTS,
                Permissions.READ_CONTACTS_EXPLANATION);
        Permission location = new Permission(Permissions.ACCESS_FINE_LOCATION,
                Permissions.ACCESS_FINE_LOCATION_EXPLANATION);
        Permission phone = new Permission(Permissions.READ_PHONE_STATE,
                Permissions.READ_PHONE_STATE_EXPLANATION);
        Permission sms = new Permission(Permissions.SEND_SMS,
                Permissions.SEND_SMS_EXPLANATION);
        Permission storage = new Permission(Permissions.READ_EXTERNAL_STORAGE,
                Permissions.WRITE_EXTERNAL_STORAGE_EXPLANATION);


        new PermissionsActivity(getBaseContext())
                .withAppName(getResources().getString(R.string.app_name))
                .withPermissions(writeContacts, readContacts, location, sms, storage)
                .withPermissionFlowCallback(new PermissionsActivity.PermissionFlowCallback() {
                    @Override
                    public void onPermissionGranted(Permission permission) {
                        // I want to show a toast here
                        // if the permission was granted


                        //loadNextActivity();
                        someMethod();

                    }

                    @Override
                    public void onPermissionDenied(Permission permission) {
                        // I want to show a toast here
                        // if the permission was denied

                        AlertDialog.Builder builder1 = new AlertDialog.Builder(getApplicationContext(), R.style.AppTheme);
                        builder1.setMessage("OrdStores requires these permissions in order to function normally");
                        builder1.setCancelable(false);

                        builder1.setPositiveButton(
                                "I'm in",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        launchMultiplePermsFlow();

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
                })

                .setBackgroundColor(Color.parseColor("#388E3C"))
                .setStatusBarColor(Color.parseColor("#388E3C"))
                .setBarColor(Color.parseColor("#388E3C"))
                .launch();
    }

    private void loadNextActivity() {
        Intent intent = new Intent(this, SignUpActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }


    @TargetApi(Build.VERSION_CODES.M)
    public void someMethod() {
        if (!Settings.canDrawOverlays(this)) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName()));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivityForResult(intent, OVERLAY_PERMISSION_REQ_CODE);
        } else {

            loadNextActivity();

        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == OVERLAY_PERMISSION_REQ_CODE) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (Settings.canDrawOverlays(this)) {
                    // SYSTEM_ALERT_WINDOW permission granted...
                    loadNextActivity();

                } else {

                    finish();
                    //someMethod();

                }
            }
        }
    }


    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onResume() {
        super.onResume();
    }


    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}




