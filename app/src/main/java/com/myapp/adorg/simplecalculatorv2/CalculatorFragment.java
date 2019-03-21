package com.myapp.adorg.simplecalculatorv2;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class CalculatorFragment extends Fragment {
    private int mHour, mMinute, mTreatmentMins, mUnpaidMins, mPaidMins;
    private double mProductivity;
    private boolean mIsTwentyFour = false;
    private TextView mStartTimeView, mProductivityView, treatmentMinutes, unpaidMins, paidMins;


    private Button mCalculateButton;
    private static final String DIALOG_PROD = "DialogProd";
    private static final String DIALOG_START_TIME = "DialogStartTime";
    private static final int REQUEST_TIME = 0;
    private static final int REQUEST_PROD = 1;
    private Calendar c= Calendar.getInstance();
    private Calendar d= Calendar.getInstance();
    private Calendar e;
    private TimeCard mTimeCard;
    private SimpleDateFormat df24 = new SimpleDateFormat("HH:mm");
    private SimpleDateFormat df12 = new SimpleDateFormat("h:mm a");


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container,false);

        mTimeCard = new TimeCard();

        mIsTwentyFour = Preferences.get24Hr(getActivity());
        if(savedInstanceState!=null){
            mIsTwentyFour = savedInstanceState.getBoolean("ISTWENTYFOUR");
            mHour = savedInstanceState.getInt("HOUR");
            mMinute = savedInstanceState.getInt("MINUTE");


        }
        else {
            mHour = c.get(Calendar.HOUR_OF_DAY);
            mMinute =c.get(Calendar.MINUTE);

        }
        c.set(0,0,0, mHour, mMinute);

        // User enters start time

        mStartTimeView = view.findViewById(R.id.txtViewStartTime);
        mStartTimeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();
                com.myapp.adorg.simplecalculatorv2.TimePickerDialog dialog = com.myapp.adorg.simplecalculatorv2.TimePickerDialog.newInstance(mHour, mMinute, mIsTwentyFour);
                dialog.setTargetFragment(CalculatorFragment.this, REQUEST_TIME);
                dialog.show(fm,DIALOG_START_TIME);
            }
        });
        SimpleDateFormat df;
        if(mIsTwentyFour) {
            df = df24;
        }
        else df = df12;
        mStartTimeView.setText(df.format(c.getTime()));

        //Set Productivity
        mProductivityView = view.findViewById(R.id.txtViewProductivity);
        mProductivityView.setText(getProdString());
        mProductivityView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();
                ProductivityPicker dialog = ProductivityPicker.newInstance(mProductivity);
                dialog.setTargetFragment(CalculatorFragment.this, REQUEST_PROD);
                dialog.show(fm, DIALOG_PROD);
            }
        });

        treatmentMinutes = view.findViewById(R.id.txtTreatmentMinutes);
        treatmentMinutes.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    mTreatmentMins = Integer.parseInt(s.toString());
                }catch (NumberFormatException e){
                    mTreatmentMins = 0;
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        unpaidMins = view.findViewById(R.id.txtUnpaidMins);
        unpaidMins.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    mUnpaidMins = Integer.parseInt(s.toString());
                } catch (NumberFormatException c){
                    mUnpaidMins = 0;
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        paidMins = view.findViewById(R.id.txtPaidMins);
        paidMins.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    mPaidMins = Integer.parseInt(s.toString());
                }catch (NumberFormatException c){
                    mPaidMins = 0;
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        mCalculateButton = view.findViewById(R.id.button2);
        mCalculateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int totalTreatMins;
                int totalTreatHrs;
                int totalTreatMns;
                int endHr, endMin;

                double inverse = mProductivity / 100;
                totalTreatMins = (int) (mTreatmentMins / inverse) + mPaidMins;
                totalTreatHrs = totalTreatMins / 60;
                totalTreatMns = totalTreatMins % 60;
                int startTime = (mHour * 60) + mMinute;
                int endTime = startTime + totalTreatMins + mUnpaidMins;
                endHr = endTime / 60;
                endMin = endTime % 60;

                d.set(0,0,0, endHr, endMin);
                DecimalFormat decimalFormat2 = new DecimalFormat("#");

                if(mIsTwentyFour) {
                    mTimeCard.setmEndHour(endHr);
                    mTimeCard.setmEndMinute(endMin);
                    mTimeCard.setProductivity(decimalFormat2.format(mProductivity));
                    mTimeCard.setStartTime(df24.format(c.getTime()));
                    mTimeCard.setEndTime(df24.format(d.getTime()));
                    mTimeCard.setTravelTime(String.valueOf(mPaidMins));
                    mTimeCard.setUnpaidTime(String.valueOf(mUnpaidMins));
                    mTimeCard.setPaidTime(decimalFormat2.format(totalTreatHrs) + " hrs " + decimalFormat2.format(totalTreatMns) + " mins");

                }
                else{
                    mTimeCard.setmEndHour(endHr);
                    mTimeCard.setmEndMinute(endMin);
                    mTimeCard.setProductivity(decimalFormat2.format(mProductivity));
                    mTimeCard.setStartTime(df12.format(c.getTime()));
                    mTimeCard.setEndTime(df12.format(d.getTime()));
                    mTimeCard.setTravelTime(String.valueOf(mPaidMins));
                    mTimeCard.setUnpaidTime(String.valueOf(mUnpaidMins));
                    mTimeCard.setPaidTime(decimalFormat2.format(totalTreatHrs) + " hrs " + decimalFormat2.format(totalTreatMns) + " mins");
                }
                SimpleDateFormat month_date = new SimpleDateFormat("MMMM");
                e = Calendar.getInstance();
                String month_name = month_date.format(e.getTime());
                int day = e.get(Calendar.DATE);
                int year = e.get(Calendar.YEAR);
                mTimeCard.setDate(month_name + " " + day + ", " + year);


                Intent intent = new Intent(getContext(), TimeCardFragment.class);
                intent.putExtra("object", mTimeCard);
                startActivity(intent);

            }
        });



        return view;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt("HOUR", mHour);
        outState.putInt("MINUTE", mMinute);
        outState.putBoolean("ISTWENTYFOUR", mIsTwentyFour);

    }

    private String getProdString() {
        mProductivity = com.myapp.adorg.simplecalculatorv2.Preferences.getPrefProd(getActivity());
        int str = (int) mProductivity;
        return Integer.toString(str) + "%";
    }

    @SuppressLint("SimpleDateFormat")
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK){
            return;
        }
        if (requestCode == REQUEST_TIME){
            mIsTwentyFour = (boolean) data.getBooleanExtra(com.myapp.adorg.simplecalculatorv2.TimePickerDialog.EXTRA_ISTWENTYFOUR, false);
            mHour = data.getIntExtra(com.myapp.adorg.simplecalculatorv2.TimePickerDialog.EXTRA_HOUR, 0);
            mMinute = data.getIntExtra(com.myapp.adorg.simplecalculatorv2.TimePickerDialog.EXTRA_MINUTE, 0);
            SimpleDateFormat df;
            if (!mIsTwentyFour) {
                df = new SimpleDateFormat("h:mm a");
                c.set(0, 0, 0, mHour, mMinute);
            } else {
                df = new SimpleDateFormat("HH:mm");
                c.set(0, 0, 0, mHour, mMinute);
            }
            mStartTimeView.setText(df.format(c.getTime()));
        } else if (requestCode == REQUEST_PROD) {
            mProductivity = data.getIntExtra(ProductivityPicker.EXTRA_PRODUCTIVITY, 0);
            com.myapp.adorg.simplecalculatorv2.Preferences.setPrefProd(getActivity(), (int) mProductivity);
            mProductivityView.setText(getProdString());

        }
    }
    @Override
    public void onResume() {
        super.onResume();
        mIsTwentyFour = Preferences.get24Hr(getActivity());
        c.set(0, 0, 0, mHour, mMinute);
        if(mIsTwentyFour)
            mStartTimeView.setText(df24.format(c.getTime()));
        else mStartTimeView.setText(df12.format(c.getTime()));
    }


}


