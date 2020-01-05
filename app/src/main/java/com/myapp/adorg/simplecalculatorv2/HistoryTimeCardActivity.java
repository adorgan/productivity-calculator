package com.myapp.adorg.simplecalculatorv2;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.AppCompatActivity;


import java.util.UUID;

public class HistoryTimeCardActivity extends AppCompatActivity{

    private static final String EXTRA_CRIME_ID = "com.example.criminalintent.crime_id";
    private UUID uuid;

    public static Intent newIntent(Context packageContext, UUID crimeId) {
        Intent intent = new Intent(packageContext, HistoryTimeCardActivity.class);
        intent.putExtra(EXTRA_CRIME_ID, crimeId);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        if (Preferences.getDarkMode(getApplicationContext())) {
            setTheme(R.style.DarkTheme);
        } else {
            setTheme(R.style.LightTheme);
        }
        setContentView(R.layout.content_history_time_card);

        uuid = (UUID) getIntent()
                .getSerializableExtra(EXTRA_CRIME_ID);
        super.onCreate(savedInstanceState);
        FragmentManager fm = getSupportFragmentManager();

        Fragment sfragment = fm.findFragmentById(R.id.history_time_card_frag);
        if (sfragment == null) {
            sfragment = HistoryTimeCardFragment.newInstance(uuid);
            fm.beginTransaction()
                    .add(R.id.history_time_card_frag, sfragment)
                    .commit();
        }
    }

}

