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
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;

public class ProductivityPicker extends DialogFragment {

    private NumberPicker mNumberPicker;
    private static final String ARG_PRODUCTIVITY = "productivity";
    public static final String EXTRA_PRODUCTIVITY =
            "com.adorgan.therapyproductivitycalculator.productivity";

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View v = LayoutInflater.from(getActivity())
                .inflate(R.layout.number_picker, null);

        double prodVal = getArguments().getDouble(ARG_PRODUCTIVITY);

        mNumberPicker = v.findViewById(R.id.layout_number_picker);
        mNumberPicker.setMinValue(1);
        mNumberPicker.setMaxValue(100);
        mNumberPicker.setValue((int) prodVal);


        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setTitle(R.string.set_productivity)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int productivity = mNumberPicker.getValue();
                        sendProdResult(Activity.RESULT_OK, productivity);
                    }
                })
                .create();
    }

    public static ProductivityPicker newInstance(double productivity){
        Bundle args = new Bundle();
        args.putDouble(ARG_PRODUCTIVITY, productivity);
        ProductivityPicker fragment = new ProductivityPicker();
        fragment.setArguments(args);
        return fragment;
    }
    private void sendProdResult(int resultCode, int mProductivity){
        if(getTargetFragment()==null){
            return;
        }
        Intent intent = new Intent();
        intent.putExtra(EXTRA_PRODUCTIVITY, mProductivity);

        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent);
    }



}