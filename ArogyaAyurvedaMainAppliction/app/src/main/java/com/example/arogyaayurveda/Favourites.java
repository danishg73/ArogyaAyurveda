package com.example.arogyaayurveda;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Favourites extends AppCompatActivity
{
    List<dashboard_list> array_dashboard_list = new ArrayList<>();
    ListView listView;
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourites);
        listView = findViewById(R.id.listview);
        sp = this.getSharedPreferences("data", MODE_PRIVATE);

        if( sp.contains("favourite"))
        {
            insertdata(sp.getString("favourite", "").trim());

        }
        else
        {
            Toast.makeText(this, getResources().getString(R.string.nodata), Toast.LENGTH_SHORT).show();
        }


        ImageView backarrow;
        backarrow = findViewById(R.id.backarrow);
        backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Favourites.super.onBackPressed();
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(Favourites.this, Detail_page.class);
                intent.putExtra("title",  array_dashboard_list.get(position).title);
                intent.putExtra("description",  array_dashboard_list.get(position).description);
                intent.putExtra("id",  array_dashboard_list.get(position).id);
                intent.putExtra("timestamp",  array_dashboard_list.get(position).timestamp);
                intent.putExtra("subcategory",  array_dashboard_list.get(position).subcategory);
                intent.putExtra("category",  array_dashboard_list.get(position).category);
                intent.putExtra("favourite",  "yes");
                startActivity(intent);
            }
        });


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
                d_list = null;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        dashboard_list_adapter adapter_dashboard = new dashboard_list_adapter(array_dashboard_list,this,this);
        listView.setAdapter(adapter_dashboard);
        adapter_dashboard.notifyDataSetChanged();
    }


}