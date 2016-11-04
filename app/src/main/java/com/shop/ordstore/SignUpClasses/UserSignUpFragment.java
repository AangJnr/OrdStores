package com.shop.ordstore.SignUpClasses;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.shop.ordstore.UserClasses.User;
import com.shop.ordstore.UserClasses.MainActivity;


/**
 * Created by AangJnr on 9/2/16.
 */
public class UserSignUpFragment extends Fragment {
    public static final String Prefs_Name = "user";
    public static final String UserId = "uidKey";
    public static final String Name = "nameKey";
    public static final String Email = "emailKey";
    public static final String Phone = "phoneKey";

    Boolean isUserFragment = true;

    View rootView;
    EditText _emailText, _passwordText, _nameText, _phone_noText;
    TextInputLayout email_layout, password_layout, name_layout, phone_no_layout;
    Boolean pendingIntroAnimation;
    TextView bottomText;
    TextView bottomTextQuerry;
    Animation fadeIn, slideUp, fadeOut;
    FloatingActionButton login_fab;
    ImageView ord, basket;
    boolean sign_in_activated = true;
    SharedPreferences user_prefs;
    SharedPreferences.Editor editor;
    boolean isPasswordVisible = false;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference users_database;
    private String name, email, password, phone;

    ImageView visibility;
    private final TextWatcher mTextEditorWatcher = new TextWatcher() {

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        public void afterTextChanged(Editable s) {
            if (s.length() == 0)
                password_layout.setError("Not Entered");
            else if (s.length() < 6)
                password_layout.setError("EASY");
            else if (s.length() < 10)
                password_layout.setError("MEDIUM");
            else if (s.length() < 15)
                password_layout.setError("STRONG");
            else
                password_layout.setError("STRONGEST");

            if (s.length() == 20)
                password_layout.setError("Password Max Length Reached");
        }
    };


    public UserSignUpFragment() {
        super();

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.activity_signup_user, container, false);

        mAuth = FirebaseAuth.getInstance();
        users_database = FirebaseDatabase.getInstance().getReference();


        fadeIn = AnimationUtils.loadAnimation(getContext(), R.anim.fade_in);
        fadeOut = AnimationUtils.loadAnimation(getContext(), R.anim.fade_out);
        slideUp = AnimationUtils.loadAnimation(getContext(), R.anim.slide_up);


        name_layout = (TextInputLayout) rootView.findViewById(R.id.user_username_layout);
        email_layout = (TextInputLayout) rootView.findViewById(R.id.user_email_layout);
        password_layout = (TextInputLayout) rootView.findViewById(R.id.user_password_layout);
        phone_no_layout = (TextInputLayout) rootView.findViewById(R.id.user_phone_no_layout);

        _nameText = (EditText) rootView.findViewById(R.id.user_username);
        _emailText = (EditText) rootView.findViewById(R.id.user_email);
        _passwordText = (EditText) rootView.findViewById(R.id.user_password);
        _phone_noText = (EditText) rootView.findViewById(R.id.user_phone);

        bottomTextQuerry = (TextView) rootView.findViewById(R.id.bottom_text_querry);


        login_fab = (FloatingActionButton) rootView.findViewById(R.id.user_login_fab);
        bottomText = (TextView) rootView.findViewById(R.id.bottom_text);

        ord = (ImageView) rootView.findViewById(R.id.ord);
        basket = (ImageView) rootView.findViewById(R.id.basket);


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


        login_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = _nameText.getText().toString();
                email = _emailText.getText().toString();
                password = _passwordText.getText().toString();
                phone = _phone_noText.getText().toString();


