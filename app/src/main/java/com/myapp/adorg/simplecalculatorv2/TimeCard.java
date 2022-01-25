package com.myapp.adorg.simplecalculatorv2;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

public class TimeCard implements Serializable {
    private UUID mId;
    private Date mTimeCardDate;
    private String mDateString;
    private String mStartTimeString;
    private String mEndTimeString;
    private String mProductivityString;
    private String mTreatmentTimeString;
    private String mPaidTimeString;
    private String mUnpaidTimeString;
    private String mTravelTimeString;
    private int mEndHour;
    private int mEndMinute;
    private Integer mPaidTimeInt;
    private double mProductivityDouble;
    private int mStartHour;
    private int mStartMinute;
    private int mIs24HourMode;

    public TimeCard(UUID id) {
        mId = id;
    }

    public int getIs24Hour() {
        return mIs24HourMode;
    }

    public void setIs24Hour(int mIs24Hour) {
        this.mIs24HourMode = mIs24Hour;
    }

    public int getStartHour() {
        return mStartHour;
    }

    public void setStartHour(int mStartHour) {
        this.mStartHour = mStartHour;
    }

    public int getStartMinute() {
        return mStartMinute;
    }

    public void setStartMinute(int mStartMinute) {
        this.mStartMinute = mStartMinute;
    }

    public Integer getPaidTimeInt() {
        return mPaidTimeInt;
    }

    public void setPaidTimeInt(Integer mPaidTimeInt) {
        this.mPaidTimeInt = mPaidTimeInt;
    }

    public double getProductivityDouble() {
        return mProductivityDouble;
    }

    public void setProductivityDouble(double val) { this.mProductivityDouble = val; }

    public Date getTimeCardDate() {
        return mTimeCardDate;
    }

    public void setTimeCardDate(Date date) {
        this.mTimeCardDate = date;
    }

    public String getTreatmentTimeString() {return mTreatmentTimeString; }

    public void setTreatmentTimeString(String strTx) { this.mTreatmentTimeString = strTx; }

    public int getEndHourInt() {
        return mEndHour;
    }

    public void setEndHourInt(int mEndHour) {
        this.mEndHour = mEndHour;
    }

    public int getEndMinuteInt() {
        return mEndMinute;
    }

    public void setEndMinuteInt(int mEndMinute) {
        this.mEndMinute = mEndMinute;
    }

    public String getProductivityString() {
        return mProductivityString;
    }

    public void setProductivityString(String productivity) {
        mProductivityString = productivity;
    }

    public UUID getId() {
        return mId;
    }

    public void setId(UUID id) {
        mId = id;
    }

    public String getStartTime() {
        return mStartTimeString;
    }

    public void setStartTime(String startTime) {
        mStartTimeString = startTime;
    }

    public String getEndTime() {
        return mEndTimeString;
    }

    public void setEndTime(String endTime) {
        mEndTimeString = endTime;
    }

    public String getPaidTime() {
        return mPaidTimeString;
    }

    public void setPaidTime(String paidTime) {
        mPaidTimeString = paidTime;
    }

    public String getUnpaidTime() {
        return mUnpaidTimeString;
    }

    public void setUnpaidTime(String unpaidTime) {
        mUnpaidTimeString = unpaidTime;
    }

    public String getDate() {
        return mDateString;
    }

    public void setDate(String date) {
        mDateString = date;
    }

    public String getTravelTime() {
        return mTravelTimeString;
    }

    public void setTravelTime(String travelTime) {
        mTravelTimeString = travelTime;
    }
}



