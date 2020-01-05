package com.myapp.adorg.simplecalculatorv2.help;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.tabs.TabLayout;

import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.MenuItem;

import com.myapp.adorg.simplecalculatorv2.Preferences;
import com.myapp.adorg.simplecalculatorv2.R;

public class HelpViewPager extends AppCompatActivity {


    private ViewPager viewPager;
    private Toolbar toolbar;
    private TabLayout tabLayout;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Preferences.getDarkMode(getApplicationContext())) {
            setTheme(R.style.DarkTheme);
        } else {
            setTheme(R.style.LightTheme);
        }
        setContentView(R.layout.help_viewpager);

        viewPager = findViewById(R.id.helpViewPager);
        toolbar = findViewById(R.id.helpToolbar);
        tabLayout = findViewById(R.id.tabLayout);
        setSupportActionBar(toolbar);
        final ActionBar ab = getSupportActionBar();
        assert ab != null;
        ab.setDisplayHomeAsUpEnabled(true);

        FragmentManager fragmentManager = getSupportFragmentManager();
        viewPager.setAdapter(new ViewPagerAdapter(fragmentManager));
        tabLayout.setupWithViewPager(viewPager);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.help_menu, menu);

        return true;
    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.helpMenu: {


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
}
