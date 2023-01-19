package com.example.arogyaayurveda;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Bydisease extends AppCompatActivity
{
    RecyclerView subcategory;
    String Response;
    List<category_list> array_cat_list = new ArrayList<>();
    ProgressDialog progressDialog;
    ArrayList<NameValuePair> list = new ArrayList<NameValuePair>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bydisease);
        subcategory = findViewById(R.id.subcategory);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getResources().getString(R.string.Loading));
        progressDialog.setCancelable(false);

        getdata();

        ImageView backarrow;
        backarrow = findViewById(R.id.backarrow);
        backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 Bydisease.super.onBackPressed();
            }
        });



    }






    public void getdata()
    {

        list.add(new BasicNameValuePair("category","disease"));
        progressDialog.show();
        uploadToServer readService= new uploadToServer(new ReadServiceResponse() {

            @Override
            public void processFinish(String output)
            {
                progressDialog.dismiss();
                if(output.contains("No details found"))
                {
                    Toast.makeText(Bydisease.this, getResources().getString(R.string.nodetails), Toast.LENGTH_SHORT).show();
                }
                else
                {
                    insertcat(output);
                }


            }
        });readService.execute();
    }

    public void insertcat(String output)
    {


        try {
            JSONArray jsonObject = new JSONArray(output);
            for (int i=0; i<jsonObject.length(); i++)
            {

                category_list categoryList = new category_list();
                JSONObject obj = jsonObject.getJSONObject(i);
                categoryList.subcategory =obj.getString("subcategory").trim();
                categoryList.category_id =obj.getString("id").trim();
                categoryList.parentcategory =obj.getString("category").trim();
                array_cat_list.add(categoryList);
                categoryList = null;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        category_list_adapter adapter = new category_list_adapter(array_cat_list);
        subcategory.setAdapter(adapter);
        adapter.notifyDataSetChanged();

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

            String link=getResources().getString(R.string.link)+"getsubcategory.php";


            try {
                HttpPost httppost = new HttpPost(link);
                HttpClient httpclient = new DefaultHttpClient();
                httppost.setEntity(new UrlEncodedFormEntity(list));
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