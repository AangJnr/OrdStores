package com.shop.ordstore.introslides;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.TypefaceSpan;
import android.view.View;


import com.heinrichreimersoftware.materialintro.app.IntroActivity;
import com.heinrichreimersoftware.materialintro.app.NavigationPolicy;
import com.heinrichreimersoftware.materialintro.app.OnNavigationBlockedListener;
import com.heinrichreimersoftware.materialintro.slide.FragmentSlide;
import com.heinrichreimersoftware.materialintro.slide.SimpleSlide;
import com.heinrichreimersoftware.materialintro.slide.Slide;
import com.shop.ordstore.R;
import com.shop.ordstore.signUpClasses.SignUpActivity;


/**
 * Created by AangJnr on 7/24/16.
 */


public class AppTourPermissions extends IntroActivity {


    String[] PERMISSIONS = {Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.SEND_SMS,
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.WRITE_CONTACTS,
                            Manifest.permission.READ_CONTACTS };

    int OVERLAY_PERMISSION_REQ_CODE = 10;
    Boolean hasAllPermissions = null;





    @Override
    protected void onCreate(Bundle savedInstanceState) {

         /* Enable/disable fullscreen */
        setFullscreen(true);

        super.onCreate(savedInstanceState);

        setButtonBackVisible(false);
        setButtonNextVisible(false);
        setPageScrollDuration(500);

        setButtonCtaTintMode(BUTTON_CTA_TINT_MODE_BACKGROUND);

        TypefaceSpan labelSpan = new TypefaceSpan(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ? "sans-serif-medium" : "sans serif");
        SpannableString label = SpannableString.valueOf("Request Permissions");
        label.setSpan(labelSpan, 0, label.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        setButtonCtaLabel(label);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setPageScrollInterpolator(android.R.interpolator.fast_out_slow_in);
        }

        addSlide(new SimpleSlide.Builder()
                .title(R.string.slide_1_title)
                .description(R.string.slide_1_desc)
                .image(R.drawable.icon_splash_screen)
                .background(R.color.color_slide_intro)
                .backgroundDark(R.color.color_dark_slide_intro)
                .layout(R.layout.slide_1)
                .build());

        addSlide(new SimpleSlide.Builder()
                .title(R.string.slide_2_title)
                .description(R.string.slide_2_desc)
                .image(R.drawable.no_orders)
                .background(R.color.color_slide_intro)
                .backgroundDark(R.color.color_dark_slide_intro)
                .layout(R.layout.slide_1)
                .build());

        addSlide(new SimpleSlide.Builder()
                .title(R.string.slide_3_title)
                .description(R.string.slide_3_desc)
                .image(R.drawable.nostarred)
                .background(R.color.color_slide_intro)
                .backgroundDark(R.color.color_dark_slide_intro)
                .layout(R.layout.slide_1)
                .build());

       final Slide permissionsSlide = new FragmentSlide.Builder()
                .background(R.color.color_slide_intro)
                .backgroundDark(R.color.color_dark_slide_intro)
                .fragment(R.layout.permissions_fragment_layout, R.style.AppTheme)
        .canGoBackward(true)
                .build();
        addSlide(permissionsSlide);

        autoplay(3000);





        addOnPageChangeListener(new ViewPager.OnPageChangeListener(){
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {


            }
            @Override
            public void onPageSelected(int position) {

                Slide slide = getSlide(position);
                if(slide == permissionsSlide){

                    cancelAutoplay();
                    setButtonCtaVisible(true);
                }else{

                    setButtonCtaVisible(false);

                }
            }
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });



        addOnNavigationBlockedListener(new OnNavigationBlockedListener() {
            @Override
            public void onNavigationBlocked(int position, int direction) {
                View contentView = findViewById(android.R.id.content);
                if (contentView != null) {
                    Slide slide = getSlide(position);

                    if (slide == permissionsSlide) {



                            if(android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){


                                if(launchMultiplePermissions() && !Settings.canDrawOverlays(AppTourPermissions.this)) {

                                    checkOverlayPermission();
                                }
                            else if(Settings.canDrawOverlays(AppTourPermissions.this)){


                                    loadNextActivity();
                                }

                            }else{

                                loadNextActivity();

                            }






                    }
                }
            }
        });


        setNavigationPolicy(new NavigationPolicy() {
            @Override
            public boolean canGoForward(int position) {
                Slide slide = getSlide(position);

                if(slide == permissionsSlide){
                    return false;

                }else {

                    return true;
                }
            }

            @Override
            public boolean canGoBackward(int position) {
                return true;
            }
        });





    }


