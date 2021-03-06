package com.shop.ordstore.utilities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.shop.ordstore.merchantClasses.MerchantMainActivity;
import com.shop.ordstore.R;
import com.shop.ordstore.signUpClasses.SignUpActivity;
import com.shop.ordstore.userClasses.MainActivity;
import com.shop.ordstore.introslides.AppTourPermissions;

/**
 * Created by AangJnr on 4/9/16.
 */
public class SplashActivity extends Activity {


    Thread splashTread;
    boolean userFirstRun, merchantFirstRun, isFirstInstall;
    SharedPreferences sharedpreferences;

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


        sharedpreferences = PreferenceManager.getDefaultSharedPreferences(SplashActivity.this);

        userFirstRun = sharedpreferences.getBoolean(ConstantStrings.IS_USER_SIGNED_IN, Boolean.FALSE);


        merchantFirstRun = sharedpreferences.getBoolean(ConstantStrings.IS_MERCHANT_SIGNED_IN, Boolean.FALSE);

        isFirstInstall = sharedpreferences.getBoolean(ConstantStrings.IS_FIRST_INSTALL, Boolean.TRUE);


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

                    if (userFirstRun && !isFirstInstall) {

                        Intent intent = new Intent(SplashActivity.this,
                                MainActivity.class);
                        startActivity(intent);


                    } else if (merchantFirstRun && !isFirstInstall) {

                        Intent intent = new Intent(SplashActivity.this,
                                MerchantMainActivity.class);
                        startActivity(intent);
                    }
                    else if(!isFirstInstall){

                        Intent intent = new Intent(SplashActivity.this,
                                SignUpActivity.class);
                        startActivity(intent);

                    }else {

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
