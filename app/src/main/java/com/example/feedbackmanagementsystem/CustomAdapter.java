package com.example.feedbackmanagementsystem;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Trainee> trainees;
    public CustomAdapter(Context context, ArrayList<Trainee> trainees) {
        this.context = context;
        this.trainees = trainees;
    }

    @Override
    public int getCount() {
        return trainees.size();
    }
    @Override
    public Object getItem(int position) {
        return trainees.get(position);
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
        }
        TextView id = convertView.findViewById(R.id.item_id_value);
        TextView name = convertView.findViewById(R.id.item_name_value);
        TextView email = convertView.findViewById(R.id.item_email_value);
        TextView phone = convertView.findViewById(R.id.item_phone_value);
        TextView gender = convertView.findViewById(R.id.item_gender_value);


        Trainee trainee = trainees.get(position);
        name.setText(trainee.getName());
        email.setText(trainee.getEmail());
        phone.setText(trainee.getPhone());
        gender.setText(trainee.getGender());

        return convertView;
    }
}

