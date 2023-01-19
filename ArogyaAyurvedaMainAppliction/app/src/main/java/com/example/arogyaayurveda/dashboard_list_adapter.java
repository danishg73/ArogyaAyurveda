package com.example.arogyaayurveda;

import android.app.Activity;
import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;



public class dashboard_list_adapter extends BaseAdapter {
    Context context;
    List<dashboard_list> valueList;
    static int position = 0;
    Dashboard mainActivity;

    Subcategory mainActivity2;
    Favourites mainActivity3;
    daily_tips mainActivity4;
    String classtype="" ;
    Calendar c = Calendar.getInstance();
    ViewEM finalViewItem1;







    public dashboard_list_adapter(List<dashboard_list> listValue, Context context, Dashboard mainActivity ) {
        this.context = context;
        this.valueList = listValue;
        this.mainActivity = mainActivity;
    }
    public dashboard_list_adapter(List<dashboard_list> listValue, Context context,  Subcategory mainActivity2 ) {
        this.context = context;
        this.valueList = listValue;
        this.mainActivity2 = mainActivity2;
    }
    public dashboard_list_adapter(List<dashboard_list> listValue, Context context,  Favourites mainActivity3 ) {
        this.context = context;
        this.valueList = listValue;
        this.mainActivity3 = mainActivity3;
    }
    public dashboard_list_adapter(List<dashboard_list> listValue, Context context,  daily_tips mainActivity4,String classtype ) {
        this.context = context;
        this.valueList = listValue;
        this.classtype = classtype;
        this.mainActivity4 = mainActivity4;
    }


    @Override
    public int getCount() {
        return this.valueList.size();
    }

    @Override
    public Object getItem(int position) {
        return this.valueList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {

        return position;
    }

    @Override
    public int getViewTypeCount() {

        if (getCount() > 0) {
            return getCount();
        } else {
            return super.getViewTypeCount();
        }
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        ViewEM viewItem = null;
        dashboard_list_adapter.position=position;

        if(convertView == null)
        {
            viewItem = new ViewEM();

            LayoutInflater layoutInfiater = (LayoutInflater)this.context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

            if(classtype.contains("dailytip"))
            {
                convertView = layoutInfiater.inflate(R.layout.custom_daily_tip, null);
                viewItem.description = convertView.findViewById(R.id.description);
                viewItem.title = convertView.findViewById(R.id.title_list);
                convertView.setTag(viewItem);
                viewItem.title.setText(valueList.get(position).title);
                viewItem.description.setText(valueList.get(position).description);
            }
            else
            {
                convertView = layoutInfiater.inflate(R.layout.custom_dashboard_list, null);
                viewItem.title = convertView.findViewById(R.id.title_list);
                convertView.setTag(viewItem);
                viewItem.title.setText(valueList.get(position).title);
            }



            finalViewItem1 = viewItem;


            ViewEM finalViewItem = viewItem;





        }
        else
        {
            viewItem = (ViewEM) convertView.getTag();
        }

        return convertView;
    }






}

class ViewEM {
    TextView title;
    TextView description;
}

