package com.aman.thinkin.planner.planner.planner_components;

import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.aman.thinkin.R;
import com.aman.thinkin.planner.planner.adapter.MeetingAdapter;
import com.aman.thinkin.planner.planner.planner_components.date_time_pickers.ActivityCommunicator;
import com.aman.thinkin.planner.planner.planner_components.date_time_pickers.SelectDateFragment;
import com.aman.thinkin.planner.planner.planner_components.date_time_pickers.TimePickerFragment;
import com.firebase.client.AuthData;
import com.firebase.client.Firebase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class Planner_Reminder extends AppCompatActivity implements ActivityCommunicator {
    private static String FIREBASE_URL = "https://think-in.firebaseio.com";
    String[] reminderCategory={"Select reminder","Call","Meet","Event"};
    int[] arrayImages={17170445, R.drawable.ic_contact_phone_black_48dp, R.drawable.ic_supervisor_account_black_48dp, R.drawable.ic_class_black_48dp};
    private TextView txt_heading,txt_teacherName,txt_teacherCabin;
    private Spinner spinner;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private EditText editText_Note;
    private TextView pickDate,pickTime;
    private Button btn_call,btn_meet,btn_savetofirebase;
    private int year;
    private int month;
    private int day;
    private String meet_time,meet_date,name,cabin,mobileNo;
    private Intent intent;
    private Firebase mRoot,mChild;
    private AuthData authData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_planner_reminder);
        Firebase.setAndroidContext(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_reminder);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_white_24dp);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        /**initialize variables*/
        setUpVariables();

       /**
        * Spinner initialized in setUpVariables()
        * custom spinner has been set using Custom Adapter i.e. MyAdapter class*/
        spinner.setAdapter(new MyAdapter(Planner_Reminder.this, R.layout.reminder_spinner_row,reminderCategory));
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
                    Snackbar.make(view,"Remind me to"+title, Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mRecyclerView= (RecyclerView)findViewById(R.id.teacher_call_reminder_list);

        mLayoutManager=new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter=new MeetingAdapter(this);
        mRecyclerView.setAdapter(mAdapter);

        /**
         * Section section of code is for Date picking*/
        final Calendar c=Calendar.getInstance();
        year=c.get(Calendar.YEAR);
        month=c.get(Calendar.MONTH);
        day=c.get(Calendar.DAY_OF_MONTH);

        pickDate= (TextView) findViewById(R.id.btn_reminder_call_date);
        pickDate.setText(new StringBuilder().append(day).append("/").append(month + 1).append("/").append(year).append(" "));
        pickDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new SelectDateFragment();
                newFragment.show(getFragmentManager(), "tag");
            }
        });
        meet_date=pickDate.getText().toString();


        /**
         * This section of code is for Time picking*/
        Date d=new Date();
        SimpleDateFormat sdf=new SimpleDateFormat("hh:mm a");
        String currentDateTimeString = sdf.format(d);
        pickTime= (TextView) findViewById(R.id.btn_reminder_call_time);
        pickTime.setText(currentDateTimeString);
        pickTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new TimePickerFragment();
                newFragment.show(getFragmentManager(), "tag");
            }
        });
        meet_time=currentDateTimeString;


        intent=getIntent();
        name=intent.getStringExtra("AdminName");
        cabin=intent.getStringExtra("AdminCabin");
        mobileNo=intent.getStringExtra("AdminMobile");

        txt_teacherName.setText(name);
        txt_teacherCabin.setText(cabin);

        btn_meet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(txt_teacherName.getText().toString().equals("Teacher Name")){
                    Toast.makeText(Planner_Reminder.this, "Select a member from list", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(v.getContext(), "Meeting fixed with " + name + "\nMeeting Date:" + meet_date + "\nMeeting Time:" + meet_time + "", Toast.LENGTH_LONG).show();
                }

            }
        });
        btn_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(name==null){
                    Toast.makeText(Planner_Reminder.this, "Select a member from list", Toast.LENGTH_SHORT).show();
                }
                else if(name.equals(txt_teacherName.getText().toString())){
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + mobileNo));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    v.getContext().startActivity(intent);
                }
            }
        });
        mRoot=new Firebase(FIREBASE_URL);
        btn_savetofirebase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                authData=mRoot.getAuth();
                mChild=mRoot.child("gourav").child("planner").push();
                mChild.child("userID").setValue(authData.getUid());
                mChild.child("reminder").setValue( meet_date + " Meet "+name+" Meeting Time " + meet_time);
                mChild.child("note").setValue(editText_Note.getText().toString());
            }
        });

    }

    private void setUpVariables(){
        btn_savetofirebase= (Button) findViewById(R.id.btn_save_data);
        txt_heading= (TextView) findViewById(R.id.txt_remind_me);
        spinner= (Spinner) findViewById(R.id.spinner_reminder_category);
        btn_call= (Button)findViewById(R.id.btn_call_meeting);
        btn_meet= (Button)findViewById(R.id.btn_fix_meeting);
        txt_teacherName= (TextView) findViewById(R.id.txt_call_teacher_name);
        txt_teacherCabin= (TextView) findViewById(R.id.txt_call_teacher_cabin);
        editText_Note= (EditText) findViewById(R.id.edt_planner_note);
    }

    @Override
    public void passTimetoActivity(String someTalue) {
        meet_time=someTalue;
    }

    @Override
    public void passDatetoActivity(String someDate) {
        meet_date=someDate;
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
