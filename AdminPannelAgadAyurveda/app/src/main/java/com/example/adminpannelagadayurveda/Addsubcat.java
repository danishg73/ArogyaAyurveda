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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.util.ArrayList;

public class Addsubcat extends AppCompatActivity {
    EditText subcat;
    String getsubcat,category="",Response;
    Button submit;
    RadioGroup radioGroup;
    ProgressDialog progressDialog;
    ArrayList<NameValuePair> list = new ArrayList<NameValuePair>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_addsubcat);
        subcat = findViewById(R.id.subcat);
        submit = findViewById(R.id.submit);
        radioGroup=(RadioGroup)findViewById(R.id.radioGroup);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Adding");
        progressDialog.setCancelable(false);
        ImageView backarrow;
        backarrow = findViewById(R.id.backarrow);
        backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Addsubcat.super.onBackPressed();
            }
        });

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch(i) {
                    case R.id.radiodisease:
                        category="disease";
                        break;
                    case R.id.radioherb:
                        category="herb";
                        break;
                }
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getsubcat = subcat.getText().toString().trim();
                if(category.equals(""))
                {
                    Toast.makeText(Addsubcat.this, "Select Category", Toast.LENGTH_SHORT).show();
                }
                else if (TextUtils.isEmpty(getsubcat))
                {
                    Toast.makeText(Addsubcat.this, "Sub Category Cannot be Empty", Toast.LENGTH_SHORT).show();

                }
                else
                {
                    list.add(new BasicNameValuePair("category",category));
                    list.add(new BasicNameValuePair("subcategory",getsubcat));
                    InputMethodManager imm =(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
                    progressDialog.setMessage("Adding");
                    progressDialog.show();
                    uploadToServer readService= new uploadToServer(new ReadServiceResponse() {

                        @Override
                        public void processFinish(String output)
                        {
                            progressDialog.dismiss();
                            subcat.setText("");
                            radioGroup.clearCheck();
                            category="";
                            getsubcat="";
                            if(output.contains("Added Successfully"))
                            {
                                Toast.makeText(Addsubcat.this, "Added Successfully", Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                Toast.makeText(Addsubcat.this, output+"", Toast.LENGTH_SHORT).show();
                            }


                        }
                    });readService.execute("addsubcat.php");

                }
            }
        });


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
            String z = (String)arg0[0];

            String link=getResources().getString(R.string.link)+z;


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