package com.shop.ordstore.merchantClasses;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.shop.ordstore.userClasses.MainActivity;
import com.shop.ordstore.utilities.ConstantStrings;
import com.shop.ordstore.utilities.DepthPageTransformer;
import com.shop.ordstore.userClasses.OrdServiceRevised;
import com.shop.ordstore.R;
import com.shop.ordstore.signUpClasses.SignUpActivity;
import com.shop.ordstore.utilities.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by AangJnr on 9/5/16.
 */

public class MerchantMainActivity extends AppCompatActivity {
    public static String POSITION = "POSITION";
    Toolbar toolbar;
    TabLayout tabLayout;
    ViewPager viewPager;
    DrawerLayout navDrawer;
    NavigationView navView;

    ActionBarDrawerToggle actionBarDrawerToggle;


    static String _name, _email, _phone, _uid;

    private static FirebaseAuth mAuth;
    DatabaseHelper dbHelper;

    private int[] tabIcons = {
            R.drawable.ic_stat_action_view_stream,
            R.drawable.ic_stat_action_view_module,
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_merchant);
        dbHelper = new DatabaseHelper(this);

        startService(new Intent(this, MerchantService.class));


        mAuth = FirebaseAuth.getInstance();


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setFitsSystemWindows(true);

        navDrawer = (DrawerLayout) findViewById(R.id.merchant_drawer_layout);
        //android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        actionBarDrawerToggle = new ActionBarDrawerToggle(
                this,
                navDrawer,
                toolbar,
                R.string.open,
                R.string.close
        )

        {
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                invalidateOptionsMenu();
                syncState();
            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
                syncState();
            }
        };
        navDrawer.addDrawerListener(actionBarDrawerToggle);



        //Set the custom toolbar
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.app_name);
        //getSupportActionBar().setIcon(R.drawable.ord_icon_no_border);

        actionBarDrawerToggle.syncState();




        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setPageTransformer(true, new DepthPageTransformer());
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        setupPagerIcons();



        navView = (NavigationView) findViewById(R.id.merchant_navigation_view);
        View hView = navView.getHeaderView(0);
        TextView name = (TextView) hView.findViewById(R.id.header_merchant_name);
        TextView email = (TextView) hView.findViewById(R.id.header_merchant_email);




        SharedPreferences sharedpreferences = PreferenceManager.getDefaultSharedPreferences(MerchantMainActivity.this);

        if (sharedpreferences.contains(ConstantStrings.MERCHANT_NAME)) {
            _name = sharedpreferences.getString(ConstantStrings.MERCHANT_NAME, null);
            name.setText(_name);

        }
        if (sharedpreferences.contains(ConstantStrings.MERCHANT_EMAIL)) {
            _email = sharedpreferences.getString(ConstantStrings.MERCHANT_EMAIL, null);
            email.setText(_email);



        }
        if (sharedpreferences.contains(ConstantStrings.MERCHANT_UID)) {
            _uid = sharedpreferences.getString(ConstantStrings.MERCHANT_UID, null);

        }
        if (sharedpreferences.contains(ConstantStrings.MERCHANTS_PHONE)) {
            _phone = sharedpreferences.getString(ConstantStrings.MERCHANTS_PHONE, null);

        }




        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                int alphaCurrent = (int) (255 - (128 * Math.abs(positionOffset)));
                int alphaNext = (int) (128 + (128 * Math.abs(positionOffset)));
                if (positionOffset != 0) {
                    switch (position) {
                        case 0:
                            tabLayout.getTabAt(0).getIcon().setAlpha(alphaCurrent);
                            tabLayout.getTabAt(1).getIcon().setAlpha(alphaNext);
                            break;
                        case 1:
                            tabLayout.getTabAt(1).getIcon().setAlpha(alphaCurrent);
                            tabLayout.getTabAt(0).getIcon().setAlpha(alphaNext);
                            break;
                    }

                }
            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    tabLayout.getTabAt(0).getIcon().setColorFilter(ContextCompat.getColor(getApplicationContext(),
                            R.color.white), PorterDuff.Mode.SRC_IN);


                    toolbar.setTitle("Pending Orders");

                } else if (position == 1) {

                    tabLayout.getTabAt(1).getIcon().setColorFilter(ContextCompat.getColor(getApplicationContext(),
                            R.color.white), PorterDuff.Mode.SRC_IN);
                    toolbar.setTitle("Products List");
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }


    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new PendingOrdersFragment());
        adapter.addFrag(new ProductsListFragment());
        viewPager.setAdapter(adapter);
    }


    private void setupPagerIcons() {

        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);


        tabLayout.getTabAt(0).getIcon().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
        tabLayout.getTabAt(1).getIcon().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.merchant_main_activity, menu);

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // This is required to make the drawer toggle work


    /*
     * if you have other menu items in your activity/toolbar
     * handle them here and return true
     */

        switch (item.getItemId()) {
            case R.id.logout: {

                navDrawer.closeDrawers();
                final AlertDialog.Builder logout_alert_builder = new AlertDialog.Builder(MerchantMainActivity.this, R.style.AppAlertDialog)
                        .setTitle("Logging you out!")
                        .setMessage("Are you sure?")
                        .setCancelable(true)
                        .setIcon(R.drawable.attention_96)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {


                                try {
                                    if(mAuth.getCurrentUser() != null){
                                        mAuth.signOut();
                                    }
                                    if(mAuth.getCurrentUser() == null){

                                        SharedPreferences sharedpreferences = PreferenceManager.
                                                getDefaultSharedPreferences(MerchantMainActivity.this);
                                        SharedPreferences.Editor editor = sharedpreferences.edit();
                                        editor.clear();
                                        editor.apply();

                                        editor.putBoolean(ConstantStrings.IS_FIRST_INSTALL, false);
                                        editor.apply();
                                        dbHelper.deleteAllMerchantTables();
                                        stopService(new Intent(MerchantMainActivity.this, OrdServiceRevised.class));
                                        startActivity(new Intent(MerchantMainActivity.this, SignUpActivity.class));
                                        finish();
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }



                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                                dialog.dismiss();
                            }
                        });
                AlertDialog logout_dialog = logout_alert_builder.create();
                logout_dialog.show();


                return true;
            }
        }


        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(POSITION, tabLayout.getSelectedTabPosition());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        viewPager.setCurrentItem(savedInstanceState.getInt(POSITION));
    }

    @Override
    public void onBackPressed() {
        if (viewPager.getCurrentItem() == 0) {

            super.onBackPressed();

        } else {

            viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);

        }
    }

    @Override
    public void onResume() {
        super.onResume();


        if (viewPager.getCurrentItem() == 0) {
            tabLayout.getTabAt(1).getIcon().setColorFilter(ContextCompat.getColor(getApplicationContext(),
                    R.color.text_white_75), PorterDuff.Mode.SRC_IN);


        }else if (viewPager.getCurrentItem() == 1){
            tabLayout.getTabAt(0).getIcon().setColorFilter(ContextCompat.getColor(getApplicationContext(),
                    R.color.text_white_75), PorterDuff.Mode.SRC_IN);

        }

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


    public static String getUid(){
        String uid = _uid;

        return uid;
    }


    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onStop() {
        super.onStop();

    }




    @Override
    public void onDestroy(){
        super.onDestroy();



    }
}




























