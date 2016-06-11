package com.aman.thinkin.planner.planner.planner_components.date_time_pickers;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.TextView;
import android.widget.TimePicker;

import com.aman.thinkin.R;

import java.util.Calendar;

/**
 * Created by 1305274 on 20-05-2016.
 */
public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener
{
    int hour,minutes;
    ActivityCommunicator activityCommunicator;
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // use the current time as the default values for the picker
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog=new TimePickerDialog(getActivity(), this, hour, minute, DateFormat.is24HourFormat(getActivity()));
        // create a new instance of TimePickerDialog and return it
        return timePickerDialog;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        activityCommunicator= (ActivityCommunicator) getActivity();
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        hour   = hourOfDay;
        minutes = minute;

        updateTime(hour,minute);
    }
    private void updateTime(int hours, int mins) {
        TextView pickTime= (TextView) getActivity().findViewById(R.id.btn_reminder_call_time);
        String timeSet = "";
        if (hours > 12) {
            hours -= 12;
            timeSet = "PM";
        } else if (hours == 0) {
            hours += 12;
            timeSet = "AM";
        } else if (hours == 12)
            timeSet = "PM";
        else
            timeSet = "AM";


        String minutes = "";
        if (mins < 10)
            minutes = "0" + mins;
        else
            minutes = String.valueOf(mins);

        // Append in a StringBuilder
        String aTime = new StringBuilder().append(hours).append(':')
                .append(minutes).append(" ").append(timeSet).toString();
        pickTime.setText(aTime);
    activityCommunicator.passTimetoActivity(aTime);
    }
}

