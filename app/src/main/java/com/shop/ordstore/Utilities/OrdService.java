package com.shop.ordstore.utilities;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.support.design.widget.TextInputLayout;
import android.support.v7.view.ContextThemeWrapper;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.shop.ordstore.R;

import jp.co.recruit_lifestyle.android.floatingview.FloatingViewListener;
import jp.co.recruit_lifestyle.android.floatingview.FloatingViewManager;

/**
 * Created by AangJnr on 8/14/16.
 */

public class OrdService extends Service implements FloatingViewListener {
    public static String pdt_code, pdt_name, pdt_price, pdt_photo, pdt_details, codeHolder, productStoreName;
    Animation translate;
    boolean showing = true;
    WindowManager.LayoutParams params;
    ImageView ord_icon;
    FloatingViewManager mFloatingViewManager;
    private TextInputLayout codeLayout;
    private TextInputLayout quantityLayout;
    private LinearLayout extrasLayout;
    private TextInputLayout detailsLayout;
    private String fashion;
    private String edibles;
    private EditText codeEditText;
    private EditText quantityEditText;
    private EditText sizeEditText;
    private EditText itemColorEditText;
    private EditText details;
    private WindowManager windowManager;
    private boolean mRunning;
    private LayoutInflater inflater;

    @Override
    public IBinder onBind(Intent intent) {
        // Not used
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mRunning = false;


    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (!mRunning) {
            mRunning = true;

            // Let it continue running until it is stopped.
            windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
            DisplayMetrics displaymetrics = new DisplayMetrics();
            windowManager.getDefaultDisplay().getMetrics(displaymetrics);
            int height = displaymetrics.heightPixels;


            params = new WindowManager.LayoutParams(
                    WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.TYPE_PHONE,
                    WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN,
                    PixelFormat.TRANSLUCENT);

            params.gravity = Gravity.BOTTOM;


            final ContextThemeWrapper ctx = new ContextThemeWrapper(this, R.style.AppTheme);
            final LayoutInflater inflater = LayoutInflater.from(ctx);
            final View floating_ord = inflater.inflate(R.layout.ord, null);


            mFloatingViewManager = new FloatingViewManager(this, this);
            mFloatingViewManager.setFixedTrashIconImage(R.drawable.close);


            final FloatingViewManager.Options options = new FloatingViewManager.Options();
            options.shape = FloatingViewManager.SHAPE_CIRCLE;
            options.floatingViewY = height / 2;


            mFloatingViewManager.addViewToWindow(floating_ord, options);


            final View lay = inflater.inflate(R.layout.bubble_layout, (ViewGroup) floating_ord, false);
            //Animation animation = AnimationUtils.loadAnimation(getBaseContext(), R.anim.translate);
            //lay.startAnimation(animation);


            final EditText editText = (EditText) lay.findViewById(R.id.dialog_size);


            if (editText.hasFocus()) {
                final KeyEvent event = null;
                if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
                    windowManager.removeView(lay);

                }

            }

            floating_ord.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (showing) {
                        windowManager.addView(lay, params);
                        showSoftKeyboard();
                        showing = false;

                    } else {

                        windowManager.removeView(lay);
                        hideSoftKeyboard();
                        showing = true;
                    }
                }
            });
        }
        return START_NOT_STICKY;
    }


    @Override
    public void onDestroy() {
        //super.onDestroy();
        if (mFloatingViewManager != null) {
            mFloatingViewManager.removeAllViewToWindow();
            mRunning = false;
            mFloatingViewManager = null;
        }
    }


    public void showSoftKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) this.getSystemService(this.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    public void hideSoftKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) this.getSystemService(this.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_IMPLICIT_ONLY);

    }


    @Override
    public void onFinishFloatingView() {
        stop();
    }

    public void stop() {
        stopSelf();
        mRunning = false;
    }


}