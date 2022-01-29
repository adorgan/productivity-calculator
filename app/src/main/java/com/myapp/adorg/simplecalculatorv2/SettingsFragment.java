package com.myapp.adorg.simplecalculatorv2;

import android.os.Bundle;
import android.os.Handler;

import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreference;


public class SettingsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {

        setPreferencesFromResource(R.xml.pref_generals, rootKey);
        SwitchPreference darkPreference = findPreference("switch_preference_2");
        assert darkPreference != null;
        darkPreference.setOnPreferenceChangeListener((preference, newValue) -> {
            Handler handler = new Handler();
            handler.postDelayed(() -> requireActivity().recreate(), 300);
            return true;
        });
    }
}
