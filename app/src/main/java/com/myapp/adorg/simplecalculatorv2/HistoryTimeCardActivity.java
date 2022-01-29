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

    private static final String EXTRA_TIME_CARD_ID = "com.myapp.adorg.simplecalculatorv2.timecard.id";

    public static Intent newIntent(Context packageContext, UUID crimeId) {
        Intent intent = new Intent(packageContext, HistoryTimeCardActivity.class);
        intent.putExtra(EXTRA_TIME_CARD_ID, crimeId);
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

        UUID uuid = (UUID) getIntent()
                .getSerializableExtra(EXTRA_TIME_CARD_ID);
        super.onCreate(savedInstanceState);
        FragmentManager fm = getSupportFragmentManager();

        Fragment fragment = fm.findFragmentById(R.id.history_time_card_frag);
        if (fragment == null) {
            fragment = HistoryTimeCardFragment.newInstance(uuid);
            fm.beginTransaction()
                    .add(R.id.history_time_card_frag, fragment)
                    .commit();
        }
    }
}

