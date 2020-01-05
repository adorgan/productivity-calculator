package com.myapp.adorg.simplecalculatorv2;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
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

    private EditText editTextMM, editTextHH, editTextMinutes;
    private String strTitle;
    private int finalTxInt, finalHH, finalMM;

    boolean blMinutes=false, blHHMM = false;
    private static final String ARG_DIALOG_TITLE = "dialog_title_Minutes_Dialog";
    public static final String EXTRA_MINUTE = "dialog_extra_minute";

    public static MinutesDialog newMinutesDialog(String dialogTitle) {
        Bundle args = new Bundle();
        args.putString(ARG_DIALOG_TITLE, dialogTitle);
        MinutesDialog fragment = new MinutesDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        View v = LayoutInflater.from(getActivity())
                .inflate(R.layout.dialog_minutes, null);

        strTitle = getArguments().getString(ARG_DIALOG_TITLE, "");
        editTextHH = v.findViewById(R.id.editTextDialogHH);
        editTextMM = v.findViewById(R.id.editTextDialogMM);
        editTextMinutes = v.findViewById(R.id.editTextDialogMinutes);



        editTextHH.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(!s.toString().equals("")) {
                    editTextMinutes.setFocusable(false);
                    blHHMM = true;
                }
                else {

                    if(editTextMM.getText().toString().equals("")){
                        editTextMinutes.setFocusable(true);
                        editTextMinutes.setFocusableInTouchMode(true);
                        blHHMM = false;
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {


            }
        });
        editTextMM.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().equals("")) {
                    editTextMinutes.setFocusable(false);
                    blHHMM = true;
                }
                else {
                    if(editTextHH.getText().toString().equals("")){
                        editTextMinutes.setFocusable(true);
                        editTextMinutes.setFocusableInTouchMode(true);
                        blHHMM = false;
                    }


                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        editTextMinutes.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().equals("")) {
                    editTextHH.setFocusable(false);
                    editTextMM.setFocusable(false);
                    blMinutes = true;
                }
                else {
                    editTextHH.setFocusable(true);
                    editTextHH.setFocusableInTouchMode(true);
                    editTextMM.setFocusable(true);
                    editTextMM.setFocusableInTouchMode(true);
                    blMinutes = false;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        return new AlertDialog.Builder(getActivity(), R.style.MyDialogTheme)
                .setView(v)
                .setTitle(strTitle)
                .setPositiveButton(android.R.string.ok,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                if(blMinutes){
                                    if(editTextMinutes.getText().toString().equals(""))
                                        finalTxInt = 0;
                                    else finalTxInt = Integer.parseInt(editTextMinutes.getText().toString());
                                }else if(blHHMM) {
                                    if(editTextHH.getText().toString().equals("")&editTextMM.getText().toString().equals("")){
                                        finalTxInt = 0;
                                    }else if(editTextHH.getText().toString().equals("") & !editTextMM.getText().toString().equals("")){
                                        finalTxInt = Integer.parseInt(editTextMM.getText().toString());
                                    }else if(!editTextHH.getText().toString().equals("") & editTextMM.getText().toString().equals("")) {
                                        finalTxInt = Integer.parseInt(editTextHH.getText().toString()) * 60;
                                    }else finalTxInt = (Integer.parseInt(editTextHH.getText().toString()) * 60) + Integer.parseInt(editTextMM.getText().toString());
                                }

                                sendResult(Activity.RESULT_OK, finalTxInt);
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
    private void sendResult(int resultCode, int txMins) {
        if (getTargetFragment() == null) {
            return;
        }
        Intent intent = new Intent();
        intent.putExtra(EXTRA_MINUTE, txMins);
        getTargetFragment()
                .onActivityResult(getTargetRequestCode(), resultCode, intent);
    }
}
