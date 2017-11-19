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

public class SetnumbersActivity extends BaseActivity implements OnClickListener {
    private Button btnAdd;
    private Button btnDelete;
    private final OnWheelChangedListener changedListener = new C05173();
    private Context ctx = this;
    private Toolbar mToolbar;
    OnWheelScrollListener scrolledListener = new C05162();
    private SmsManager smsManager;
    private EditText text1;
    private String[] wheelMenu2 = new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};
    private boolean wheelScrolled = false;

    class C03171 implements OnClickListener {
        C03171() {
        }

        public void onClick(View view) {
            SetnumbersActivity.this.startActivity(new Intent(SetnumbersActivity.this.ctx, CtrldashboardActivity.class));
        }
    }

    class C03194 implements DialogInterface.OnClickListener {
        C03194() {
        }

        public void onClick(DialogInterface dialog, int id) {
            SetnumbersActivity.this.sendSMS(Prefs.readString(SetnumbersActivity.this.ctx, "settingPhone", ""), "1234A" + SetnumbersActivity.this.getWheel(R.id.p7).getCurrentItem());
        }
    }

    class C03205 implements DialogInterface.OnClickListener {
        C03205() {
        }

        public void onClick(DialogInterface dialog, int id) {
            dialog.cancel();
        }
    }

    class C05162 implements OnWheelScrollListener {

        class C03181 implements Runnable {
            C03181() {
            }

            public void run() {
            }
        }

        C05162() {
        }

        public void onScrollStarts(WheelView wheel) {
            SetnumbersActivity.this.wheelScrolled = true;
        }

        public void onScrollEnds(WheelView wheel) {
            SetnumbersActivity.this.wheelScrolled = false;
        }

        public void onScrollingStarted(WheelView wheel) {
        }

        public void onScrollingFinished(WheelView wheel) {
            new Handler().post(new C03181());
        }
    }

    class C05173 implements OnWheelChangedListener {
        C05173() {
        }

        public void onChanged(WheelView wheel, int oldValue, int newValue) {
            if (!SetnumbersActivity.this.wheelScrolled) {
            }
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_sync2_time);
        initWheel7(R.id.p7);
        initWidgets();
    }

    private void initWidgets() {
        this.mToolbar = (Toolbar) findViewById(R.id.toolbar);
        this.mToolbar.setTitle((CharSequence) "Alarm Setting");
        setSupportActionBar(this.mToolbar);
        getSupportActionBar().setHomeAsUpIndicator(getResources().getDrawable(R.drawable.ic_action_back));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.mToolbar.setNavigationOnClickListener(new C03171());
        this.btnAdd = (Button) findViewById(R.id.btn1);
        this.btnDelete = (Button) findViewById(R.id.btn2);
        this.text1 = (EditText) findViewById(R.id.r3);
        this.btnAdd.setOnClickListener(this);
        this.btnDelete.setOnClickListener(this);
        this.smsManager = SmsManager.getDefault();
    }

    private void initWheel7(int id) {
        WheelView wheel = (WheelView) findViewById(id);
        wheel.setViewAdapter(new ArrayWheelAdapter(this.ctx, this.wheelMenu2));
        wheel.setVisibleItems(2);
        wheel.setCurrentItem(1);
        wheel.addChangingListener(this.changedListener);
        wheel.setCyclic(true);
    }

    private WheelView getWheel(int id) {
        return (WheelView) findViewById(id);
    }

    private int getWheelValue(int id) {
        return getWheel(id).getCurrentItem();
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn1:
                String txt1 = this.text1.getText().toString();
                if (txt1.equals("") || txt1 == null) {
                    Utils.showShortToast(this.ctx, "Enter Phone Number").show();
                    return;
                }
                sendSMS(Prefs.readString(this.ctx, "settingPhone", ""), "1234A" + getWheel(R.id.p7).getCurrentItem() + "T" + txt1);
                this.text1.setText("");
                return;
            case R.id.btn2:
                Builder builder1 = new Builder(this.ctx);
                builder1.setMessage((CharSequence) "Are you sure to delete?");
                builder1.setCancelable(true);
                builder1.setPositiveButton((CharSequence) "Yes", new C03194());
                builder1.setNegativeButton((CharSequence) "No", new C03205());
                builder1.create().show();
                return;
            default:
                return;
        }
    }
}
