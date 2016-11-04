package com.shop.ordstore.UserClasses;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.ActivityManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.shop.ordstore.DepthPageTransformer;
import com.shop.ordstore.R;
import com.shop.ordstore.SignUpClasses.SignUpActivity;
import com.shop.ordstore.DatabaseHelper;
import com.shop.ordstore.StoresListClasses.StoresListActivity;
import com.shop.ordstore.Utils;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private final static String FEED_URL = "https://raw.githubusercontent.com/AangJnr/cerebro_wallpapers/master/ls.json";
    private static final int ANIM_DURATION_TOOLBAR = 300;
    private static final int ANIM_DURATION_FAB = 400;
    public static String POSITION = "POSITION";
    final String Prefs_Name = "user";
    DatabaseHelper dbHelper;
    DrawerLayout navDrawer;
    NavigationView navView;
    Boolean pendingIntroAnimation;
    FloatingActionButton fab;
    ImageView ord;
    ActionBarDrawerToggle actionBarDrawerToggle;
    Toolbar toolbar;
    TabLayout tabLayout;
    ViewPager viewPager;
    TextView appbar_email, appbar_name;
    static String _name, _email, _phone, _user_uid;


    private int[] tabIcons = {
            R.drawable.ic_stat_action_view_stream,
            R.drawable.ic_stat_action_store,
    };

    private FirebaseAuth mAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        dbHelper = new DatabaseHelper(this);


        appbar_name = (TextView) findViewById(R.id.appbar_user_name);
        appbar_email = (TextView) findViewById(R.id.appbar_user_email);


        if (!isMyServiceRunning(OrdServiceRevised.class)) {
            startService(new Intent(this, OrdServiceRevised.class));
        }

        startService(new Intent(this, UserService.class));




        fab = (FloatingActionButton) findViewById(R.id.fab_main);

        ord = (ImageView) findViewById(R.id.ordLogo);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);

        //setSupportActionBar(toolbar);

        navDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
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
        navDrawer.setDrawerListener(actionBarDrawerToggle);

        //Set the custom toolbar
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        actionBarDrawerToggle.syncState();





        if (savedInstanceState == null) {
            pendingIntroAnimation = true;
        }


        if (pendingIntroAnimation != null && pendingIntroAnimation) {
            pendingIntroAnimation = false;
            startIntroAnimation();
        }


        //navDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navView = (NavigationView) findViewById(R.id.navigation_view);
        View hView = navView.getHeaderView(0);
        TextView name = (TextView) hView.findViewById(R.id.header_user_name);
        TextView email = (TextView) hView.findViewById(R.id.header_user_email);



       /* final CircleImageView profile_photo = (CircleImageView) hView.findViewById(R.id.profile_photo);

        Bitmap photo =((BitmapDrawable)profile_photo.getDrawable()).getBitmap();
        if(photo != null) {
            Palette.generateAsync(photo, new Palette.PaletteAsyncListener() {
                public void onGenerated(Palette palette) {
                    int mutedLight = palette.getMutedColor(getResources().getColor(R.color.colorPrimaryDark));
                    profile_photo.setBorderColor(mutedLight);
                }
            });
        }*/


        //String Prefs_Name = "user";
        String Name = "nameKey";
        String Email = "emailKey";
        String Uid = "uidKey";
        String Phone = "phoneKey";


        SharedPreferences sharedpreferences = getSharedPreferences(Prefs_Name,
                Context.MODE_PRIVATE);

        if (sharedpreferences.contains(Name)) {
            _name = sharedpreferences.getString(Name, "Null");
            name.setText(_name);
            appbar_name.setText(_name);
        }
        if (sharedpreferences.contains(Email)) {
            _email = sharedpreferences.getString(Email, "Null");
            email.setText(_email);
            appbar_email.setText(_email);

        }
        if (sharedpreferences.contains(Uid)) {
            _user_uid = sharedpreferences.getString(Uid, "Null");

        }
        if (sharedpreferences.contains(Phone)) {
            _phone = sharedpreferences.getString(Phone, "Null");

        }








        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setPageTransformer(true, new DepthPageTransformer());
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        setupPagerIcons();


        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_main);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            private int state = 0;
            private boolean isFloatButtonHidden = false;
            private int position = 0;

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                if (isFloatButtonHidden == false && state == 1 && positionOffset != 0.0) {
                    isFloatButtonHidden = true;

                    //hide floating action button
                    swappingAway();
                }

            }

            @Override
            public void onPageSelected(int position) {


                this.position = position;

                if (state == 2) {
                    //have end in selected tab
                    isFloatButtonHidden = false;
                    selectedTabs(position);

                }

                if (position == 0) {

                    fab.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (!isMyServiceRunning(OrdServiceRevised.class)) {
                                startService(new Intent(MainActivity.this, OrdServiceRevised.class));
                            } else

                                Toast.makeText(getApplicationContext(), "Bubble already added!", Toast.LENGTH_LONG).show();


                        }
                    });

                } else if (position == 1) {


                    fab.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent storesList = new Intent(MainActivity.this, StoresListActivity.class);
                            startActivity(storesList);

                        }
                    });
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                this.state = state;
                if (state == 0) {
                    isFloatButtonHidden = false;
                } else if (state == 2 && isFloatButtonHidden) {
                    //this only happen if user is swapping but swap back to current tab (cancel to change tab)
                    selectedTabs(position);
                }


            }


        });









        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.starred:
                        navDrawer.closeDrawers();
                        startActivity(new Intent(MainActivity.this, StarredOrders.class));
                        break;


                    case R.id.sign_out:
                        navDrawer.closeDrawers();
                        final AlertDialog.Builder logout_alert_builder = new AlertDialog.Builder(MainActivity.this)
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

                                                SharedPreferences sharedpreferences = getSharedPreferences(Prefs_Name,
                                                        Context.MODE_PRIVATE);
                                                SharedPreferences.Editor editor = sharedpreferences.edit();
                                                editor.clear();
                                                editor.commit();

                                                dbHelper.deleteAllUserTables();

                                            stopService(new Intent(MainActivity.this, OrdServiceRevised.class));
                                            startActivity(new Intent(MainActivity.this, SignUpActivity.class));
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
                        break;

                }


                return false;
            }
        });
    }

    private void setupPagerIcons() {

        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);


        tabLayout.getTabAt(0).getIcon().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
        tabLayout.getTabAt(1).getIcon().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new OrdersFragment(), "Orders");
        adapter.addFrag(new StoresFragment(), "Stores");
        viewPager.setAdapter(adapter);
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
    public boolean onTouchEvent(MotionEvent event) {


        return true;
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

    }

    private void startIntroAnimation() {
        fab.setTranslationY(2 * getResources().getDimensionPixelOffset(R.dimen.btn_fab_size));

        int actionbarSize = Utils.dpToPx(56);
        toolbar.setTranslationY(-actionbarSize);
        ord.setTranslationY(-actionbarSize);


        toolbar.animate()
                .translationY(0)
                .setDuration(ANIM_DURATION_TOOLBAR)
                .setStartDelay(300);
        ord.animate()
                .translationY(0)
                .setDuration(ANIM_DURATION_TOOLBAR)
                .setStartDelay(400)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        startContentAnimation();
                    }
                })
                .start();
    }

    private void startContentAnimation() {
        fab.animate()
                .translationY(0)
                .setInterpolator(new OvershootInterpolator(1.f))
                .setStartDelay(300)
                .setDuration(ANIM_DURATION_FAB)
                .start();

    }




    public boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    private void swappingAway() {
        fab.clearAnimation();
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.pop_down);
        fab.startAnimation(animation);
    }

    private void selectedTabs(int tab) {
        fab.show();

        //a bit animation of popping up.
        fab.clearAnimation();
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.pop_up);
        fab.startAnimation(animation);
        if (tab == 0) {
            fab.setImageResource(R.drawable.ic_stat_image_control_point_duplicate);

        } else if (tab == 1) {

            fab.setImageResource(R.drawable.ic_stat_action_store);


        }
        //you can do more task. for example, change color for each tabs, or custom action for each tabs.
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // This is required to make the drawer toggle work
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            //Toast.makeText(MainActivity.this, "Toggled! Now fix it!", Toast.LENGTH_SHORT).show();
            return true;
        }

    /*
     * if you have other menu items in your activity/toolbar
     * handle them here and return true
     */

        return super.onOptionsItemSelected(item);
    }

    /**
     * Sets up the Floating action button.
     */


    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

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

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
            // return null;
        }

    }





    public static String get_name(){
        String username = _name;
        return username;
    }

    public static String get_email(){
        String useremail = _email;
        return useremail;
    }

    public static String get_user_uid(){
        String uid = _user_uid;
        return uid;
    }

    public static String get_phone(){
        String phone = _phone;
        return phone;
    }
}


