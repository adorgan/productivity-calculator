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


public class ChangeNumberDialog extends DialogFragment {

    private EditText editTextMM, editTextHH, editTextMinutes;
    private String strTitle;
    private int finalTxInt, finalHH, finalMM;

    boolean blMinutes=false, blHHMM = false;

    private int newMinuteAmount;
    private double newTotalAmount;
    private static final String ARG_MINUTES = "minutes";
    public static final String EXTRA_Minutes =
            "com.adorgan.therapyproductivitycalculator.minutes";
    private static final String ARG_TOTAL = "totalminutes";
    private static final String ARG_STRING_TITLE = "string_title";
    public static final String EXTRA_TOTAL =
            "com.adorgan.therapyproductivitycalculator.total";


    public static ChangeNumberDialog newInstance(String title, int minutes, double endtotalmins) {
        Bundle args = new Bundle();
        args.putInt(ARG_MINUTES, minutes);
        args.putDouble(ARG_TOTAL, endtotalmins);
        args.putString(ARG_STRING_TITLE, title);
        ChangeNumberDialog fragment = new ChangeNumberDialog();
        fragment.setArguments(args);
        return fragment;
    }


    private void sendMinuteResult(int resultCode, int minutes, double total){
        if(getTargetFragment()==null){
            return;
        }
        Intent intent = new Intent();
        intent.putExtra(EXTRA_Minutes, minutes);
        intent.putExtra(EXTRA_TOTAL, total);
        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View v = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_minutes, null);

        //determine original value of End Time before new minutes are added in case they are changed
        //multiple times.
        newMinuteAmount = getArguments().getInt(ARG_MINUTES);
        newTotalAmount = getArguments().getDouble(ARG_TOTAL) - newMinuteAmount;
        String title = getArguments().getString(ARG_STRING_TITLE);

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
                .setTitle(title)
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

                                newMinuteAmount = finalTxInt;
                                sendMinuteResult(Activity.RESULT_OK, newMinuteAmount, newTotalAmount);
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

}
