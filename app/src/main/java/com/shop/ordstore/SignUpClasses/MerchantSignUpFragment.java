/*
package com.shop.ordstore.signUpClasses;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.method.PasswordTransformationMethod;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
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
import com.shop.ordstore.merchantClasses.Merchant;
import com.shop.ordstore.merchantClasses.MerchantMainActivity;
import com.shop.ordstore.R;


*/
/**
 * Created by AangJnr on 9/2/16.
 *//*

public class MerchantSignUpFragment extends Fragment implements TextView.OnEditorActionListener {
    View rootView;
    EditText _emailText, _passwordText;
    FloatingActionButton login_fab;
    TextInputLayout email_layout, password_layout;
    String Merchant_Prefs_Name = "merchant";
    String Merchant_Id = "uidKey";
    String Merchant_Name = "nameKey";
    String Merchant_Email = "emailKey";
    String Merchant_Phone = "phoneKey";
    boolean isPasswordVisible = false;
    SharedPreferences merchant_prefs;
    SharedPreferences.Editor editor;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference merchants_database;
    private String email, password;
    Boolean isUserFragment = false;
    ImageView visibility2;

    public MerchantSignUpFragment() {
        super();


    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.activity_sign_up_merchant, container, false);
        mAuth = FirebaseAuth.getInstance();
        merchants_database = FirebaseDatabase.getInstance().getReference();
        login_fab = (FloatingActionButton) rootView.findViewById(R.id.merchant_login_fab);

        email_layout = (TextInputLayout) rootView.findViewById(R.id.email_layout);
        password_layout = (TextInputLayout) rootView.findViewById(R.id.password_layout);

        _emailText = (EditText) rootView.findViewById(R.id.email);
        _passwordText = (EditText) rootView.findViewById(R.id.password);




        login_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                email = _emailText.getText().toString();
                password = _passwordText.getText().toString();


                signIn();


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
        super.onActivityCreated(savedInstanceState);


        _emailText.setOnEditorActionListener(this);
        _passwordText.setOnEditorActionListener(this);

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);


    }


    public void signIn() {


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

                            merchant_prefs = getActivity().getSharedPreferences(Merchant_Prefs_Name,
                                    Context.MODE_PRIVATE);

                            if (user != null) {
                                final String uid = user.getUid();

                                final ProgressDialog progressDialog = new ProgressDialog(getActivity());
                                progressDialog.setIndeterminate(true);
                                progressDialog.setTitle("Authenticated!");
                                progressDialog.setMessage("Hello! Please wait...");
                                progressDialog.show();



                                merchants_database.child("merchants").child(uid).addListenerForSingleValueEvent(
                                        new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {

                                                Merchant merchant = dataSnapshot.getValue(Merchant.class);

                                                if(merchant != null){
                                                editor = merchant_prefs.edit();
                                                editor.putString(Merchant_Id, uid);
                                                editor.putString(Merchant_Name, merchant.getName());
                                                editor.putString(Merchant_Email, merchant.getEmail());
                                                editor.putString(Merchant_Phone, merchant.getPhone());
                                                editor.putBoolean("IsSignedIn", true);
                                                editor.commit();
                                                progressDialog.dismiss();
                                                onSignInSuccess();
                                                }


                                                else{

                                                    progressDialog.dismiss();
                                                    Toast.makeText(getActivity(), "Are you a merchant? " +
                                                            "Please sign up with OrdStores", Toast.LENGTH_SHORT).show();

                                                }

                                            }


                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {
                                                progressDialog.dismiss();
                                                Toast.makeText(getActivity(), "Please contact OrdStores", Toast.LENGTH_SHORT).show();

                                            }
                                        });


                            } else {
                                 Toast.makeText(getActivity(), "Please sign in", Toast.LENGTH_LONG).show();

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
            password_layout.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            password_layout.setError(null);
        }

        return valid;
    }




    @Override
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {

            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0);

            email = _emailText.getText().toString();
            password = _passwordText.getText().toString();


            signIn();

            return true;
    }
}
*/
