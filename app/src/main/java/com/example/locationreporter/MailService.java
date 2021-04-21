package com.example.locationreporter;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;

public class MailService extends Service {
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
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run()
            {
                GMailSender gMailSender = new GMailSender(Config.email,Config.password);
                String message = MapsActivity.Latitude+""+MapsActivity.Longitude;
                try {
                    gMailSender.sendMail("My location",message,Config.email,Config.R_mail);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                mHandler.postDelayed(this, time);
            }
        }, time);




        return START_NOT_STICKY;


    }
    @Override
    public void onDestroy()
    {
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }



}
