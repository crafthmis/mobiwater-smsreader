package com.techbridge.smsreader.views.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import at.markushi.ui.CircleButton;
import com.techbridge.smsreader.R;
import com.techbridge.smsreader.utils.Prefs;
import com.techbridge.smsreader.utils.wheelpicker.ArrayWheelAdapter;
import com.techbridge.smsreader.utils.wheelpicker.OnWheelChangedListener;
import com.techbridge.smsreader.utils.wheelpicker.OnWheelScrollListener;
import com.techbridge.smsreader.utils.wheelpicker.WheelView;
import java.util.ArrayList;
import java.util.Calendar;
import org.apache.commons.lang3.StringUtils;

public class SynctimeActivity extends BaseActivity implements OnClickListener {
    private CircleButton btnSubmit;
    private final OnWheelChangedListener changedListener = new C05193();
    private Context ctx = this;
    private Calendar gCalendar;
    private Toolbar mToolbar;
    OnWheelScrollListener scrolledListener = new C05182();
    private SmsManager smsManager;
    private EditText text1;
    private EditText text2;
    private EditText text3;
    private EditText text4;
    private EditText text5;
    private EditText text6;
    String[] wheelMenu2 = new String[]{"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"};
    String[] wheelMenu3 = new String[]{"MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY", "SUNDAY"};
    private boolean wheelScrolled = false;

    class C03221 implements OnClickListener {
        C03221() {
        }

        public void onClick(View view) {
            SynctimeActivity.this.startActivity(new Intent(SynctimeActivity.this.ctx, CtrldashboardActivity.class));
        }
    }

    class C05182 implements OnWheelScrollListener {
        C05182() {
        }

        public void onScrollStarts(WheelView wheel) {
            SynctimeActivity.this.wheelScrolled = true;
        }

        public void onScrollEnds(WheelView wheel) {
            SynctimeActivity.this.wheelScrolled = false;
            SynctimeActivity.this.updateStatus();
        }

        public void onScrollingStarted(WheelView wheel) {
        }

        public void onScrollingFinished(final WheelView wheel) {
            new Handler().post(new Runnable() {
                public void run() {
                    if (wheel.getId() == R.id.p1 || wheel.getId() == R.id.p2) {
                        SynctimeActivity.this.initWheel3(R.id.p3);
                    }
                    SystemClock.sleep(100);
                    SynctimeActivity.this.setWheelWeekDay(R.id.p7);
                }
            });
        }
    }

    class C05193 implements OnWheelChangedListener {
        C05193() {
        }

        public void onChanged(WheelView wheel, int oldValue, int newValue) {
            if (!SynctimeActivity.this.wheelScrolled) {
                SynctimeActivity.this.updateStatus();
            }
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_sync_time);
        this.gCalendar = Calendar.getInstance();
        initWheel1(R.id.p1);
        initWheel2(R.id.p2);
        initWheel3(R.id.p3);
        initWheel4(R.id.p4);
        initWheel5(R.id.p5);
        initWheel6(R.id.p6);
        initWheel7(R.id.p7);
        initWidgets();
    }

    private void initWidgets() {
        this.mToolbar = (Toolbar) findViewById(R.id.toolbar);
        this.mToolbar.setTitle((CharSequence) "Controller Setting");
        setSupportActionBar(this.mToolbar);
        getSupportActionBar().setHomeAsUpIndicator(getResources().getDrawable(R.drawable.ic_action_back));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.mToolbar.setNavigationOnClickListener(new C03221());
        this.text1 = (EditText) findViewById(R.id.r1);
        this.text2 = (EditText) findViewById(R.id.r2);
        this.text3 = (EditText) findViewById(R.id.r3);
        this.text4 = (EditText) findViewById(R.id.r4);
        this.text5 = (EditText) findViewById(R.id.r5);
        this.text6 = (EditText) findViewById(R.id.r6);
        this.btnSubmit = (CircleButton) findViewById(R.id.circleButton);
        this.btnSubmit.setOnClickListener(this);
        this.smsManager = SmsManager.getDefault();
        updateStatus();
    }

