package com.myapp.adorg.simplecalculatorv2;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

public class TimeCard implements Serializable {
    private UUID mId;
    private String mStartTime;
    private String mEndTime;
    private String mPaidTime;
    private String mUnpaidTime;
    private String mDate;
    private Date mcardDate;
    private String mTravelTime;
    private String mProductivity;
    private int mEndHour;
    private int mEndMinute;
    private String mTreatmentTime;
    private int mPosition;
    private Integer mPaidTimeInt;
    private double mProductivityDouble;
    private int mStartHour;
    private int mStartMinute;
    private int mIs24Hour;

    public int getIs24Hour() {
        return mIs24Hour;
    }

    public void setIs24Hour(int mIs24Hour) {
        this.mIs24Hour = mIs24Hour;
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

    public Integer getmPaidTimeInt() {
        return mPaidTimeInt;
    }

    public void setmPaidTimeInt(Integer mPaidTimeInt) {
        this.mPaidTimeInt = mPaidTimeInt;
    }

    public double getmProductivityDouble() {
        return mProductivityDouble;
    }

    public void setmProductivityDouble(double mProductivityDouble) {
        this.mProductivityDouble = mProductivityDouble;
    }

    public Date getMcardDate() {
        return mcardDate;
    }

    public void setMcardDate(Date mcardDate) {
        this.mcardDate = mcardDate;
    }

    //public int getmPosition() {
       // return mPosition;
   // }

    //public void setmPosition(int mPosition) {
      //  this.mPosition = mPosition;
    //}

    public String getmTreatmentTime() {return mTreatmentTime;
    }

    public void setmTreatmentTime(String mTreatmentTime) {
        this.mTreatmentTime = mTreatmentTime;
    }

    public int getmEndHour() {
        return mEndHour;
    }

    public void setmEndHour(int mEndHour) {
        this.mEndHour = mEndHour;
    }

    public int getmEndMinute() {
        return mEndMinute;
    }

    public void setEndMinute(int mEndMinute) {
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

     String getStartTime() {
        return mStartTime;
    }

    public void setStartTime(String startTime) {
        mStartTime = startTime;
    }

     String getEndTime() {
        return mEndTime;
    }

    public void setEndTime(String endTime) {
        mEndTime = endTime;
    }

     String getPaidTime() {
        return mPaidTime;
    }

    public void setPaidTime(String paidTime) {
        mPaidTime = paidTime;
    }

     String getUnpaidTime() {
        return mUnpaidTime;
    }

    public void setUnpaidTime(String unpaidTime) {
        mUnpaidTime = unpaidTime;
    }

     String getDate() {
        return mDate;
    }

    public void setDate(String date) {
        mDate = date;
    }

     String getTravelTime() {
        return mTravelTime;
    }

    public void setTravelTime(String breakTime) {
        mTravelTime = breakTime;
    }

    public TimeCard() {
        this(UUID.randomUUID());

    }
    public TimeCard(UUID id) {
        mId = id;

    }

}



