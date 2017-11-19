package com.techbridge.smsreader.utils;

public class Reminder {
    private String mActive;
    private String mDate;
    private int mID;
    private String mRepeat;
    private String mRepeatNo;
    private String mRepeatType;
    private String mTime;
    private String mTitle;

    public Reminder(){}

    public Reminder(int ID, String Title, String Date, String Time, String Repeat, String RepeatNo, String RepeatType, String Active) {
        this.mID = ID;
        this.mTitle = Title;
        this.mDate = Date;
        this.mTime = Time;
        this.mRepeat = Repeat;
        this.mRepeatNo = RepeatNo;
        this.mRepeatType = RepeatType;
        this.mActive = Active;
    }

    public Reminder(String Title, String Date, String Time, String Repeat, String RepeatNo, String RepeatType, String Active) {
        this.mTitle = Title;
        this.mDate = Date;
        this.mTime = Time;
        this.mRepeat = Repeat;
        this.mRepeatNo = RepeatNo;
        this.mRepeatType = RepeatType;
        this.mActive = Active;
    }

    public int getID() {
        return this.mID;
    }

    public void setID(int ID) {
        this.mID = ID;
    }

    public String getTitle() {
        return this.mTitle;
    }

    public void setTitle(String title) {
        this.mTitle = title;
    }

    public String getDate() {
        return this.mDate;
    }

    public void setDate(String date) {
        this.mDate = date;
    }

    public String getTime() {
        return this.mTime;
    }

    public void setTime(String time) {
        this.mTime = time;
    }

    public String getRepeatType() {
        return this.mRepeatType;
    }

    public void setRepeatType(String repeatType) {
        this.mRepeatType = repeatType;
    }

    public String getRepeatNo() {
        return this.mRepeatNo;
    }

    public void setRepeatNo(String repeatNo) {
        this.mRepeatNo = repeatNo;
    }

    public String getRepeat() {
        return this.mRepeat;
    }

    public void setRepeat(String repeat) {
        this.mRepeat = repeat;
    }

    public String getActive() {
        return this.mActive;
    }

    public void setActive(String active) {
        this.mActive = active;
    }
}
