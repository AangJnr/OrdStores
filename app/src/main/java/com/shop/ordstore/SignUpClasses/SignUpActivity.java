package com.shop.ordstore.SignUpClasses;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.shop.ordstore.R;
import com.shop.ordstore.UserClasses.OrdServiceRevised;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by AangJnr on 9/1/16.
 */
public class SignUpActivity extends AppCompatActivity {

    static Boolean isSignUpPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow(); // in Activity's onCreate() for instance
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
        setContentView(R.layout.activity_signup);


        ViewPager viewPager = (ViewPager) findViewById(R.id.signup_viewpager);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new UserSignUpFragment());
        adapter.addFrag(new MerchantSignUpFragment());
        viewPager.setAdapter(adapter);



        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                if(position == 0){

                    isSignUpPage = true;
                }else{

                    isSignUpPage = false;
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });




    }


///////////////////////////////

    public void onBackPressed() {
        super.onBackPressed();

        //finish();
        moveTaskToBack(true);

    }

    public void showSoftKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) this.getSystemService(this.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();


        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }


        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment) {
            mFragmentList.add(fragment);

        }
    }



    public static Boolean isSignUpPage(){

     Boolean yes = isSignUpPage;


        return yes;
    }




}
