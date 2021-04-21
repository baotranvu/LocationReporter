package com.example.locationreporter;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.telephony.SmsManager;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class SMSService extends Service
{
    Handler mHandler = new Handler();
    @Override
    public void onCreate()
    {
        super.onCreate();
    }
    @Override
    public int onStartCommand(Intent intent,int flags, int startId)
    {
        SettingsActivity settingsActivity = new SettingsActivity();
        final int time = SettingsActivity.time;
        Intent map = new Intent(this,MainActivity.class);
        map.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, map, 0);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, Notify.CHANNEL_2_ID);
        mBuilder.setSmallIcon(R.drawable.logo);
        mBuilder.setContentTitle("My Location");
        mBuilder.setContentText("SMS Sending");
        mBuilder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
        mBuilder.setAutoCancel(true);
        mBuilder.setContentIntent(pendingIntent);
        mBuilder.build();
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
        notificationManagerCompat.notify(1,mBuilder.build());
        startForeground(1,mBuilder.build());
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run()
            {

                sendSMS();
                mHandler.postDelayed(this, time);

            }
        }, time);



        return START_NOT_STICKY;


    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;

    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
    }
    public void sendSMS()
    {
        SettingsActivity settingsActivity = new SettingsActivity();
        double x = MapsActivity.Latitude;
        double y = MapsActivity.Longitude;
        String phoneNo = Config.phone_num;
        String message =   x + " " + y ;
        try {

            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNo, null, message, null, null);
            Toast.makeText(getApplicationContext(), "SMS sent.", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "SMS failed, please try again.", Toast.LENGTH_LONG).show();
            e.printStackTrace();
            Intent intent = new Intent(this,SMSService.class);
            stopService(intent);
        }
    }




}