    @TargetApi(Build.VERSION_CODES.M)
    public void checkOverlayPermission() {
        if (!Settings.canDrawOverlays(this)) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName()));
            //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivityForResult(intent, OVERLAY_PERMISSION_REQ_CODE);
        } else {

            loadNextActivity();

        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == OVERLAY_PERMISSION_REQ_CODE) {
            if (resultCode == PackageManager.PERMISSION_GRANTED && Settings.canDrawOverlays(this)) {
                // SYSTEM_ALERT_WINDOW permission granted...
                loadNextActivity();


            }else{


                AlertDialog.Builder builder1 = new AlertDialog.Builder(AppTourPermissions.this, R.style.DialogTheme);
                builder1.setMessage("To get most out of OrdStores, you need to provide overlay permission. If you select Nay, OrdStores will quit.");
                builder1.setCancelable(false);

                builder1.setPositiveButton(
                        "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                                checkOverlayPermission();

                            }
                        });

                builder1.setNegativeButton(
                        "Nay",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                                finish();
                            }
                        });

                AlertDialog alert11 = builder1.create();
                alert11.show();
            }
        }
    }




    public  boolean launchMultiplePermissions(){

        for(String permission : PERMISSIONS){
            if(!hasPermission(AppTourPermissions.this, permission)){


                if (ActivityCompat.shouldShowRequestPermissionRationale(AppTourPermissions.this, permission)) {
                    ActivityCompat.requestPermissions(AppTourPermissions.this, PERMISSIONS, 30);
                } else {
                    ActivityCompat.requestPermissions(AppTourPermissions.this, PERMISSIONS, 30);
                }

                return false;
            }


        }

        return true;
    }


    public static boolean hasPermission(Context context, String PERMISSION){

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null) {

            if (ActivityCompat.checkSelfPermission(context, PERMISSION) != PackageManager.PERMISSION_GRANTED) {

                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, PERMISSION)){


                }
                return false;
            }

        }
        return true;
    }


    private void loadNextActivity() {
        Intent intent = new Intent(this, SignUpActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }








    @Override
    public void onResume() {





        super.onResume();
    }


    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }











    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permission, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permission, grantResults);



        if(grantResults[0] != PackageManager.PERMISSION_GRANTED ) {
            AlertDialog.Builder builder1 = new AlertDialog.Builder(AppTourPermissions.this, R.style.DialogTheme);
            builder1.setMessage("To get most out of OrdStores, you need to provide all permissions. If you select Nay, OrdStores will quit");
            builder1.setCancelable(false);

            builder1.setPositiveButton(
                    "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                            launchMultiplePermissions();

                        }
                    });

            builder1.setNegativeButton(
                    "Nay",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                            finish();
                        }
                    });

            AlertDialog alert11 = builder1.create();
            alert11.show();

        }else if(!Settings.canDrawOverlays(AppTourPermissions.this)){

            if(AppTourPermissions.this != null) {
                final AlertDialog.Builder builder1 = new AlertDialog.Builder(AppTourPermissions.this, R.style.DialogTheme);
                builder1.setMessage("Please grant OrdStores permission to draw over other apps? ");
                builder1.setCancelable(false);

                builder1.setPositiveButton(
                        "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                checkOverlayPermission();

                            }
                        });

                builder1.setNegativeButton(
                        "CANCEL",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();

                                builder1.setMessage("To get most out of OrdStores, you need to provide overlay permission. If you select Nay, OrdStores will quit.");
                                builder1.setCancelable(false);

                                builder1.setPositiveButton(
                                        "I'm in",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                checkOverlayPermission();

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
                        });

                AlertDialog alert11 = builder1.create();
                alert11.show();
            }

        }

    }







}




