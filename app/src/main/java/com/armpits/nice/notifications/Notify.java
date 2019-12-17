package com.armpits.nice.notifications;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.armpits.nice.R;

public class Notify {

    // Channel IDs
    private static final String CHANNEL_1_ID = "CH_1";
    private static final String CHANNEL_2_ID = "CH_2";

    // Create notification channels on app start with context of MainActivity
    public static void createNotificationChannels(Context context) {
        // Create Notification Channels (req. on API 26+)

        // CHANNEL 1
        CharSequence ch_1_name = context.getString(R.string.notif_ch_1_dl);
        String ch_1_description = context.getString(R.string.notif_ch_1_desc);
        int ch_1_importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel_1 = new NotificationChannel(CHANNEL_1_ID, ch_1_name, ch_1_importance);
        channel_1.setDescription(ch_1_description);
        // CHANNEL 2
        CharSequence ch_2_name = context.getString(R.string.notif_ch_2_ddl);
        String ch_2_description = context.getString(R.string.notif_ch_2_desc);
        int ch_2_importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel_2 = new NotificationChannel(CHANNEL_2_ID, ch_2_name, ch_2_importance);
        channel_2.setDescription(ch_2_description);

        // Register the channel with the system; you can't change the importance
        // or other notification behaviors after this
        NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
        if (notificationManager != null) {
            notificationManager.createNotificationChannel(channel_1);
            notificationManager.createNotificationChannel(channel_2);
        }
    }

    // Return notification builder called by notification manager over channel 1 or channel 2
    private static NotificationCompat.Builder buildNotification(Context context, String channelID, boolean onGoing,
                                                                        String title, String msg) {

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelID);
        builder.setSmallIcon(R.drawable.ic_notifications_black_24dp);
        builder.setContentTitle(title);
        builder.setContentText(msg);
        builder.setPriority(NotificationCompat.PRIORITY_HIGH);
        builder.setOngoing(onGoing);

        // set intent to start when user taps notification
        // only if onGoing is not true
        if (!onGoing) {
            // Get current active application as pending intent to revert to
            // when dismissing notification on tap
            PendingIntent notifyPIntent = PendingIntent.getActivity(context.getApplicationContext(),
                    0, new Intent(), 0);
            builder.setContentIntent(notifyPIntent);
            builder.setAutoCancel(true);
        }

        return builder;
    }

    public static int displayNotification(Context context, int channel, boolean onGoing,
                                          String title, String msg) {
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        String channelID = CHANNEL_1_ID;
        if (channel == 2) channelID = CHANNEL_2_ID;
        // notificationId is a unique int for each notification that you must define
        int notificationId = NotificationID.getID();
        notificationManager.notify(notificationId, buildNotification(context,
                channelID, onGoing, title, msg).build());
        return notificationId;
    }
}
