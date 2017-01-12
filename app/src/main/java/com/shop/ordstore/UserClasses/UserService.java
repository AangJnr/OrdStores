package com.shop.ordstore.userClasses;

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
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.shop.ordstore.utilities.ConstantStrings;
import com.shop.ordstore.utilities.DatabaseHelper;
import com.shop.ordstore.R;

import java.util.Random;

/**
 * Created by AangJnr on 9/22/16.
 */
public class UserService extends Service {
    DatabaseReference userDatabase;
    DatabaseReference orderStatusRef;
    DatabaseHelper ordersDataBaseHelper;
    String confirmation;
    private FirebaseAuth mAuth;
    private ChildEventListener mChildEventListener;
    String _userUid;
    Context context;




    public void onCreate() {
        super.onCreate();
        // This method is invoked when the service is called.
        mAuth = FirebaseAuth.getInstance();
        userDatabase = FirebaseDatabase.getInstance().getReference().child("users");
        ordersDataBaseHelper = new DatabaseHelper(this);

        SharedPreferences sharedpreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        if (sharedpreferences.contains(ConstantStrings.USER_UID)) {
            _userUid = sharedpreferences.getString(ConstantStrings.USER_UID, null);

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
        //Log.d(Utils.LogTag, "ChatHeadService.onStartCommand() -> startId=" + startId);

        if(mAuth.getCurrentUser() != null && _userUid != null) {

            Log.i("User Service start", _userUid);

            orderStatusRef = userDatabase.child(_userUid).child("order_status");

                    ChildEventListener childEventListener = new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                            String po_order_id = dataSnapshot.getKey(); //order uid
                            UserOrderStatus os_value = dataSnapshot.getValue(UserOrderStatus.class);

                            boolean exists = ordersDataBaseHelper.orderExists(os_value.getuserTimestamp());
                            boolean existedBefore = ordersDataBaseHelper.orderExisted(po_order_id);


                            Log.i("Order exist", String.valueOf(exists));
                            Log.i("Order existed", String.valueOf(existedBefore));

                        if(!existedBefore && exists) {

                            String is_confirmed = os_value.getConfirmed();
                            if (is_confirmed.equalsIgnoreCase("true")) {
                                confirmation = "approved";

                            } else if (is_confirmed.equalsIgnoreCase("false")) {
                                confirmation = "out of stock";

                            }
                            boolean _status = ordersDataBaseHelper.setConfirmationStatus(os_value.getorderCode(),
                                    os_value.getuserTimestamp(), is_confirmed);

                            Log.i("Order Status", os_value.getorderCode() + "is confirmed?" + String.valueOf(_status));

                            ordersDataBaseHelper.addOrderToHistory(os_value.getuserTimestamp(), po_order_id);

                            if (OrdersFragment.adapter != null)
                                OrdersFragment.adapter.notifyDataSetChanged();
                            Log.i("OrdersFrag adapter", "NOTIFIED");


                            sendNotification("New order status!", os_value.getorderCode() + " is " + confirmation);
                        }


                        }

                        @Override
                        public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                            String po_order_id = dataSnapshot.getKey(); //order uid
                            UserOrderStatus os_value = dataSnapshot.getValue(UserOrderStatus.class);

                            String status = null;

                            boolean exists = ordersDataBaseHelper.orderExists(os_value.getuserTimestamp());



                            if(exists){

                                String is_confirmed = os_value.getConfirmed();
                                if (is_confirmed.equalsIgnoreCase("true")) {
                                    status = "approved";

                                } else if (is_confirmed.equalsIgnoreCase("false")) {
                                    status = "out of stock";
                                }
                                ordersDataBaseHelper.setConfirmationStatus(os_value.getorderCode(),
                                        os_value.getuserTimestamp(), is_confirmed);

                                sendNotification("Order updated!", os_value.getorderCode() + " is " + status);

                                if (OrdersFragment.adapter != null)
                                    OrdersFragment.adapter.notifyDataSetChanged();
                                Log.i("OrdersFrag adapter", "NOTIFIED");

                            }


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


            orderStatusRef.addChildEventListener(childEventListener);
        }

        return START_STICKY;
    }

    private void sendNotification(String title, String messageBody) {
        final int notifyID = generateRandom();
        Intent intent = new Intent(this, MainActivity.class);
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
            orderStatusRef.removeEventListener(mChildEventListener);
        }
    }
}