                if (sign_in_activated) {

                    signIn();

                } else if (!sign_in_activated) {

                    _passwordText.addTextChangedListener(mTextEditorWatcher);
                    signUp();

                }
            }
        });




        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        // Always call the superclass so it can save the view hierarchy state

        super.onCreate(savedInstanceState);


        if (savedInstanceState == null) {
            pendingIntroAnimation = true;
        }


        if (pendingIntroAnimation) {
            pendingIntroAnimation = false;
            startContentAnimation();
        }




        visibility = (ImageView) getActivity().findViewById(R.id.user_visibility_toggle);
        visibility.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int start, end;

                if (!isPasswordVisible) {
                    visibility.setImageResource(R.drawable.ic_visibility_white_24dp);
                    start = _passwordText.getSelectionStart();
                    end = _passwordText.getSelectionEnd();
                    _passwordText.setTransformationMethod(null);
                    _passwordText.setSelection(start, end);
                    isPasswordVisible = true;

                } else {
                    visibility.setImageResource(R.drawable.ic_visibility_off_white_24dp);
                    start = _passwordText.getSelectionStart();
                    end = _passwordText.getSelectionEnd();
                    _passwordText.setTransformationMethod(new PasswordTransformationMethod());
                    _passwordText.setSelection(start, end);
                    isPasswordVisible = false;
                }


            }
        });



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


        bottomTextQuerry.startAnimation(fadeIn);
        //bottomTextQuerry.startAnimation(slideUp);
        bottomTextQuerry.setVisibility(View.VISIBLE);

        bottomText.startAnimation(fadeIn);
        bottomText.setVisibility(View.VISIBLE);


    }

    private void startContentAnimation() {
        Animation anim = AnimationUtils.loadAnimation(getActivity(), R.anim.translate_splash_screen);
        anim.reset();

        basket.clearAnimation();
        basket.startAnimation(anim);

        anim = AnimationUtils.loadAnimation(getActivity(), R.anim.right_slide);
        anim.reset();

        ord.clearAnimation();
        ord.startAnimation(anim);


    }

    public void signUp() {
        sign_in_activated = false;

        if (!signUpValidate()) {
            Toast.makeText(getActivity(), "Please provide valid info", Toast.LENGTH_LONG).show();
            return;
        }

        login_fab.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();


        // TODO: Implement your own signup logic here.


        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (!task.isSuccessful()) {
                            progressDialog.dismiss();
                            onSignupFailed();

                        } else {
                            progressDialog.dismiss();

                            FirebaseUser user = mAuth.getCurrentUser();

                            user_prefs = getActivity().getSharedPreferences(Prefs_Name,
                                    Context.MODE_PRIVATE);

                            if (user != null) {
                                final String uid = user.getUid();

                                final ProgressDialog progressDialog = new ProgressDialog(getActivity());
                                progressDialog.setIndeterminate(true);
                                progressDialog.setTitle("Authenticated!");
                                progressDialog.setMessage("Signing up...");
                                progressDialog.show();


                                //Sign Up

                                    User _user = new User(name, email, phone);
                                    // Write a userdata to the database


                                    users_database.child("users").child(uid).setValue(_user)
                                            .addOnCompleteListener(getActivity(), new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {

                                                    if (task.isSuccessful()) {


                                                        editor = user_prefs.edit();
                                                        editor.putString(UserId, uid);
                                                        editor.putString(Name, name);
                                                        editor.putString(Email, email);
                                                        editor.putString(Phone, phone);
                                                        editor.putBoolean("IsSignedIn", true);
                                                        editor.commit();
                                                        progressDialog.dismiss();
                                                        onSignupSuccess();
                                                    } else {
                                                        progressDialog.dismiss();
                                                        Toast.makeText(getActivity(), "Error connecting to server", Toast.LENGTH_SHORT).show();
                                                    }

                                                }
                                            });



                            } else {
                                // Toast.makeText(getActivity(), "Oops! Something unexpected happened", Toast.LENGTH_LONG).show();


                            }

                        }


                    }
                });


    }

    public void signIn() {
        sign_in_activated = true;


        if (!signInValidate()) {
            Toast.makeText(getActivity(), "Please check email and password", Toast.LENGTH_LONG).show();
            return;
        }

        login_fab.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();


        // TODO: Implement your own authentication logic here.
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {


                        if (!task.isSuccessful()) {
                            progressDialog.dismiss();
                            onSignInFailed();

                        } else {
                            progressDialog.dismiss();

                            FirebaseUser user = mAuth.getCurrentUser();

                            user_prefs = getActivity().getSharedPreferences(Prefs_Name,
                                    Context.MODE_PRIVATE);

                            if (user != null) {
                                final String uid = user.getUid();

                                final ProgressDialog progressDialog = new ProgressDialog(getActivity());
                                progressDialog.setIndeterminate(true);
                                progressDialog.setTitle("Authenticated!");
                                progressDialog.setMessage("Signing in...");
                                progressDialog.show();


                                //Sign In

                                    //Sign in

                                    users_database.child("users").child(uid).addListenerForSingleValueEvent(
                                            new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                    // Get user value
                                                    User user = dataSnapshot.getValue(User.class);


                                                    editor = user_prefs.edit();
                                                    editor.putString(UserId, uid);
                                                    editor.putString(Name, user.getName());
                                                    editor.putString(Email, user.getEmail());
                                                    editor.putString(Phone, user.getPhone());
                                                    editor.putBoolean("IsSignedIn", true);
                                                    editor.commit();
                                                    progressDialog.dismiss();
                                                    onSignInSuccess();
                                                }

                                                @Override
                                                public void onCancelled(DatabaseError databaseError) {
                                                    progressDialog.dismiss();
                                                    Toast.makeText(getActivity(), "Error connecting to server", Toast.LENGTH_SHORT).show();

                                                }
                                            });
                            } else {
                                // Toast.makeText(getActivity(), "Please sign up or sign in", Toast.LENGTH_LONG).show();


                            }

                        }
                    }
                });


    }

    public boolean signInValidate() {
        boolean valid = true;

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

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

    public void onSignInSuccess() {
        login_fab.setEnabled(true);
        getActivity().startActivity(new Intent(getActivity(), MainActivity.class));
        getActivity().finish();
         /* Intent intent = new Intent(getActivity(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
*/



    }

    public void onSignInFailed() {
        Toast.makeText(getActivity(), "Login failed", Toast.LENGTH_LONG).show();

        login_fab.setEnabled(true);
    }

    public void onSignupSuccess() {
        login_fab.setEnabled(true);
        getActivity().startActivity(new Intent(getActivity(), MainActivity.class));
        getActivity().finish();


    }

    public void onSignupFailed() {
        Toast.makeText(getActivity(), "Sign up failed", Toast.LENGTH_LONG).show();

        login_fab.setEnabled(true);
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onStop() {
        super.onStop();

    }


}
