package com.example.arogyaayurveda;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Detail_page extends AppCompatActivity {
    TextView headertitle,title,description;
    Button share,whatsapp,addtofav;
    ImageView backarrow,fav_img;
    String gettitle,getdescription,id,timestamp,category,subcategory;
    List<dashboard_list> array_dashboard_list = new ArrayList<>();
    SharedPreferences sp;
    String favourite;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_detail_page);
        headertitle = findViewById(R.id.headertitle);
        title = findViewById(R.id.title);
        description = findViewById(R.id.description);
        share = findViewById(R.id.share);
        whatsapp = findViewById(R.id.whatsapp);
        addtofav = findViewById(R.id.addfav);
        backarrow = findViewById(R.id.backarrow);
        fav_img = findViewById(R.id.fav);


        gettitle = getIntent().getStringExtra("title");
        getdescription = getIntent().getStringExtra("description");
        id = getIntent().getStringExtra("id");
        timestamp = getIntent().getStringExtra("timestamp");
        subcategory = getIntent().getStringExtra("subcategory");
        category = getIntent().getStringExtra("category");
        favourite = getIntent().getStringExtra("favourite" ).trim();

        headertitle.setText(gettitle);
        title.setText(gettitle);
        description.setText(getdescription);




        ImageView backarrow;
        backarrow = findViewById(R.id.backarrow);
        backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Detail_page.super.onBackPressed();
            }
        });

        sp = this.getSharedPreferences("data", MODE_PRIVATE);
        if(favourite.contains("yes"))
        {
            fav_img.setBackgroundResource(0);
            fav_img.setImageResource(R.drawable.ic_fav_yes);
        }
        else

        {
            if( sp.contains("favourite"))
            {
                insertdata(sp.getString("favourite", "").trim());
            }
        }


        whatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, gettitle+"\n"+getdescription);
                sendIntent.setType("text/plain");
                sendIntent.setPackage("com.whatsapp");
                startActivity(sendIntent);
            }
        });
        fav_img.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(favourite.equals("yes"))
                {
                    removefav();
                }
                else
                {
                    addtofav();
                }
            }
        });
        addtofav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if(favourite.equals("yes"))
                {
                    Toast.makeText(Detail_page.this, getResources().getString(R.string.already_fav), Toast.LENGTH_SHORT).show();
                }
                else
                    {

                        addtofav();
                    }
            }
        });

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, gettitle+"\n"+getdescription);
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
            }
        });

    }

    public  void removefav()
    {
        String favlist = sp.getString("favourite", "").trim();
        try {

            JSONArray jsonObject = new JSONArray(favlist);

            int i=0;
            do{
                JSONObject obj = jsonObject.getJSONObject(i);
                if(obj.getString("id").trim().equals(id))
                {
                    jsonObject.remove(i);
                     i =jsonObject.length();
                    Toast.makeText(this, getResources().getString(R.string.remove_fav), Toast.LENGTH_SHORT).show();
                    favourite="";
                    fav_img.setBackgroundResource(0);
                    fav_img.setImageResource(R.drawable.ic_fav_no);
                }
                i++;
            }while(i<jsonObject.length());

            SharedPreferences.Editor edit = sp.edit();
            edit.putString("favourite", String.valueOf(jsonObject));
            edit.commit();


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public  void addtofav()
    {


        if( sp.contains("favourite"))
        {

            String favlist = sp.getString("favourite", "").trim();
            try {

                JSONArray jsonObject = new JSONArray(favlist);
                JSONObject favourite = new JSONObject();

                    favourite.put("title", gettitle);
                    favourite.put("description", getdescription);
                    favourite.put("id", id);
                    favourite.put("category", category);
                    favourite.put("subcategory", subcategory);
                    favourite.put("timestamp", timestamp);
                    jsonObject.put(favourite);
                    SharedPreferences.Editor edit = sp.edit();
                    edit.putString("favourite", String.valueOf(jsonObject));
                    edit.commit();


            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
        else
        {
            JSONArray jsonArray = new JSONArray();
            JSONObject favourite = new JSONObject();
            try {
                favourite.put("title", gettitle);
                favourite.put("description", getdescription);
                favourite.put("id", id);
                favourite.put("category", category);
                favourite.put("subcategory", subcategory);
                favourite.put("timestamp", timestamp);
                jsonArray.put(favourite);
                SharedPreferences.Editor edit = sp.edit();
                edit.putString("favourite", String.valueOf(jsonArray));
                edit.commit();


            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        favourite="yes";
        fav_img.setBackgroundResource(0);
        fav_img.setImageResource(R.drawable.ic_fav_yes);
        Toast.makeText(this, getResources().getString(R.string.addtofav), Toast.LENGTH_SHORT).show();
    }
    public void insertdata(String output)
    {


        try {
            JSONArray jsonObject = new JSONArray(output);
            for (int i=0; i<jsonObject.length(); i++)
            {
                dashboard_list d_list = new dashboard_list();
                JSONObject obj = jsonObject.getJSONObject(i);
                d_list.title =obj.getString("title").trim();
                d_list.description =obj.getString("description").trim();
                d_list.id =obj.getString("id").trim();
                d_list.category =obj.getString("category").trim();
                d_list.subcategory=obj.getString("subcategory").trim();
                d_list.timestamp=obj.getString("timestamp").trim();
                array_dashboard_list.add(d_list);
                if(obj.getString("id").trim().equals(id))
                {
                    favourite="yes";
                    fav_img.setBackgroundResource(0);
                    fav_img.setImageResource(R.drawable.ic_fav_yes);
                }
                d_list = null;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

}