package com.myapp.adorg.simplecalculatorv2;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.appcompat.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;


public class DatePickerDialog extends DialogFragment {
    private static final String ARG_DATE = "arg_date_picker_date";
    private DatePicker mDatePicker;
    public static final String EXTRA_DATE = "com.myapp.adorg.simplecalculatorv.extra.date";

    public static DatePickerDialog newInstance(Date date) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_DATE, date);
        DatePickerDialog fragment = new DatePickerDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View v = LayoutInflater.from(getActivity())
                .inflate(R.layout.fragment_date_picker_dialog, null);
        mDatePicker = v.findViewById(R.id.dialog_date_picker);
        if (Preferences.getDarkMode(getContext())) {
            mDatePicker.setBackgroundColor(getResources().getColor(R.color.darkGray));
        }

        // give date picker state when user pressed button to change
        assert getArguments() != null;
        Date date = (Date) getArguments().getSerializable(ARG_DATE);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        mDatePicker.init(year, month, day, null);

        return new AlertDialog.Builder(requireActivity(), R.style.MyDialogTheme)
                .setView(v)
                .setPositiveButton(android.R.string.ok,
                        (dialog, which) -> {
                            Date newDate = new GregorianCalendar(mDatePicker.getYear(),
                                    mDatePicker.getMonth(), mDatePicker.getDayOfMonth()).getTime();
                            sendResult(newDate);
                        })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) { }
                })
                .create();

    }
    private void sendResult(Date date) {
        if (getTargetFragment() == null) {
            return;
        }
        Intent intent = new Intent();
        intent.putExtra(EXTRA_DATE, date);
        getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);
    }
}
