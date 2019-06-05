package com.myapp.adorg.simplecalculatorv2.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.myapp.adorg.simplecalculatorv2.TimeCard;

import java.util.Date;
import java.util.UUID;

public class TimeCardCursorWrapper extends CursorWrapper {
    /**
     * Creates a cursor wrapper.
     *
     * @param cursor The underlying cursor to wrap.
     */
    public TimeCardCursorWrapper(Cursor cursor) {
        super(cursor);
    }
    public TimeCard getTimeCard() {

        String uuidString = getString(getColumnIndex(TimeCardDbSchema.TimeCardTable.Cols.UUID));
        String date = getString(getColumnIndex(TimeCardDbSchema.TimeCardTable.Cols.DATE));
        String startTime = getString(getColumnIndex(TimeCardDbSchema.TimeCardTable.Cols.START_TIME));
        String endTime = getString(getColumnIndex(TimeCardDbSchema.TimeCardTable.Cols.END_TIME));
        String productivity = getString(getColumnIndex(TimeCardDbSchema.TimeCardTable.Cols.PRODUCTIVITY));
        String paidTime = getString(getColumnIndex(TimeCardDbSchema.TimeCardTable.Cols.PAID_TIME));
        String unpaidTime = getString(getColumnIndex(TimeCardDbSchema.TimeCardTable.Cols.UNPAID_BREAK));
        String meetingTravel = getString(getColumnIndex(TimeCardDbSchema.TimeCardTable.Cols.MEETING_TRAVEL));
        String treatmentTime = getString(getColumnIndex(TimeCardDbSchema.TimeCardTable.Cols.TREATMENT_TIME));
        int endHour = getInt(getColumnIndex(TimeCardDbSchema.TimeCardTable.Cols.END_HOUR));
        int endMinute = getInt(getColumnIndex(TimeCardDbSchema.TimeCardTable.Cols.END_MINUTE));
        long timeCardDate = getLong(getColumnIndex(TimeCardDbSchema.TimeCardTable.Cols.TIME_CARD_DATE));
        int paidTimeInt = getInt(getColumnIndex(TimeCardDbSchema.TimeCardTable.Cols.PAID_TIME_INT));
        double productivityInt = getDouble(getColumnIndex(TimeCardDbSchema.TimeCardTable.Cols.PRODUCTIVITY_INT));
        int startHour = getInt(getColumnIndex(TimeCardDbSchema.TimeCardTable.Cols.START_HOUR));
        int startMinute = getInt(getColumnIndex(TimeCardDbSchema.TimeCardTable.Cols.START_MINUTE));
        int is24Hour = getInt(getColumnIndex(TimeCardDbSchema.TimeCardTable.Cols.IS_24_HOUR));


        TimeCard timeCard = new TimeCard(UUID.fromString(uuidString));
        timeCard.setmTreatmentTime(treatmentTime);
        timeCard.setmEndHour(endHour);
        timeCard.setEndMinute(endMinute);
        timeCard.setDate(date);
        timeCard.setStartTime(startTime);
        timeCard.setEndTime(endTime);
        timeCard.setProductivity(productivity);
        timeCard.setPaidTime(paidTime);
        timeCard.setUnpaidTime(unpaidTime);
        timeCard.setTravelTime(meetingTravel);
        timeCard.setMcardDate(new Date(timeCardDate));
        timeCard.setmPaidTimeInt(paidTimeInt);
        timeCard.setmProductivityDouble(productivityInt);
        timeCard.setStartHour(startHour);
        timeCard.setStartMinute(startMinute);
        timeCard.setIs24Hour(is24Hour);
        return timeCard;

    }
}

