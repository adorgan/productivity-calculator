package com.myapp.adorg.simplecalculatorv2;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.provider.AlarmClock;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.UUID;


public class HistoryTimeCardFragment extends Fragment{

    private static final String ARG_HISTORY_TIME_CARD_ID = "history_time_card_ID";
    private TimeCard mTimeCard;
    private TextView endTimeText, startTimeText, paidTime, txtUnpaidTime, txtPaidBreak, txtProductivity, txtTreatmentTime, txtToolBar;
    private int  startHr, startMin;
    private UUID timeCardID;
    private static final int REQUEST_DELETED = 3;
    private boolean isEdited = false;
    private Toolbar toolbar;
    private MenuItem checkMenu, DeleteMenu, EditMenu;
    private static final int REQUEST_TIME_HISTORY = 0;
    private static final String DIALOG_END_TIME_HISTORY = "History_dialog_end_time";
    private static final int REQUEST_START_TIME = 5;
    private static final String DIALOG_START_TIME = "History_dialog_start_time";
    private static final int REQUEST_DATE = 6;
    private static final String DIALOG_DATE = "History_date";
    private static final int REQUEST_TREAT_MINS = 7;
    private static final String DIALOG_TREAT_MINS = "History_treat_mins";
    private static final int REQUEST_UNPAID = 8;
    private static final String DIALOG_UNPAID = "History_unpaid";
    private static final int REQUEST_TRAVEL = 9;
    private static final String DIALOG_TRAVEL = "History_travel";
    private static final int REQUEST_PRODUCTVITY = 10;
    private static final String DIALOG_PRODUCTIVITY = "history_productivity";
    private String newPaidString,treatStr;
    private int mMinuteHistory, mHourHistory;
    private SimpleDateFormat df;
    private final Calendar c = Calendar.getInstance();
    private double newProductivity;
    private String newProdString;
    private boolean is24HourMode;
    private String startTimeString, endTimeString, dateString;
    private int newPaidTime, newTreatmentMins;
    private Date newDate;
    private boolean dateChanged = false;
    private double newUnpaidTime, newTravelTime;
    private String newUnpaidString;
    private double endHourTotal;
    private FloatingActionButton fab, fabAlarm, fabEmail, fabSms;
    private boolean isFabMenuOpen;



    public HistoryTimeCardFragment() {
        // Required empty public constructor
    }

    public static HistoryTimeCardFragment newInstance(UUID uuid) {
        HistoryTimeCardFragment fragment = new HistoryTimeCardFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_HISTORY_TIME_CARD_ID, uuid);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {

            timeCardID = (UUID) getArguments().getSerializable(ARG_HISTORY_TIME_CARD_ID);
            mTimeCard = TimeCardLab.get(getActivity()).getTimeCard(timeCardID);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_history_time_card, container, false);

        // set up tool bar
        toolbar = v.findViewById(R.id.historyTimeCardToolbar);
        ((AppCompatActivity) requireActivity()).setSupportActionBar(toolbar);
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setTitle(null);
        ActionBar ab = ((AppCompatActivity) requireActivity()).getSupportActionBar();
        assert ab != null;
        ab.setDisplayHomeAsUpEnabled(true);

        txtToolBar = v.findViewById(R.id.historyTimeCardToolBarText);
        txtToolBar.setOnClickListener(v15 -> {
            FragmentManager fm = getParentFragmentManager();
            DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(newDate);
            datePickerDialog.setTargetFragment(HistoryTimeCardFragment.this, REQUEST_DATE );
            datePickerDialog.show(fm, DIALOG_DATE);
        });

        // set up fabs
        fab = v.findViewById(R.id.fabHistoryTC);
        fab.setOnClickListener(v1 -> toggleFabMenu());

        fabAlarm = v.findViewById(R.id.fab1HistoryTC);
        fabAlarm.setOnClickListener(v12 -> setAlarm());

        fabEmail = v.findViewById(R.id.fab2HistoryTC);
        fabEmail.setOnClickListener(v13 -> sendEmail());

        fabSms = v.findViewById(R.id.fab3HistoryTC);
        fabSms.setOnClickListener(v14 -> sendSms());

        is24HourMode = mTimeCard.getIs24Hour() != 0;

