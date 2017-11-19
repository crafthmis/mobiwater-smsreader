package com.techbridge.smsreader.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.techbridge.smsreader.db.DBHelper;
import java.util.Calendar;

public class BootReceiver extends BroadcastReceiver {
    private static final long milDay = 86400000;
    private static final long milHour = 3600000;
    private static final long milMinute = 60000;
    private static final long milMonth = 2592000000L;
    private static final long milWeek = 604800000;
    private String mActive;
    private AlarmReceiver mAlarmReceiver;
    private Calendar mCalendar;
    private String mDate;
    private String[] mDateSplit;
    private int mDay;
    private int mHour;
    private int mMinute;
    private int mMonth;
    private int mReceivedID;
    private String mRepeat;
    private String mRepeatNo;
    private long mRepeatTime;
    private String mRepeatType;
    private String mTime;
    private String[] mTimeSplit;
    private String mTitle;
    private int mYear;

    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            DBHelper rb = new DBHelper(context);
            this.mCalendar = Calendar.getInstance();
            this.mAlarmReceiver = new AlarmReceiver();
            for (Reminder rm : rb.getAllReminders()) {
                this.mReceivedID = rm.getID();
                this.mRepeat = rm.getRepeat();
                this.mRepeatNo = rm.getRepeatNo();
                this.mRepeatType = rm.getRepeatType();
                this.mActive = rm.getActive();
                this.mDate = rm.getDate();
                this.mTime = rm.getTime();
                this.mDateSplit = this.mDate.split("/");
                this.mTimeSplit = this.mTime.split(":");
                this.mDay = Integer.parseInt(this.mDateSplit[0]);
                this.mMonth = Integer.parseInt(this.mDateSplit[1]);
                this.mYear = Integer.parseInt(this.mDateSplit[2]);
                this.mHour = Integer.parseInt(this.mTimeSplit[0]);
                this.mMinute = Integer.parseInt(this.mTimeSplit[1]);
                Calendar calendar = this.mCalendar;
                int i = this.mMonth - 1;
                this.mMonth = i;
                calendar.set(Calendar.MONTH, i);
                this.mCalendar.set(Calendar.YEAR, this.mYear);
                this.mCalendar.set(Calendar.DAY_OF_MONTH, this.mDay);
                this.mCalendar.set(Calendar.HOUR, this.mHour);
                this.mCalendar.set(Calendar.MINUTE, this.mMinute);
                this.mCalendar.set(Calendar.SECOND, 0);
                if (this.mRepeatType.equals("Minute")) {
                    this.mRepeatTime = ((long) Integer.parseInt(this.mRepeatNo)) * 60000;
                } else if (this.mRepeatType.equals("Hour")) {
                    this.mRepeatTime = ((long) Integer.parseInt(this.mRepeatNo)) * 3600000;
                } else if (this.mRepeatType.equals("Day")) {
                    this.mRepeatTime = ((long) Integer.parseInt(this.mRepeatNo)) * 86400000;
                } else if (this.mRepeatType.equals("Week")) {
                    this.mRepeatTime = ((long) Integer.parseInt(this.mRepeatNo)) * milWeek;
                } else if (this.mRepeatType.equals("Month")) {
                    this.mRepeatTime = ((long) Integer.parseInt(this.mRepeatNo)) * milMonth;
                }
                if (this.mActive.equals("true")) {
                    if (this.mRepeat.equals("true")) {
                        this.mAlarmReceiver.setRepeatAlarm(context, this.mCalendar, this.mReceivedID, this.mRepeatTime);
                    } else if (this.mRepeat.equals("false")) {
                        this.mAlarmReceiver.setAlarm(context, this.mCalendar, this.mReceivedID);
                    }
                }
            }
        }
    }
}
