package com.techbridge.smsreader.views.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import com.techbridge.smsreader.R;
import com.techbridge.smsreader.db.DBHelper;
import com.techbridge.smsreader.models.Setting;
import com.techbridge.smsreader.utils.Prefs;
import com.techbridge.smsreader.utils.Utils;

public class EditsettingActivity extends BaseActivity implements OnClickListener {
    private EditText baseArea;
    private Context context = this;
    private DBHelper dbHelper;
    private FloatingActionButton fab1;
    private EditText lowerLimit;
    private Toolbar mToolbar;
    private EditText maxHeight;
    private EditText phoneNumber;
    private Setting setting;
    private String state;
    private EditText tankName;
    private EditText upperLimit;

    class C03081 implements OnClickListener {
        C03081() {
        }

        public void onClick(View view) {
            EditsettingActivity.this.startActivity(new Intent(EditsettingActivity.this.context, SettingActivity.class));
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_edit_settings);
        initWidget();
        loadSetting(Prefs.readString(this.context, "sId", ""));
    }

    public void initWidget() {
        this.mToolbar = (Toolbar) findViewById(R.id.toolbar);
        this.mToolbar.setTitle((CharSequence) "Edit Tank Details");
        setSupportActionBar(this.mToolbar);
        getSupportActionBar().setHomeAsUpIndicator(getResources().getDrawable(R.drawable.ic_action_back));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.mToolbar.setNavigationOnClickListener(new C03081());
        this.fab1 = (FloatingActionButton) findViewById(R.id.fab1);
        this.tankName = (EditText) findViewById(R.id.tank_name);
        this.maxHeight = (EditText) findViewById(R.id.maximum_height);
        this.upperLimit = (EditText) findViewById(R.id.upper_limit);
        this.lowerLimit = (EditText) findViewById(R.id.lower_limit);
        this.phoneNumber = (EditText) findViewById(R.id.phone_number);
        this.baseArea = (EditText) findViewById(R.id.base_area);
        this.dbHelper = new DBHelper(this.context);
        this.fab1.setOnClickListener(this);
        this.state = "edit";
    }

    public void toggleEdit(String currentState, View v) {
        if (currentState.equals("edit")) {
            ((FloatingActionButton) v).setImageResource(R.drawable.ic_save_white);
            this.tankName.setEnabled(true);
            this.maxHeight.setEnabled(true);
            this.upperLimit.setEnabled(true);
            this.lowerLimit.setEnabled(true);
            this.phoneNumber.setEnabled(true);
            this.baseArea.setEnabled(true);
            if (VERSION.SDK_INT >= 16) {
                this.tankName.setBackground(getResources().getDrawable(R.drawable.rounded_border_dark));
                this.maxHeight.setBackground(getResources().getDrawable(R.drawable.rounded_border_dark));
                this.upperLimit.setBackground(getResources().getDrawable(R.drawable.rounded_border_dark));
                this.lowerLimit.setBackground(getResources().getDrawable(R.drawable.rounded_border_dark));
                this.phoneNumber.setBackground(getResources().getDrawable(R.drawable.rounded_border_dark));
                this.baseArea.setBackground(getResources().getDrawable(R.drawable.rounded_border_dark));
            }
            this.state = "save";
            return;
        }
        ((FloatingActionButton) v).setImageResource(R.drawable.ic_create_white);
        this.tankName.setEnabled(false);
        this.maxHeight.setEnabled(false);
        this.upperLimit.setEnabled(false);
        this.lowerLimit.setEnabled(false);
        this.phoneNumber.setEnabled(false);
        this.baseArea.setEnabled(false);
        validateFields();
        this.state = "edit";
    }

    public void loadSetting(String sId) {
        Setting setting = this.dbHelper.getSettings(sId);
        this.tankName.setText(setting.getTankName());
        this.maxHeight.setText(setting.getTopHeight());
        this.upperLimit.setText(setting.getUpperLimit());
        this.lowerLimit.setText(setting.getLowerLimit());
        this.phoneNumber.setText(setting.getPhoneNumber());
        this.baseArea.setText(setting.getBaseArea());
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab1:
                toggleEdit(this.state, v);
                return;
            default:
                return;
        }
    }

    public void validateFields() {
        String name = this.tankName.getText().toString();
        String height = this.maxHeight.getText().toString();
        String uLimit = this.upperLimit.getText().toString();
        String lLimit = this.lowerLimit.getText().toString();
        String pNumber = this.phoneNumber.getText().toString();
        String bArea = this.baseArea.getText().toString();
        if (name.equals("") || name.length() < 1) {
            Utils.showShortToast(this.context, "Enter tank name").show();
        } else if (height.equals("") || height.length() < 1) {
            Utils.showShortToast(this.context, "Enter height").show();
        } else if (uLimit.equals("") || uLimit.length() < 1) {
            Utils.showShortToast(this.context, "Enter upper limit").show();
        } else if (lLimit.length() < 1) {
            Utils.showShortToast(this.context, "Enter lower  limit").show();
        } else if (pNumber.length() != 10 && !pNumber.startsWith("07")) {
            Utils.showShortToast(this.context, "Enter correct phone number").show();
        } else if (bArea.length() < 1) {
            Utils.showShortToast(this.context, "Enter the base area").show();
        } else {
            this.dbHelper.updateSetting(new Setting(Prefs.readString(this.context, "sId", ""), name, height, uLimit, lLimit, pNumber, bArea));
            Utils.showShortToast(this.context, "Updated Successfully").show();
            startActivity(new Intent(this.context, SettingActivity.class));
        }
    }

    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this.context, SettingActivity.class));
    }
}
