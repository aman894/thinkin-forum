package com.aman.thinkin.planner.planner_components;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.aman.thinkin.R;

public class Planner_Reminder extends AppCompatActivity {
    String[] reminderCategory={"Select reminder","Call","Meet","Event"};
    int[] arrayImages={17170445,R.drawable.ic_contact_phone_black_48dp,R.drawable.ic_supervisor_account_black_48dp,R.drawable.ic_class_black_48dp};
    TextView txt_heading;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_planner_reminder);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_reminder);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_white_24dp);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        txt_heading= (TextView) findViewById(R.id.txt_remind_me);
        Spinner spinner= (Spinner) findViewById(R.id.spinner_reminder_category);
        spinner.setAdapter(new MyAdapter(Planner_Reminder.this,R.layout.reminder_spinner_row,reminderCategory));
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String title=((TextView)view.findViewById(R.id.txt_spinner_row)).getText().toString();
                if(title.equalsIgnoreCase(reminderCategory[0]))
                {
                    txt_heading.setText("Reminder for ...");
                }
                else{
                    txt_heading.setText("Reminder for "+title);
                    Snackbar.make(view,"Remind me to"+title,Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    public class MyAdapter extends ArrayAdapter<String>{
        public MyAdapter(Context context, int resource, String[] objects) {
            super(context, resource, objects);
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            return getCustomView(position,convertView,parent);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return getCustomView(position,convertView,parent);
        }

        public View getCustomView(int position, View convertView, ViewGroup parent){
            LayoutInflater inflater=getLayoutInflater();
            View row=inflater.inflate(R.layout.reminder_spinner_row, parent, false);
            TextView txt_category= (TextView) row.findViewById(R.id.txt_spinner_row);
            ImageView img_category= (ImageView) row.findViewById(R.id.img_spinner_row);

            txt_category.setText(reminderCategory[position]);
            img_category.setImageResource(arrayImages[position]);
            return row;
        }
    }
}