        // allow user to edit start time
        startTimeText = v.findViewById(R.id.startTimeHistoryTC);
        startTimeText.setOnClickListener(v16 -> {
            FragmentManager fm = getParentFragmentManager();
            TimePickerDialog timePickerDialog = TimePickerDialog.newInstance(startHr, startMin, is24HourMode);
            timePickerDialog.setTargetFragment(HistoryTimeCardFragment.this, REQUEST_START_TIME);
            timePickerDialog.show(fm,DIALOG_START_TIME);
        });

        // allow user to edit end time
        endTimeText = v.findViewById(R.id.EndTimeHistoryTC);
        endTimeText.setOnClickListener(v17 -> {
            FragmentManager fm = getParentFragmentManager();
            TimePickerDialog timePickerDialog = TimePickerDialog.newInstance(mHourHistory, mMinuteHistory, is24HourMode);
            timePickerDialog.setTargetFragment(HistoryTimeCardFragment.this, REQUEST_TIME_HISTORY);
            timePickerDialog.show(fm,DIALOG_END_TIME_HISTORY);
        });

        // allow user to edit tx time
        txtTreatmentTime = v.findViewById(R.id.treatmentTimeHistoryTC);
        txtTreatmentTime.setOnClickListener(v18 -> {
            FragmentManager fm = getParentFragmentManager();
            MinutesDialog changeNumberDialog = MinutesDialog.newMinutesDialog("Enter New Treatment Amount");
            changeNumberDialog.setTargetFragment(HistoryTimeCardFragment.this, REQUEST_TREAT_MINS );
            changeNumberDialog.show(fm, DIALOG_TREAT_MINS);
        });

        // allow user to edit unpaid time
        txtUnpaidTime = v.findViewById(R.id.unPaidTimeHistory);
        txtUnpaidTime.setOnClickListener(v19 -> {
            FragmentManager fm = getParentFragmentManager();
            ChangeNumberDialog changeNumberDialog = ChangeNumberDialog.newInstance("New Unpaid Time", (int)newUnpaidTime, endHourTotal);
            changeNumberDialog.setTargetFragment(HistoryTimeCardFragment.this, REQUEST_UNPAID);
            changeNumberDialog.show(fm, DIALOG_UNPAID);

        });

        // allow user to edit paid time
        paidTime = v.findViewById(R.id.PaidTimeHistoryTC);
        txtPaidBreak = v.findViewById(R.id.txtPaidBreakHistoryTC);
        txtPaidBreak.setOnClickListener(v110 -> {
            FragmentManager fm = getParentFragmentManager();
            ChangeNumberDialog changeNumberDialog = ChangeNumberDialog.newInstance("New Paid Time", (int)newTravelTime, endHourTotal);
            changeNumberDialog.setTargetFragment(HistoryTimeCardFragment.this, REQUEST_TRAVEL);
            changeNumberDialog.show(fm, DIALOG_TRAVEL);
        });

        // allow user to edit productivity
        txtProductivity = v.findViewById(R.id.productivityTextViewHistoryTC);
        txtProductivity.setOnClickListener(v111 -> {
            FragmentManager fm = getParentFragmentManager();
            ProductivityPicker productivityPicker = ProductivityPicker.newInstance(newProductivity);
            productivityPicker.setTargetFragment(HistoryTimeCardFragment.this, REQUEST_PRODUCTVITY);
            productivityPicker.show(fm, DIALOG_PRODUCTIVITY);
        });

        // set all values clickable before user clicks edit icon and enters edit mode
        endTimeText.setClickable(false);
        startTimeText.setClickable(false);
        txtToolBar.setClickable(false);
        txtProductivity.setClickable(false);
        txtPaidBreak.setClickable(false);
        txtUnpaidTime.setClickable(false);
        txtTreatmentTime.setClickable(false);

