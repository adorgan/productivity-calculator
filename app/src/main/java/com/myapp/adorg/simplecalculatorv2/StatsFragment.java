package com.myapp.adorg.simplecalculatorv2;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;


public class StatsFragment extends DialogFragment {

    private static final String ARG_PAID_TIME = "paidTime";
    private static final String ARG_PRODUCTIVITY = "productivity";

    public StatsFragment() {
        // Required empty public constructor
    }

    public static StatsFragment newInstance(double paidTimes,double productivity) {
        StatsFragment fragment = new StatsFragment();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putDouble(ARG_PAID_TIME, paidTimes);
        args.putDouble(ARG_PRODUCTIVITY, productivity);

        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View v = LayoutInflater.from(getActivity())
                .inflate(R.layout.fragment_stats, null);

        TextView hoursWorkedView = v.findViewById(R.id.totalHoursWorkedTxt);
        TextView productivityView = v.findViewById(R.id.averageProdTxt);

        Bundle bundle = getArguments();
        assert bundle != null;
        double paidTimes = bundle.getDouble(ARG_PAID_TIME);
        double productivity = bundle.getDouble(ARG_PRODUCTIVITY);

        // calculate average productivity of all selected time cards
        String averageProductivity;
        if(paidTimes == 0)
            averageProductivity = "0";
        else {
            double averageProd = round((productivity / paidTimes));
            NumberFormat decimalFormat = new DecimalFormat("#0.0");
            NumberFormat noDecimal = new DecimalFormat("#0");
            if (Double.toString(averageProd).endsWith(".0")) {
                averageProductivity = noDecimal.format(averageProd);
            } else averageProductivity = decimalFormat.format(averageProd);
        }

        hoursWorkedView.setText("Total Paid Time: " +  ((int) paidTimes /60) + " hrs " + ((int) paidTimes %60) + " mins");
        productivityView.setText("Average Productivity: " + averageProductivity + "%");

        int resId;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            resId = R.style.MyDialogTheme;
        } else {
            resId = 0;
        }

        return new AlertDialog.Builder(getActivity(), resId)
                .setView(v)
                .setPositiveButton("OK", (dialog, which) -> { })
                .create();
    }
    private double round(double value) {
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(1, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}