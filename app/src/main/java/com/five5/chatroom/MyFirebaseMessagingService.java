package com.five5.chatroom;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    String CHANNEL_ID ="chatroom";

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Uri tone = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Intent i= new Intent(getApplicationContext(),Output_Chatroom.class);
        i.putExtra("Name",remoteMessage.getData().get("channel"));

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(),CHANNEL_ID)
                .setContentText(remoteMessage.getNotification().getBody())
                .setContentTitle(remoteMessage.getNotification().getTitle())
                .setSubText(remoteMessage.getData().get("channel"))
                .setSound(tone)
                .setContentIntent(PendingIntent.getActivity(getApplicationContext(),00,i,PendingIntent.FLAG_UPDATE_CURRENT))
                .setSmallIcon(R.drawable.icon)
                .setAutoCancel(true);


            NotificationManager mNotify=getSystemService(NotificationManager.class);

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O)
        {NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID,"message", NotificationManager.IMPORTANCE_DEFAULT);
        mNotify.createNotificationChannel(notificationChannel);
        }
       if(!FirebaseAuth.getInstance().getCurrentUser().getUid().equals(remoteMessage.getData().get("id")))
        mNotify.notify(1,builder.build());
        Log.e("Hello","true");
        Log.e("e",FirebaseAuth.getInstance().getCurrentUser().getEmail());
        Log.e("e1",remoteMessage.getData().get("channel"));


    }
}