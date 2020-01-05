package com.myapp.adorg.simplecalculatorv2;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Configuration;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatDelegate;


public class MainActivity extends AppCompatActivity{


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
/*
        int nightModeFlags =
                getApplicationContext().getResources().getConfiguration().uiMode &
                        Configuration.UI_MODE_NIGHT_MASK;
        switch (nightModeFlags) {
            case Configuration.UI_MODE_NIGHT_YES:
                setTheme(R.style.DarkTheme);
                break;

            case Configuration.UI_MODE_NIGHT_NO:
                setTheme(R.style.LightTheme);
                break;

            case Configuration.UI_MODE_NIGHT_UNDEFINED:
                setTheme(R.style.LightTheme);
                break;
        }
        */
        if (Preferences.getDarkMode(getApplicationContext())) {
            setTheme(R.style.DarkTheme);
        } else {
            setTheme(R.style.LightTheme);
        }

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

}