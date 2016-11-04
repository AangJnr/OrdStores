package com.shop.ordstore;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.shop.ordstore.MerchantClasses.MerchantMainActivity;
import com.shop.ordstore.UserClasses.MainActivity;
import com.shop.ordstore.introslides.AppTourPermissions;

/**
 * Created by AangJnr on 4/9/16.
 */
public class SplashActivity extends Activity {


    Thread splashTread;
    boolean user_first_run, merchant_first_run;
    SharedPreferences user_sharedpreferences, merchant_sharedpreferences;

    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        Window window = getWindow();
        window.setFormat(PixelFormat.RGBA_8888);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow(); // in Activity's onCreate() for instance
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
        setContentView(R.layout.splash_screen);

        String Prefs_Name = "user";
        String merchant_Prefs_Name = "merchant";


        user_sharedpreferences = getSharedPreferences(Prefs_Name, Context.MODE_PRIVATE);
        user_first_run = user_sharedpreferences.getBoolean("IsSignedIn", Boolean.FALSE);

        merchant_sharedpreferences = getSharedPreferences(merchant_Prefs_Name, Context.MODE_PRIVATE);
        merchant_first_run = merchant_sharedpreferences.getBoolean("IsSignedIn", Boolean.FALSE);


        StartAnimations();


    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        finish();
    }


    private void StartAnimations() {
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.alpha);
        anim.reset();
        RelativeLayout splash = (RelativeLayout) findViewById(R.id.splash_layout);
        splash.clearAnimation();
        splash.startAnimation(anim);

        anim = AnimationUtils.loadAnimation(this, R.anim.translate_splash_screen);
        anim.reset();
        ImageView iv = (ImageView) findViewById(R.id.splash_logo);
        iv.clearAnimation();
        iv.startAnimation(anim);

        splashTread = new Thread() {
            @Override
            public void run() {
                try {
                    int waited = 0;
                    // Splash screen pause time
                    while (waited < 2500) {
                        sleep(100);
                        waited += 100;
                    }

                    if (user_first_run) {

                        Intent intent = new Intent(SplashActivity.this,
                                MainActivity.class);
                        startActivity(intent);


                    } else if (merchant_first_run) {

                        Intent intent = new Intent(SplashActivity.this,
                                MerchantMainActivity.class);
                        startActivity(intent);
                    } else {

                        Intent intent = new Intent(SplashActivity.this,
                                AppTourPermissions.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(intent);
                    }


                    SplashActivity.this.finish();
                } catch (InterruptedException e) {
                    // do nothing
                } finally {
                    SplashActivity.this.finish();
                }

            }
        };
        splashTread.start();

    }

}
