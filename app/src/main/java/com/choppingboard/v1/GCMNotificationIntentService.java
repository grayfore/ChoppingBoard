package com.choppingboard.v1;

/**
 * Created by Jeff on 6/28/15.
 */

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;

public class GCMNotificationIntentService extends IntentService {
    // Sets an ID for the notification, so it can be updated
    public static final int notifyID = 9001;
    NotificationCompat.Builder builder;
    DatabaseHandler db;
    public GCMNotificationIntentService() {
        super("GcmIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);

        String messageType = gcm.getMessageType(intent);

        if (!extras.isEmpty()) {
            if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR
                    .equals(messageType)) {
                sendNotification("Send error: " + extras.toString());
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED
                    .equals(messageType)) {
                sendNotification("Deleted messages on server: "
                        + extras.toString());
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE
                    .equals(messageType)) {

                String one = "" + extras.get(ApplicationConstants.MENU_KEY);
                Log.v("lambo", one);
                if(!one.equals("") && !one.equals("null")) {
                    createMenu(one);
                }
                String two = "" + extras.get(ApplicationConstants.MSG_KEY);
                Log.v("lambohoho", two);
                if(!two.equals("") && !two.equals("null")){
                    sendNotification("" + extras.get(ApplicationConstants.MSG_KEY));
                }
//                String three = "" + extras.get(ApplicationConstants.MENU_MAT_KEY);
//                Log.v("lambododo", three);
//                if(!three.equals("") && !three.equals("null")){
//                    createMat(three);
//                }
            }
        }
        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }

    private void sendNotification(String msg) {
        // If the See screen is not open, it will take you to that activity
        if(!SeeScreen.active){
            Intent resultIntent = new Intent(this, SeeScreen.class);
//            resultIntent.putExtra("msg", msg);
            db = new DatabaseHandler(this);
            db.addOrder(msg);
            PendingIntent resultPendingIntent = PendingIntent.getActivity(this, 0,
                    resultIntent, PendingIntent.FLAG_ONE_SHOT);

            NotificationCompat.Builder mNotifyBuilder;
            NotificationManager mNotificationManager;

            mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            mNotifyBuilder = new NotificationCompat.Builder(this)
                    .setContentTitle("Alert")
                    .setContentText("You've received new message.")
                    .setSmallIcon(R.drawable.colorbg);
            // Set pending intent
            mNotifyBuilder.setContentIntent(resultPendingIntent);

            // Set Vibrate, Sound and Light
            int defaults = 0;
            defaults = defaults | Notification.DEFAULT_LIGHTS;
            defaults = defaults | Notification.DEFAULT_VIBRATE;
            defaults = defaults | Notification.DEFAULT_SOUND;

            mNotifyBuilder.setDefaults(defaults);
            // Set the content for Notification
            mNotifyBuilder.setContentText("New order received");
            // Set autocancel
            mNotifyBuilder.setAutoCancel(true);
            // Post a notification
            mNotificationManager.notify(notifyID, mNotifyBuilder.build());
        }
        // If the See screen is already open, then this will just add things to existing screen
        else{
            Intent resultIntent = new Intent("order");
            resultIntent.putExtra("msg", msg);
            LocalBroadcastManager.getInstance(this).sendBroadcast(resultIntent);
            PendingIntent resultPendingIntent = PendingIntent.getActivity(this, 0,
                    resultIntent, PendingIntent.FLAG_ONE_SHOT);

            NotificationCompat.Builder mNotifyBuilder;
            NotificationManager mNotificationManager;

            mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            mNotifyBuilder = new NotificationCompat.Builder(this)
                    .setContentTitle("Alert")
                    .setContentText("You've received new message.")
                    .setSmallIcon(R.drawable.colorbg);
            // Set pending intent
            mNotifyBuilder.setContentIntent(resultPendingIntent);

            // Set Vibrate, Sound and Light
            int defaults = 0;
            defaults = defaults | Notification.DEFAULT_LIGHTS;
            defaults = defaults | Notification.DEFAULT_VIBRATE;
            defaults = defaults | Notification.DEFAULT_SOUND;

            mNotifyBuilder.setDefaults(defaults);
            // Set the content for Notification
            mNotifyBuilder.setContentText("New order received");
            // Set autocancel
            mNotifyBuilder.setAutoCancel(true);
            // Post a notification
            mNotificationManager.notify(notifyID, mNotifyBuilder.build());

        }
    }

    public void createMenu(String str){

        db = new DatabaseHandler(this);
        db.createCAT(str);

        Log.v("lambokoko","WE MADE IT");
    }

    public void createMat(String str){

        db.createMAT(str);
    }
}
