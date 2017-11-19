package com.techbridge.smsreader.utils;

public class DateTimeSorter {
    public String mDateTime;
    public int mIndex;

    public DateTimeSorter(int index, String DateTime) {
        this.mIndex = index;
        this.mDateTime = DateTime;
    }

    public int getIndex() {
        return this.mIndex;
    }

    public void setIndex(int index) {
        this.mIndex = index;
    }

    public String getDateTime() {
        return this.mDateTime;
    }

    public void setDateTime(String dateTime) {
        this.mDateTime = dateTime;
    }
}
