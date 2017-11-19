package com.techbridge.smsreader.views.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import com.techbridge.smsreader.R;
import com.techbridge.smsreader.db.DBHelper;
import com.techbridge.smsreader.utils.Utils;

public class AddsettingActivity extends AppCompatActivity implements OnClickListener {
    private Button btnSetting;
    private Context context = AddsettingActivity.this;
    private DBHelper dbhelper;
    private EditText edtBasearea;
    private EditText edtLowerlmt;
    private EditText edtMaxHght;
    private EditText edtPhone;
    private EditText edtTankName;
    private EditText edtUpperlmt;
    private Toolbar mToolbar;

    class C02941 implements OnClickListener {
        C02941() {
        }

        public void onClick(View view) {
            startActivity(new Intent(context, DashboardActivity.class));
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_setting);
        initWidgets();
    }

    private void initWidgets() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle((CharSequence) "Add Setting");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setHomeAsUpIndicator(getResources().getDrawable(R.drawable.ic_action_back));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(new C02941());
        edtTankName = (EditText) findViewById(R.id.tank_name);
        edtMaxHght = (EditText) findViewById(R.id.maximum_height);
        edtUpperlmt = (EditText) findViewById(R.id.upper_limit);
        edtLowerlmt = (EditText) findViewById(R.id.lower_limit);
        edtPhone = (EditText) findViewById(R.id.phone);
        edtBasearea = (EditText) findViewById(R.id.base_area);
        btnSetting = (Button) findViewById(R.id.btn_add_setting);
        btnSetting.setOnClickListener(this);
        dbhelper = new DBHelper(this);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_add_setting:
                validateFields();
                return;
            default:
                return;
        }
    }

    public void validateFields() {
        String tankName = edtTankName.getText().toString();
        String maxHeight = edtMaxHght.getText().toString();
        String upperLimit = edtUpperlmt.getText().toString();
        String lowerLimit = edtLowerlmt.getText().toString();
        String phoneNumber = edtPhone.getText().toString();
        String bArea = edtBasearea.getText().toString();
        if (tankName.equals("") || tankName.length() < 1) {
            Utils.showShortToast(context, "Enter tank name").show();
        } else if (maxHeight.equals("") || maxHeight.length() < 1) {
            Utils.showShortToast(context, "Enter height").show();
        } else if (upperLimit.equals("") || upperLimit.length() < 1) {
            Utils.showShortToast(context, "Enter upper limit").show();
        } else if (lowerLimit.length() < 1) {
            Utils.showShortToast(context, "Enter lower  limit").show();
        } else if (phoneNumber.length() != 10 && !phoneNumber.startsWith("07")) {
            Utils.showShortToast(context, "Enter correct phone number").show();
        } else if (bArea.length() < 1) {
            Utils.showShortToast(context, "Enter the base area").show();
        } else {
            dbhelper.addSetting(tankName, maxHeight, upperLimit, lowerLimit, phoneNumber, bArea);
            Utils.showShortToast(context, "Add Successfully").show();
            startActivity(new Intent(context, SettingActivity.class));
        }
    }

    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(context, DashboardActivity.class));
    }
}
