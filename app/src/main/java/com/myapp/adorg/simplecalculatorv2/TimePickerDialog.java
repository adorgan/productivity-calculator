package com.myapp.adorg.simplecalculatorv2;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TimePicker;

public class TimePickerDialog extends DialogFragment {
    private TimePicker mTimePicker;
    private static final String ARG_HOUR = "hour";
    private static final String ARG_MINUTE = "minute";
    private static final String ARG_24HOUR = "24Hour";
    public static final String EXTRA_HOUR = "therapyproductivitycalculator.hour";
    public static final String EXTRA_MINUTE = "therapyproductivitycalculator.minute";
    public static final String EXTRA_ISTWENTYFOUR = "therapyproductivitycalculator.istwentyfour";

    public static TimePickerDialog newInstance(int hour, int minute, boolean is24Hour) {
        Bundle args = new Bundle();
        args.putInt(ARG_HOUR, hour);
        args.putInt(ARG_MINUTE, minute);
        args.putBoolean(ARG_24HOUR, is24Hour);
        TimePickerDialog fragment = new TimePickerDialog();
        fragment.setArguments(args);
        return fragment;
    }

    private void sendTimeResult(int hour, int minute, boolean isTwentyFour){
        if (getTargetFragment()==null){
            return;
        }
        Intent intent = new Intent();
        intent.putExtra(EXTRA_HOUR, hour);
        intent.putExtra(EXTRA_MINUTE, minute);
        intent.putExtra(EXTRA_ISTWENTYFOUR, isTwentyFour);
        getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View v = LayoutInflater.from(getActivity())
                .inflate(R.layout.time_picker_dialog, null);

        mTimePicker = v.findViewById(R.id.time_picker);
        if (Preferences.getDarkMode(getContext())) {
            mTimePicker.setBackgroundColor(getResources().getColor(R.color.darkGray));
        }

        assert getArguments() != null;
        final int parentHour = getArguments().getInt(ARG_HOUR);
        final int parentMinute = getArguments().getInt(ARG_MINUTE);
        boolean is24HourMode = getArguments().getBoolean(ARG_24HOUR);

        setTime(parentHour, parentMinute, is24HourMode);

        return new AlertDialog.Builder(getActivity(), 0)
                .setView(v)
                .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                    int newHour;
                    int newMinute;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        newHour = mTimePicker.getHour();
                        newMinute = mTimePicker.getMinute();
                    } else {
                        newHour = mTimePicker.getCurrentHour() ;
                        newMinute = mTimePicker.getCurrentMinute();
                    }
                    boolean isTwentyFour = mTimePicker.is24HourView();

                    // don't send time result if nothing changes
                    if(parentHour != newHour || parentMinute != newMinute){
                        sendTimeResult(newHour, newMinute, isTwentyFour);
                    }
                })
                .setNegativeButton(android.R.string.cancel, (dialog, which) -> { })
                .create();
    }

    /**
     * set TimePicker time state
     * @param hour hour value
     * @param minute minutes value
     * @param is24HourMode 24 hr clock if true, else AM/PM clock
     */
    private void setTime(int hour, int minute, boolean is24HourMode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mTimePicker.setHour(hour);
            mTimePicker.setMinute(minute);
        } else {
            mTimePicker.setCurrentHour(hour);
            mTimePicker.setCurrentMinute(minute);
        }
        mTimePicker.setIs24HourView(is24HourMode);
    }

}

