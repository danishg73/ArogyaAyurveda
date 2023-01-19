package com.example.adminpannelagadayurveda;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity
{

    Button sendtip,addtip,addcat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sendtip = findViewById(R.id.dailytip);
        addtip = findViewById(R.id.addtip);
        addcat = findViewById(R.id.addcat);

        sendtip.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(MainActivity.this,SendDailyTip.class);
                startActivity(intent);
            }
        });
        addtip.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(MainActivity.this,Addtip.class);
                startActivity(intent);
            }
        });
        addcat.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(MainActivity.this,Addsubcat.class);
                startActivity(intent);
            }
        });
    }
}