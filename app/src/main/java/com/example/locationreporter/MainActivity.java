package com.example.locationreporter;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.chip.Chip;

public class MainActivity extends AppCompatActivity  {

    private static final int RC_SIGN_IN = 0 ;
    private static final String TAG = "";
    protected String[] listPermissions = new String[]
            {
                    Manifest.permission.READ_CONTACTS,
                    Manifest.permission.INTERNET,
                    Manifest.permission.SEND_SMS,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
            };
    private View.OnClickListener log_click;


    @SuppressLint("PrivateResource")
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        overridePendingTransition(R.anim.fragment_open_enter,R.anim.fragment_close_enter);
        signIn();



// notificationId is a unique int for each notification that you must define


        if(checkPermission(Manifest.permission.INTERNET) || checkPermission(Manifest.permission.READ_CONTACTS) || checkPermission(Manifest.permission.ACCESS_FINE_LOCATION) || checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION) || checkPermission(Manifest.permission.SEND_SMS))
        {
            this.requestPermissions(listPermissions,0);
        } //Đã cấp quyền

        Chip chipSetting = (Chip) this.findViewById(R.id.chipSet);
        Chip chipMap = (Chip) this.findViewById(R.id.chipMap);
        Chip chipExit = (Chip) this.findViewById(R.id.chipExit);
        Chip chipInfo = (Chip) this.findViewById(R.id.chipInfo);


        class setting_clicked implements View.OnClickListener {
            @Override
            public void onClick(View v) {
                overridePendingTransition(R.anim.fragment_open_exit,R.anim.fragment_close_exit);
                Intent setting = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(setting);
            }
        }
        View.OnClickListener set_click = new setting_clicked();
        chipSetting.setOnClickListener(set_click);
        class Map_clicked implements View.OnClickListener {

            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                overridePendingTransition(R.anim.fragment_open_exit,R.anim.fragment_close_exit);
                CheckConnection checkConnection = new CheckConnection(getApplicationContext());
                 boolean checkconnect = checkConnection.isInternetConnected();
                if(checkconnect) {
                    Intent map = new Intent(MainActivity.this, MapsActivity.class);
                    startActivity(map);


                }
                else
                {
                    Intent map = new Intent(MainActivity.this, OfflineActivity.class);
                    startActivity(map);


                }
            }
        }
        View.OnClickListener map_click = new Map_clicked();
        chipMap.setOnClickListener(map_click);


        class Info_clicked implements View.OnClickListener
        {

            @Override
            public void onClick(View v)
            {
                overridePendingTransition(R.anim.fragment_open_exit,R.anim.fragment_close_exit);
                Intent info =new Intent(MainActivity.this,InfoActivity.class);
                startActivity(info);

            }
        }
        View.OnClickListener info_click = new Info_clicked();
        chipInfo.setOnClickListener(info_click);

        class Exit_clicked implements View.OnClickListener {

            @Override
            public void onClick(View v) {

                overridePendingTransition(R.anim.fragment_fast_out_extra_slow_in,R.anim.fragment_fade_exit);
                finishAndRemoveTask();
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(1);
            }
        }
        View.OnClickListener exit_click = new Exit_clicked();
        chipExit.setOnClickListener(exit_click);

    }
    boolean checkPermission(String per){
        return ContextCompat.checkSelfPermission(this, per) != PackageManager.PERMISSION_GRANTED;
    }
    @Override
    public void onStart()
    {

        super.onStart();



    }

    public void signIn()
    {
        // Configure sign-in to request the user's ID, email address, and basic
// profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        // Build a GoogleSignInClient with the options specified by gso.
        GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        // Check for existing Google Sign In account, if the user is already signed in
// the GoogleSignInAccount will be non-null.
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);



// Build a GoogleSignInClient with the options specified by gso.

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                handleSignInResult(task);
                
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask)  {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            Config.email = account.getEmail();


        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());

        }


        }

    }






