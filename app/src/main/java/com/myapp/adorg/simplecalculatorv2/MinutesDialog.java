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


public class MinutesDialog extends DialogFragment {

    private EditText editTextHH, editTextMM, editTextMinutes;
    private int finalTxInt;
    boolean isMinutesOnlyMode; // minutes int format if true, else HH:MM format
    private static final String ARG_DIALOG_TITLE = "dialog_title_Minutes_Dialog";
    public static final String EXTRA_MINUTE = "dialog_extra_minute";
    public static final String EXTRA_MINUTES_MODE = "dialog_extra_minutes_mode";

    public static MinutesDialog newMinutesDialog(String dialogTitle) {
        Bundle args = new Bundle();
        args.putString(ARG_DIALOG_TITLE, dialogTitle);
        MinutesDialog fragment = new MinutesDialog();
        fragment.setArguments(args);
        return fragment;
    }

    private void sendResult(int totalMinutes, boolean isMinutesOnlyMode) {
        if (getTargetFragment() == null) {
            return;
        }
        Intent intent = new Intent();
        intent.putExtra(EXTRA_MINUTE, totalMinutes);
        intent.putExtra(EXTRA_MINUTES_MODE, isMinutesOnlyMode);
        getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View v = LayoutInflater.from(getActivity())
                .inflate(R.layout.dialog_minutes, null);

        assert getArguments() != null;
        String strTitle = getArguments().getString(ARG_DIALOG_TITLE, "");
        editTextHH = v.findViewById(R.id.editTextDialogHH);
        editTextMM = v.findViewById(R.id.editTextDialogMM);
        editTextMinutes = v.findViewById(R.id.editTextDialogMinutes);

        editTextHH.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // disable minutes only EditText if HH has characters, otherwise enable
                if(!s.toString().equals("")) {
                    editTextMinutes.setFocusable(false);
                    isMinutesOnlyMode = false;
                }
                else {
                    if(editTextMM.getText().toString().equals("")){
                        editTextMinutes.setFocusable(true);
                        editTextMinutes.setFocusableInTouchMode(true);
                        isMinutesOnlyMode = true;
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
                    isMinutesOnlyMode = false;
                }
                else {
                    if(editTextHH.getText().toString().equals("")){
                        editTextMinutes.setFocusable(true);
                        editTextMinutes.setFocusableInTouchMode(true);
                        isMinutesOnlyMode = true;
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
                    isMinutesOnlyMode = true;
                }
                else {
                    editTextHH.setFocusable(true);
                    editTextHH.setFocusableInTouchMode(true);
                    editTextMM.setFocusable(true);
                    editTextMM.setFocusableInTouchMode(true);
                    isMinutesOnlyMode = false;
                }
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });

        return new AlertDialog.Builder(requireActivity(), R.style.MyDialogTheme)
                .setView(v)
                .setTitle(strTitle)
                .setPositiveButton(android.R.string.ok,
                        (dialog, which) -> {
                            if (isMinutesOnlyMode){
                                if(editTextMinutes.getText().toString().equals(""))
                                    finalTxInt = 0;
                                else finalTxInt = Integer.parseInt(editTextMinutes.getText().toString());
                            }else {
                                if(editTextHH.getText().toString().equals("")&editTextMM.getText().toString().equals("")){
                                    finalTxInt = 0;
                                }else if(editTextHH.getText().toString().equals("") & !editTextMM.getText().toString().equals("")){
                                    finalTxInt = Integer.parseInt(editTextMM.getText().toString());
                                }else if(!editTextHH.getText().toString().equals("") & editTextMM.getText().toString().equals("")) {
                                    finalTxInt = Integer.parseInt(editTextHH.getText().toString()) * 60;
                                }else finalTxInt = (Integer.parseInt(editTextHH.getText().toString()) * 60) + Integer.parseInt(editTextMM.getText().toString());
                            }
                            sendResult(finalTxInt, isMinutesOnlyMode);
                        })
                .setNegativeButton(android.R.string.cancel, (dialog, which) -> { })
                .create();
    }
}
