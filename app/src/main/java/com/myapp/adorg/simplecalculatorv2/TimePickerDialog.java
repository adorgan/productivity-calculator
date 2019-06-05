package com.myapp.adorg.simplecalculatorv2;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TimePicker;

public class TimePickerDialog extends DialogFragment {
    private static final String ARG_HOUR = "hour";
    private static final String ARG_MINUTE = "minute";
    private static final String ARG_24HOUR = "24Hour";

    public static final String EXTRA_HOUR =
            "com.adorgan.therapyproductivitycalculator.hour";
    public static final String EXTRA_MINUTE =
            "com.adorgan.therapyproductivitycalculator.minute";
    public static final String EXTRA_ISTWENTYFOUR =
            "com.adorgan.therapyproductivitycalculator.istwentyfour";
    private TimePicker mTimePicker;

    public static TimePickerDialog newInstance(int hour, int minute, boolean is24Hour) {
        Bundle args = new Bundle();
        args.putInt(ARG_HOUR, hour);
        args.putInt(ARG_MINUTE, minute);
        args.putBoolean(ARG_24HOUR, is24Hour);
        TimePickerDialog fragment = new TimePickerDialog();
        fragment.setArguments(args);
        return fragment;
    }


    private void sendTimeResult(int resultCode, int hour, int minute, boolean isTwentyFour){
        if(getTargetFragment()==null){
            return;
        }
        Intent intent = new Intent();
        intent.putExtra(EXTRA_HOUR, hour);
        intent.putExtra(EXTRA_MINUTE, minute);
        intent.putExtra(EXTRA_ISTWENTYFOUR, isTwentyFour);

        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        final int mHour = getArguments().getInt(ARG_HOUR);
        final int mMinute = getArguments().getInt(ARG_MINUTE);
        boolean is24Hour = getArguments().getBoolean(ARG_24HOUR);


        View v = LayoutInflater.from(getActivity())
                .inflate(R.layout.time_picker_dialog, null);

        mTimePicker = v.findViewById(R.id.time_picker);
        setTime(mHour, mMinute);
        if(is24Hour){
            mTimePicker.setIs24HourView(true);
        }else mTimePicker.setIs24HourView(false);


        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int hour;
                        int minute;



                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            hour = mTimePicker.getHour();
                            minute = mTimePicker.getMinute();
                        } else {
                            hour = mTimePicker.getCurrentHour() ;
                            minute = mTimePicker.getCurrentMinute();
                        }
                        boolean isTwentyFour = mTimePicker.is24HourView();

                        if(mHour==hour & mMinute == minute)
                            return;
                        else sendTimeResult(Activity.RESULT_OK, hour, minute, isTwentyFour);
                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                })
                .create();


    }
    private void setTime(int hour, int minute) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mTimePicker.setHour(hour);
            mTimePicker.setMinute(minute);
        } else {
            mTimePicker.setCurrentHour(hour);
            mTimePicker.setCurrentMinute(minute);
        }
    }

}

