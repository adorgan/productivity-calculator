package com.myapp.adorg.simplecalculatorv2;

import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.provider.AlarmClock;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Interpolator;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class TimeCardFragment extends AppCompatActivity {

    private TextView endTimeText, startTimeText, paidTime, txtDate, txtUnpaidTime, txtPaidBreak, txtProductivity;
    private Toolbar toolbar;
    private FloatingActionButton fab1, fab2, fab3, fab;
    private boolean isFABOpen;
    private int endHr, endMin;
    private TimeCard mTimeCard;
    private String[] suffixes =
            //    0     1     2     3     4     5     6     7     8     9
            { "th", "st", "nd", "rd", "th", "th", "th", "th", "th", "th",
                    //    10    11    12    13    14    15    16    17    18    19
                    "th", "th", "th", "th", "th", "th", "th", "th", "th", "th",
                    //    20    21    22    23    24    25    26    27    28    29
                    "th", "st", "nd", "rd", "th", "th", "th", "th", "th", "th",
                    //    30    31
                    "th", "st" };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_card);

        final Calendar c = Calendar.getInstance();
        SimpleDateFormat month_date = new SimpleDateFormat("MMMM");
        String month_name = month_date.format(c.getTime());
        int day = c.get(Calendar.DATE);
        int year = c.get(Calendar.YEAR);


        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(month_name + " " + day + suffixes[day] + ", " + year);
        // Show the Up button in the action bar.
        actionBar.setDisplayHomeAsUpEnabled(true);


        mTimeCard = (TimeCard) getIntent().getSerializableExtra("object");

        fab = findViewById(R.id.fab);
        fab1 = findViewById(R.id.fab1);
        fab2 = findViewById(R.id.fab2);
        fab3 = findViewById(R.id.fab3);



        txtUnpaidTime = findViewById(R.id.unPaidTime);
        endTimeText = findViewById(R.id.EndTime);
        startTimeText = findViewById(R.id.startTime);
        paidTime = findViewById(R.id.textView4);
        txtPaidBreak = findViewById(R.id.txtPaidBreak);
        txtProductivity = findViewById(R.id.productivityTextView);
        endHr = mTimeCard.getmEndHour();
        endMin = mTimeCard.getmEndMinute();


        startTimeText.setText("Start Time: " + mTimeCard.getStartTime());
        endTimeText.setText("End Time: " + mTimeCard.getEndTime());
        paidTime.setText("Paid Time: " + mTimeCard.getPaidTime());
        txtUnpaidTime.setText("Unpaid Time: " + mTimeCard.getUnpaidTime() + " mins");
        txtPaidBreak.setText("Meeting/Travel: " + mTimeCard.getTravelTime() + " mins");
        txtProductivity.setText(mTimeCard.getProductivity() + "%");


    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.settingsHistory) {
            Intent feedbackIntent = new Intent(Intent.ACTION_SENDTO);
            String uriText = "mailto:" + Uri.encode("productivity.calculator.app@gmail.com") +
                    "?subject=" + Uri.encode("Feedback for Productivity Calculator App") + "&body=" + Uri.encode("");
            Uri uri = Uri.parse(uriText);

            feedbackIntent.setData(uri);
            if (feedbackIntent.resolveActivity(getPackageManager()) != null) {
                startActivity(feedbackIntent);

            }
            return true;
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

    public void showFABMenu(View view) {
        final Interpolator interpolator = null;

        if (!isFABOpen) {
            showFABMenu();

        } else {
            closeFABMenu();
        }
        }



    public void openFabMenu(View view) {
        final Interpolator interpolator = null;

        if (!isFABOpen) {
            showFABMenu();

        } else {
            closeFABMenu();
        }
    }






    public void sendEmail(View view) {
        final Calendar c = Calendar.getInstance();
        int day = c.get(Calendar.DATE);
        int month = c.get(Calendar.MONTH) + 1;
        int year = c.get(Calendar.YEAR);



            StringBuilder email = new StringBuilder();
            String endLine;
            email.append(System.getProperty("line.separator"));


            endLine = "Start Time: " + mTimeCard.getStartTime()+
                    "\nEnd Time: " + mTimeCard.getEndTime()+
                    "\nPaid Time: " + mTimeCard.getPaidTime()+
                    "\nUnpaid Time: " + mTimeCard.getUnpaidTime() + " mins"+
                    "\nMeeting/Travel: " + mTimeCard.getTravelTime() + " mins";


        Intent send = new Intent(Intent.ACTION_SENDTO);
            String uriText = "mailto:" + Uri.encode("") +
                    "?subject=" + Uri.encode("Time Stamp for " + month + "-" + day + "-" + year) + "&body=" + Uri.encode(endLine);
            Uri uri = Uri.parse(uriText);

            send.setData(uri);
            if (send.resolveActivity(getPackageManager()) != null) {
                startActivity(send);

            }

         closeFABMenu();
    }

    public void sendSms(View view) {
        final Calendar c = Calendar.getInstance();
        int day = c.get(Calendar.DATE);
        int month = c.get(Calendar.MONTH) + 1;
        int year = c.get(Calendar.YEAR);




            String endLine;


            endLine = "Time Stamp For " + month + "-" + day + "-" + year +
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

        if(endHr>=24) endHr -=24;

                alarm.putExtra(AlarmClock.EXTRA_HOUR, endHr);
                alarm.putExtra(AlarmClock.EXTRA_MINUTES, endMin);
                startActivity(alarm);

         closeFABMenu();
    }



}
