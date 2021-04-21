package com.example.locationreporter;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class LoadActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load);

        Thread time = new Thread()
        {
            public void run()
            {
                try
                {
                    sleep(5000);

                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
                finally
                {
                    overridePendingTransition(R.anim.fragment_open_exit,R.anim.fragment_close_exit);
                    Intent intent = new Intent(LoadActivity.this,MainActivity.class);
                    startActivity(intent);


                }
            }
        };
        time.start();
    }
    protected void onPause()
    {
        super.onPause();
        finish();
    }

}
