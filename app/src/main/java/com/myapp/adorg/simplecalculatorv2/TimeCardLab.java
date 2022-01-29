package com.myapp.adorg.simplecalculatorv2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


import com.myapp.adorg.simplecalculatorv2.database.TimeCardCursorWrapper;
import com.myapp.adorg.simplecalculatorv2.database.TimeCardDbSchema;
import com.myapp.adorg.simplecalculatorv2.database.TimeCardHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TimeCardLab {
    private static TimeCardLab sTimeCardLab;
    private final SQLiteDatabase mDatabase;

    public static TimeCardLab get(Context context){
        if(sTimeCardLab == null){
            sTimeCardLab = new TimeCardLab(context);
        }return sTimeCardLab;
    }

    private TimeCardLab(Context context){
        Context mContext = context.getApplicationContext();
        mDatabase = new TimeCardHelper(mContext).getWritableDatabase();
    }

    private static ContentValues getContentValues(TimeCard timeCard) {
        ContentValues values = new ContentValues();

        values.put(TimeCardDbSchema.TimeCardTable.Cols.UUID, timeCard.getId().toString());
        values.put(TimeCardDbSchema.TimeCardTable.Cols.DATE, timeCard.getDate());
        values.put(TimeCardDbSchema.TimeCardTable.Cols.START_TIME, timeCard.getStartTime());
        values.put(TimeCardDbSchema.TimeCardTable.Cols.END_TIME, timeCard.getEndTime());
        values.put(TimeCardDbSchema.TimeCardTable.Cols.PRODUCTIVITY, timeCard.getProductivityString());
        values.put(TimeCardDbSchema.TimeCardTable.Cols.PAID_TIME, timeCard.getPaidTime());
        values.put(TimeCardDbSchema.TimeCardTable.Cols.UNPAID_BREAK, timeCard.getUnpaidTime());
        values.put(TimeCardDbSchema.TimeCardTable.Cols.MEETING_TRAVEL, timeCard.getTravelTime());
        values.put(TimeCardDbSchema.TimeCardTable.Cols.TREATMENT_TIME, timeCard.getTreatmentTimeString());
        values.put(TimeCardDbSchema.TimeCardTable.Cols.END_HOUR, timeCard.getEndHourInt());
        values.put(TimeCardDbSchema.TimeCardTable.Cols.END_MINUTE, timeCard.getEndMinuteInt());
        values.put(TimeCardDbSchema.TimeCardTable.Cols.TIME_CARD_DATE, timeCard.getTimeCardDate().getTime());
        values.put(TimeCardDbSchema.TimeCardTable.Cols.PRODUCTIVITY_INT, timeCard.getProductivityDouble());
        values.put(TimeCardDbSchema.TimeCardTable.Cols.PAID_TIME_INT, timeCard.getPaidTimeInt());
        values.put(TimeCardDbSchema.TimeCardTable.Cols.START_HOUR, timeCard.getStartHour());
        values.put(TimeCardDbSchema.TimeCardTable.Cols.START_MINUTE, timeCard.getStartMinute());
        values.put(TimeCardDbSchema.TimeCardTable.Cols.IS_24_HOUR, timeCard.getIs24Hour());
        return values;
    }

    public void addTimeCard(TimeCard timeCard){
        ContentValues values = getContentValues(timeCard);
        mDatabase.insert(TimeCardDbSchema.TimeCardTable.NAME, null, values);
    }
    private TimeCardCursorWrapper queryCrimes(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                TimeCardDbSchema.TimeCardTable.NAME,
                null, // columns - null selects all columns
                whereClause,
                whereArgs,
                null, // groupBy
                null, // having
                TimeCardDbSchema.TimeCardTable.Cols.TIME_CARD_DATE+" ASC" // orderBy
        );
        return new TimeCardCursorWrapper(cursor);

    }

     List<TimeCard> getTimeCards() {
        List<TimeCard> timeCards = new ArrayList<>();

         try (TimeCardCursorWrapper cursor = queryCrimes(null, null)) {
             cursor.moveToFirst();
             while (!cursor.isAfterLast()) {
                 timeCards.add(cursor.getTimeCard());
                 cursor.moveToNext();
             }
         }
        return timeCards;
    }
    public TimeCard getTimeCard(UUID uuid) {

        try (TimeCardCursorWrapper cursor = queryCrimes(TimeCardDbSchema.TimeCardTable.Cols.UUID + " = ?",
                new String[]{uuid.toString()}
        )) {
            if (cursor.getCount() == 0) {
                return null;
            }
            cursor.moveToFirst();
            return cursor.getTimeCard();
        }
    }

     void deleteAll() {
        mDatabase.execSQL("delete from "+ TimeCardDbSchema.TimeCardTable.NAME);
    }

    void searchAndDelete(UUID uuid){
        mDatabase.execSQL("DELETE FROM "
                + TimeCardDbSchema.TimeCardTable.NAME
                + " WHERE "
                + TimeCardDbSchema.TimeCardTable.Cols.UUID
                + "= '"
                + uuid
                + "'");
    }


    public void updateTimeCardDB(TimeCard timeCard) {
        String uuidString = timeCard.getId().toString();
        ContentValues values = getContentValues(timeCard);
        mDatabase.update(TimeCardDbSchema.TimeCardTable.NAME, values,
                TimeCardDbSchema.TimeCardTable.Cols.UUID + " = ?",
                new String[] { uuidString });
    }
}
