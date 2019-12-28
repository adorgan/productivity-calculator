package com.myapp.adorg.simplecalculatorv2;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.provider.AlarmClock;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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
import java.util.UUID;


public class HistoryTimeCardFragment extends Fragment{

    private static final String ARG_HISTORY_TIME_CARD_ID = "history_time_card_ID";
    private TimeCard mTimeCard;
    private TextView endTimeText, startTimeText, paidTime, txtUnpaidTime, txtPaidBreak, txtProductivity, txtTreatmentTime, txtToolBar;
    private int  startHr, startMin;
    private UUID crimeId;
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
    private boolean mIsTwentyFourHistory;
    private int mMinuteHistory, mHourHistory;
    private SimpleDateFormat df;
    private Calendar c = Calendar.getInstance();
    private double newProductivity;
    private String newProdString;
    private boolean savedIs24Hour;
    private String startTimeString, endTimeString, dateString;
    private int newPaidTime, newTreatmentMins;
    private Date newDate;
    private boolean dateChanged = false;
    private double newUnpaidTime, newTravelTime;
    private String newUnpaidString, newTravelString;
    private double endHourTotal;
    private FloatingActionButton fab, fab1, fab2, fab3;
    private boolean isFABOpen;



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

            crimeId = (UUID) getArguments().getSerializable(ARG_HISTORY_TIME_CARD_ID);
            mTimeCard = TimeCardLab.get(getActivity()).getTimeCard(crimeId);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_history_time_card, container, false);
        fab = v.findViewById(R.id.fabHistoryTC);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFabMenu();
            }
        });
        fab1 = v.findViewById(R.id.fab1HistoryTC);
        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAlarm();
            }
        });
        fab2 = v.findViewById(R.id.fab2HistoryTC);
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendEmail();
            }
        });
        fab3 = v.findViewById(R.id.fab3HistoryTC);
        fab3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendSms();
            }
        });
        toolbar = v.findViewById(R.id.historyTimeCardToolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(null);
        ActionBar ab = ((AppCompatActivity)getActivity()).getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        txtToolBar = v.findViewById(R.id.historyTimeCardToolBarText);

        txtToolBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();
                DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(newDate);
                datePickerDialog.setTargetFragment(HistoryTimeCardFragment.this, REQUEST_DATE );
                datePickerDialog.show(fm, DIALOG_DATE);
            }
        });

        startTimeText = v.findViewById(R.id.startTimeHistoryTC);
        savedIs24Hour = mTimeCard.getIs24Hour() != 0;


        startTimeText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();
                TimePickerDialog timePickerDialog = TimePickerDialog.newInstance(startHr, startMin, savedIs24Hour);
                timePickerDialog.setTargetFragment(HistoryTimeCardFragment.this, REQUEST_START_TIME);
                timePickerDialog.show(fm,DIALOG_START_TIME);
            }
        });

        endTimeText = v.findViewById(R.id.EndTimeHistoryTC);
        endTimeText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();
                TimePickerDialog timePickerDialog = TimePickerDialog.newInstance(mHourHistory, mMinuteHistory, savedIs24Hour);
                timePickerDialog.setTargetFragment(HistoryTimeCardFragment.this, REQUEST_TIME_HISTORY);
                timePickerDialog.show(fm,DIALOG_END_TIME_HISTORY);
            }
        });
        endTimeText.setClickable(false);
        startTimeText.setClickable(false);
        txtToolBar.setClickable(false);
        txtTreatmentTime = v.findViewById(R.id.treatmentTimeHistoryTC);
        txtTreatmentTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();
                ChangeNumberDialog changeNumberDialog = ChangeNumberDialog.newInstance(newTreatmentMins, endHourTotal);
                changeNumberDialog.setTargetFragment(HistoryTimeCardFragment.this, REQUEST_TREAT_MINS );
                changeNumberDialog.show(fm, DIALOG_TREAT_MINS);
            }
        });
        txtTreatmentTime.setClickable(false);
        txtUnpaidTime = v.findViewById(R.id.unPaidTimeHistory);
        txtUnpaidTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();
                ChangeNumberDialog changeNumberDialog = ChangeNumberDialog.newInstance((int)newUnpaidTime, endHourTotal);
                changeNumberDialog.setTargetFragment(HistoryTimeCardFragment.this, REQUEST_UNPAID);
                changeNumberDialog.show(fm, DIALOG_UNPAID);

            }
        });
        txtUnpaidTime.setClickable(false);
        paidTime = v.findViewById(R.id.PaidTimeHistoryTC);
        txtPaidBreak = v.findViewById(R.id.txtPaidBreakHistoryTC);
        txtPaidBreak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();
                ChangeNumberDialog changeNumberDialog = ChangeNumberDialog.newInstance((int)newTravelTime, endHourTotal);
                changeNumberDialog.setTargetFragment(HistoryTimeCardFragment.this, REQUEST_TRAVEL);
                changeNumberDialog.show(fm, DIALOG_TRAVEL);
            }
        });
        txtPaidBreak.setClickable(false);
        txtProductivity = v.findViewById(R.id.productivityTextViewHistoryTC);
        txtProductivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();
                ProductivityPicker productivityPicker = ProductivityPicker.newInstance(newProductivity);
                productivityPicker.setTargetFragment(HistoryTimeCardFragment.this, REQUEST_PRODUCTVITY);
                productivityPicker.show(fm, DIALOG_PRODUCTIVITY);
            }
        });
        txtProductivity.setClickable(false);


        setOriginalValues();

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.deleteHistoryItem: {


                FragmentManager fm = getFragmentManager();
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
                fab.animate().translationY(getResources().getDimension(R.dimen.standard_105));


                return true;
            }
            case android.R.id.home: {

                if(!isEdited) getActivity().onBackPressed();
                else{
                    returnFromEdit();
                    setOriginalValues();
                }
                return true;

            }case R.id.menuItemSaved:{
                mTimeCard.setEndMinute(mMinuteHistory);
                mTimeCard.setmEndHour(mHourHistory);
                mTimeCard.setEndTime(endTimeString);
                mTimeCard.setmProductivityDouble(newProductivity);
                mTimeCard.setProductivity(newProdString);
                mTimeCard.setPaidTime(newPaidString);

                mTimeCard.setStartHour(startHr);
                mTimeCard.setStartMinute(startMin);
                mTimeCard.setStartTime(startTimeString);
                mTimeCard.setmPaidTimeInt(newPaidTime);
                mTimeCard.setDate(dateString);
                mTimeCard.setMcardDate(newDate);
                mTimeCard.setUnpaidTime(Integer.toString((int)newUnpaidTime));
                mTimeCard.setmTreatmentTime(Integer.toString(newTreatmentMins));
                mTimeCard.setTravelTime(Integer.toString((int)newTravelTime));

                setOriginalValues();
                returnFromEdit();
                if(dateChanged) Preferences.setDateChange(getContext(), true);
            }
        }

        return super.onOptionsItemSelected(item);
    }

    private void setClickable(TextView txtview){
        txtview.setClickable(true);
        txtview.setTextColor(getResources().getColor(R.color.colorPrimary));
    }
    private void setUnClickable(TextView txtview){
        txtview.setClickable(false);
        txtview.setTextColor(getResources().getColor(R.color.colorBlack));
    }
    private void returnFromEdit(){
        setUnClickable(startTimeText);
        setUnClickable(endTimeText);
        setUnClickable(txtTreatmentTime);
        setUnClickable(txtUnpaidTime);
        setUnClickable(txtPaidBreak);
        setUnClickable(txtProductivity);
        txtToolBar.setClickable(false);
        txtToolBar.setTextColor(getResources().getColor(R.color.colorWgite));
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
            boolean cleared = data.getBooleanExtra(DeleteHistoryItemDialog.EXTRA_DELETED, false);
            if(cleared) {
                TimeCardLab timeCardLab = TimeCardLab.get(getContext());
                timeCardLab.searchAndDelete(crimeId);
                Preferences.setPrefDelete(getContext(), true);
                getActivity().onBackPressed();
            }
        } else if (requestCode == REQUEST_TIME_HISTORY) {
            mIsTwentyFourHistory = data.getBooleanExtra(TimePickerDialog.EXTRA_ISTWENTYFOUR, false);
            double testendHourTotal = (data.getIntExtra(com.myapp.adorg.simplecalculatorv2.TimePickerDialog.EXTRA_HOUR, 0)*60) + data.getIntExtra(com.myapp.adorg.simplecalculatorv2.TimePickerDialog.EXTRA_MINUTE, 0);
            double startHourTotal = (startHr*60) + startMin;
            double difference;
            if (testendHourTotal > startHourTotal)
                difference = (testendHourTotal - startHourTotal) - newUnpaidTime - newTravelTime;
            else if (startHourTotal > testendHourTotal)
                difference = 1440 - ((startHourTotal - testendHourTotal) + newUnpaidTime + newTravelTime);
            else difference = 0;

            if((difference-newUnpaidTime)<=0){
                Toast.makeText(getContext(), "End time error", Toast.LENGTH_LONG).show();
            }
            else {
                mHourHistory = data.getIntExtra(com.myapp.adorg.simplecalculatorv2.TimePickerDialog.EXTRA_HOUR, 0);
                mMinuteHistory = data.getIntExtra(com.myapp.adorg.simplecalculatorv2.TimePickerDialog.EXTRA_MINUTE, 0);
                reCalculateEndTime();
            }

        } else if (requestCode == REQUEST_START_TIME) {
            mIsTwentyFourHistory = data.getBooleanExtra(TimePickerDialog.EXTRA_ISTWENTYFOUR, false);
            double startHourTotal = (data.getIntExtra(com.myapp.adorg.simplecalculatorv2.TimePickerDialog.EXTRA_HOUR, 0)*60) + data.getIntExtra(com.myapp.adorg.simplecalculatorv2.TimePickerDialog.EXTRA_MINUTE, 0);
            endHourTotal = (mHourHistory*60) + mMinuteHistory;
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
        } else if (requestCode == REQUEST_DATE) {
            newDate = (Date) data
                    .getSerializableExtra(DatePickerDialog.EXTRA_DATE);
            SimpleDateFormat df = new SimpleDateFormat("EE, MMM d, yyyy", Locale.US);
            dateString = df.format(newDate);
            txtToolBar.setText(dateString);
            dateChanged = true;
        }
        else if(requestCode == REQUEST_TREAT_MINS){

            if(data.getIntExtra(ChangeNumberDialog.EXTRA_Minutes, 0)==0)
                Toast.makeText(getContext(), "Unable to calculate", Toast.LENGTH_LONG).show();
            else {
                newTreatmentMins = data.getIntExtra(ChangeNumberDialog.EXTRA_Minutes, 0);

                treatStr = (newTreatmentMins / 60) + " hrs " + (newTreatmentMins % 60) + " mins (" + newTreatmentMins + " mins)";
                txtTreatmentTime.setText(treatStr);
                reCalculateTreatTime();
            }
        } else if (requestCode == REQUEST_UNPAID) {
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

            if (!mIsTwentyFourHistory) {
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
            if (!mIsTwentyFourHistory) {
                df = new SimpleDateFormat("h:mm a");
                c.set(0, 0, 0, mHourHistory, mMinuteHistory);
            } else {
                df = new SimpleDateFormat("HH:mm");
                c.set(0, 0, 0, mHourHistory, mMinuteHistory);
            }
            endTimeString = df.format(c.getTime());
            endTimeText.setText(endTimeString);

            newProductivity = round(((treatmentMins / difference) * 100), 1);
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

        if (!mIsTwentyFourHistory) {
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

            if (!mIsTwentyFourHistory) {
                df = new SimpleDateFormat("h:mm a");
                c.set(0, 0, 0, startHr, startMin);
            } else {
                df = new SimpleDateFormat("HH:mm");
                c.set(0, 0, 0, startHr, startMin);
            }

            startTimeString = df.format(c.getTime());
            startTimeText.setText(startTimeString);

            newProductivity = round(((treatmentMins / difference) * 100), 1);
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

            newProductivity = round(((newTreatmentMins / difference) * 100), 1);
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
        if (!mIsTwentyFourHistory) {
            df = new SimpleDateFormat("h:mm a");
            c.set(0, 0, 0, mHourHistory, mMinuteHistory);
        } else {
            df = new SimpleDateFormat("HH:mm");
            c.set(0, 0, 0, mHourHistory, mMinuteHistory);
        }
        endTimeString = df.format(c.getTime());
        endTimeText.setText(endTimeString);

    }

    private double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    private void setOriginalValues(){
        String startStr = mTimeCard.getStartTime();
        String endStr = mTimeCard.getEndTime();
        String paidStr = "Paid Time: " + mTimeCard.getPaidTime();
        String unpaidStr = mTimeCard.getUnpaidTime() + " mins";
        String meetingStr = mTimeCard.getTravelTime() + " mins";
        String prodStr = mTimeCard.getProductivity() + "%";
        String treatTime = mTimeCard.getmTreatmentTime();

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
        mHourHistory = mTimeCard.getmEndHour();
        mMinuteHistory = mTimeCard.getmEndMinute();
        startHr = mTimeCard.getStartHour();
        startMin = mTimeCard.getStartMinute();
        startTimeString = mTimeCard.getStartTime();
        newTreatmentMins = Integer.parseInt(mTimeCard.getmTreatmentTime());
        endTimeString = mTimeCard.getEndTime();
        newProductivity = mTimeCard.getmProductivityDouble();
        newPaidString = mTimeCard.getPaidTime();
        newPaidTime = mTimeCard.getmPaidTimeInt();
        newProdString = mTimeCard.getProductivity();
        newDate = mTimeCard.getMcardDate();
        dateString = mTimeCard.getDate();
        txtToolBar.setText(dateString);
        newUnpaidTime = Double.parseDouble(mTimeCard.getUnpaidTime());
        newTravelTime = Double.parseDouble(mTimeCard.getTravelTime());
        newUnpaidString = mTimeCard.getUnpaidTime();
        newTravelString = mTimeCard.getTravelTime();
        endHourTotal = (mHourHistory*60) + mMinuteHistory;



    }

    private void showFABMenu() {
        isFABOpen = true;
        fab1.show();
        fab2.show();
        fab3.show();


        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            fab1.animate().translationY(-getResources().getDimension(R.dimen.standard_55L));
            fab2.animate().translationY(-getResources().getDimension(R.dimen.standard_105L));
            fab3.animate().translationY(-getResources().getDimension(R.dimen.standard_155L));
        }
        else {
            fab1.animate().translationY(-getResources().getDimension(R.dimen.standard_55));
            fab2.animate().translationY(-getResources().getDimension(R.dimen.standard_105));
            fab3.animate().translationY(-getResources().getDimension(R.dimen.standard_155));
        }


    }
    private void closeFABMenu() {
        isFABOpen = false;
        fab1.animate().translationY(0);
        fab2.animate().translationY(0);
        fab3.animate().translationY(0);
        fab1.hide();
        fab2.hide();
        fab3.hide();


    }


    private void openFabMenu() {

        if (!isFABOpen) {
            showFABMenu();

        } else {
            closeFABMenu();
        }
    }

    private void sendEmail() {

        SimpleDateFormat df = new SimpleDateFormat("M/d/yy", Locale.US);
        String emailDate = df.format(mTimeCard.getMcardDate());

        String endLine = "Start Time: " + mTimeCard.getStartTime()+
                "\nEnd Time: " + mTimeCard.getEndTime()+
                "\nPaid Time: " + mTimeCard.getPaidTime()+
                "\nUnpaid Time: " + mTimeCard.getUnpaidTime() + " mins"+
                "\nMeeting/Travel: " + mTimeCard.getTravelTime() + " mins";


        Intent send = new Intent(Intent.ACTION_SENDTO);
        String uriText = "mailto:" + Uri.encode("") +
                "?subject=" + Uri.encode("Time Stamp for " + emailDate) + "&body=" + Uri.encode(endLine);
        Uri uri = Uri.parse(uriText);

        send.setData(uri);
        if (send.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivity(send);

        }

        closeFABMenu();
    }

    private void sendSms() {


        String endLine;
        SimpleDateFormat df = new SimpleDateFormat("M/d/yy", Locale.US);
        String emailDate = df.format(mTimeCard.getMcardDate());

        endLine = "Time Stamp for " + emailDate +
                "\nStart Time: " + mTimeCard.getStartTime()+
                "\nEnd Time: " + mTimeCard.getEndTime()+
                "\nPaid Time: " + mTimeCard.getPaidTime()+
                "\nUnpaid Time: " + mTimeCard.getUnpaidTime() + " mins"+
                "\nMeeting/Travel: " + mTimeCard.getTravelTime() + " mins";



        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("smsto:"));  // This ensures only SMS apps respond
        intent.putExtra("sms_body", endLine);

        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivity(intent);
        }
        else
            Toast.makeText(getContext(), "No SMS app installed.", Toast.LENGTH_SHORT).show();
        closeFABMenu();
    }

    private void setAlarm() {
        Intent alarm = new Intent(AlarmClock.ACTION_SET_ALARM);

            alarm.putExtra(AlarmClock.EXTRA_HOUR, mHourHistory);
            alarm.putExtra(AlarmClock.EXTRA_MINUTES, mMinuteHistory);
            getActivity().startActivity(alarm);

        closeFABMenu();
    }

    @Override
    public void onPause() {
        super.onPause();
        TimeCardLab.get(getActivity())
                .updateTimeCardDB(mTimeCard);

        
    }
}
