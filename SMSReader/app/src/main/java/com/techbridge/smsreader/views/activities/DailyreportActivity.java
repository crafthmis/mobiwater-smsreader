package com.techbridge.smsreader.views.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog.Builder;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import com.techbridge.smsreader.R;
import com.techbridge.smsreader.utils.Prefs;
import com.techbridge.smsreader.utils.Utils;
import com.techbridge.smsreader.utils.wheelpicker.ArrayWheelAdapter;
import com.techbridge.smsreader.utils.wheelpicker.OnWheelChangedListener;
import com.techbridge.smsreader.utils.wheelpicker.OnWheelScrollListener;
import com.techbridge.smsreader.utils.wheelpicker.WheelView;
import java.util.ArrayList;
import org.apache.commons.lang3.StringUtils;

public class DailyreportActivity extends BaseActivity implements OnClickListener {
    private Button btnAdd;
    private Button btnDelete;
    private final OnWheelChangedListener changedListener = new C05113();
    private Context ctx = this;
    private Toolbar mToolbar;
    OnWheelScrollListener scrolledListener = new C05102();
    private SmsManager smsManager;
    private EditText text1;
    private EditText text2;
    private boolean wheelScrolled = false;

    class C02981 implements OnClickListener {
        C02981() {
        }

        public void onClick(View view) {
            DailyreportActivity.this.startActivity(new Intent(DailyreportActivity.this.ctx, CtrldashboardActivity.class));
        }
    }

    class C03004 implements DialogInterface.OnClickListener {
        C03004() {
        }

        public void onClick(DialogInterface dialog, int id) {
            DailyreportActivity.this.sendSMS(Prefs.readString(DailyreportActivity.this.ctx, "settingPhone", ""), "1234DRDEL".trim());
        }
    }

    class C03015 implements DialogInterface.OnClickListener {
        C03015() {
        }

        public void onClick(DialogInterface dialog, int id) {
            dialog.cancel();
        }
    }

    class C05102 implements OnWheelScrollListener {

        class C02991 implements Runnable {
            C02991() {
            }

            public void run() {
            }
        }

        C05102() {
        }

        public void onScrollStarts(WheelView wheel) {
            DailyreportActivity.this.wheelScrolled = true;
        }

        public void onScrollEnds(WheelView wheel) {
            DailyreportActivity.this.wheelScrolled = false;
            DailyreportActivity.this.updateStatus();
        }

        public void onScrollingStarted(WheelView wheel) {
        }

        public void onScrollingFinished(WheelView wheel) {
            new Handler().post(new C02991());
        }
    }

    class C05113 implements OnWheelChangedListener {
        C05113() {
        }

        public void onChanged(WheelView wheel, int oldValue, int newValue) {
            if (!DailyreportActivity.this.wheelScrolled) {
                DailyreportActivity.this.updateStatus();
            }
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_sync3_time);
        initWheel7(R.id.p8);
        initWheel8(R.id.p9);
        initWidgets();
    }

    private void initWidgets() {
        this.mToolbar = (Toolbar) findViewById(R.id.toolbar);
        this.mToolbar.setTitle((CharSequence) "Daily Reporting Setting");
        setSupportActionBar(this.mToolbar);
        getSupportActionBar().setHomeAsUpIndicator(getResources().getDrawable(R.drawable.ic_action_back));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.mToolbar.setNavigationOnClickListener(new C02981());
        this.btnAdd = (Button) findViewById(R.id.btn1);
        this.btnDelete = (Button) findViewById(R.id.btn2);
        this.text1 = (EditText) findViewById(R.id.r3);
        this.text2 = (EditText) findViewById(R.id.r4);
        this.btnAdd.setOnClickListener(this);
        this.btnDelete.setOnClickListener(this);
        this.smsManager = SmsManager.getDefault();
    }

    private void updateStatus() {
        this.text2.setText(((String[]) getHours().toArray(new String[getHours().size()]))[getWheel(R.id.p8).getCurrentItem()] + ":" + ((String[]) getMinutes().toArray(new String[getMinutes().size()]))[getWheel(R.id.p9).getCurrentItem()]);
    }

    private void initWheel7(int id) {
        WheelView wheel = (WheelView) findViewById(id);
        wheel.setViewAdapter(new ArrayWheelAdapter(this.ctx, getHours().toArray(new String[getHours().size()])));
        wheel.setVisibleItems(1);
        wheel.setCurrentItem(1);
        wheel.addChangingListener(this.changedListener);
        wheel.addScrollingListener(this.scrolledListener);
        wheel.setCyclic(false);
    }

    private void initWheel8(int id) {
        WheelView wheel = (WheelView) findViewById(id);
        wheel.setViewAdapter(new ArrayWheelAdapter(this.ctx, getMinutes().toArray(new String[getMinutes().size()])));
        wheel.setVisibleItems(1);
        wheel.setCurrentItem(1);
        wheel.addChangingListener(this.changedListener);
        wheel.addScrollingListener(this.scrolledListener);
        wheel.setCyclic(false);
    }

    private WheelView getWheel(int id) {
        return (WheelView) findViewById(id);
    }

    private int getWheelValue(int id) {
        return getWheel(id).getCurrentItem();
    }

    private ArrayList<String> getHours() {
        ArrayList<String> hrs = new ArrayList();
        for (int j = 0; j < 24; j++) {
            hrs.add(StringUtils.leftPad(j + "", 2, "0"));
        }
        return hrs;
    }

    private ArrayList<String> getMinutes() {
        ArrayList<String> mins = new ArrayList();
        for (int j = 0; j < 60; j++) {
            mins.add(StringUtils.leftPad(j + "", 2, "0"));
        }
        return mins;
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn1:
                String txt1 = this.text2.getText().toString();
                if (txt1.equals("") || txt1 == null) {
                    Utils.showShortToast(this.ctx, "Enter Time").show();
                    return;
                }
                sendSMS(Prefs.readString(this.ctx, "settingPhone", ""), ("1234DR" + getWheel(R.id.p8).getCurrentItem() + "T" + txt1).trim());
                this.text1.setText("");
                return;
            case R.id.btn2:
                Builder builder1 = new Builder(this.ctx);
                builder1.setMessage((CharSequence) "Are you sure to delete?");
                builder1.setCancelable(true);
                builder1.setPositiveButton((CharSequence) "Yes", new C03004());
                builder1.setNegativeButton((CharSequence) "No", new C03015());
                builder1.create().show();
                return;
            default:
                return;
        }
    }
}
