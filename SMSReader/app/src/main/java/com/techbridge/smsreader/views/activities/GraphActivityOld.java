package com.techbridge.smsreader.views.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import com.techbridge.smsreader.R;
import com.techbridge.smsreader.db.DBHelper;
import com.techbridge.smsreader.utils.Prefs;
import com.techbridge.smsreader.utils.Utils;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class GraphActivityOld extends AppCompatActivity implements OnClickListener {
    private Button btnBack;
    private Button btnNext;
    private Context context = this;
    private ArrayAdapter<String> dataAdapter;
    private DBHelper dbhelper;
    private TextView graphText;
    private int f64i = 0;
    private LinearLayout mChart;
    private Toolbar mToolbar;
    private Spinner spinner;

    class C03111 implements OnClickListener {
        C03111() {
        }

        public void onClick(View view) {
            GraphActivityOld.this.startActivity(new Intent(GraphActivityOld.this.context, DashboardActivity.class));
        }
    }

    class C03122 implements OnItemSelectedListener {
        C03122() {
        }

        @RequiresApi(api = 24)
        public void onItemSelected(AdapterView<?> adapterView, View v, int position, long id) {
            String tankUniqueId = GraphActivityOld.this.dbhelper.getTankUniqueIndex(position);
            Prefs.writeString(GraphActivityOld.this.context, "uId", GraphActivityOld.this.dbhelper.getTankUniqueIndex(position));
            GraphActivityOld.this.mChart.removeAllViews();
            GraphActivityOld.this.mChart.addView(GraphActivityOld.this.dbhelper.drawGraph1(tankUniqueId, 0));
            GraphActivityOld.this.f64i = 0;
        }

        public void onNothingSelected(AdapterView<?> adapterView) {
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_graph_old);
        initWidgets();
        loadSpinnerData();
    }

    private void initWidgets() {
        this.mToolbar = (Toolbar) findViewById(R.id.toolbar);
        this.mToolbar.setTitle((CharSequence) "Daily Water Levels");
        setSupportActionBar(this.mToolbar);
        getSupportActionBar().setHomeAsUpIndicator(getResources().getDrawable(R.drawable.ic_action_back));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.mToolbar.setNavigationOnClickListener(new C03111());
        this.mChart = (LinearLayout) findViewById(R.id.linechart);
        this.spinner = (Spinner) findViewById(R.id.spinner);
        this.btnBack = (Button) findViewById(R.id.back_btn);
        this.btnNext = (Button) findViewById(R.id.next_btn);
        this.btnBack.setOnClickListener(this);
        this.btnNext.setOnClickListener(this);
        this.graphText = (TextView) findViewById(R.id.graph_text);
    }

    private void loadSpinnerData() {
        this.dbhelper = new DBHelper(this.context);
        this.dataAdapter = new ArrayAdapter(this.context, android.R.layout.simple_list_item_1, this.dbhelper.getTankNames());
        this.dataAdapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        this.dataAdapter.notifyDataSetChanged();
        this.spinner.setAdapter(this.dataAdapter);
        this.spinner.setOnItemSelectedListener(new C03122());
    }

    public void onClick(View v) {
        String tankUniqueId = Prefs.readString(this.context, "uId", "");
        switch (v.getId()) {
            case R.id.back_btn:
                if (this.f64i <= 0) {
                    this.mChart.removeAllViews();
                    this.mChart.addView(this.dbhelper.drawGraph1(tankUniqueId, this.f64i));
                    getGraphText(this.f64i);
                    this.f64i++;
                    return;
                }
                Utils.showShortToast(this.context, "Today").show();
                this.f64i = 0;
                return;
            case R.id.next_btn:
                this.f64i--;
                if (this.f64i > -8) {
                    this.mChart.removeAllViews();
                    this.mChart.addView(this.dbhelper.drawGraph1(tankUniqueId, this.f64i));
                    getGraphText(this.f64i);
                    return;
                }
                this.f64i = -7;
                Utils.showShortToast(this.context, "Last 7th Day").show();
                return;
            default:
                return;
        }
    }

    public void getGraphText(int interval) {
        Calendar cal = new GregorianCalendar();
        cal.add(Calendar.DAY_OF_MONTH, interval);
        Date dayOfmonth = cal.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("E dd MMMM yyyy");
        switch (interval) {
            case -8:
            case -7:
            case -6:
            case -5:
            case -4:
            case -3:
            case -2:
                this.graphText.setText(String.valueOf(sdf.format(dayOfmonth)));
                return;
            case -1:
                this.graphText.setText("Yesterday");
                return;
            case 0:
                this.graphText.setText("Today");
                return;
            default:
                return;
        }
    }

    private String getDayNumberSuffix(int day) {
        if (day >= 11 && day <= 13) {
            return "th";
        }
        switch (day % 10) {
            case 1:
                return "st";
            case 2:
                return "nd";
            case 3:
                return "rd";
            default:
                return "th";
        }
    }

    private String getMonthName(int interval) {
        return new DateFormatSymbols().getMonths()[interval];
    }

    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this.context, DashboardActivity.class));
    }
}
