package com.myapp.adorg.simplecalculatorv2.help;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;

import com.myapp.adorg.simplecalculatorv2.ClearFragment;
import com.myapp.adorg.simplecalculatorv2.DeleteHistoryItemDialogActivity;
import com.myapp.adorg.simplecalculatorv2.R;
import com.myapp.adorg.simplecalculatorv2.StatsFragment;

public class HelpViewPager extends AppCompatActivity {


    private ViewPager viewPager;
    private Toolbar toolbar;
    private TabLayout tabLayout;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
