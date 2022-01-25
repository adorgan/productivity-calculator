package com.myapp.adorg.simplecalculatorv2;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Preferences.getDarkMode(getApplicationContext())) {
            setTheme(R.style.ActionBarAppThemeDark);
        } else {
            setTheme(R.style.ActionBarAppTheme);
        }
        setContentView(R.layout.settings_activity_layout);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings_container, new SettingsFragment(), "settings")
                .commit();
    }
}
