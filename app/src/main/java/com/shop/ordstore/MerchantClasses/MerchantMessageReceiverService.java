package com.shop.ordstore.MerchantClasses;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.shop.ordstore.R;
import com.shop.ordstore.UserClasses.MainActivity;

import java.util.Random;

/**
 * Created by AangJnr on 9/22/16.
 */
public class MerchantMessageReceiverService extends FirebaseMessagingService {



    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {


        sendNotification(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody());
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
                //.setContentIntent(pendingIntent)
                .setSound(defaultSoundUri);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(notifyID, notificationBuilder.build());
    }

    public int generateRandom(){
        Random random = new Random();
        return random.nextInt(9999 - 1000) + 1000;
    }





}
