package com.shop.ordstore.signUpClasses;

import com.shop.ordstore.R;

/**
 * Created by AangJnr on 9/1/16.
 */
public enum SignupEnum {

    USER(R.string.user, R.layout.activity_signup_user),
    MERCHANT(R.string.merchant, R.layout.activity_sign_up_merchant);


    private int mTitleResId;
    private int mLayoutResId;

    SignupEnum(int titleResId, int layoutResId) {
        mTitleResId = titleResId;
        mLayoutResId = layoutResId;
    }

    public int getTitleResId() {
        return mTitleResId;
    }

    public int getLayoutResId() {
        return mLayoutResId;
    }

}