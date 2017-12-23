package com.techbridge.smsreader.views.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import com.techbridge.smsreader.R;
import com.techbridge.smsreader.utils.Prefs;

public class CtrldashboardActivity extends BaseActivity implements OnClickListener {
    private Button btnNumbers;
    private Button btnReport;
    private Button btnSync;
    private Context context = this;
    private Toolbar mToolbar;

    class C02961 implements OnClickListener {
        C02961() {
        }

        public void onClick(View view) {
            CtrldashboardActivity.this.startActivity(new Intent(CtrldashboardActivity.this.context, CtrlsettingsActivity.class));
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ctrldashboard);
        initWidgets();
    }

    public void initWidgets() {
        this.mToolbar = (Toolbar) findViewById(R.id.toolbar);
        this.mToolbar.setTitle("Setting " + Prefs.readString(this.context, "settingTank", ""));
        setSupportActionBar(this.mToolbar);
        getSupportActionBar().setHomeAsUpIndicator(getResources().getDrawable(R.drawable.ic_action_back));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.mToolbar.setNavigationOnClickListener(new C02961());
        this.btnSync = (Button) findViewById(R.id.b1);
        this.btnNumbers = (Button) findViewById(R.id.b2);
        this.btnReport = (Button) findViewById(R.id.b4);
        this.btnSync.setOnClickListener(this);
        this.btnNumbers.setOnClickListener(this);
        this.btnReport.setOnClickListener(this);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.b1:
                startActivity(new Intent(this.context, SynctimeActivity.class));
                return;
            case R.id.b2:
                startActivity(new Intent(this.context, SetnumbersActivity.class));
                return;
            case R.id.b4:
                startActivity(new Intent(this.context, DailyreportActivity.class));
                return;
            default:
                return;
        }
    }
}
