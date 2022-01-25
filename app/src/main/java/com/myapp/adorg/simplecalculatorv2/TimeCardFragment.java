package com.myapp.adorg.simplecalculatorv2;

import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.provider.AlarmClock;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;



import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Objects;

public class TimeCardFragment extends AppCompatActivity{

    private FloatingActionButton fabAlarm;
    private FloatingActionButton fabEmail;
    private FloatingActionButton fabSMS;
    private boolean isFABOpen;
    private int endHr, endMin;
    private TimeCard mTimeCard;
    private int addTimeCardCounter = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // set up content view based on user's light/dark mode preference
        if (Preferences.getDarkMode(getApplicationContext())) {
            setTheme(R.style.DarkTheme);
        } else {
            setTheme(R.style.LightTheme);
        }
        setContentView(R.layout.activity_time_card);

        if(savedInstanceState != null){
            addTimeCardCounter = savedInstanceState.getInt("INT_I");
        }
        mTimeCard = (TimeCard) getIntent().getSerializableExtra("object");

        // setup toolbar
        Toolbar timeCardToolbar = findViewById(R.id.timeCardToolbar);
        String titleDate = mTimeCard.getDate();
        setSupportActionBar(timeCardToolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle(titleDate);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        // initialize FABs
        FloatingActionButton fabMain = findViewById(R.id.fab);
        fabAlarm = findViewById(R.id.fab1);
        fabEmail = findViewById(R.id.fab2);
        fabSMS = findViewById(R.id.fab3);

        fabMain.setOnClickListener(this::openFabMenu);
        fabEmail.setOnClickListener(this::sendEmail);
        fabAlarm.setOnClickListener(this::setAlarm);
        fabSMS.setOnClickListener(this::sendSms);

        // initialize text in time card window
        TextView txtTreatmentTime = findViewById(R.id.treatmentTimeTimeCard);
        TextView txtUnpaidTime = findViewById(R.id.unPaidTime);
        TextView endTimeText = findViewById(R.id.EndTime);
        TextView startTimeText = findViewById(R.id.startTime);
        TextView paidTime = findViewById(R.id.textView4);
        TextView txtPaidBreak = findViewById(R.id.txtPaidBreak);
        TextView txtProductivity = findViewById(R.id.productivityTextView);

        // parse time card data
        String startStr = "Start Time: " + mTimeCard.getStartTime();
        String endStr = "End Time: " + mTimeCard.getEndTime();
        String paidStr = "Paid Time: " + mTimeCard.getPaidTime();
        String unpaidStr = "Unpaid Time: " + mTimeCard.getUnpaidTime() + " mins";
        String meetingStr = "Meeting/Travel: " + mTimeCard.getTravelTime() + " mins";
        String prodStr = mTimeCard.getProductivityString() + "%";
        String treatTime = mTimeCard.getTreatmentTimeString();

        String treatStr;
        String strHH, strMM, strMM2;
        // format Treatment string
        if(Integer.parseInt(treatTime) == 0){
            treatStr = "Treatment: 0 mins";
        }
        else {
            int hr  = (Integer.parseInt(treatTime)) / 60;
            int min = (Integer.parseInt(treatTime)) % 60;
            strHH = (hr == 1) ? " hr " : " hrs ";
            strMM = (min == 1) ? " min" : " mins";
            strMM2 = (Integer.parseInt(treatTime) == 1) ? " min" : " mins";

            // format string: Treatment: X hrs Y mins (Y mins)
            treatStr = "Treatment: " + hr + strHH + min + strMM + " (" + treatTime + strMM2 + ")";
        }

        txtTreatmentTime.setText(treatStr);
        startTimeText.setText(startStr);
        endTimeText.setText(endStr);
        paidTime.setText(paidStr);
        txtUnpaidTime.setText(unpaidStr);
        txtPaidBreak.setText(meetingStr);
        txtProductivity.setText(prodStr);

        endHr = mTimeCard.getEndHourInt();
        endMin = mTimeCard.getEndMinuteInt();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("INT_I", addTimeCardCounter);
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

        if (id == R.id.timeCardHistory) {
            TimeCardLab timeCardLab  = TimeCardLab.get(getApplicationContext());
            if(mTimeCard.getPaidTimeInt()==0){
                Toast.makeText(getApplicationContext(), "Unable to save empty time card", Toast.LENGTH_SHORT).show();
            }
            else {
                // make sure user can't repeatedly save the same time card to the DB
                if (addTimeCardCounter == 1) {
                    timeCardLab.addTimeCard(mTimeCard);
                }
                Toast.makeText(getApplicationContext(), "Time card has been saved to history", Toast.LENGTH_SHORT).show();
                addTimeCardCounter++;
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * expands fab menu for email/sms/alarm options
     */
    private void showFABMenu() {
        isFABOpen = true;
        fabAlarm.show();
        fabEmail.show();
        fabSMS.show();

        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            fabAlarm.animate().translationY(-getResources().getDimension(R.dimen.standard_55L));
            fabEmail.animate().translationY(-getResources().getDimension(R.dimen.standard_105L));
            fabSMS.animate().translationY(-getResources().getDimension(R.dimen.standard_155L));
        }
        else {
            fabAlarm.animate().translationY(-getResources().getDimension(R.dimen.standard_65));
            fabEmail.animate().translationY(-getResources().getDimension(R.dimen.standard_120));
            fabSMS.animate().translationY(-getResources().getDimension(R.dimen.standard_175));
        }
    }

    /**
     * closes fab icons back to original state
     */
    private void closeFABMenu() {
        isFABOpen = false;
        fabAlarm.animate().translationY(0);
        fabEmail.animate().translationY(0);
        fabSMS.animate().translationY(0);
        fabAlarm.hide();
        fabEmail.hide();
        fabSMS.hide();
    }

    /**
     * changes state of fabs
     * @param view current view
     */
    public void openFabMenu(View view) {
        if (!isFABOpen) {
            showFABMenu();
        }
        else {
            closeFABMenu();
        }
    }


    /**
     * Sends an email with the current time card's data via an intent
     * @param view time card view
     */
    public void sendEmail(View view) {
        // set email data
        SimpleDateFormat df = new SimpleDateFormat("M/d/yy", Locale.US);
        String emailDate = df.format(mTimeCard.getTimeCardDate());

        String body =   "Start Time: "      + mTimeCard.getStartTime() +
                        "\nEnd Time: "      + mTimeCard.getEndTime() +
                        "\nPaid Time: "     + mTimeCard.getPaidTime() +
                        "\nUnpaid Time: "   + mTimeCard.getUnpaidTime() + " mins" +
                        "\nMeeting/Travel: "+ mTimeCard.getTravelTime() + " mins";

        String uriText =    "mailto:"   + Uri.encode("") +
                            "?subject=" + Uri.encode("Time Stamp for " + emailDate) +
                            "&body="    + Uri.encode(body);

        // send email intent
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse(uriText));

        // check to make sure they have email app installed
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }

        // fold fabs back down
        closeFABMenu();
    }

    /**
     * Opens up SMS application for sending time card data
     * @param view current view
     */
    public void sendSms(View view) {
        String endLine;
        SimpleDateFormat df = new SimpleDateFormat("M/d/yy", Locale.US);
        String emailDate = df.format(mTimeCard.getTimeCardDate());

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

    /**
     * Sets an alarm for the calculated end time
     * @param view current view
     */
    public void setAlarm(View view) {
        Intent alarm = new Intent(AlarmClock.ACTION_SET_ALARM);

        alarm.putExtra(AlarmClock.EXTRA_HOUR, endHr);
        alarm.putExtra(AlarmClock.EXTRA_MINUTES, endMin);
        startActivity(alarm);

        closeFABMenu();
    }
}
