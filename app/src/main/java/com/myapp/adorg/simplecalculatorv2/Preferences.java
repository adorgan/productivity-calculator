package com.myapp.adorg.simplecalculatorv2;

import android.content.Context;
import android.preference.PreferenceManager;

class Preferences {

    private static final String KEY_PROD = "prod_num";
    private static final String PREF_IS_24 = "switch_preference_1";
    private static final String KEY_DELETE = "deleteItem";
    private static final String CHANGE_DATE = "date_changed";
    private static final String CHANGE_LOG = "change_log";

    static int getPrefProd(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getInt(KEY_PROD, 90);
    }
    static void setPrefProd(Context context, int productivity) {
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
    static boolean get24Hr(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean(PREF_IS_24, false);
    }
    static boolean getPrefDelete(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean(KEY_DELETE, false);
    }
    static void setPrefDelete(Context context, boolean isDeleted) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putBoolean(KEY_DELETE, isDeleted)
                .apply();
    }
    static boolean getDateChange(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean(CHANGE_DATE, false);
    }
    static void setDateChange(Context context, boolean isChanged) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putBoolean(CHANGE_DATE, isChanged)
                .apply();
    }
    static boolean getChangeLogSeen(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean(CHANGE_LOG, false);
    }
    static void setChangeLogSeen(Context context, boolean isChanged) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putBoolean(CHANGE_LOG, isChanged)
                .apply();
    }
}
