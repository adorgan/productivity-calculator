package com.myapp.adorg.simplecalculatorv2;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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
import java.util.Objects;
import java.util.UUID;

public class CalculatorFragment extends Fragment{
    private int mHour, mMinute, mTreatmentMins, mUnpaidMins, mPaidMins;
    private double mProductivity;
    private boolean mIs24HrMode = false;
    private TextView mStartTimeView, mProductivityView, treatmentMinutes, unpaidMins, paidMins;
    private String mDateString;
    private Button mToolbarDateButton;
    private static final String DIALOG_PROD = "DialogProd";
    private static final String DIALOG_START_TIME = "DialogStartTime";
    private static final String DIALOG_DATE = "DialogDATE";
    private static final int REQUEST_TIME = 0;
    private static final int REQUEST_PROD = 1;
    private static final int REQUEST_DATE = 2;
    private static final int REQUEST_TX_MINUTES = 3;
    private static final int REQUEST_UNPAID_MINUTES = 4;
    private static final int REQUEST_PAID_MINUTES = 5;
    private Date mDate;
    private final Calendar c= Calendar.getInstance();
    private final Calendar d= Calendar.getInstance();
    private TimeCard mTimeCard;
    private final SimpleDateFormat df24 = new SimpleDateFormat("HH:mm", Locale.US);
    private final SimpleDateFormat df12 = new SimpleDateFormat("h:mm a", Locale.US);
    private boolean isDarkMode;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container,false);
        isDarkMode = Preferences.getDarkMode(getContext());

        //Change log prompt
        if(!Preferences.getChangeLogSeen2(getContext())){
            FragmentManager fm = getParentFragmentManager();
            ChangeLog cm = new ChangeLog();
            fm.beginTransaction().add(cm, "change_log2").commit();
        }

        mTimeCard = new TimeCard(UUID.randomUUID());

        mIs24HrMode = Preferences.get24Hr(getActivity());
        if(savedInstanceState!=null){
            mIs24HrMode = savedInstanceState.getBoolean("ISTWENTYFOUR");
            mHour = savedInstanceState.getInt("HOUR");
            mMinute = savedInstanceState.getInt("MINUTE");
            mDate = new Date(savedInstanceState.getLong("TIME_CARD_DATE"));
            mDateString = savedInstanceState.getString("DATE_STRING");
            mTreatmentMins = savedInstanceState.getInt("TXMINUTES");
            mPaidMins = savedInstanceState.getInt("PAID_MINS");
            mUnpaidMins = savedInstanceState.getInt("UNPAID_MINS");
        }
        else {
            mHour = c.get(Calendar.HOUR_OF_DAY);
            mMinute =c.get(Calendar.MINUTE);
            getDateString();

        }
        c.set(0,0,0, mHour, mMinute);

        // Setup toolbar to show current date and act as button for changing date
        Toolbar mMainToolbar = view.findViewById(R.id.mainToolbar);
        ((AppCompatActivity) requireActivity()).setSupportActionBar(mMainToolbar);
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setTitle(null);
        mToolbarDateButton = view.findViewById(R.id.dateToolbarButton);
        mToolbarDateButton.setText(mDateString);

        // set up start time
        mStartTimeView = view.findViewById(R.id.txtViewStartTime);
        SimpleDateFormat df;
        if(mIs24HrMode) {
            df = df24;
        }
        else df = df12;
        mStartTimeView.setText(df.format(c.getTime()));

        // all user to change the current date
        mToolbarDateButton.setOnClickListener(v -> {
            FragmentManager fm = getParentFragmentManager();
            DatePickerDialog dialog = DatePickerDialog
                    .newInstance(mDate);
            dialog.setTargetFragment(CalculatorFragment.this, REQUEST_DATE);
            dialog.show(fm, DIALOG_DATE);
        });

        // user can change start time of shift
        mStartTimeView.setOnClickListener(v -> {
            FragmentManager fm = getFragmentManager();
            TimePickerDialog dialog = TimePickerDialog.newInstance(mHour, mMinute, mIs24HrMode);
            dialog.setTargetFragment(CalculatorFragment.this, REQUEST_TIME);
            assert fm != null;
            dialog.show(fm,DIALOG_START_TIME);
        });

        // user can change productivity target
        mProductivityView = view.findViewById(R.id.txtViewProductivity);
        mProductivityView.setText(getProdString());
        mProductivityView.setOnClickListener(v -> {
            FragmentManager fm = getFragmentManager();
            ProductivityPicker dialog = ProductivityPicker.newInstance(mProductivity);
            dialog.setTargetFragment(CalculatorFragment.this, REQUEST_PROD);
            assert fm != null;
            dialog.show(fm, DIALOG_PROD);
        });

        // user can change treatment minutes
        treatmentMinutes = view.findViewById(R.id.txtTreatmentMinutes);
        treatmentMinutes.setOnClickListener(v -> {
            FragmentManager fm = getParentFragmentManager();
            MinutesDialog minutesDialog = MinutesDialog.newMinutesDialog("Total Treatment Time");
            minutesDialog.setTargetFragment(CalculatorFragment.this, REQUEST_TX_MINUTES);
            minutesDialog.show(fm,"dialog_minutes");
        });

        // user can change unpaid break/lunch time
        unpaidMins = view.findViewById(R.id.txtUnpaidMins);
        unpaidMins.setOnClickListener(v -> {
            FragmentManager fm = getParentFragmentManager();
            MinutesDialog minutesDialog = MinutesDialog.newMinutesDialog("Unpaid Break/Lunch");
            minutesDialog.setTargetFragment(CalculatorFragment.this, REQUEST_UNPAID_MINUTES);
            minutesDialog.show(fm,"dialog_unpaid_minutes");
        });

        // user can change paid/travel time
        paidMins = view.findViewById(R.id.txtPaidMins);
        paidMins.setOnClickListener(v -> {
            FragmentManager fm = getParentFragmentManager();
            MinutesDialog minutesDialog = MinutesDialog.newMinutesDialog("Paid Meeting/Travel");
            minutesDialog.setTargetFragment(CalculatorFragment.this, REQUEST_PAID_MINUTES);
            minutesDialog.show(fm,"dialog_paid_minutes");
        });

        //Calculate Time Card
        Button mCalculateButton = view.findViewById(R.id.button2);
        mCalculateButton.setOnClickListener(v -> {
            int totalTreatMins;
            int totalTreatHrs;
            int totalTreatMns;
            int endHr, endMin;
            String strHH, strMM;

            // calculate start of shift and end of shift time in HH::MM format
            double percentProductivity = mProductivity / 100;
            totalTreatMins = (int) (mTreatmentMins / percentProductivity) + mPaidMins;
            totalTreatHrs = totalTreatMins / 60;
            totalTreatMns = totalTreatMins % 60;
            int startTime = (mHour * 60) + mMinute;
            int endTime = startTime + totalTreatMins + mUnpaidMins;
            endHr = endTime / 60;
            endMin = endTime % 60;

            d.set(0,0,0, endHr, endMin);
            DecimalFormat dfTimeCard = new DecimalFormat("#");

            if (totalTreatHrs == 1) strHH = " hr ";
            else strHH = " hrs ";

            if (totalTreatMns == 1) strMM = " min";
            else strMM = " mins";

            // don't allow users to work more than 24hrs
            if((totalTreatMins + mUnpaidMins) <1440) {
                if (mIs24HrMode) {
                    mTimeCard.setStartTime(df24.format(c.getTime()));
                    mTimeCard.setEndTime(df24.format(d.getTime()));
                    mTimeCard.setIs24Hour(1);
                } else {
                    mTimeCard.setStartTime(df12.format(c.getTime()));
                    mTimeCard.setEndTime(df12.format(d.getTime()));
                    mTimeCard.setIs24Hour(0);
                }
                mTimeCard.setId(UUID.randomUUID());
                mTimeCard.setTreatmentTimeString(Integer.toString(mTreatmentMins));
                mTimeCard.setEndMinuteInt(d.get(Calendar.MINUTE));
                mTimeCard.setEndHourInt(d.get(Calendar.HOUR_OF_DAY));
                mTimeCard.setProductivityString(dfTimeCard.format(mProductivity));
                mTimeCard.setTravelTime(String.valueOf(mPaidMins));
                mTimeCard.setUnpaidTime(String.valueOf(mUnpaidMins));
                mTimeCard.setPaidTime(dfTimeCard.format(totalTreatHrs) + strHH + dfTimeCard.format(totalTreatMns) + strMM);
                mTimeCard.setDate(mDateString);
                mTimeCard.setTimeCardDate(mDate);
                mTimeCard.setProductivityDouble(mProductivity);
                mTimeCard.setPaidTimeInt(totalTreatMins);
                mTimeCard.setStartHour(mHour);
                mTimeCard.setStartMinute(mMinute);

                Intent intent = new Intent(getContext(), TimeCardFragment.class);
                intent.putExtra("object", mTimeCard);
                startActivity(intent);
            }
            else Toast.makeText(getContext(), "Unable to calculate more than 24 hours worked", Toast.LENGTH_LONG).show();

        });
        return view;
    }

    /**
     * Calculates a string for the current date in Jan 24, 2010 format
     */
    private void getDateString(){
        Calendar toolbarCalendar = Calendar.getInstance();
        int day = toolbarCalendar.get(Calendar.DAY_OF_MONTH);
        int year = toolbarCalendar.get(Calendar.YEAR);
        int month = toolbarCalendar.get(Calendar.MONTH);
        mDate = new GregorianCalendar(year, month, day).getTime();
        SimpleDateFormat df = new SimpleDateFormat("EE, MMM d, yyyy", Locale.US);
        mDateString = df.format(mDate);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.main_menu, menu);
    }

    @SuppressLint("NonConstantResourceId")
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
        outState.putString("DATE_STRING", mDateString);
        outState.putInt("HOUR", mHour);
        outState.putInt("MINUTE", mMinute);
        outState.putBoolean("ISTWENTYFOUR", mIs24HrMode);
        outState.putInt("TXMINUTES", mTreatmentMins);
        outState.putInt("PAID_MINS", mPaidMins);
        outState.putInt("UNPAID_MINS", mUnpaidMins);

    }

    /**
     * Convert productivity int to string
     * @return productivity string
     */
    private String getProdString() {
        mProductivity = Preferences.getPrefProd(getActivity());
        int str = (int) mProductivity;
        return str + "%";
    }

    @SuppressLint("SimpleDateFormat")
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK){
            return;
        }
        if (requestCode == REQUEST_TIME){
            // update start time
            mIs24HrMode = data.getBooleanExtra(TimePickerDialog.EXTRA_ISTWENTYFOUR, false);
            mHour = data.getIntExtra(com.myapp.adorg.simplecalculatorv2.TimePickerDialog.EXTRA_HOUR, 0);
            mMinute = data.getIntExtra(com.myapp.adorg.simplecalculatorv2.TimePickerDialog.EXTRA_MINUTE, 0);
            SimpleDateFormat df;
            if (!mIs24HrMode) {
                df = new SimpleDateFormat("h:mm a");
            } else {
                df = new SimpleDateFormat("HH:mm");
            }
            c.set(0, 0, 0, mHour, mMinute);
            mStartTimeView.setText(df.format(c.getTime()));
        }
        else if (requestCode == REQUEST_PROD) {
            // update productivity
            mProductivity = data.getIntExtra(ProductivityPicker.EXTRA_PRODUCTIVITY, 0);
            com.myapp.adorg.simplecalculatorv2.Preferences.setPrefProd(getActivity(), (int) mProductivity);
            mProductivityView.setText(getProdString());
        }
        else if (requestCode == REQUEST_DATE){
            // update toolbar date
            mDate = (Date) data
                    .getSerializableExtra(DatePickerDialog.EXTRA_DATE);
            SimpleDateFormat df = new SimpleDateFormat("EE, MMM d, yyyy", Locale.US);
            mDateString = df.format(mDate);
            mToolbarDateButton.setText(mDateString);
        }
        else if (requestCode == REQUEST_TX_MINUTES) {
            // update tx minutes
            mTreatmentMins = data.getIntExtra(MinutesDialog.EXTRA_MINUTE, 0);
            treatmentMinutes.setText(createMinutesString(mTreatmentMins));
        }
        else if (requestCode == REQUEST_UNPAID_MINUTES) {
            // update unpaid minutes
            mUnpaidMins = data.getIntExtra(MinutesDialog.EXTRA_MINUTE, 0);
            unpaidMins.setText(createMinutesString(mUnpaidMins));
        }
        else if (requestCode == REQUEST_PAID_MINUTES) {
            // update paid minutes
            mPaidMins = data.getIntExtra(MinutesDialog.EXTRA_MINUTE, 0);
            paidMins.setText(createMinutesString(mPaidMins));
        }
    }

    /**
     * creates a string in the format x hours y minutes (x + y mintes)
     * @param minutes minutes for calculating
     * @return string
     */
    String createMinutesString(int minutes){
        String strHH, strMM, strMM2;
        if (minutes == 0){
            return "";
        }
        else {
            int hr = minutes / 60;
            int min = minutes % 60;

            if (hr == 1) strHH = " hr ";
            else strHH = " hrs ";

            if (min == 1) strMM = " min";
            else strMM = " mins";

            if (minutes == 1) strMM2 = " min";
            else strMM2 = " mins";

            return hr + strHH + min + strMM + " (" + minutes + strMM2 + ")";
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mIs24HrMode = Preferences.get24Hr(getActivity());
        c.set(0, 0, 0, mHour, mMinute);
        if(mIs24HrMode)
            mStartTimeView.setText(df24.format(c.getTime()));
        else mStartTimeView.setText(df12.format(c.getTime()));
        getDateString();
        mToolbarDateButton.setText(mDateString);

        // sets up db tables **IMPORTANT
        TimeCardLab timeCard = TimeCardLab.get(getContext());

        if(Preferences.getDarkMode(getActivity())){
            if(!isDarkMode){
                isDarkMode = true;
                requireActivity().recreate();
            }
        }
        else{
            if(isDarkMode){
                isDarkMode = false;
                requireActivity().recreate();
            }
        }
    }
}


