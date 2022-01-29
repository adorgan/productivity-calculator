package com.myapp.adorg.simplecalculatorv2;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.appcompat.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;

public class ProductivityPicker extends DialogFragment {

    private NumberPicker mNumberPicker;
    private static final String ARG_PRODUCTIVITY = "arg_productivity";
    public static final String EXTRA_PRODUCTIVITY = "therapyproductivitycalculator.productivity";

    public static ProductivityPicker newInstance(double productivity){
        Bundle args = new Bundle();
        args.putDouble(ARG_PRODUCTIVITY, productivity);
        ProductivityPicker fragment = new ProductivityPicker();
        fragment.setArguments(args);
        return fragment;
    }
    private void sendProdResult(int mProductivity){
        if(getTargetFragment()==null){
            return;
        }
        Intent intent = new Intent();
        intent.putExtra(EXTRA_PRODUCTIVITY, mProductivity);
        getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View v = LayoutInflater.from(getActivity())
                .inflate(R.layout.number_picker, null);

        assert getArguments() != null;
        double productivityValue = getArguments().getDouble(ARG_PRODUCTIVITY);

        mNumberPicker = v.findViewById(R.id.layout_number_picker);
        mNumberPicker.setMinValue(1);
        mNumberPicker.setMaxValue(150);
        mNumberPicker.setValue((int) productivityValue);

        return new AlertDialog.Builder(requireActivity(), R.style.MyDialogTheme)
                .setView(v)
                .setTitle(R.string.set_productivity)
                .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                    sendProdResult(mNumberPicker.getValue());
                })
                .create();
    }
}