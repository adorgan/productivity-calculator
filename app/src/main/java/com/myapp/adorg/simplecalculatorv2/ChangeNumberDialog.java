package com.myapp.adorg.simplecalculatorv2;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.appcompat.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;


public class ChangeNumberDialog extends DialogFragment {

    private EditText editTextMM, editTextHH, editTextMinutes;
    private boolean usingMinutes = false;
    private boolean usingHoursMinutes = false;
    private int newMinuteAmount;
    private double newTotalAmount;
    private static final String ARG_MINUTES = "arg_change_minutes";
    private static final String ARG_TOTAL = "arg_change_total_minutes";
    private static final String ARG_STRING_TITLE = "arg_string_title";
    public static final String EXTRA_Minutes = "com.adorgan.therapyproductivitycalculator.minutes";
    public static final String EXTRA_TOTAL = "com.adorgan.therapyproductivitycalculator.total";

    public static ChangeNumberDialog newInstance(String title, int minutes, double endTotalMins) {
        Bundle args = new Bundle();
        args.putInt(ARG_MINUTES, minutes);
        args.putDouble(ARG_TOTAL, endTotalMins);
        args.putString(ARG_STRING_TITLE, title);
        ChangeNumberDialog fragment = new ChangeNumberDialog();
        fragment.setArguments(args);
        return fragment;
    }

    private void sendMinuteResult(int minutes, double total){
        if(getTargetFragment()==null){
            return;
        }
        Intent intent = new Intent();
        intent.putExtra(EXTRA_Minutes, minutes);
        intent.putExtra(EXTRA_TOTAL, total);
        getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View v = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_minutes, null);

        //determine original value of End Time before new minutes are added in case they are changed
        //multiple times.
        assert getArguments() != null;
        newMinuteAmount = getArguments().getInt(ARG_MINUTES);
        newTotalAmount = getArguments().getDouble(ARG_TOTAL) - newMinuteAmount;
        String title = getArguments().getString(ARG_STRING_TITLE);

        editTextHH = v.findViewById(R.id.editTextDialogHH);
        editTextMM = v.findViewById(R.id.editTextDialogMM);
        editTextMinutes = v.findViewById(R.id.editTextDialogMinutes);

        if (newMinuteAmount != 0){
            editTextHH.setText(String.valueOf(newMinuteAmount / 60));
            editTextMM.setText(String.valueOf(newMinuteAmount % 60));
        }

        editTextHH.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().equals("")) {
                    editTextMinutes.setFocusable(false);
                    usingHoursMinutes = true;
                }
                else {
                    if(editTextMM.getText().toString().equals("")){
                        editTextMinutes.setFocusable(true);
                        editTextMinutes.setFocusableInTouchMode(true);
                        usingHoursMinutes = false;
                    }
                }
            }
            @Override
            public void afterTextChanged(Editable s) { }
        });

        editTextMM.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().equals("")) {
                    editTextMinutes.setFocusable(false);
                    usingHoursMinutes = true;
                }
                else {
                    if(editTextHH.getText().toString().equals("")){
                        editTextMinutes.setFocusable(true);
                        editTextMinutes.setFocusableInTouchMode(true);
                        usingHoursMinutes = false;
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });

        editTextMinutes.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().equals("")) {
                    editTextHH.setFocusable(false);
                    editTextMM.setFocusable(false);
                    usingMinutes = true;
                }
                else {
                    editTextHH.setFocusable(true);
                    editTextHH.setFocusableInTouchMode(true);
                    editTextMM.setFocusable(true);
                    editTextMM.setFocusableInTouchMode(true);
                    usingMinutes = false;
                }
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });

        return new AlertDialog.Builder(requireActivity(), R.style.MyDialogTheme)
                .setView(v)
                .setTitle(title)
                .setPositiveButton(android.R.string.ok,
                        (dialog, which) -> {
                            if(usingMinutes){
                                if(editTextMinutes.getText().toString().equals(""))
                                    newMinuteAmount = 0;
                                else newMinuteAmount = Integer.parseInt(editTextMinutes.getText().toString());
                            }else if(usingHoursMinutes) {
                                if(editTextHH.getText().toString().equals("")&editTextMM.getText().toString().equals("")){
                                    newMinuteAmount = 0;
                                }else if(editTextHH.getText().toString().equals("") & !editTextMM.getText().toString().equals("")){
                                    newMinuteAmount = Integer.parseInt(editTextMM.getText().toString());
                                }else if(!editTextHH.getText().toString().equals("") & editTextMM.getText().toString().equals("")) {
                                    newMinuteAmount = Integer.parseInt(editTextHH.getText().toString()) * 60;
                                }else newMinuteAmount = (Integer.parseInt(editTextHH.getText().toString()) * 60) + Integer.parseInt(editTextMM.getText().toString());
                            }
                            sendMinuteResult(newMinuteAmount, newTotalAmount);
                        })
                .setNegativeButton(android.R.string.cancel, (dialog, which) -> { })
                .create();
    }
}
