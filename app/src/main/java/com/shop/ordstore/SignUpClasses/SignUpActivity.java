package com.shop.ordstore.signUpClasses;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shop.ordstore.R;
import com.shop.ordstore.merchantClasses.Merchant;
import com.shop.ordstore.merchantClasses.MerchantMainActivity;
import com.shop.ordstore.userClasses.MainActivity;
import com.shop.ordstore.userClasses.User;
import com.shop.ordstore.utilities.ConstantStrings;
import com.shop.ordstore.utilities.Utils;

/**
 * Created by AangJnr on 9/1/16.
 */
public class SignUpActivity extends AppCompatActivity implements TextView.OnEditorActionListener {
    View rootView;
    TextInputLayout email_layout, password_layout, name_layout, phone_no_layout;
    Boolean pendingIntroAnimation;
    TextView bottomText;
    TextView bottomTextQuerry;
    Animation fadeIn, slideUp, fadeOut;
    TextView signIn;
    RelativeLayout login_layout;
    LinearLayout signIn_layout;

    ImageView ord, basket;
    boolean sign_in_activated = true;
    SharedPreferences sharedPreference;
    SharedPreferences.Editor editor;
    ProgressDialog progressDialog;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference ordstores_database;
    private String name, email, password, phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow(); // in Activity's onCreate() for instance
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        setContentView(R.layout.activity_signup_user);


        progressDialog = new ProgressDialog(SignUpActivity.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);


        mAuth = FirebaseAuth.getInstance();
        ordstores_database = FirebaseDatabase.getInstance().getReference();
        sharedPreference = PreferenceManager.getDefaultSharedPreferences(SignUpActivity.this);


        fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        fadeOut = AnimationUtils.loadAnimation(this, R.anim.fade_out);
        slideUp = AnimationUtils.loadAnimation(this, R.anim.slide_up);

        signIn = (TextView) findViewById(R.id.sign_in_text);

        name_layout = (TextInputLayout) findViewById(R.id.user_username_layout);
        email_layout = (TextInputLayout) findViewById(R.id.user_email_layout);
        password_layout = (TextInputLayout) findViewById(R.id.user_password_layout);
        phone_no_layout = (TextInputLayout) findViewById(R.id.user_phone_no_layout);


        bottomTextQuerry = (TextView) findViewById(R.id.bottom_text_querry);


        login_layout = (RelativeLayout) findViewById(R.id.button_lay);
        signIn_layout = (LinearLayout) findViewById(R.id.sign_in_layout);

        bottomText = (TextView) findViewById(R.id.bottom_text);

        ord = (ImageView) findViewById(R.id.ord);
        basket = (ImageView) findViewById(R.id.basket);


        bottomText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (name_layout.getVisibility() == view.GONE)

