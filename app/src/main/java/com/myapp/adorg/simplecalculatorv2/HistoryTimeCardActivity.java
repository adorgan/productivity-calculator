package com.myapp.adorg.simplecalculatorv2;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;


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

