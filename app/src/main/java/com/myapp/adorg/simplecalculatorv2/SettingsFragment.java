package com.myapp.adorg.simplecalculatorv2;

import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreference;


public class SettingsFragment extends PreferenceFragmentCompat {

    private SwitchPreference darkPreference;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {



        setPreferencesFromResource(R.xml.pref_generals, rootKey);
        darkPreference = findPreference("switch_preference_2");


        darkPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getActivity().recreate();
                    }
                }, 300);


                return true;
            }
        });
    }
}
