package com.payable.demo.services;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.payable.demo.activities.MainActivity;
import com.payable.sdk.Payable;

public class FirebaseService extends FirebaseMessagingService {

    @Override
    public void onNewToken(String newToken) {

        super.onNewToken(newToken);

        Log.e("FCM", "onNewToken: " + newToken);
    }


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        super.onMessageReceived(remoteMessage);

        Log.e("FCM", "onMessageReceived: " + remoteMessage.getData());

        if (remoteMessage.getData().get("payment_type") != null) {

            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.putExtra("PAYMENT_TYPE", Integer.valueOf(remoteMessage.getData().get("payment_type")));
            i.putExtra("AMOUNT", Double.valueOf(remoteMessage.getData().get("amount")));
            startActivity(i);

        }
    }
}


