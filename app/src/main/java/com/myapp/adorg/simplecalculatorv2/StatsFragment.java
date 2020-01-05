package com.myapp.adorg.simplecalculatorv2;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;


public class StatsFragment extends DialogFragment {

    private static final String ARG_PAID_TIME = "paidTime";
    private static final String ARG_PRODUCTIVITY = "productivity";
    private TextView hoursWorkedView, productivityView;
    private double paidTimes;
    private double productivity;
    private double averageProd;
    private String prod;


    public StatsFragment() {
        // Required empty public constructor
    }

    public static StatsFragment newInstance(double paidTimes,double productivity) {
        StatsFragment f = new StatsFragment();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putDouble(ARG_PAID_TIME, paidTimes);
        args.putDouble(ARG_PRODUCTIVITY, productivity);

        f.setArguments(args);

        return f;
    }



    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View v = LayoutInflater.from(getActivity())
                .inflate(R.layout.fragment_stats, null);

        hoursWorkedView = v.findViewById(R.id.totalHoursWorkedTxt);
        productivityView = v.findViewById(R.id.averageProdTxt);

        Bundle bundle = getArguments();
        paidTimes = bundle.getDouble(ARG_PAID_TIME);
        productivity = bundle.getDouble(ARG_PRODUCTIVITY);


        if(paidTimes == 0)
            prod = "0";
        else {
            averageProd = round((productivity / paidTimes), 1);
            NumberFormat decimalFormat = new DecimalFormat("#0.0");
            NumberFormat noDecimal = new DecimalFormat("#0");
            if (Double.toString(averageProd).endsWith(".0")) {
                prod = noDecimal.format(averageProd);
            } else prod = decimalFormat.format(averageProd);
        }

        hoursWorkedView.setText("Total Paid Time: " +  ((int)paidTimes/60) + " hrs " + ((int)paidTimes%60) + " mins");
        productivityView.setText("Average Productvity: " + prod + "%");

        int resId;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            resId = R.style.MyDialogTheme;
        } else {
            resId = 0;
        }

        return new AlertDialog.Builder(getActivity(), resId)
                .setView(v)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                })


                .create();
    }
    private double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}