                {
                    activateSignUpScreen();

                } else if (name_layout.getVisibility() == view.VISIBLE) {

                    activateSignInScreen();
                }

            }
        });


        signIn_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (Utils.checkInternetConnection(SignUpActivity.this)) {
                    name = name_layout.getEditText().getText().toString();
                    email = email_layout.getEditText().getText().toString();
                    password = password_layout.getEditText().getText().toString();
                    phone = phone_no_layout.getEditText().getText().toString();


                    if (sign_in_activated) {

                        if (!signInValidate()) {
                            Toast.makeText(SignUpActivity.this, "Please provide valid info", Toast.LENGTH_LONG).show();

                        } else signIn();

                    } else {

                        if (!signUpValidate()) {
                            Toast.makeText(SignUpActivity.this, "Please provide valid info", Toast.LENGTH_LONG).show();

                        } else signUp();
                    }
                } else
                    Toast.makeText(SignUpActivity.this, "Please ensure you have an Internet Connection and try again", Toast.LENGTH_SHORT).show();
            }
        });


        if (savedInstanceState == null) {
            pendingIntroAnimation = true;
        }


        if (pendingIntroAnimation) {
            pendingIntroAnimation = false;
            startContentAnimation();

            if (!sign_in_activated)
            password_layout.getEditText().addTextChangedListener(mTextEditorWatcher);

            if(sign_in_activated)
                password_layout.getEditText().addTextChangedListener(mTextEditorWatcher_2);
        }


        name_layout.getEditText().setOnEditorActionListener(this);
        email_layout.getEditText().setOnEditorActionListener(this);
        password_layout.getEditText().setOnEditorActionListener(this);
        phone_no_layout.getEditText().setOnEditorActionListener(this);


        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user != null) {

                    if (sign_in_activated) { //SignIn. Put logic to handle whether its a user or a merchant

                        final String uid = user.getUid();

                        ordstores_database.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                DataSnapshot merchant_ref = dataSnapshot.child("merchants").child(uid);
                                DataSnapshot user_ref = dataSnapshot.child("users").child(uid);

                                Log.i("SignIn Merchant ", String.valueOf(merchant_ref.exists()));
                                Log.i("SignIn User ", String.valueOf(user_ref.exists()));


                                if (merchant_ref.exists() && user_ref.exists()) {//Show dialog, ask user to sign in as a user or as a merchant
                                    if(progressDialog != null) progressDialog.dismiss();



                                    final AlertDialog.Builder logout_alert_builder = new AlertDialog.Builder(SignUpActivity.this, R.style.AppAlertDialog)
                                            .setTitle("Choose a sign in option")
                                            .setMessage("Sign in as?")
                                            .setCancelable(false)
                                            .setNeutralButton("USER", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.dismiss();
                                                    signInAsUser(uid);

                                                }
                                            })
                                            .setPositiveButton("MERCHANT", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.dismiss();
                                                    signInAsMerchant(uid);
                                                }
                                            });
                                    AlertDialog logout_dialog = logout_alert_builder.create();
                                    logout_dialog.show();

                                } else if (!merchant_ref.exists() && user_ref.exists()){//user only has a user account, sign the user in
                                    signInAsUser(uid);
                                }else if (merchant_ref.exists() && !user_ref.exists()){

                                    signInAsMerchant(uid);
                                }

                                else{

                                    Toast.makeText(SignUpActivity.this, "Please Sign Up!", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });


                    } else { //SignUp. Sign up user and get creds.

                        final String uid = user.getUid();


                        progressDialog.setTitle("Authenticated!");
                        progressDialog.setMessage("Signing up...");


                        //Sign Up

                        User _user = new User(name, email, phone, "");
                        // Write a userdata to the database


                        ordstores_database.child("users").child(uid).setValue(_user)
                                .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        if (task.isSuccessful()) {
                                            editor = sharedPreference.edit();
                                            editor.putString(ConstantStrings.USER_UID, uid);
                                            editor.putString(ConstantStrings.USER_NAME, name);
                                            editor.putString(ConstantStrings.USER_EMAIL, email);
                                            editor.putString(ConstantStrings.USERS_PHONE, phone);
                                            editor.putBoolean(ConstantStrings.IS_USER_SIGNED_IN, true);
                                            editor.apply();
                                            progressDialog.dismiss();

                                            //onSignupSuccess();

                                        } else {
                                            progressDialog.dismiss();
                                            Toast.makeText(SignUpActivity.this, "Error connecting. Try again later", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                });
                    }

                } else {
                    Toast.makeText(SignUpActivity.this, "Seems you are signed out. Please sign in", Toast.LENGTH_SHORT).show();
                    Log.d("SIGN UP ACTIVITY", "onAuthStateChanged:signed_out");
                }
            }

        };
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);


    }

    void activateSignUpScreen() {
        sign_in_activated = false;

        bottomTextQuerry.setVisibility(View.GONE);
        bottomTextQuerry.setText("Have an account?");

        bottomText.setVisibility(View.GONE);
        bottomText.setText("SIGN IN");
        signIn.setText("SIGN UP");


        name_layout.startAnimation(fadeIn);
        name_layout.setVisibility(View.VISIBLE);

        phone_no_layout.startAnimation(fadeIn);
        phone_no_layout.setVisibility(View.VISIBLE);

        bottomTextQuerry.startAnimation(fadeIn);
        bottomTextQuerry.setVisibility(View.VISIBLE);

        bottomText.startAnimation(fadeIn);
        bottomText.setVisibility(View.VISIBLE);

        password_layout.startAnimation(fadeIn);
        password_layout.setVisibility(View.VISIBLE);


    }

    void activateSignInScreen() {
        sign_in_activated = true;

        name_layout.startAnimation(fadeOut);
        name_layout.setVisibility(View.GONE);

        phone_no_layout.startAnimation(fadeOut);
        phone_no_layout.setVisibility(View.GONE);


        password_layout.startAnimation(slideUp);


        bottomTextQuerry.setVisibility(View.GONE);
        bottomTextQuerry.setText("Don't have an account?");

        bottomText.setVisibility(View.GONE);

        bottomText.setText("SIGN UP");
        signIn.setText("SIGN IN");


        bottomTextQuerry.startAnimation(fadeIn);
        //bottomTextQuerry.startAnimation(slideUp);
        bottomTextQuerry.setVisibility(View.VISIBLE);

        bottomText.startAnimation(fadeIn);
        bottomText.setVisibility(View.VISIBLE);


    }

    private void startContentAnimation() {
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.translate_splash_screen);
        anim.reset();

        basket.clearAnimation();
        basket.startAnimation(anim);

        anim = AnimationUtils.loadAnimation(this, R.anim.right_slide);
        anim.reset();

        ord.clearAnimation();
        ord.startAnimation(anim);


    }

    public void signUp() {
        sign_in_activated = false;
        signIn.setEnabled(false);


        progressDialog.setMessage("Creating account.");
        progressDialog.show();


        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (!task.isSuccessful()) {
                            progressDialog.dismiss();
                            Toast.makeText(SignUpActivity.this, "Couldn't sign in. Try again later", Toast.LENGTH_LONG).show();
                            signIn.setEnabled(true);

                        }
                    }
                });


    }

    public void signIn() {
        sign_in_activated = true;
        signIn.setEnabled(false);

        progressDialog.setMessage("Authenticating...");
        progressDialog.show();


        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {


                        if (!task.isSuccessful()) {
                            progressDialog.dismiss();
                            Toast.makeText(SignUpActivity.this, "Error connecting. Try again later", Toast.LENGTH_SHORT).show();

                        }
                    }
                });


    }

    public boolean signInValidate() {
        boolean valid = true;

        String email = email_layout.getEditText().getText().toString();
        String password = password_layout.getEditText().getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            email_layout.setError("enter a valid email address");
            valid = false;
        } else {
            email_layout.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            password_layout.setError("6+ alphanumeric characters");
            valid = false;
        } else {
            password_layout.setError(null);
        }

        return valid;
    }

    public boolean signUpValidate() {
        boolean valid = true;


        if (name.isEmpty() || name.length() < 3) {
            name_layout.setError("enter your full name");
            valid = false;
        } else {
            name_layout.setError(null);
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            email_layout.setError("enter a valid email address");
            valid = false;
        } else {
            email_layout.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            password_layout.setError("6+ alphanumeric characters");
            valid = false;
        } else {
            password_layout.setError(null);
        }

        if (phone.isEmpty() || phone.length() < 10) {
            phone_no_layout.setError("enter valid phone number");
            valid = false;
        } else {
            phone_no_layout.setError(null);
        }

        return valid;
    }




    @Override
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0);

        if (Utils.checkInternetConnection(SignUpActivity.this)) {
            name = name_layout.getEditText().getText().toString();
            email = email_layout.getEditText().getText().toString();
            password = password_layout.getEditText().getText().toString();
            phone = phone_no_layout.getEditText().getText().toString();


            if (sign_in_activated) {

                if (!signInValidate()) {
                    Toast.makeText(SignUpActivity.this, "Please provide valid info", Toast.LENGTH_LONG).show();

                } else signIn();

            } else if (!sign_in_activated) {

                if (!signUpValidate()) {
                    Toast.makeText(SignUpActivity.this, "Please provide valid info", Toast.LENGTH_LONG).show();

                } else signUp();

            }


        } else
            Toast.makeText(SignUpActivity.this, "Please ensure you have an Internet Connection and try again", Toast.LENGTH_SHORT).show();
        return true;
    }


    public void onBackPressed() {
        super.onBackPressed();

        //finish();
        moveTaskToBack(true);

    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    public void showSoftKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) this.getSystemService(this.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    private final TextWatcher mTextEditorWatcher = new TextWatcher() {

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {


        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        public void afterTextChanged(Editable s) {
            if (s.length() == 0) {
                password_layout.setError("Not Entered");

                try {
                    login_layout.setBackgroundColor(ContextCompat.getColor(SignUpActivity.this, R.color.divider));
                } catch (NullPointerException e) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                        login_layout.setBackgroundColor(ContextCompat.getColor(SignUpActivity.this, R.color.divider));

                    else
                        login_layout.setBackgroundColor(ContextCompat.getColor(SignUpActivity.this, R.color.divider));
                }


            } else if (s.length() < 6) {
                password_layout.setError("EASY");

                try {
                    login_layout.setBackgroundColor(ContextCompat.getColor(SignUpActivity.this, R.color.divider));
                } catch (NullPointerException e) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                        login_layout.setBackgroundColor(ContextCompat.getColor(SignUpActivity.this, R.color.divider));

                    else
                        login_layout.setBackgroundColor(ContextCompat.getColor(SignUpActivity.this, R.color.divider));
                }

            } else if (s.length() == 6 && android.util.Patterns.EMAIL_ADDRESS.matcher(email_layout.getEditText().getText().toString()).matches()) {

                try {
                    login_layout.setBackgroundColor(ContextCompat.getColor(SignUpActivity.this, R.color.colorAccent));
                } catch (NullPointerException e) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                        login_layout.setBackgroundColor(ContextCompat.getColor(SignUpActivity.this, R.color.colorAccent));

                    else
                        login_layout.setBackgroundColor(ContextCompat.getColor(SignUpActivity.this, R.color.colorAccent));
                }
            } else if (s.length() < 10)
                password_layout.setError("MEDIUM");
            else if (s.length() < 15)
                password_layout.setError("STRONG");
            else
                password_layout.setError("STRONGEST");

            if (s.length() == 20)
                password_layout.setError("Password Max Length Reached");
        }
    };

    private final TextWatcher mTextEditorWatcher_2 = new TextWatcher() {

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {


        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        public void afterTextChanged(Editable s) {
             if (s.length() == 6 && android.util.Patterns.EMAIL_ADDRESS.matcher(email_layout.getEditText().getText().toString()).matches()) {

                try {
                    login_layout.setBackgroundColor(ContextCompat.getColor(SignUpActivity.this, R.color.colorAccent));
                } catch (NullPointerException e) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                        login_layout.setBackgroundColor(ContextCompat.getColor(SignUpActivity.this, R.color.colorAccent));

                    else
                        login_layout.setBackgroundColor(ContextCompat.getColor(SignUpActivity.this, R.color.colorAccent));
                }
            }
        }
    };


    private void signInAsUser(final String uid) {
        if(progressDialog != null) progressDialog.dismiss();
        progressDialog.setTitle("Authenticated!");
        progressDialog.setMessage("Signing in...");
        progressDialog.show();

        ordstores_database.child("users").child(uid).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Get user value
                        User user = dataSnapshot.getValue(User.class);
                        editor = sharedPreference.edit();
                        editor.putString(ConstantStrings.USER_UID, uid);
                        editor.putString(ConstantStrings.USER_NAME, user.getName());
                        editor.putString(ConstantStrings.USER_EMAIL, user.getEmail());
                        editor.putString(ConstantStrings.USERS_PHONE, user.getPhone());
                        editor.putString(ConstantStrings.USER_PHOTO_URL, user.getPhoto());
                        editor.putBoolean(ConstantStrings.IS_USER_SIGNED_IN, true);
                        editor.apply();


                        Log.i("//", " ");
                        Log.i("Name", String.valueOf(user.getName()));
                        Log.i("Email", String.valueOf(user.getEmail()));
                        Log.i("Number", String.valueOf(user.getPhone()));
                        Log.i("Profile Photo", String.valueOf(user.getPhoto()));
                        Log.i("//", " ");



                        progressDialog.dismiss();
                        signIn.setEnabled(true);
                        startActivity(new Intent(SignUpActivity.this, MainActivity.class));
                        finish();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        progressDialog.dismiss();
                        Toast.makeText(SignUpActivity.this, "Error connecting to server", Toast.LENGTH_SHORT).show();

                    }
                });


    }

    private void signInAsMerchant(final String uid) {

        if(progressDialog != null) progressDialog.dismiss();
        progressDialog.setTitle("Authenticated!");
        progressDialog.setMessage("Hello! Please wait...");
        progressDialog.show();


        ordstores_database.child("merchants").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Merchant merchant = dataSnapshot.getValue(Merchant.class);

                if (merchant != null) {
                    editor = sharedPreference.edit();
                    editor.putString(ConstantStrings.MERCHANT_UID, uid);
                    editor.putString(ConstantStrings.MERCHANT_NAME, merchant.getName());
                    editor.putString(ConstantStrings.MERCHANT_EMAIL, merchant.getEmail());
                    editor.putString(ConstantStrings.MERCHANTS_PHONE, merchant.getPhone());
                    editor.putBoolean(ConstantStrings.IS_MERCHANT_SIGNED_IN, true);
                    editor.commit();
                    progressDialog.dismiss();
                    signIn.setEnabled(true);
                    startActivity(new Intent(SignUpActivity.this, MerchantMainActivity.class));
                    finish();}

                else{
                    progressDialog.dismiss();
                    Toast.makeText(SignUpActivity.this, "Please contact the OrdStores team", Toast.LENGTH_SHORT).show();



                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}



