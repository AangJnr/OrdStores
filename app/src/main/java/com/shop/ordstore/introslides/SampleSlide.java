package com.shop.ordstore.introslides;

/**
 * Created by AangJnr on 7/24/16.
 */

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.heinrichreimersoftware.materialintro.app.SlideFragment;
import com.shop.ordstore.R;

public class SampleSlide extends SlideFragment {

    private EditText fakeUsername;

    private EditText fakePassword;

    private Button fakeLogin;

    private boolean loggedIn = false;

    private Handler loginHandler = new Handler();


    public SampleSlide() {
        // Required empty public constructor
    }

    public static SampleSlide newInstance() {
        return new SampleSlide();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.permissions_fragment_layout, container, false);

       /* fakeUsername = (EditText) root.findViewById(R.id.fakeUsername);
        fakePassword = (EditText) root.findViewById(R.id.fakePassword);
        fakeLogin = (Button) root.findViewById(R.id.fakeLogin);

        fakeUsername.setEnabled(!loggedIn);
        fakePassword.setEnabled(!loggedIn);
        fakeLogin.setEnabled(!loggedIn);

        fakeLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fakeUsername.setEnabled(false);
                fakePassword.setEnabled(false);
                fakeLogin.setEnabled(false);
                fakeLogin.setText(R.string.label_fake_login_loading);

                loginHandler.postDelayed(loginRunnable, 2000);
            }
        });*/

        return root;
    }

    @Override
    public void onDestroy() {

        super.onDestroy();
    }

    @Override
    public boolean canGoForward() {
        return loggedIn;
    }
}