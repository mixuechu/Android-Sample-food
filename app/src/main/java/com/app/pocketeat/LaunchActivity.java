package com.app.pocketeat;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;

import com.app.pocketeat.Dashboard.Dashboard;

public class LaunchActivity extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 500;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new Handler().postDelayed(new Runnable() {

            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void run() {

                    if(Utils.getUserList(LaunchActivity.this)!=null)
                    {
                        Utils.user=Utils.getUserList(LaunchActivity.this).get(0);
                        Intent intent = new Intent(LaunchActivity.this, Dashboard.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }else {
                        //call login api to  register firebase id

                        Intent intent = new Intent(LaunchActivity.this, LoginActivity.class);
                        startActivity(intent);
                    }

                finish();

            }
        }, SPLASH_TIME_OUT);
    }
}
