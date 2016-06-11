package com.aman.thinkin.planner.planner.planner_components.date_time_pickers;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.TextView;

import com.aman.thinkin.R;

import java.util.Calendar;

/**
 * Created by 1305274 on 20-05-2016.
 */
public class SelectDateFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        ActivityCommunicator activityCommunicator;
@Override
public Dialog onCreateDialog(Bundle savedInstanceState) {
final Calendar calendar = Calendar.getInstance();
        int yy = calendar.get(Calendar.YEAR);
        int mm = calendar.get(Calendar.MONTH);
        int dd = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog= new DatePickerDialog(getActivity(), this, yy, mm, dd);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        return datePickerDialog;
        }

        @Override
        public void onAttach(Activity activity) {
                super.onAttach(activity);
                activityCommunicator= (ActivityCommunicator) getActivity();
        }

        public void onDateSet(DatePicker view, int yy, int mm, int dd) {
        populateSetDate(yy, mm+1, dd);
        }
public void populateSetDate(int year, int month, int day) {
        TextView pickDate= (TextView)getActivity().findViewById(R.id.btn_reminder_call_date);
        pickDate.setText(day+"/"+month+"/"+year);
        activityCommunicator.passDatetoActivity(pickDate.getText().toString());
        }

        }
