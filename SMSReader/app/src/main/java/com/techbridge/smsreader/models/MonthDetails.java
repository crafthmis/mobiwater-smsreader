package com.techbridge.smsreader.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class MonthDetails implements Parcelable {
    public static final Creator<MonthDetails> CREATOR = new C02851();
    private String dayReading;
    private String dayString;
    private String timeString;

    static class C02851 implements Creator<MonthDetails> {
        C02851() {
        }

        public MonthDetails createFromParcel(Parcel in) {
            return new MonthDetails(in);
        }

        public MonthDetails[] newArray(int size) {
            return new MonthDetails[size];
        }
    }

    public MonthDetails(String dayString, String timeString, String dayReading) {
        this.dayString = dayString;
        this.timeString = timeString;
        this.dayReading = dayReading;
    }

    protected MonthDetails(Parcel in) {
        this.dayString = in.readString();
        this.timeString = in.readString();
        this.dayReading = in.readString();
    }

    public String getDayString() {
        return this.dayString;
    }

    public String getTimeString() {
        return this.timeString;
    }

    public String getDayreading() {
        return this.dayReading;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.dayString);
        dest.writeString(this.timeString);
        dest.writeString(this.dayReading);
    }
}