    private void updateStatus() {
        int currYr = getWheel(R.id.p1).getCurrentItem();
        int currMnth = getWheel(R.id.p2).getCurrentItem();
        String[] daysArray = (String[]) getDays(currYr, currMnth).toArray(new String[getDays(currYr, currMnth).size()]);
        String[] hoursArray = (String[]) getHours().toArray(new String[getHours().size()]);
        String[] minutesArray = (String[]) getMinutes().toArray(new String[getMinutes().size()]);
        this.text1.setText(((String[]) getYears().toArray(new String[getYears().size()]))[getWheel(R.id.p1).getCurrentItem()]);
        this.text2.setText(this.wheelMenu2[getWheel(R.id.p2).getCurrentItem()]);
        this.text3.setText(daysArray[getWheel(R.id.p3).getCurrentItem()]);
        this.text4.setText(hoursArray[getWheel(R.id.p4).getCurrentItem()]);
        this.text5.setText(minutesArray[getWheel(R.id.p5).getCurrentItem()]);
        this.text6.setText(minutesArray[getWheel(R.id.p6).getCurrentItem()]);
    }

    private void initWheel1(int id) {
        WheelView wheel = (WheelView) findViewById(id);
        wheel.setViewAdapter(new ArrayWheelAdapter(this.ctx, getYears().toArray(new String[getYears().size()])));
        wheel.setVisibleItems(2);
        wheel.setCurrentItem(this.gCalendar.get(Calendar.YEAR));
        wheel.addChangingListener(this.changedListener);
        wheel.addScrollingListener(this.scrolledListener);
        wheel.setCyclic(true);
    }

    private void initWheel2(int id) {
        WheelView wheel = (WheelView) findViewById(id);
        wheel.setViewAdapter(new ArrayWheelAdapter(this.ctx, getMonths().toArray(new String[getMonths().size()])));
        wheel.setVisibleItems(2);
        wheel.setCurrentItem(this.gCalendar.get(Calendar.MONTH));
        wheel.addChangingListener(this.changedListener);
        wheel.addScrollingListener(this.scrolledListener);
        wheel.setCyclic(true);
    }

    private void initWheel3(int id) {
        int currYr = getWheel(R.id.p1).getCurrentItem();
        int currMnth = getWheel(R.id.p2).getCurrentItem();
        WheelView wheel = (WheelView) findViewById(id);
        wheel.setViewAdapter(new ArrayWheelAdapter(this.ctx, getDays(currYr, currMnth).toArray(new String[getDays(currYr, currMnth).size()])));
        wheel.setVisibleItems(2);
        wheel.setCurrentItem(this.gCalendar.get(Calendar.DAY_OF_MONTH) - 1);
        wheel.addChangingListener(this.changedListener);
        wheel.addScrollingListener(this.scrolledListener);
        wheel.setCyclic(true);
    }

    private void initWheel4(int id) {
        WheelView wheel = (WheelView) findViewById(id);
        wheel.setViewAdapter(new ArrayWheelAdapter(this.ctx, getHours().toArray(new String[getHours().size()])));
        wheel.setVisibleItems(2);
        wheel.setCurrentItem(this.gCalendar.get(Calendar.HOUR));
        wheel.addChangingListener(this.changedListener);
        wheel.setCyclic(true);
    }

    private void initWheel5(int id) {
        WheelView wheel = (WheelView) findViewById(id);
        wheel.setViewAdapter(new ArrayWheelAdapter(this.ctx, getMinutes().toArray(new String[getMinutes().size()])));
        wheel.setVisibleItems(2);
        wheel.setCurrentItem(this.gCalendar.get(Calendar.MINUTE));
        wheel.addChangingListener(this.changedListener);
        wheel.setCyclic(true);
    }

    private void initWheel6(int id) {
        WheelView wheel = (WheelView) findViewById(id);
        wheel.setViewAdapter(new ArrayWheelAdapter(this.ctx, getMinutes().toArray(new String[getMinutes().size()])));
        wheel.setVisibleItems(2);
        wheel.setCurrentItem(this.gCalendar.get(Calendar.SECOND));
        wheel.addChangingListener(this.changedListener);
        wheel.setCyclic(true);
    }

