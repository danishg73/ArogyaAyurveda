package com.example.arogyaayurveda;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.messaging.FirebaseMessaging;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.core.view.GravityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
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

public class Dashboard extends AppCompatActivity {

    long back_pressed;
    String Response;
    LinearLayout every_day_tips, fav_tips;
    List<dashboard_list> array_dashboard_list = new ArrayList<>();
    ListView listView;
    LinearLayout bydisease,byherbs;
    ProgressDialog progressDialog;
    SwipeRefreshLayout swipeRefreshLayout;
    ImageView bell;
    SharedPreferences sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setItemIconTintList(null);

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.

        every_day_tips = findViewById(R.id.every_day_tips);
        fav_tips = findViewById(R.id.fav_tips);
        listView = findViewById(R.id.listview);
        bydisease = findViewById(R.id.by_disease);
        byherbs = findViewById(R.id.byherbs);
        bell =  findViewById(R.id.bell);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getResources().getString(R.string.Loading));
        progressDialog.setCancelable(false);
        FirebaseMessaging.getInstance().subscribeToTopic("dailytip");
        sp = this.getSharedPreferences("data", MODE_PRIVATE);

        if( sp.contains("dashboard_data"))
        {
            insertdata(sp.getString("dashboard_data", "").trim());

        }
        else
        {

            progressDialog.show();
            getdata();
        }
        //------------------------------------------------------
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                // Handle navigation view item clicks here.
                int id = menuItem.getItemId();

                if (id == R.id.nav_daily_tips)
                {
                    Intent intent = new Intent(Dashboard.this, daily_tips.class);
                    startActivity(intent);
                }
                else if (id == R.id.nav_rating)
                {
//                    Intent intent = new Intent(Dashboard.this, give_rating.class);
//                    startActivity(intent);
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps")));
                }
                else if (id == R.id.nav_share)
                {
                    Intent i=new Intent(Intent.ACTION_SEND);
                    i.setType("text/plain");
                    i.putExtra(Intent.EXTRA_SUBJECT,getResources().getString(R.string.app_name));
                    i.putExtra(Intent.EXTRA_TEXT," get app by http:playstore.com/sasbajb");
                    startActivity(getIntent().createChooser(i, getResources().getString(R.string.Share)));
                }
                else if (id == R.id.nav_about_us)
                {
                    Intent intent = new Intent(Dashboard.this, about_us.class);
                    startActivity(intent);
                }
                else if (id == R.id.nav_privacy_policy)
                {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://peoplesblogger.com/?page_id=16"));
                    browserIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                     startActivity(browserIntent);

                }

                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);

                return false;
            }
        });

        //-----------------------------------------------------------




//        adapter_main = new categories_list(array_employeelist_punched, getBaseContext(), Manager_Dashboard.this, "punch", manager_username);
//        categories_list.setAdapter(null);
//        categories_list.setAdapter(adapter_main);
//        adapter_main.notifyDataSetChanged();
        bell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                Intent intent = new Intent(Dashboard.this, daily_tips.class);
                startActivity(intent);

            }
        });
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh()
            {
                array_dashboard_list.clear();
                getdata();
            }
        });
        fav_tips.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(Dashboard.this, Favourites.class);
                startActivity(intent);
            }
        });

        every_day_tips.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(Dashboard.this, daily_tips.class);
                startActivity(intent);
            }
        });
        bydisease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Dashboard.this,Bydisease.class);
                startActivity(intent);
            }
        });
        byherbs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Dashboard.this,Byherbs.class);
                startActivity(intent);
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Intent intent = new Intent(Dashboard.this, Detail_page.class);
                intent.putExtra("title",  array_dashboard_list.get(position).title);
                intent.putExtra("description",  array_dashboard_list.get(position).description);
                intent.putExtra("id",  array_dashboard_list.get(position).id);
                intent.putExtra("timestamp",  array_dashboard_list.get(position).timestamp);
                intent.putExtra("subcategory",  array_dashboard_list.get(position).subcategory);
                intent.putExtra("category",  array_dashboard_list.get(position).category);
                intent.putExtra("favourite",  "");
                startActivity(intent);
            }
        });




    }
    public void getdata()
    {

        uploadToServer readService= new uploadToServer(new ReadServiceResponse() {

            @Override
            public void processFinish(String output)
            {
                progressDialog.dismiss();
                if(output.contains("No details found"))
                {
                    Toast.makeText(Dashboard.this, getResources().getString(R.string.nodetails), Toast.LENGTH_SHORT).show();
                }
                else
                {
                    SharedPreferences.Editor edit = sp.edit();
                    edit.putString("dashboard_data", output);
                    edit.commit();
                    insertdata(output);
                }


            }
        });readService.execute();
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
        swipeRefreshLayout.setRefreshing(false);




    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        return true;
    }

    @Override
    public void onBackPressed() {
        if (back_pressed + 1000 > System.currentTimeMillis())
        {
            super.onBackPressed();
        }
        else{
            exit();
//            Toast.makeText(getBaseContext(),
//                    "Press once again to exit!", Toast.LENGTH_SHORT)
//                    .show();
        }
        back_pressed = System.currentTimeMillis();
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

            String link=getResources().getString(R.string.link)+"getdetails.php";


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
    public void exit()
    {

        AlertDialog.Builder builder = new AlertDialog.Builder(Dashboard.this);
        LayoutInflater inflater = ((Dashboard) Dashboard.this).getLayoutInflater();
        View dialogLayout = inflater.inflate(R.layout.custom_exit,
                null);

        final AlertDialog dialog = builder.create();
        dialog.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        dialog.setView(dialogLayout, 0, 0, 0, 0);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        WindowManager.LayoutParams wlmp = dialog.getWindow()
                .getAttributes();
        wlmp.gravity = Gravity.CENTER_VERTICAL;


        Button share =   dialogLayout.findViewById(R.id.share);
        Button rating =   dialogLayout.findViewById(R.id.rating);
        Button homepage =   dialogLayout.findViewById(R.id.homepage);
        Button exit =   dialogLayout.findViewById(R.id.exit);


        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent i=new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_SUBJECT,getResources().getString(R.string.app_name));
                i.putExtra(Intent.EXTRA_TEXT," get app by http:playstore.com/sasbajb");
                startActivity(getIntent().createChooser(i, getResources().getString(R.string.Share)));

            }
        });

        rating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps")));
            }
        });

        homepage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                dialog.dismiss();

            }
        });

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveTaskToBack(true);
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(1);

            }
        });


        builder.setView(dialogLayout);

        dialog.show();

    }


}