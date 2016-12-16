package com.shop.ordstore.merchantClasses;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.shop.ordstore.utilities.DatabaseHelper;
import com.shop.ordstore.utilities.DateUtil;
import com.shop.ordstore.R;

import java.util.Random;

/**
 * Created by AangJnr on 9/19/16.
 */


public class MerchantService extends Service {
    DatabaseReference merchantDatabase;
    DatabaseReference pendingOrdersRef;
    DatabaseHelper merchantDataBaseHelper;
    private ChildEventListener mChildEventListener;
    FirebaseAuth mAuth;

    PendingOrder dataToAdd;
    String Merchant_Prefs_Name = "merchant";
    String Merchant_Id = "uidKey";
    String Merchant_Name = "nameKey";
    String Merchant_Email = "emailKey";
    String Merchant_Phone = "phoneKey";
    boolean isActivityRunning;

    static String _name, _email, _phone, _uid;

    public void onCreate() {
        super.onCreate();
        // This method is invoked when the service is called.
        merchantDatabase = FirebaseDatabase.getInstance().getReference().child("merchants");
        merchantDataBaseHelper = new DatabaseHelper(this);
        mAuth = FirebaseAuth.getInstance();

        SharedPreferences sharedpreferences = getSharedPreferences(Merchant_Prefs_Name,
                Context.MODE_PRIVATE);

        if (sharedpreferences.contains(Merchant_Id)) {
            _uid = sharedpreferences.getString(Merchant_Id, "Null");

        }


    }





    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }



    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // TODO Auto-generated method stub

        if(mAuth.getCurrentUser() != null) {

            Log.i("Merchant Service start", _uid);

            pendingOrdersRef = merchantDatabase.child(_uid).child("pending_orders");

            ChildEventListener childEventListener = new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                            String po_order_id = dataSnapshot.getKey(); //orderId

                            PendingOrder po_value = dataSnapshot.getValue(PendingOrder.class);

                            boolean exists = merchantDataBaseHelper.pendingOrderExists(po_value.getUserTimestamp());
                            boolean existedBefore = merchantDataBaseHelper.pendingOrderExisted(po_order_id);




                            Log.i("Pending order status", String.valueOf(exists));
                            Log.i("Pending order existed", String.valueOf(existedBefore));



                            if(!existedBefore && !exists ) {

                                po_value.setTimestamp(DateUtil.getDateInMillisToString());

                                merchantDataBaseHelper.addPendingOrder(po_value);
                                merchantDataBaseHelper.addPendingOrderToHistory(po_value.getTimestamp(), po_order_id);


                                dataToAdd = po_value;

                                SharedPreferences sp = getSharedPreferences("MerchantActivity", MODE_PRIVATE);
                                isActivityRunning = sp.getBoolean("active", false);

                                if(PendingOrdersFragment.emptyView != null && PendingOrdersFragment.adapter != null){
                                PendingOrdersFragment.emptyView.setVisibility(View.GONE);
                                PendingOrdersFragment.pending_orders.add(0, po_value);
                                PendingOrdersFragment.adapter.notifyItemInserted(0);
                                }

                                Log.i("Merchant Service", _uid);
                                Log.i("po.id", po_order_id);
                                Log.i("po username", po_value.getUserName());
                                Log.i("po userTime", po_value.getUserTimestamp());
                                Log.i("po pdt name", po_value.getProductName());
                                Log.i("po pdt code", po_value.getProductCode());
                                Log.i("Time is", po_value.getTimestamp());


                                sendNotification("New order from " + po_value.getUserName(),
                                        po_value.getProductName());


                            }
                        }

                        @Override
                        public void onChildChanged(DataSnapshot dataSnapshot, String s) {


                        }

                        @Override
                        public void onChildRemoved(DataSnapshot dataSnapshot) {

                        }

                        @Override
                        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    };

            pendingOrdersRef.addChildEventListener(childEventListener);

            mChildEventListener = childEventListener;

        }

        return START_STICKY;


    }


    private void sendNotification(String title, String messageBody) {
        final int notifyID = generateRandom();
        Intent intent = new Intent(this, MerchantMainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, notifyID , intent,
                PendingIntent.FLAG_ONE_SHOT|PendingIntent.FLAG_UPDATE_CURRENT);

        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.ic_notification_large);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_shopping_cart_white_24dp)
                .setLargeIcon(bm)
                .setContentTitle(title)
                .setAutoCancel(true)
                .setContentText(messageBody)
                .setContentIntent(pendingIntent)
                .setSound(defaultSoundUri);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(notifyID, notificationBuilder.build());
    }

    public int generateRandom(){
        Random random = new Random();
        return random.nextInt(9999 - 1000) + 1000;
    }

    @Override
    public void onDestroy() {

        cleanupListener();


    }

    public void cleanupListener() {
        if (mChildEventListener != null) {
            pendingOrdersRef.removeEventListener(mChildEventListener);
        }
    }

}
