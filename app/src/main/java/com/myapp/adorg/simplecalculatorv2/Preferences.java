package com.myapp.adorg.simplecalculatorv2;

import android.content.Context;
import android.preference.PreferenceManager;

public class Preferences {

    private static final String KEY_PROD = "prod_num";
    private static final String PREF_IS_24 = "switch_preference_1";

    public static int getPrefProd(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getInt(KEY_PROD, 90);
    }
    public static void setPrefProd(Context context, int productivity) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putInt(KEY_PROD, productivity)
                .apply();
    }
    public static void set24Hr(Context context, boolean is24Hr) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putBoolean(PREF_IS_24, is24Hr)
                .apply();
    }
    public static boolean get24Hr(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean(PREF_IS_24, false);
    }
}
