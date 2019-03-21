package com.myapp.adorg.simplecalculatorv2;

import java.io.Serializable;
import java.util.UUID;

public class TimeCard implements Serializable {
    private UUID mId;
    private String mStartTime;
    private String mEndTime;
    private String mPaidTime;
    private String mUnpaidTime;
    private String mDate;
    private String mTravelTime;
    private String mProductivity;
    private int mEndHour;
    private int mEndMinute;

    public int getmEndHour() {
        return mEndHour;
    }

    public void setmEndHour(int mEndHour) {
        this.mEndHour = mEndHour;
    }

    public int getmEndMinute() {
        return mEndMinute;
    }

    public void setmEndMinute(int mEndMinute) {
        this.mEndMinute = mEndMinute;
    }

    public String getProductivity() {
        return mProductivity;
    }

    public void setProductivity(String productivity) {
        mProductivity = productivity;
    }

    public UUID getId() {
        return mId;
    }

    public void setId(UUID id) {
        mId = id;
    }

    public String getStartTime() {
        return mStartTime;
    }

    public void setStartTime(String startTime) {
        mStartTime = startTime;
    }

    public String getEndTime() {
        return mEndTime;
    }

    public void setEndTime(String endTime) {
        mEndTime = endTime;
    }

    public String getPaidTime() {
        return mPaidTime;
    }

    public void setPaidTime(String paidTime) {
        mPaidTime = paidTime;
    }

    public String getUnpaidTime() {
        return mUnpaidTime;
    }

    public void setUnpaidTime(String unpaidTime) {
        mUnpaidTime = unpaidTime;
    }

    public String getDate() {
        return mDate;
    }

    public void setDate(String date) {
        mDate = date;
    }

    public String getTravelTime() {
        return mTravelTime;
    }

    public void setTravelTime(String breakTime) {
        mTravelTime = breakTime;
    }
    public TimeCard(){
        mId = UUID.randomUUID();
    }
}



