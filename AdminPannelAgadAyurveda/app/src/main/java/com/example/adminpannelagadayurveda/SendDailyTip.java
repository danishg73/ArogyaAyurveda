package com.example.adminpannelagadayurveda;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
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
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;




import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.okhttp.MediaType;


public class SendDailyTip extends AppCompatActivity {


    EditText title,description;
    String gettitle,getdescription,Response;
    Button send;
    ProgressDialog progressDialog;
    ArrayList<NameValuePair> list = new ArrayList<NameValuePair>();

    private String URL = "https://fcm.googleapis.com/fcm/send";
    private RequestQueue mRequestQue;
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_daily_tip);
        title= findViewById(R.id.title);
        description = findViewById(R.id.description);
        send = findViewById(R.id.submit);
        ImageView backarrow;
        backarrow = findViewById(R.id.backarrow);
        backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendDailyTip.super.onBackPressed();
            }
        });

        mRequestQue = Volley.newRequestQueue(this);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Sending");
        progressDialog.setCancelable(false);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {


                gettitle = title.getText().toString().trim();
                getdescription = description.getText().toString().trim();

                if(TextUtils.isEmpty(gettitle) || TextUtils.isEmpty(getdescription)    )
                {
                    Toast.makeText(SendDailyTip.this, "All Fields are required", Toast.LENGTH_SHORT).show();
                }
                else
                {
                        InputMethodManager imm =(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
                        list.add(new BasicNameValuePair("title",gettitle));
                        list.add(new BasicNameValuePair("description",getdescription));
                        progressDialog.show();
                        uploadToServer readService= new  uploadToServer(new ReadServiceResponse() {

                        @Override
                        public void processFinish(String output)
                        {

                            if(output.contains("Added Successfully"))
                            {
                                progressDialog.setMessage("Sending Notification");
                                sendNotification();
                            }
                            else
                            {
                                progressDialog.dismiss();
                                Toast.makeText(SendDailyTip.this, output+"", Toast.LENGTH_SHORT).show();


                            }


                        }
                    });readService.execute();




                }


            }
        });
    }

    private void sendNotification() {
        String t ="/topics/dailytip";

        JSONObject json = new JSONObject();
        try {
            json.put("to", t);
            JSONObject notificationObj = new JSONObject();
            notificationObj.put("title",gettitle);
            notificationObj.put("body",getdescription);
            JSONObject extraData = new JSONObject();

            json.put("notification",notificationObj);
            json.put("data",extraData);


            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, URL,
                    json,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response)
                        {
                            Toast.makeText(SendDailyTip.this, R.string.Notification_Sent, Toast.LENGTH_SHORT).show();
                            title.setText("");
                            description.setText("");
                            progressDialog.dismiss();
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("MUR", "onError: "+error.networkResponse);
                    progressDialog.dismiss();
                }
            }
            ){
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String,String> header = new HashMap<>();
                    header.put("content-type","application/json");
                    header.put("authorization","key=AAAAmGI-pcw:APA91bHFLVM4aaOczx9YPisfsROxflEkhXJ7Yt1iJ32XHkv0LbPBG1vQizrxjjl30o1uzhTQZTFRK-p1IjeK96TNiCdrEloIDM216hb1rPsakVaxlmBjPZ8xfhhBvIBLduKbbk87MbB_");
                    return header;
                }
            };
            mRequestQue.add(request);
        }
        catch (JSONException e)

        {
            progressDialog.dismiss();
            e.printStackTrace();
        }


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

            String link=getResources().getString(R.string.link)+"dailytip.php";


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