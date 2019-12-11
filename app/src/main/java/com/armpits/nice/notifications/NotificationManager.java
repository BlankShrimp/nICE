package com.armpits.nice.notifications;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationChannel;
import android.os.Build;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import com.armpits.nice.R;

public class NotificationManager extends Application {

    // Use NotificationManager to show our notifications.
    // Compat wraps around the normal notification manager and has internal check
    // for background compatibility.
    private NotificationManagerCompat notificationManager;
    private EditText editTextTitle;
    private EditText editTextMessage;

    private Button button1;

    // should give a name that explains what it's for, e.g. notif_download, notif_ddl
    public static final String CHANNEL_1_ID = "channel_1";
    public static final String CHANNEL_2_ID = "channel_2";

    @Override
    public void onCreate() {
        super.onCreate();

        notificationManager = NotificationManagerCompat.from(this);
        //editTextTitle = findViewById(R.id.edit_text_title);
        //editTextMessage = findViewById(R.id.edit_text_message);
        createNotificationChannels();
    }

    private void createNotificationChannels() {

        // Build O = Oreo (API level 26+ requires notification channels)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            // CHANNEL 1
            NotificationChannel channel_1 = new NotificationChannel(
                    CHANNEL_1_ID,
                    "Channel 1",
                    // IMPORTANCE_HIGH = Heads-up notification; only displays when unlocked.
                    android.app.NotificationManager.IMPORTANCE_DEFAULT
            );
            // Explains what channel is used for.
            channel_1.setDescription("This is channel 1");

            // CHANNEL 2
            NotificationChannel channel_2 = new NotificationChannel(
                    CHANNEL_2_ID,
                    "Channel 2",
                    android.app.NotificationManager.IMPORTANCE_DEFAULT
            );
            // Explains what channel is used for.
            channel_2.setDescription("This is channel 2");

            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this.
            android.app.NotificationManager manager = getSystemService(android.app.NotificationManager.class);
            manager.createNotificationChannel(channel_1);
            manager.createNotificationChannel(channel_2);
        }
    }

    // Notifications
    public void sendOnChannel_1(View v) {

        //String title = editTextTitle.getText().toString();
        String title = "Farts title 1";
        //String message = editTextMessage.getText().toString();
        String message = "Farts title 2";

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_1_ID)
                .setSmallIcon(R.drawable.ic_one)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .build();

        notificationManager.notify(1, notification);
    }

    public void sendOnChannel_2(View v) {
        //String title = editTextTitle.getText().toString();
        String title = "Farts title 2";
        //String message = editTextMessage.getText().toString();
        String message = "Farts message 2";


        Notification notification = new NotificationCompat.Builder(this, CHANNEL_2_ID)
                .setSmallIcon(R.drawable.ic_two)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .build();

        notificationManager.notify(2, notification);
    }

}
