package com.example.intruderalert;

import java.net.MalformedURLException;
import java.net.SocketException;
import java.net.URL;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.content.SharedPreferences;

import java.io.IOException;
import java.net.InetAddress;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import android.os.Looper;
import android.os.StrictMode;
import android.os.AsyncTask;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.net.NetworkInterface;
import java.util.Enumeration;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;


import androidx.annotation.Nullable;


public class MyBackgroundService extends Service {

    static String serverIp;
    static int serverPort;

    static int notificationId = 1;
    String receivedMessage = "";


    private static final String SHARED_PREFS_NAME = "MySharedPrefs";
    private SharedPreferences sp;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        sp = getSharedPreferences(SHARED_PREFS_NAME, MODE_PRIVATE);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitNetwork().build();
        StrictMode.setThreadPolicy(policy);


        //notification
        CharSequence name = "Alerts";
        String description = "Recieve Alerts";
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new NotificationChannel("1", name, importance);
        channel.setDescription(description);

        // Register the channel with the system
        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);


        // Implement the code that you want to run continuously here
        // This code will keep running in the background even when the app is in the background
        URL url;


        try {
            url = new URL(sp.getString("url", "http://192.168.43.27:5556/video_feed"));


        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        serverIp = url.getHost();

        serverPort = url.getPort() + 1;

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(new Runnable() {
            @Override
            public void run() {

                try {
//
                    DatagramSocket clientSocket = new DatagramSocket(serverPort);


                    for (int i = 0; i < 30; i++) {

                        byte[] receiveData = new byte[1024];

                        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                        System.out.println("checking for incoming data");
                        clientSocket.receive(receivePacket);


                        receivedMessage = new String(receivePacket.getData(), 0, receivePacket.getLength());
                        System.out.println("Recieved" + receivedMessage);

                        handler.post(new Runnable() {
                            @Override
                            public void run() {

                                Toast.makeText(MyBackgroundService.this, receivedMessage, Toast.LENGTH_SHORT).show();
                                showNotification(receivedMessage);


                            }
                        });

                        // Send a message to the Python server
//                    String message = "Connection request";
//                    byte[] sendData = message.getBytes();
//                    InetAddress serverAddress = InetAddress.getByName(serverIp);
//                    DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, serverAddress, serverPort);
//                    clientSocket.send(sendPacket);

                        // Receive a response from the Python server
                    }

                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }


                //UI Thread work here


            }
        });


        System.out.println("hi");

        // Return START_STICKY to ensure the service restarts if it gets terminated by the system
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        // You don't need to implement this if your service doesn't support binding
        return null;
    }


    private void showNotification(String message) {

        Intent intent4 = new Intent(this, VideoFeed.class);
        intent4.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent4, PendingIntent.FLAG_IMMUTABLE);


        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "1")
                .setSmallIcon(R.drawable.baseline_notifications_active_24)
                .setContentTitle("Unknown face detected")
                .setContentText("Time: "+message)

                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                // Set the intent that will fire when the user taps the notification
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        notificationId +=1;
        notificationManager.notify(notificationId, builder.build());


        //Logactivity.updateListView("Unknown face detected @ "+ message);
        }




    }