    private void initWheel7(int id) {
        WheelView wheel = (WheelView) findViewById(id);
        wheel.setViewAdapter(new ArrayWheelAdapter(this.ctx, this.wheelMenu3));
        wheel.setVisibleItems(2);
        wheel.setCurrentItem(0);
        wheel.addChangingListener(this.changedListener);
        wheel.setCyclic(true);
    }

    private void setWheelWeekDay(int id) {
        int wkDayIndex = getWeekDay(getWheel(R.id.p3).getCurrentItem());
        WheelView wheel = (WheelView) findViewById(id);
        wheel.setViewAdapter(new ArrayWheelAdapter(this.ctx, this.wheelMenu3));
        wheel.setVisibleItems(2);
        wheel.setCurrentItem(wkDayIndex);
        wheel.setCyclic(true);
    }

    private WheelView getWheel(int id) {
        return (WheelView) findViewById(id);
    }

    private int getWheelValue(int id) {
        return getWheel(id).getCurrentItem();
    }

    private ArrayList<String> getYears() {
        ArrayList<String> yrs = new ArrayList();
        for (int j = 2017; j <= 2060; j++) {
            yrs.add(j+"");
        }
        return yrs;
    }

    private ArrayList<String> getMinutes() {
        ArrayList<String> mins = new ArrayList();
        for (int j = 0; j < 60; j++) {
            mins.add(StringUtils.leftPad(j+"",2,"0"));
        }
        return mins;
    }

    private ArrayList<String> getMonths() {
        ArrayList<String> mnths = new ArrayList();
        for (int j = 1; j < 13; j++) {
            mnths.add(StringUtils.leftPad(j + "", 2, "0"));
        }
        return mnths;
    }

    private ArrayList<String> getHours() {
        ArrayList<String> hrs = new ArrayList();
        for (int j = 0; j < 24; j++) {
            hrs.add(StringUtils.leftPad(j + "", 2, "0"));
        }
        return hrs;
    }

    private ArrayList<String> getDays(int yrIndex, int mthIndex) {
        String[] yrsArray = (String[]) getYears().toArray(new String[getYears().size()]);
        String[] monthsArray = (String[]) getMonths().toArray(new String[getMonths().size()]);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, Integer.parseInt(yrsArray[yrIndex]));
        calendar.set(Calendar.MONTH, Integer.parseInt(monthsArray[mthIndex]) - 1);
        int numDays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        ArrayList<String> days = new ArrayList();
        for (int j = 1; j <= numDays; j++) {
            days.add(StringUtils.leftPad(j + "", 2, "0"));
        }
        return days;
    }

    private int getWeekDay(int dayIndex) {
        int currYr = getWheel(R.id.p1).getCurrentItem();
        int currMnth = getWheel(R.id.p2).getCurrentItem();
        String[] yrsArray = (String[]) getYears().toArray(new String[getYears().size()]);
        String[] monthsArray = (String[]) getMonths().toArray(new String[getMonths().size()]);
        String[] daysArray = (String[]) getDays(currYr, currMnth).toArray(new String[getDays(currYr, currMnth).size()]);
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.SUNDAY);
        calendar.set(Calendar.YEAR, Integer.parseInt(yrsArray[currYr]));
        calendar.set(Calendar.MONTH, currMnth);
        calendar.set(Calendar.DAY_OF_MONTH,Integer.parseInt(daysArray[dayIndex]));
        return calendar.get(Calendar.DAY_OF_WEEK);
    }

    public void onClick(View v) {
        sendSMS(Prefs.readString(this.ctx, "settingPhone", ""), ((((("1234D" + this.text1.getText()) + "-" + this.text2.getText().toString()) + "-" + this.text3.getText().toString()) + "T" + this.text4.getText().toString()) + ":" + this.text5.getText().toString()) + ":" + this.text6.getText().toString());
    }
}
