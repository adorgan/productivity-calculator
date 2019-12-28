package com.myapp.adorg.simplecalculatorv2;

import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.provider.AlarmClock;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Interpolator;
import android.widget.TextView;
import android.widget.Toast;



import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class TimeCardFragment extends AppCompatActivity{

    private TextView endTimeText, startTimeText, paidTime, txtUnpaidTime, txtPaidBreak, txtProductivity, txtTreatmentTime;
    private FloatingActionButton fab1, fab2, fab3, fab;
    private boolean isFABOpen;
    private int endHr, endMin;
    private TimeCard mTimeCard;
    private int i = 1;
    private Toolbar timeCardToolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_card);



        if(savedInstanceState != null){
            i = savedInstanceState.getInt("INT_I");
        }
        mTimeCard = (TimeCard) getIntent().getSerializableExtra("object");

        String titleDate = mTimeCard.getDate();

        timeCardToolbar = findViewById(R.id.timeCardToolbar);
        setSupportActionBar(timeCardToolbar);
        getSupportActionBar().setTitle(titleDate);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);


        fab = findViewById(R.id.fab);
        fab1 = findViewById(R.id.fab1);
        fab2 = findViewById(R.id.fab2);
        fab3 = findViewById(R.id.fab3);


        txtTreatmentTime = findViewById(R.id.treatmentTimeTimeCard);
        txtUnpaidTime = findViewById(R.id.unPaidTime);
        endTimeText = findViewById(R.id.EndTime);
        startTimeText = findViewById(R.id.startTime);
        paidTime = findViewById(R.id.textView4);
        txtPaidBreak = findViewById(R.id.txtPaidBreak);
        txtProductivity = findViewById(R.id.productivityTextView);
        endHr = mTimeCard.getmEndHour();
        endMin = mTimeCard.getmEndMinute();

        String startStr = "Start Time: " + mTimeCard.getStartTime();
        String endStr = "End Time: " + mTimeCard.getEndTime();
        String paidStr = "Paid Time: " + mTimeCard.getPaidTime();
        String unpaidStr = "Unpaid Time: " + mTimeCard.getUnpaidTime() + " mins";
        String meetingStr = "Meeting/Travel: " + mTimeCard.getTravelTime() + " mins";
        String prodStr = mTimeCard.getProductivity() + "%";
        String treatTime = mTimeCard.getmTreatmentTime();
        String treatStr;
        String strHH, strMM, strMM2;
        if(Integer.parseInt(treatTime) == 0){
            treatStr = "Treatment: 0 mins";
        }
        else {

            int hr = (Integer.parseInt(treatTime)) / 60;
            int min = (Integer.parseInt(treatTime)) % 60;

            if (hr == 1) strHH = " hr ";
            else strHH = " hrs ";

            if (min == 1) strMM = " min";
            else strMM = " mins";

            if (Integer.parseInt(treatTime) == 1) strMM2 = " min";
            else strMM2 = " mins";
            treatStr = "Treatment: " + hr + strHH
                    + min + strMM + " (" + treatTime + strMM2 + ")";
        }


        txtTreatmentTime.setText(treatStr);
        startTimeText.setText(startStr);
        endTimeText.setText(endStr);
        paidTime.setText(paidStr);
        txtUnpaidTime.setText(unpaidStr);
        txtPaidBreak.setText(meetingStr);
        txtProductivity.setText(prodStr);




    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("INT_I", i);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.time_card_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.timeCardHistory) {
            TimeCardLab timeCardLab  = TimeCardLab.get(getApplicationContext());
            if(mTimeCard.getmPaidTimeInt()==0){
                Toast.makeText(getApplicationContext(), "Unable to save empty time card", Toast.LENGTH_SHORT).show();
                return true;
            }
            else {
                if (i == 1) {
                    timeCardLab.addTimeCard(mTimeCard);
                }
                Toast.makeText(getApplicationContext(), "Time card has been saved to history", Toast.LENGTH_SHORT).show();
                i++;
                return true;
            }
        }

        return super.onOptionsItemSelected(item);
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


    public void openFabMenu(View view) {

        if (!isFABOpen) {
            showFABMenu();

        } else {
            closeFABMenu();
        }
    }






    public void sendEmail(View view) {

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
        if (send.resolveActivity(getPackageManager()) != null) {
            startActivity(send);

        }

        closeFABMenu();
    }

    public void sendSms(View view) {


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

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
        else
            Toast.makeText(this, "No SMS app installed.", Toast.LENGTH_SHORT).show();
        closeFABMenu();
    }

    public void setAlarm(View view) {
        Intent alarm = new Intent(AlarmClock.ACTION_SET_ALARM);

        alarm.putExtra(AlarmClock.EXTRA_HOUR, endHr);
        alarm.putExtra(AlarmClock.EXTRA_MINUTES, endMin);
        startActivity(alarm);

        closeFABMenu();
    }



}
