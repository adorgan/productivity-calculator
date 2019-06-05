package com.myapp.adorg.simplecalculatorv2.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class TimeCardHelper extends SQLiteOpenHelper {

    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "timeCard.db";

    public TimeCardHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TimeCardDbSchema.TimeCardTable.NAME + "(" +
                " _id integer primary key autoincrement, " +
                TimeCardDbSchema.TimeCardTable.Cols.UUID + ", " +
                TimeCardDbSchema.TimeCardTable.Cols.DATE + ", " +
                TimeCardDbSchema.TimeCardTable.Cols.START_TIME + ", " +
                TimeCardDbSchema.TimeCardTable.Cols.END_TIME + ", " +
                TimeCardDbSchema.TimeCardTable.Cols.PRODUCTIVITY + ", " +
                TimeCardDbSchema.TimeCardTable.Cols.PAID_TIME + ", " +
                TimeCardDbSchema.TimeCardTable.Cols.UNPAID_BREAK + ", " +
                TimeCardDbSchema.TimeCardTable.Cols.TREATMENT_TIME + ", " +
                TimeCardDbSchema.TimeCardTable.Cols.END_HOUR + ", " +
                TimeCardDbSchema.TimeCardTable.Cols.END_MINUTE + ", " +
                TimeCardDbSchema.TimeCardTable.Cols.TIME_CARD_DATE + ", " +
                TimeCardDbSchema.TimeCardTable.Cols.PRODUCTIVITY_INT + ", " +
                TimeCardDbSchema.TimeCardTable.Cols.PAID_TIME_INT + ", " +
                TimeCardDbSchema.TimeCardTable.Cols.START_HOUR + ", " +
                TimeCardDbSchema.TimeCardTable.Cols.START_MINUTE + ", " +
                TimeCardDbSchema.TimeCardTable.Cols.IS_24_HOUR + ", " +
                TimeCardDbSchema.TimeCardTable.Cols.MEETING_TRAVEL +
                ")"
        );

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}


