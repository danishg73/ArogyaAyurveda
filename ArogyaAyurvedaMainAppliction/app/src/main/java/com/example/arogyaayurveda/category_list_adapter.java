package com.example.arogyaayurveda;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;


//public class category_list_adapter extends BaseAdapter {


public class category_list_adapter extends RecyclerView.Adapter<category_list_adapter.ViewHolder>{
    List<category_list> listdata;

    public category_list_adapter(List<category_list> listdata) {
        this.listdata = listdata;
    }

    // RecyclerView recyclerView;
//    public MyListAdapter(MyListData[] listdata) {
//        this.listdata = listdata;
//    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.custom_category, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
//        final category_list myListData = listdata[position];
        holder.category_name.setText(listdata.get(position).subcategory);

        holder.category_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), Subcategory.class);
                intent.putExtra("subcategory",listdata.get(position).subcategory);
                intent.putExtra("parentcategory",listdata.get(position).parentcategory);
                intent.putExtra("id",listdata.get(position).category_id);
                view.getContext().startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return listdata.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView category_name;
        public LinearLayout category_item;
        public ViewHolder(View itemView) {
            super(itemView);
            this.category_name = (TextView) itemView.findViewById(R.id.category_name);
            this.category_item = (LinearLayout) itemView.findViewById(R.id.category_item);
        }
    }
}