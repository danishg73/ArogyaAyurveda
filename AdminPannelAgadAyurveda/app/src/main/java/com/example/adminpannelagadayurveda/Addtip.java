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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
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

public class Addtip extends AppCompatActivity {
    Spinner spinner;
    String Response,gettitle,getdescription,getsubcat="";
    ArrayList<NameValuePair> list = new ArrayList<NameValuePair>();
    EditText title,description;
    ProgressDialog progressDialog;
    Button submit;
    RadioGroup radioGroup;
    ArrayList<String> arrayherb = new ArrayList<>();
    ArrayList<String> arraydisease= new ArrayList<>();
    ArrayAdapter<String> adapter;

    String category ="";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addtip);

        spinner =  findViewById(R.id.select_subcat);
        title =  findViewById(R.id.title);
        description =  findViewById(R.id.description);
        submit =  findViewById(R.id.submit);

        radioGroup=(RadioGroup)findViewById(R.id.radioGroup);
        arrayherb.add("Sub Category");
        arraydisease.add("Sub Category");
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Getting Subcategories");
        progressDialog.setCancelable(false);
        progressDialog.show();
        ImageView backarrow;
        backarrow = findViewById(R.id.backarrow);
        backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Addtip.super.onBackPressed();
            }
        });

        getsubcat();
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch(i) {
                    case R.id.radiodisease:
                        adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_item, arraydisease);
                        adapter.setDropDownViewResource(R.layout.spinner_item);
                        spinner.setAdapter(adapter);
                        category="disease";
                        break;
                    case R.id.radioherb:
                        adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_item, arrayherb);
                        spinner.setAdapter(adapter);
                        category="herb";
                        break;
                }
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                gettitle = title.getText().toString().trim();
                getdescription = description.getText().toString().trim();
                if(spinner.getSelectedItem() !=null)
                {
                    getsubcat=spinner.getSelectedItem().toString();
                }

                if(TextUtils.isEmpty(gettitle) || TextUtils.isEmpty(getdescription))
                {
                    Toast.makeText(Addtip.this, "All Fields are required", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    if(getsubcat.equals("Sub Category"))
                    {
                        getsubcat="";
                    }
                    list.add(new BasicNameValuePair("title",gettitle));
                    list.add(new BasicNameValuePair("description",getdescription));
                    list.add(new BasicNameValuePair("category",category));
                    list.add(new BasicNameValuePair("subcategory",getsubcat));
                    InputMethodManager imm =(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);


                    progressDialog.setMessage("Adding");
                    progressDialog.show();
                    uploadToServer readService= new  uploadToServer(new ReadServiceResponse() {

                        @Override
                        public void processFinish(String output)
                        {
                            progressDialog.dismiss();
                            title.setText("");
                            description.setText("");
                            radioGroup.clearCheck();
                            category="";
                            getsubcat="";
                            spinner.setAdapter(null);


                            if(output.contains("Added Successfully"))
                            {
                                Toast.makeText(Addtip.this, "Added Successfully", Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                Toast.makeText(Addtip.this, output+"", Toast.LENGTH_SHORT).show();
                            }


                        }
                    });readService.execute("addtip.php");




                }
            }
        });







    }
    public void getsubcat()
    {
        uploadToServer readService= new  uploadToServer(new ReadServiceResponse() {

            @Override
            public void processFinish(String output)
            {
                progressDialog.dismiss();

                if(output.contains("No details found"))
                {
                    Toast.makeText(Addtip.this, "Subcategories not found", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    insertdata(output);

                }


            }
        });readService.execute("getsubcategory_list.php");

    }

    public void insertdata(String output)
    {
        int h=1;
        int d =1;
        try {
            JSONArray jsonObject = new JSONArray(output);
            for (int i=0; i<jsonObject.length(); i++)
            {

                JSONObject obj = jsonObject.getJSONObject(i);
                if(obj.getString("category").trim().equals("herb"))
                {
                    arrayherb.add(obj.getString("subcategory").trim());

                }
                else if((obj.getString("category").trim().equals("disease")))
                {
                    arraydisease.add(obj.getString("subcategory").trim());
                }


            }



        } catch (JSONException e) {
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