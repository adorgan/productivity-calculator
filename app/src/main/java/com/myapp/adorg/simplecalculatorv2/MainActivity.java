package com.myapp.adorg.simplecalculatorv2;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity{

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
        setContentView(R.layout.activity_main);

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);
        if (fragment == null) {
            fragment = new CalculatorFragment();
            fm.beginTransaction()
                    .add(R.id.fragment_container, fragment)
                    .commit();
        }

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.settings:{
                Intent intent2 = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent2);
                return true;
            }
            case R.id.settingsHelp:{
                Toast.makeText(getApplicationContext(), "1. Tap time to select start time.\n2. Tap productivity to set target %.\n3. Enter minutes.", Toast.LENGTH_LONG).show();
                return true;
            }
            case R.id.settingsHistory: {
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
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Calendar c = Calendar.getInstance();
        SimpleDateFormat month_date = new SimpleDateFormat("MMMM");
        String month_name = month_date.format(c.getTime());
        int day = c.get(Calendar.DATE);
        int year = c.get(Calendar.YEAR);


        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(month_name + " " + day + suffixes[day] + ", " + year);
    }
}