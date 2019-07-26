package com.app.pocketeat.Notifications;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.text.Html;

import com.app.pocketeat.Dashboard.Dashboard;
import com.app.pocketeat.LaunchActivity;
import com.app.pocketeat.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Random;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();

    private NotificationUtils notificationUtils;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        if (remoteMessage == null)
            return;


        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            sendNotification(remoteMessage);
        }
    }

    private void sendNotification(RemoteMessage remoteMessage) {
        Random random = new Random();
        int notificationid = random.nextInt(9999 - 1000) + 1000;
        NotificationManager mNotificationManager;
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(getApplicationContext(), "notify_001");
        Intent ii = new Intent(getApplicationContext(), LaunchActivity.class);
        ii.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, ii, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.BigTextStyle bigText = new NotificationCompat.BigTextStyle();
        bigText.setBigContentTitle(getResources().getString(R.string.app_name));
        mBuilder.setStyle(bigText);
        mBuilder.setAutoCancel(true);

        mBuilder.setContentIntent(pendingIntent);
        mBuilder.setSmallIcon(getNotificationIcon(mBuilder));
        try {
            mBuilder.setContentTitle(Html.fromHtml(remoteMessage.getData().get("title")));
            mBuilder.setContentText(Html.fromHtml(remoteMessage.getData().get("message")));

        }catch (Exception e)
        {
            mBuilder.setContentTitle(getResources().getString(R.string.app_name) + " notification");
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("notify_001",
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);
            mNotificationManager.createNotificationChannel(channel);
        }
        mNotificationManager.notify(notificationid, mBuilder.build());

    }

    private int getNotificationIcon(NotificationCompat.Builder notificationBuilder) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            int color = getResources().getColor(R.color.transparent);
            notificationBuilder.setColor(color);
            return R.drawable.ic_launcher_notify;

        }
        return R.drawable.ic_launcher_notify;
    }
}