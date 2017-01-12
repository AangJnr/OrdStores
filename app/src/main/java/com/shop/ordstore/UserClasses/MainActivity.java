package com.shop.ordstore.userClasses;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
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
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.BounceInterpolator;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnPausedListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.shop.ordstore.R;
import com.shop.ordstore.signUpClasses.SignUpActivity;
import com.shop.ordstore.utilities.ConstantStrings;
import com.shop.ordstore.utilities.DatabaseHelper;
import com.shop.ordstore.storesListClasses.StoresListActivity;
import com.shop.ordstore.utilities.Utils;
import com.shop.ordstore.utilities.ZoomOutPageTransformer;
import com.squareup.picasso.Picasso;


import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


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
    CircleImageView profilePhotoHolder;

    ActionBarDrawerToggle actionBarDrawerToggle;
    Toolbar toolbar;
    TabLayout tabLayout;
    ViewPager viewPager;
    //TextView appbar_email, appbar_name;
    static String _name, _email, _phone, _user_uid, _profilePhoto;

    String decodedString;
    int PERMISSION_STORAGE = 2;
    public static int RESULT_LOAD_IMG = 1;
    private StorageReference mStorageRef;
    private DatabaseReference usersDatabase;
    SharedPreferences sharedPreferences;


    private int[] tabIcons = {
            R.drawable.ic_stat_action_view_stream,
            R.drawable.ic_stat_action_store,
    };

    private FirebaseAuth mAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        usersDatabase = FirebaseDatabase.getInstance().getReference();
        mStorageRef = FirebaseStorage.getInstance().getReference();

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);

        mAuth = FirebaseAuth.getInstance();
        dbHelper = new DatabaseHelper(this);


        //appbar_name = (TextView) findViewById(R.id.appbar_user_name);
        //appbar_email = (TextView) findViewById(R.id.appbar_user_email);


        if (!isMyServiceRunning(OrdServiceRevised.class)) {
            startService(new Intent(this, OrdServiceRevised.class));
        }

        startService(new Intent(this, UserService.class));




        fab = (FloatingActionButton) findViewById(R.id.fab_main);


        toolbar = (Toolbar) findViewById(R.id.toolbar);


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
        navDrawer.addDrawerListener(actionBarDrawerToggle);

        //Set the custom toolbar
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.app_name);
        //getSupportActionBar().setIcon(R.drawable.ord_icon_no_border);

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



        profilePhotoHolder = (CircleImageView) hView.findViewById(R.id.profile_photo);

        /*Bitmap photo =((BitmapDrawable)profilePhotoHolder.getDrawable()).getBitmap();
        if(photo != null) {
            Palette.generateAsync(photo, new Palette.PaletteAsyncListener() {
                public void onGenerated(Palette palette) {
                    int mutedLight = palette.getMutedColor(getResources().getColor(R.color.colorPrimaryDark));
                    profilePhotoHolder.setBorderColor(mutedLight);
                }
            });
        }*/

        profilePhotoHolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!hasPermissions(getApplicationContext(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_STORAGE);
                }else {
                    loadImageFromGallery();

                }

            }
        });





        SharedPreferences sharedpreferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);

        if (sharedpreferences.contains(ConstantStrings.USER_NAME)) {
            _name = sharedpreferences.getString(ConstantStrings.USER_NAME, null);
            name.setText(_name);
            //appbar_name.setText(_name);
        }
        if (sharedpreferences.contains(ConstantStrings.USER_EMAIL)) {
            _email = sharedpreferences.getString(ConstantStrings.USER_EMAIL, null);
            email.setText(_email);
            //appbar_email.setText(_email);

        }
        if (sharedpreferences.contains(ConstantStrings.USER_UID)) {
            _user_uid = sharedpreferences.getString(ConstantStrings.USER_UID, null);

        }
        if (sharedpreferences.contains(ConstantStrings.USERS_PHONE)) {
            _phone = sharedpreferences.getString(ConstantStrings.USERS_PHONE, null);

        }
        if (sharedpreferences.contains(ConstantStrings.USER_PHOTO_URL)) {
            _profilePhoto = sharedpreferences.getString(ConstantStrings.USER_PHOTO_URL, null);

            Log.i("Profile Photo", _profilePhoto);

            if(_profilePhoto != null)
            Picasso.with(this).load(Uri.parse(_profilePhoto)).into(profilePhotoHolder);
        }


        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setPageTransformer(true, new ZoomOutPageTransformer());
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
                        final AlertDialog.Builder logout_alert_builder = new AlertDialog.Builder(MainActivity.this, R.style.AppAlertDialog)
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
                                                        getDefaultSharedPreferences(MainActivity.this);
                                                SharedPreferences.Editor editor = sharedpreferences.edit();
                                                editor.clear();
                                                editor.apply();

                                                editor.putBoolean(ConstantStrings.IS_FIRST_INSTALL, false);
                                                editor.apply();

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




                    case(R.id.payment):
                        navDrawer.closeDrawers();
                        startActivity(new Intent(MainActivity.this, Payments.class));

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
        //ord.setTranslationY(-actionbarSize);


        toolbar.animate()
                .translationY(0)
                .setDuration(ANIM_DURATION_TOOLBAR)
                .setStartDelay(300)
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
                .setInterpolator(new BounceInterpolator())
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


    public boolean hasPermissions(Context context, String permission) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null) {

            if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {

                if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)){


                }
                return false;
            }

        }
        return true;
    }


    public void loadImageFromGallery() {
        // Create intent to Open Image applications like Gallery, Google Photos
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // Start the Intent
        startActivityForResult(galleryIntent, RESULT_LOAD_IMG);

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //try {

            if (resultCode == RESULT_OK) {
                // When an Image is picked
                if (requestCode == RESULT_LOAD_IMG) {
                    // Get the Image from data

                    Uri selectedImage = data.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA
                    };

                    // Get the cursor
                    Cursor cursor = getContentResolver().query(selectedImage,
                            filePathColumn, null, null, null);
                    // Move to first row
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    decodedString = cursor.getString(columnIndex);
                    cursor.close();

                    if (decodedString != null) {
                        //Start Load image into view, upload to firebase and save url

                        final ProgressDialog progress = new ProgressDialog(MainActivity.this);
                        progress.setMessage("Please wait...");
                        progress.setCancelable(false);

                        profilePhotoHolder.setImageBitmap(BitmapFactory.decodeFile(decodedString));

                        if(_user_uid != null && !_user_uid.isEmpty()){
                            progress.show();



                            StorageReference profilePhotoStorage = mStorageRef.child("users").child(_user_uid).child("profilePhotoHolder.jpg");



                            Log.i("Storage Ref", String.valueOf(profilePhotoStorage));

                            profilePhotoStorage.putFile(selectedImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                    Toast.makeText(MainActivity.this, "Success", Toast.LENGTH_SHORT).show();

                                    Uri downloadUrl = taskSnapshot.getDownloadUrl();
                                    String photoUrl = String.valueOf(downloadUrl);

                                    SharedPreferences.Editor editor;
                                    editor = sharedPreferences.edit();
                                    editor.putString(ConstantStrings.USER_PHOTO_URL, photoUrl);
                                    editor.apply();


                                    usersDatabase.child("users").child(_user_uid).child("photo").setValue(photoUrl)
                                            .addOnCompleteListener(MainActivity.this, new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {

                                                    if (task.isSuccessful()) {
                                                        //hide progress
                                                        progress.dismiss();

                                                    }

                                                }
                                            });
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    //Toast.makeText(MainActivity.this, "Failure", Toast.LENGTH_SHORT).show();
                                    Log.i("PictureUploadActivity", e.toString());

                                    SharedPreferences.Editor editor;
                                    editor = sharedPreferences.edit();
                                    editor.putString(ConstantStrings.USER_PHOTO_URL, decodedString);
                                    editor.apply();
                                    progress.dismiss();

                                   // startUserActivity();
                                }
                            }).addOnPausedListener(new OnPausedListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onPaused(UploadTask.TaskSnapshot taskSnapshot) {

                                    taskSnapshot.getTask().cancel();
                                    Toast.makeText(MainActivity.this, "Image will be saved locally", Toast.LENGTH_SHORT).show();
                                    if(progress != null)progress.dismiss();
                                    //startUserActivity();

                                }
                            });
                        }
                    }
                }


            } else {
                Toast.makeText(this, "You haven't picked an Image",
                        Toast.LENGTH_LONG).show();
            }

         /*catch (Exception e) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG)
                    .show();
        }*/

    }



    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permission, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permission, grantResults);



        if(grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // Toast.makeText(this, permission[0], Toast.LENGTH_SHORT).show();

            if (requestCode == PERMISSION_STORAGE) {


                //resume tasks needing this permission
                loadImageFromGallery();

            }
        }

    }



    public static String getUserUid (){


        return _user_uid;


    }


}


