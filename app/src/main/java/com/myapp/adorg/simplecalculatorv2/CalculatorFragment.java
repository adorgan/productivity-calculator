package com.myapp.adorg.simplecalculatorv2;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.myapp.adorg.simplecalculatorv2.help.HelpViewPager;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.UUID;

public class CalculatorFragment extends Fragment{
    private int mHour, mMinute, mTreatmentMins, mUnpaidMins, mPaidMins;
    private double mProductivity;
    private boolean mIsTwentyFour = false;
    private TextView mStartTimeView, mProductivityView, treatmentMinutes, unpaidMins, paidMins;
    private String mdateString;
    private Toolbar mMainToolbar;
    private Button mToolbarDateButton;
    private Button mCalculateButton;
    private static final String DIALOG_PROD = "DialogProd";
    private static final String DIALOG_START_TIME = "DialogStartTime";
    private static final String DIALOG_DATE = "DialogDATE";
    private static final int REQUEST_TIME = 0;
    private static final int REQUEST_PROD = 1;
    private static final int REQUEST_DATE = 2;
    private Date mDate;
    private Calendar c= Calendar.getInstance();
    private Calendar d= Calendar.getInstance();
    private TimeCard mTimeCard;
    private SimpleDateFormat df24 = new SimpleDateFormat("HH:mm");
    private SimpleDateFormat df12 = new SimpleDateFormat("h:mm a");




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container,false);

        //Change log prompt
        if(!Preferences.getChangeLogSeen(getContext())){
            FragmentManager fm = getFragmentManager();
            ChangeLog cm = new ChangeLog();
            fm.beginTransaction().add(cm, "change_log").commit();
        }

        mTimeCard = new TimeCard(UUID.randomUUID());

        mIsTwentyFour = Preferences.get24Hr(getActivity());
        if(savedInstanceState!=null){
            mIsTwentyFour = savedInstanceState.getBoolean("ISTWENTYFOUR");
            mHour = savedInstanceState.getInt("HOUR");
            mMinute = savedInstanceState.getInt("MINUTE");
            mDate = new Date(savedInstanceState.getLong("TIME_CARD_DATE"));
            mdateString = savedInstanceState.getString("DATE_STRING");

        }
        else {
            mHour = c.get(Calendar.HOUR_OF_DAY);
            mMinute =c.get(Calendar.MINUTE);
            getDateString();

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

        //Enter treatment minutes
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

        //Setup toolbar to show current date and act as button for changing date
        mMainToolbar = view.findViewById(R.id.mainToolbar);
        mToolbarDateButton = view.findViewById(R.id.dateToolbarButton);
        ((AppCompatActivity)getActivity()).setSupportActionBar(mMainToolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(null);

        mToolbarDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getFragmentManager();
                DatePickerDialog dialog = DatePickerDialog
                        .newInstance(mDate);
                dialog.setTargetFragment(CalculatorFragment.this, REQUEST_DATE);
                dialog.show(manager, DIALOG_DATE);

            }
        });
        mToolbarDateButton.setText(mdateString);

        //Calculate Time Card
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

                if((totalTreatMins + mUnpaidMins) <1440) {

                    if (mIsTwentyFour) {
                        mTimeCard.setStartTime(df24.format(c.getTime()));
                        mTimeCard.setEndTime(df24.format(d.getTime()));
                        mTimeCard.setIs24Hour(1);
                    } else {
                        mTimeCard.setStartTime(df12.format(c.getTime()));
                        mTimeCard.setEndTime(df12.format(d.getTime()));
                        mTimeCard.setIs24Hour(0);
                    }
                    mTimeCard.setId(UUID.randomUUID());
                    mTimeCard.setmTreatmentTime(Integer.toString(mTreatmentMins));
                    mTimeCard.setEndMinute(d.get(Calendar.MINUTE));
                    mTimeCard.setmEndHour(d.get(Calendar.HOUR_OF_DAY));
                    mTimeCard.setProductivity(decimalFormat2.format(mProductivity));
                    mTimeCard.setTravelTime(String.valueOf(mPaidMins));
                    mTimeCard.setUnpaidTime(String.valueOf(mUnpaidMins));
                    mTimeCard.setPaidTime(decimalFormat2.format(totalTreatHrs) + " hrs " + decimalFormat2.format(totalTreatMns) + " mins");
                    mTimeCard.setDate(mdateString);
                    mTimeCard.setMcardDate(mDate);
                    mTimeCard.setmProductivityDouble(mProductivity);
                    mTimeCard.setmPaidTimeInt(totalTreatMins);
                    mTimeCard.setStartHour(mHour);
                    mTimeCard.setStartMinute(mMinute);

                    Intent intent = new Intent(getContext(), TimeCardFragment.class);
                    intent.putExtra("object", mTimeCard);
                    startActivity(intent);
                }
                else Toast.makeText(getContext(), "Unable to calculate more than 24 hours worked", Toast.LENGTH_LONG).show();

            }
        });
        return view;
    }

    private void getDateString(){
        Calendar toolbarCalendar = Calendar.getInstance();
        int day = toolbarCalendar.get(Calendar.DAY_OF_MONTH);
        int year = toolbarCalendar.get(Calendar.YEAR);
        int month = toolbarCalendar.get(Calendar.MONTH);
        mDate = new GregorianCalendar(year, month, day).getTime();
        SimpleDateFormat df = new SimpleDateFormat("EE, MMM d, yyyy", Locale.US);
        mdateString = df.format(mDate);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.main_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings: {
                Intent intent2 = new Intent(getContext(), SettingsActivity.class);
                startActivity(intent2);
                return true;
            }
            case R.id.settingsHelp: {
                Intent intent = new Intent(getContext(), HelpViewPager.class);
                startActivity(intent);
                return true;
            }
            case R.id.history: {
                startActivity(new Intent(getContext(), History.class));
                return true;
            }
        }return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong("TIME_CARD_DATE", mDate.getTime());
        outState.putString("DATE_STRING", mdateString);
        outState.putInt("HOUR", mHour);
        outState.putInt("MINUTE", mMinute);
        outState.putBoolean("ISTWENTYFOUR", mIsTwentyFour);

    }

    private String getProdString() {
        mProductivity = Preferences.getPrefProd(getActivity());
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
            mIsTwentyFour = data.getBooleanExtra(TimePickerDialog.EXTRA_ISTWENTYFOUR, false);
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

        } else if(requestCode == REQUEST_DATE){
            mDate = (Date) data
                    .getSerializableExtra(DatePickerDialog.EXTRA_DATE);
            SimpleDateFormat df = new SimpleDateFormat("EE, MMM d, yyyy", Locale.US);
            mdateString = df.format(mDate);
            mToolbarDateButton.setText(mdateString);
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
        getDateString();
        mToolbarDateButton.setText(mdateString);
        TimeCardLab timeCardLab = TimeCardLab.get(getContext());

    }



}


