package com.shop.ordstore.SignUpClasses;

import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by AangJnr on 9/1/16.
 */
public class SignUpAdapter extends PagerAdapter {
    EditText email_field, password_field, username_field;
    TextInputLayout email_layout, password_layout, name_layout;
    Button bottomButton;
    Button bottomText;
    TextView bottomTextQuerry;
    Animation fadeIn, slideUp, fadeOut;
    FloatingActionButton login_fab;

    private Context mContext;

    public SignUpAdapter(Context context) {
        mContext = context;
        //fadeIn = AnimationUtils.loadAnimation(mContext, R.anim.fade_in);
        //fadeOut = AnimationUtils.loadAnimation(mContext.getApplicationContext(), R.anim.fade_out);
        //slideUp = AnimationUtils.loadAnimation(mContext.getApplicationContext(), R.anim.slide_up);
    }


    @Override
    public Object instantiateItem(ViewGroup collection, int position) {
        SignupEnum signupEnum = SignupEnum.values()[position];
        LayoutInflater inflater = LayoutInflater.from(mContext);
        ViewGroup layout = (ViewGroup) inflater.inflate(signupEnum.getLayoutResId(), collection, false);
        collection.addView(layout);
        return layout;


    }


    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        collection.removeView((View) view);
    }

    @Override
    public int getCount() {
        return SignupEnum.values().length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        SignupEnum signupEnum = SignupEnum.values()[position];
        return mContext.getString(signupEnum.getTitleResId());
    }

    void activateSignUpScreen() {


        bottomTextQuerry.setVisibility(View.GONE);
        bottomTextQuerry.setText("Have an account?");

        bottomText.setVisibility(View.GONE);
        bottomText.setText("SIGN IN");


        name_layout.startAnimation(fadeIn);
        name_layout.setVisibility(View.VISIBLE);


        bottomTextQuerry.startAnimation(fadeIn);
        bottomTextQuerry.setVisibility(View.VISIBLE);

        bottomText.startAnimation(fadeIn);
        bottomText.setVisibility(View.VISIBLE);

        password_layout.startAnimation(fadeIn);
        //password_layout.setVisibility(View.VISIBLE);


    }


    void activateSignInScreen() {


        name_layout.startAnimation(fadeOut);
        name_layout.setVisibility(View.GONE);


        password_layout.startAnimation(slideUp);


        bottomTextQuerry.setVisibility(View.GONE);
        bottomTextQuerry.setText("Don't have an account?");

        bottomText.setVisibility(View.GONE);

        bottomText.setText("SIGN UP");

        bottomTextQuerry.startAnimation(fadeIn);
        bottomTextQuerry.setVisibility(View.VISIBLE);

        bottomText.startAnimation(fadeIn);
        bottomText.setVisibility(View.VISIBLE);


    }

}
