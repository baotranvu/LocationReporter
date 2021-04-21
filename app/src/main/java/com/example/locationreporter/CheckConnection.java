package com.example.locationreporter;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class CheckConnection
{
    private final Context context;
    public CheckConnection(Context context)
    {
        this.context = context;

    }
    public boolean isInternetConnected()
    {
        try {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            if (activeNetwork != null && activeNetwork.isConnectedOrConnecting() && activeNetwork.isAvailable()) {
                Config.isNetworkConnected = true;
            } else {
                Config.isNetworkConnected = false;
            }
        } catch (Exception e) {
            Log.e("Network check", "Error checking connection", e);

        }finally {
            return Config.isNetworkConnected;
        }
    }


}
