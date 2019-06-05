package com.myapp.adorg.simplecalculatorv2;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;


public class ChangeNumberDialog extends DialogFragment {

    private int newMinuteAmount;
    private double newTotalAmount;
    private static final String ARG_MINUTES = "minutes";
    public static final String EXTRA_Minutes =
            "com.adorgan.therapyproductivitycalculator.minutes";
    private static final String ARG_TOTAL = "totalminutes";
    public static final String EXTRA_TOTAL =
            "com.adorgan.therapyproductivitycalculator.total";


    public static ChangeNumberDialog newInstance(int minutes, double endtotalmins) {
        Bundle args = new Bundle();
        args.putInt(ARG_MINUTES, minutes);
        args.putDouble(ARG_TOTAL, endtotalmins);
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
        View v = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_change_number, null);

        //determine original value of End Time before new minutes are added in case they are changed
        //multiple times.
        newMinuteAmount = getArguments().getInt(ARG_MINUTES);
        newTotalAmount = getArguments().getDouble(ARG_TOTAL) - newMinuteAmount;

        EditText editText = v.findViewById(R.id.editTextChangeNumber);
        editText.requestFocus();
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    newMinuteAmount = Integer.parseInt(s.toString());
                }catch (NumberFormatException e){
                    newMinuteAmount = 0;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
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
