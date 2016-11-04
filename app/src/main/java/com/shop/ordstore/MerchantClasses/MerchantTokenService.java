package com.shop.ordstore.MerchantClasses;

import android.util.Log;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.shop.ordstore.DatabaseHelper;

/**
 * Created by AangJnr on 9/22/16.
 */
public class MerchantTokenService extends FirebaseInstanceIdService {
    DatabaseReference merchantDatabase;
    FirebaseAuth mAuth;


    public void onCreate() {
        super.onCreate();
        // This method is invoked when the service is called.
        mAuth = FirebaseAuth.getInstance();
        merchantDatabase = FirebaseDatabase.getInstance().getReference().child("merchants");



    }


    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();

        if(mAuth.getCurrentUser() != null) {
            sendRegistrationToServer(refreshedToken);
        }
    }

    private void sendRegistrationToServer(final String token) {
        merchantDatabase.child(MerchantMainActivity.getUid()).child("token_id").setValue(token)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        Log.i("Token is sent", token);

                    }
                });



    }



}
