package com.example.arogyaayurveda;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class daily_tips extends AppCompatActivity
{
    String Response;
    List<dashboard_list> array_dashboard_list = new ArrayList<>();
    ListView listView;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_tips);
        progressDialog = new ProgressDialog(this);
        listView = findViewById(R.id.listview);
        progressDialog.setMessage(getResources().getString(R.string.Loading));
        progressDialog.setCancelable(false);
        progressDialog.show();


        ImageView backarrow;
        backarrow = findViewById(R.id.backarrow);
        backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                daily_tips.super.onBackPressed();
            }
        });

         uploadToServer readService= new  uploadToServer(new ReadServiceResponse() {

            @Override
            public void processFinish(String output)
            {
                progressDialog.dismiss();
                if(output.contains("No details found"))
                {
                    Toast.makeText(daily_tips.this, R.string.nodetails, Toast.LENGTH_SHORT).show();
                }
                else
                {
                    insertdata(output);
                }


            }
        });readService.execute();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Intent intent = new Intent(daily_tips.this, Detail_page.class);
                intent.putExtra("title",  array_dashboard_list.get(position).title);
                intent.putExtra("description",  array_dashboard_list.get(position).description);
                intent.putExtra("id",  array_dashboard_list.get(position).id);
                intent.putExtra("timestamp",  array_dashboard_list.get(position).timestamp);
                intent.putExtra("subcategory",  "");
                intent.putExtra("category", "");
                intent.putExtra("favourite",  "");
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
                d_list.timestamp=obj.getString("date").trim();
                array_dashboard_list.add(d_list);
                d_list = null;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        dashboard_list_adapter adapter_dashboard = new dashboard_list_adapter(array_dashboard_list,this,this,"dailytip");
        listView.setAdapter(adapter_dashboard);
        adapter_dashboard.notifyDataSetChanged();




    }








    public class uploadToServer extends AsyncTask<String, Void, String> {

        ReadServiceResponse delegate =null;
        public uploadToServer(ReadServiceResponse res) {
            delegate = res;
        }
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... arg0) {

            String link=getResources().getString(R.string.link)+"getdailytip.php";


            try {
                HttpPost httppost = new HttpPost(link);
                HttpClient httpclient = new DefaultHttpClient();
//                httppost.setEntity(new UrlEncodedFormEntity(list));
                HttpResponse response = httpclient.execute(httppost);
                Response = EntityUtils.toString(response.getEntity());
            } catch (Exception e) {
                Log.v("log_tag", "Error in http connection " + e.toString());
            }
            return Response;

        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            delegate.processFinish(result);

        }
    }




}