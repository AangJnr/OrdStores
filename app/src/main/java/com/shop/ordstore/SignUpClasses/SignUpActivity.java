package com.shop.ordstore.signUpClasses;

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
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.shop.ordstore.utilities.DepthPageTransformer;
import com.shop.ordstore.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by AangJnr on 9/1/16.
 */
public class SignUpActivity extends AppCompatActivity {

    static Boolean isSignUpPage;
    LinearLayout dotsLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow(); // in Activity's onCreate() for instance
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
        setContentView(R.layout.activity_signup);


        ViewPager viewPager = (ViewPager) findViewById(R.id.signup_viewpager);
        viewPager.setPageTransformer(true, new DepthPageTransformer());

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new UserSignUpFragment());
        adapter.addFrag(new MerchantSignUpFragment());
        viewPager.setAdapter(adapter);

        final ImageView dot_1 =(ImageView) findViewById(R.id.dot_1);
        final ImageView dot_2 =(ImageView) findViewById(R.id.dot_2);



        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                if(position == 0){

                    isSignUpPage = true;
                    dot_1.setBackgroundResource(R.drawable.page_selected);
                    dot_2.setBackgroundResource(R.drawable.page_not_selected);

                }else{

                    isSignUpPage = false;
                    dot_2.setBackgroundResource(R.drawable.page_selected);
                    dot_1.setBackgroundResource(R.drawable.page_not_selected);
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
