package com.example.intruderalert;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

public class MyForegroundService extends Service {

    private static final int NOTIFICATION_ID = 1;
    private static final String CHANNEL_ID = "MyChannelId";

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Create a notification channel for the foreground service (required for Android 8.0 and above)
        createNotificationChannel();

        // Create a notification for the foreground service
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Intruder Detect")
                .setContentText("Waiting for detection")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .build();

        // Start the service as a foreground service to avoid getting killed by the system
        startForeground(NOTIFICATION_ID, notification);

        // Return START_STICKY to ensure the service restarts if it gets terminated by the system
        return START_STICKY;
    }

    private void createNotificationChannel() {
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "My App Channel", NotificationManager.IMPORTANCE_DEFAULT);
        NotificationManager manager = getSystemService(NotificationManager.class);
        manager.createNotificationChannel(channel);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        // You don't need to implement this if your service doesn't support binding
        return null;
    }
}
