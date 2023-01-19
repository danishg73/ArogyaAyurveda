package com.example.arogyaayurveda;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

public class splash_screen extends AppCompatActivity
{
    private  static final int code=2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        Handler handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run()
            {
             Intent intent = new Intent(splash_screen.this, Dashboard.class);
             startActivity(intent);
            }
        },code);
    }
}
