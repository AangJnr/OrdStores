package com.shop.ordstore.userClasses;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by AangJnr on 9/22/16.
 */
public class UserTokenService extends FirebaseInstanceIdService {
    DatabaseReference userDatabase;
    FirebaseAuth mAuth;

    public void onCreate() {
        super.onCreate();
        // This method is invoked when the service is called.
        userDatabase = FirebaseDatabase.getInstance().getReference().child("users");
        mAuth = FirebaseAuth.getInstance();

    }

    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.


            String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        if(mAuth.getCurrentUser() != null) {
            sendRegistrationToServer(refreshedToken);
        }
    }

    private void sendRegistrationToServer(String token) {


        userDatabase.child(MainActivity.get_user_uid()).child("token_id").setValue(token)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                    }
                });
    }




}