        // set up state of all data values
        setTimeCardState();

        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.history_item_menu, menu);
        checkMenu = menu.findItem(R.id.menuItemSaved);
        checkMenu.setVisible(false);
        DeleteMenu = menu.findItem(R.id.deleteHistoryItem);
        EditMenu = menu.findItem(R.id.historyMenuEdit);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.deleteHistoryItem: {
                FragmentManager fm = getParentFragmentManager();
                DeleteHistoryItemDialog dialog = DeleteHistoryItemDialog.newInstance();
                dialog.setTargetFragment(HistoryTimeCardFragment.this, REQUEST_DELETED);
                dialog.show(fm, "DIALOG_DELETED");
                return true;
            }
            case R.id.historyMenuEdit:{
                setClickable(startTimeText);
                setClickable(endTimeText);
                setClickable(txtTreatmentTime);
                setClickable(txtUnpaidTime);
                setClickable(txtPaidBreak);
                setClickable(txtProductivity);
                txtToolBar.setClickable(true);
                txtToolBar.setTextColor(getResources().getColor(R.color.colorAccent));
                isEdited=true;
                toolbar.setNavigationIcon(R.drawable.ex_icon);
                EditMenu.setVisible(false);
                DeleteMenu.setVisible(false);
                checkMenu.setVisible(true);
                fab.animate().translationY(getResources().getDimension(R.dimen.standard_120));
                closeFabMenu();

                return true;
            }
            case android.R.id.home: {
                if(!isEdited) requireActivity().onBackPressed();
                else{
                    returnFromEdit();
                    setTimeCardState();
                }
                return true;

            }case R.id.menuItemSaved:{
                mTimeCard.setEndMinuteInt(mMinuteHistory);
                mTimeCard.setEndHourInt(mHourHistory);
                mTimeCard.setEndTime(endTimeString);
                mTimeCard.setProductivityDouble(newProductivity);
                mTimeCard.setProductivityString(newProdString);
                mTimeCard.setPaidTime(newPaidString);
                mTimeCard.setStartHour(startHr);
                mTimeCard.setStartMinute(startMin);
                mTimeCard.setStartTime(startTimeString);
                mTimeCard.setPaidTimeInt(newPaidTime);
                mTimeCard.setDate(dateString);
                mTimeCard.setTimeCardDate(newDate);
                mTimeCard.setUnpaidTime(Integer.toString((int)newUnpaidTime));
                mTimeCard.setTreatmentTimeString(Integer.toString(newTreatmentMins));
                mTimeCard.setTravelTime(Integer.toString((int)newTravelTime));
                setTimeCardState();
                returnFromEdit();
                if(dateChanged) Preferences.setDateChange(getContext(), true);
            }
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * turns a read only textview into a clickable one to be edited
     * @param textView textView to be changed
     */
    private void setClickable(TextView textView){
        textView.setClickable(true);
        textView.setTextColor(getResources().getColor(R.color.colorPrimary));
    }

    /**
     * turns a clickable textview into a read only textview
     * @param textView textview to be changed
     */
    private void setUnClickable(TextView textView){
        textView.setClickable(false);
        if (Preferences.getDarkMode(getContext())){
            textView.setTextColor(getResources().getColor(R.color.colorWhite));
        }
        else{
            textView.setTextColor(getResources().getColor(R.color.colorBlack));
        }
    }

    /**
     * Returns the time card to read only.
     */
    private void returnFromEdit(){
        setUnClickable(startTimeText);
        setUnClickable(endTimeText);
        setUnClickable(txtTreatmentTime);
        setUnClickable(txtUnpaidTime);
        setUnClickable(txtPaidBreak);
        setUnClickable(txtProductivity);
        txtToolBar.setClickable(false);
        txtToolBar.setTextColor(getResources().getColor(R.color.colorWhite));
        isEdited=false;
        toolbar.setNavigationIcon(R.drawable.arrow_back);
        EditMenu.setVisible(true);
        DeleteMenu.setVisible(true);
        checkMenu.setVisible(false);
        fab.animate().translationY(0);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK){
            return;
        }
        else if (requestCode == REQUEST_DELETED){
            // user wishes to delete selected time card from history
            boolean cleared = data.getBooleanExtra(DeleteHistoryItemDialog.EXTRA_DELETED, false);
            if(cleared) {
                TimeCardLab timeCardLab = TimeCardLab.get(getContext());
                timeCardLab.searchAndDelete(timeCardID);
                Preferences.setPrefDelete(getContext(), true);
                requireActivity().onBackPressed();
            }
        }
        else if (requestCode == REQUEST_TIME_HISTORY) {
            // user changes the end time of their shift
            is24HourMode = data.getBooleanExtra(TimePickerDialog.EXTRA_ISTWENTYFOUR, false);
            double endTimeTotalMinutes = (data.getIntExtra(TimePickerDialog.EXTRA_HOUR, 0)*60)
                    + data.getIntExtra(TimePickerDialog.EXTRA_MINUTE, 0);
            double startTimeTotal = (startHr*60) + startMin;

            // calculate difference between start and stop time
            double difference;
            if (endTimeTotalMinutes > startTimeTotal)
                difference = (endTimeTotalMinutes - startTimeTotal) - newUnpaidTime - newTravelTime;
            else if (startTimeTotal > endTimeTotalMinutes)
                difference = 1440 - ((startTimeTotal - endTimeTotalMinutes) + newUnpaidTime + newTravelTime);
            else difference = 0;

            // make sure shift time can't get too small
            if((difference-newUnpaidTime)<=0){
                Toast.makeText(getContext(), "End time error", Toast.LENGTH_LONG).show();
            }
            else {
                mHourHistory = data.getIntExtra(TimePickerDialog.EXTRA_HOUR, 0);
                mMinuteHistory = data.getIntExtra(TimePickerDialog.EXTRA_MINUTE, 0);
                reCalculateEndTime();
            }

        }
        else if (requestCode == REQUEST_START_TIME) {
            // user changes shift start time
            is24HourMode = data.getBooleanExtra(TimePickerDialog.EXTRA_ISTWENTYFOUR, false);
            double startHourTotal = (data.getIntExtra(TimePickerDialog.EXTRA_HOUR, 0)*60)
                    + data.getIntExtra(TimePickerDialog.EXTRA_MINUTE, 0);
            endHourTotal = (mHourHistory*60) + mMinuteHistory;

            // make sure start time is not set too late to interfere with stop time
            double difference;
            if (endHourTotal > startHourTotal)
                difference = (endHourTotal - startHourTotal) - newUnpaidTime - newTravelTime;
            else if (startHourTotal > endHourTotal)
                difference = 1440 - ((startHourTotal - endHourTotal) + newUnpaidTime + newTravelTime);
            else difference = 0;

            if((difference-newUnpaidTime)<=0){
                Toast.makeText(getContext(), "Start time error", Toast.LENGTH_LONG).show();
            }
            else {
                startHr = data.getIntExtra(com.myapp.adorg.simplecalculatorv2.TimePickerDialog.EXTRA_HOUR, 0);
                startMin = data.getIntExtra(com.myapp.adorg.simplecalculatorv2.TimePickerDialog.EXTRA_MINUTE, 0);
                reCalculateStartTime();
            }
        }
        else if (requestCode == REQUEST_DATE) {
            // user changes date
            newDate = (Date) data
                    .getSerializableExtra(DatePickerDialog.EXTRA_DATE);
            SimpleDateFormat df = new SimpleDateFormat("EE, MMM d, yyyy", Locale.US);
            dateString = df.format(newDate);
            txtToolBar.setText(dateString);
            dateChanged = true;
        }
        else if(requestCode == REQUEST_TREAT_MINS){
            // user changes treatment minutes but can't set tx mins to zero
            if(data.getIntExtra(MinutesDialog.EXTRA_MINUTE, 0)==0)
                Toast.makeText(getContext(), "Unable to calculate", Toast.LENGTH_LONG).show();
            else {
                newTreatmentMins = data.getIntExtra(MinutesDialog.EXTRA_MINUTE, 0);
                treatStr = (newTreatmentMins / 60) + " hrs " + (newTreatmentMins % 60) + " mins (" + newTreatmentMins + " mins)";
                txtTreatmentTime.setText(treatStr);
                reCalculateTreatTime();
            }
        }
        else if (requestCode == REQUEST_UNPAID) {
            // user changes unpaid minutes
            if(newPaidTime + data.getIntExtra(ChangeNumberDialog.EXTRA_Minutes, 0)>= 1440){
                Toast.makeText(getContext(), "Unable to calculate more than 24 hours worked", Toast.LENGTH_LONG).show();
            }
            else {
                newUnpaidTime = data.getIntExtra(ChangeNumberDialog.EXTRA_Minutes, 0);
                endHourTotal = data.getDoubleExtra(ChangeNumberDialog.EXTRA_TOTAL, 0);
                newUnpaidString = (int) newUnpaidTime + " mins";
                txtUnpaidTime.setText(newUnpaidString);
                reCalculateUnpaid();
            }
        } else if (requestCode == REQUEST_TRAVEL) {

            if((newTreatmentMins/(newProductivity/100)) + data.getIntExtra(ChangeNumberDialog.EXTRA_Minutes, 0) + newUnpaidTime >= 1440){
                Toast.makeText(getContext(), "Unable to calculate more than 24 hours worked", Toast.LENGTH_LONG).show();
            } else {
                newTravelTime = data.getIntExtra(ChangeNumberDialog.EXTRA_Minutes, 0);
                endHourTotal = data.getDoubleExtra(ChangeNumberDialog.EXTRA_TOTAL, 0);
                txtPaidBreak.setText((int) newTravelTime + " mins");
                reCalculateTravelTime();
            }
        } else if (requestCode == REQUEST_PRODUCTVITY) {
            int totalTreatMins;

            double inverse = (double)data.getIntExtra(ProductivityPicker.EXTRA_PRODUCTIVITY, 0) / 100;
            totalTreatMins = (int) (newTreatmentMins / inverse) + (int) newTravelTime;
            if ((totalTreatMins + newUnpaidTime) < 1440) {

                newProductivity = data.getIntExtra(ProductivityPicker.EXTRA_PRODUCTIVITY, 0);
                newProdString = (int)newProductivity + "";
                txtProductivity.setText(newProdString + "%");
                reCalculateProductivity();
            } else
                Toast.makeText(getContext(), "Unable to calculate more than 24 hours worked", Toast.LENGTH_LONG).show();
        }
    }

    private void reCalculateProductivity() {
        int totalTreatMins;
        int totalTreatHrs;
        int totalTreatMns;
        String strHH, strMM;

        double inverse = newProductivity / 100;
        totalTreatMins = (int) (newTreatmentMins / inverse) + (int) newTravelTime;
        totalTreatHrs = totalTreatMins / 60;
        totalTreatMns = totalTreatMins % 60;
        int startTime = (startHr * 60) + startMin;
        int endTime = startTime + totalTreatMins + (int) newUnpaidTime;
        if(endTime/60 >= 24)
        mHourHistory = (endTime / 60) - 24;
        else mHourHistory = endTime/60;
        mMinuteHistory = endTime % 60;
        if(totalTreatHrs == 1) strHH = " hr ";
        else strHH = " hrs ";

        if(totalTreatMns == 1) strMM = " min";
        else strMM = " mins";

        c.set(0, 0, 0, mHourHistory, mMinuteHistory);

        if ((totalTreatMins + newUnpaidTime) < 1440) {

            if (!is24HourMode) {
                df = new SimpleDateFormat("h:mm a");
                endTimeString = df.format(c.getTime());
                endTimeText.setText(endTimeString);
            } else {
                df = new SimpleDateFormat("HH:mm");
                endTimeString = df.format(c.getTime());
                endTimeText.setText(endTimeString);
            }
            newPaidString = totalTreatHrs + strHH + totalTreatMns + strMM;
            paidTime.setText("Paid Time: " + newPaidString);
            newPaidTime = totalTreatMins;
            endHourTotal = (mHourHistory*60) + mMinuteHistory;
        } else
            Toast.makeText(getContext(), "Unable to calculate more than 24 hours worked", Toast.LENGTH_LONG).show();
    }

    private void reCalculateEndTime(){
        String strHH, strMM;
        endHourTotal = (mHourHistory*60) + mMinuteHistory;
        double startHourTotal = (startHr*60) + startMin;
        double difference;
        if (endHourTotal > startHourTotal)
            difference = (endHourTotal - startHourTotal) - newUnpaidTime - newTravelTime;
        else if (startHourTotal > endHourTotal)
            difference = 1440 - ((startHourTotal - endHourTotal) + newUnpaidTime + newTravelTime);
        else difference = 0;

        double treatmentMins = (double)newTreatmentMins;

        if((difference-newUnpaidTime)<=0){
            Toast.makeText(getContext(), "End time error", Toast.LENGTH_LONG).show();
        }

        else {
            if (!is24HourMode) {
                df = new SimpleDateFormat("h:mm a");
                c.set(0, 0, 0, mHourHistory, mMinuteHistory);
            } else {
                df = new SimpleDateFormat("HH:mm");
                c.set(0, 0, 0, mHourHistory, mMinuteHistory);
            }
            endTimeString = df.format(c.getTime());
            endTimeText.setText(endTimeString);

            newProductivity = round(((treatmentMins / difference) * 100));
            NumberFormat decimalFormat = new DecimalFormat("#0.0");
            NumberFormat noDecimal = new DecimalFormat("#0");
            if (decimalFormat.format(newProductivity).endsWith(".0")) {
                newProdString = noDecimal.format(newProductivity);
                txtProductivity.setText(newProdString + "%");
            } else {
                newProdString = decimalFormat.format(newProductivity);
                txtProductivity.setText(newProdString + "%");
            }


            newPaidTime = (int) difference + (int)newTravelTime;
            if((newPaidTime / 60) == 1) strHH = " hr ";
            else strHH = " hrs ";

            if((newPaidTime % 60) == 1) strMM = " min";
            else strMM = " mins";

            newPaidString = (newPaidTime / 60) + strHH + (newPaidTime % 60) + strMM;
            paidTime.setText("Paid Time: " + newPaidString);
        }

    }
    private void reCalculateTravelTime(){
        String strHH, strMM;
        endHourTotal = endHourTotal + newTravelTime;
        if(endHourTotal>1440) endHourTotal = endHourTotal-1440;
        mHourHistory = (int)endHourTotal/60;
        mMinuteHistory = (int)endHourTotal%60;

        if (!is24HourMode) {
            df = new SimpleDateFormat("h:mm a");
            c.set(0, 0, 0, mHourHistory, mMinuteHistory);
        } else {
            df = new SimpleDateFormat("HH:mm");
            c.set(0, 0, 0, mHourHistory, mMinuteHistory);
        }
        endTimeString = df.format(c.getTime());
        endTimeText.setText(endTimeString);


            newPaidTime = (int) ((double)newTreatmentMins/(newProductivity/100)) + (int)newTravelTime;
        if((newPaidTime / 60) == 1) strHH = " hr ";
        else strHH = " hrs ";

        if((newPaidTime % 60) == 1) strMM = " min";
        else strMM = " mins";
            newPaidString = (newPaidTime / 60) + strHH + (newPaidTime % 60) + strMM;
            paidTime.setText("Paid Time: " + newPaidString);


    }
    private void reCalculateStartTime(){
        String strHH, strMM;
        double startHourTotal = (startHr*60) + startMin;
         endHourTotal = (mHourHistory*60) + mMinuteHistory;
        double difference;
        if (endHourTotal > startHourTotal)
            difference = (endHourTotal - startHourTotal) - newUnpaidTime - newTravelTime;
        else if (startHourTotal > endHourTotal)
            difference = 1440 - ((startHourTotal - endHourTotal) + newUnpaidTime + newTravelTime);
        else difference = 0;

        double treatmentMins = (double)newTreatmentMins;

        if((difference-newUnpaidTime)<=0){
            Toast.makeText(getContext(), "Start time error", Toast.LENGTH_LONG).show();
        }

        else {

            if (!is24HourMode) {
                df = new SimpleDateFormat("h:mm a");
                c.set(0, 0, 0, startHr, startMin);
            } else {
                df = new SimpleDateFormat("HH:mm");
                c.set(0, 0, 0, startHr, startMin);
            }

            startTimeString = df.format(c.getTime());
            startTimeText.setText(startTimeString);

            newProductivity = round(((treatmentMins / difference) * 100));
            NumberFormat decimalFormat = new DecimalFormat("#0.0");
            NumberFormat noDecimal = new DecimalFormat("#0");
            if (decimalFormat.format(newProductivity).endsWith(".0")) {
                newProdString = noDecimal.format(newProductivity);
                txtProductivity.setText(newProdString + "%");
            } else {
                newProdString = decimalFormat.format(newProductivity);
                txtProductivity.setText(newProdString + "%");
            }


            newPaidTime = (int) difference + (int)newTravelTime;
            if((newPaidTime / 60) == 1) strHH = " hr ";
            else strHH = " hrs ";

            if((newPaidTime % 60) == 1) strMM = " min";
            else strMM = " mins";
            newPaidString = (newPaidTime / 60) + strHH + (newPaidTime % 60) + strMM;
            paidTime.setText("Paid Time: " + newPaidString);
        }

    }
    private void reCalculateTreatTime() {
        endHourTotal = (mHourHistory * 60) + mMinuteHistory;
        double startHourTotal = (startHr * 60) + startMin;
        double difference;
        if (endHourTotal > startHourTotal)
            difference = (endHourTotal - startHourTotal) - newUnpaidTime - newTravelTime;
        else if (startHourTotal > endHourTotal)
            difference = 1440 - ((startHourTotal - endHourTotal) + newUnpaidTime + newTravelTime);
        else difference = 0;


        if (difference != 0) {

            newProductivity = round(((newTreatmentMins / difference) * 100));
            NumberFormat decimalFormat = new DecimalFormat("#0.0");
            NumberFormat noDecimal = new DecimalFormat("#0");
            if (decimalFormat.format(newProductivity).endsWith(".0")) {
                newProdString = noDecimal.format(newProductivity);
                txtProductivity.setText(newProdString + "%");
            } else {
                newProdString = decimalFormat.format(newProductivity);
                txtProductivity.setText(newProdString + "%");
            }

        }
    }

    private void reCalculateUnpaid(){
        endHourTotal = endHourTotal + newUnpaidTime;
        if(endHourTotal>1440) endHourTotal = endHourTotal-1440;

        mHourHistory = (int) (endHourTotal/60);
        mMinuteHistory = (int)endHourTotal%60;
        if (!is24HourMode) {
            df = new SimpleDateFormat("h:mm a");
            c.set(0, 0, 0, mHourHistory, mMinuteHistory);
        } else {
            df = new SimpleDateFormat("HH:mm");
            c.set(0, 0, 0, mHourHistory, mMinuteHistory);
        }
        endTimeString = df.format(c.getTime());
        endTimeText.setText(endTimeString);

    }

    /**
     * creates rounded number with one decimal place, i.e. '89.8'
     * @param value number to be rounded
     * @return rounded value
     */
    private double round(double value) {
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(1, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    private void setTimeCardState(){
        String startStr = mTimeCard.getStartTime();
        String endStr = mTimeCard.getEndTime();
        String paidStr = "Paid Time: " + mTimeCard.getPaidTime();
        String unpaidStr = mTimeCard.getUnpaidTime() + " mins";
        String meetingStr = mTimeCard.getTravelTime() + " mins";
        String prodStr = mTimeCard.getProductivityString() + "%";
        String treatTime = mTimeCard.getTreatmentTimeString();

        if(Integer.parseInt(treatTime) == 0){
            treatStr = "0 mins";
        }
        else {
            int hr = (Integer.parseInt(treatTime)) / 60;
            int min = (Integer.parseInt(treatTime)) % 60;
            treatStr =  hr + " hrs "
                    + min + " mins (" + treatTime + " mins)";
        }
        txtTreatmentTime.setText(treatStr);
        startTimeText.setText(startStr);
        endTimeText.setText(endStr);
        paidTime.setText(paidStr);
        txtUnpaidTime.setText(unpaidStr);
        txtPaidBreak.setText(meetingStr);
        txtProductivity.setText(prodStr);
        mHourHistory = mTimeCard.getEndHourInt();
        mMinuteHistory = mTimeCard.getEndMinuteInt();
        startHr = mTimeCard.getStartHour();
        startMin = mTimeCard.getStartMinute();
        startTimeString = mTimeCard.getStartTime();
        newTreatmentMins = Integer.parseInt(mTimeCard.getTreatmentTimeString());
        endTimeString = mTimeCard.getEndTime();
        newProductivity = mTimeCard.getProductivityDouble();
        newPaidString = mTimeCard.getPaidTime();
        newPaidTime = mTimeCard.getPaidTimeInt();
        newProdString = mTimeCard.getProductivityString();
        newDate = mTimeCard.getTimeCardDate();
        dateString = mTimeCard.getDate();
        txtToolBar.setText(dateString);
        newUnpaidTime = Double.parseDouble(mTimeCard.getUnpaidTime());
        newTravelTime = Double.parseDouble(mTimeCard.getTravelTime());
        newUnpaidString = mTimeCard.getUnpaidTime();
        endHourTotal = (mHourHistory*60) + mMinuteHistory;
    }

    /**
     * changes fab icons state to shown
     */
    private void showFABMenu() {
        isFabMenuOpen = true;
        fabAlarm.show();
        fabEmail.show();
        fabSms.show();
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            fabAlarm.animate().translationY(-getResources().getDimension(R.dimen.standard_55L));
            fabEmail.animate().translationY(-getResources().getDimension(R.dimen.standard_105L));
            fabSms.animate().translationY(-getResources().getDimension(R.dimen.standard_155L));
        }
        else {
            fabAlarm.animate().translationY(-getResources().getDimension(R.dimen.standard_65));
            fabEmail.animate().translationY(-getResources().getDimension(R.dimen.standard_120));
            fabSms.animate().translationY(-getResources().getDimension(R.dimen.standard_175));
        }
    }

    /**
     * returns fab menu icons to hidden state
     */
    private void closeFabMenu() {
        isFabMenuOpen = false;
        fabAlarm.animate().translationY(0);
        fabEmail.animate().translationY(0);
        fabSms.animate().translationY(0);
        fabAlarm.hide();
        fabEmail.hide();
        fabSms.hide();
    }

    /**
     * changes menu state from open to closed, or vice versa
     */
    private void toggleFabMenu() {
        if (isFabMenuOpen) {
            closeFabMenu();
        } else {
            showFABMenu();
        }
    }

    /**
     * send current time card as email
     */
    private void sendEmail() {
        SimpleDateFormat df = new SimpleDateFormat("M/d/yy", Locale.US);
        String emailDate = df.format(mTimeCard.getTimeCardDate());
        String emailBody = "Start Time: " + mTimeCard.getStartTime()+
                "\nEnd Time: " + mTimeCard.getEndTime()+
                "\nPaid Time: " + mTimeCard.getPaidTime()+
                "\nUnpaid Time: " + mTimeCard.getUnpaidTime() + " mins"+
                "\nMeeting/Travel: " + mTimeCard.getTravelTime() + " mins";

        Intent send = new Intent(Intent.ACTION_SENDTO);
        String uriText = "mailto:" + Uri.encode("")
                + "?subject=" + Uri.encode("Time Stamp for " + emailDate)
                + "&body=" + Uri.encode(emailBody);
        Uri uri = Uri.parse(uriText);
        send.setData(uri);
        if (send.resolveActivity(requireActivity().getPackageManager()) != null) {
            startActivity(send);
        }
        closeFabMenu();
    }

    /**
     * send current time card as SMS message
     */
    private void sendSms() {
        SimpleDateFormat df = new SimpleDateFormat("M/d/yy", Locale.US);
        String emailDate = df.format(mTimeCard.getTimeCardDate());
        String smsBody = "Time Stamp for " + emailDate +
                "\nStart Time: " + mTimeCard.getStartTime()+
                "\nEnd Time: " + mTimeCard.getEndTime()+
                "\nPaid Time: " + mTimeCard.getPaidTime()+
                "\nUnpaid Time: " + mTimeCard.getUnpaidTime() + " mins"+
                "\nMeeting/Travel: " + mTimeCard.getTravelTime() + " mins";
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("smsto:"));  // This ensures only SMS apps respond
        intent.putExtra("sms_body", smsBody);
        if (intent.resolveActivity(requireActivity().getPackageManager()) != null) {
            startActivity(intent);
        }
        else {
            Toast.makeText(getContext(), "No SMS app installed.", Toast.LENGTH_SHORT).show();
        }
        closeFabMenu();
    }

    /**
     * set current time card's end time as alarm
     */
    private void setAlarm() {
        Intent alarm = new Intent(AlarmClock.ACTION_SET_ALARM);
        alarm.putExtra(AlarmClock.EXTRA_HOUR, mHourHistory);
        alarm.putExtra(AlarmClock.EXTRA_MINUTES, mMinuteHistory);
        requireActivity().startActivity(alarm);
        closeFabMenu();
    }

    @Override
    public void onPause() {
        super.onPause();
        TimeCardLab.get(getActivity())
                .updateTimeCardDB(mTimeCard);
    }
}
