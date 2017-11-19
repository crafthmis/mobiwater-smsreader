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
import android.widget.Spinner;
import android.widget.TextView;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis.XAxisPosition;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.techbridge.smsreader.R;
import com.techbridge.smsreader.db.DBHelper;
import com.techbridge.smsreader.utils.Prefs;
import com.techbridge.smsreader.utils.Utils;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class GraphActivity extends AppCompatActivity implements OnClickListener {
    private Button btnBack;
    private Button btnPrev;
    private Context context = this;
    private ArrayAdapter<String> dataAdapter;
    private DBHelper dbhelper;
    private TextView graphText;
    private int i = 0;
    private LineChart mChart;
    private Toolbar mToolbar;
    private Spinner spinner;

    class C03091 implements OnClickListener {
        C03091() {
        }

        public void onClick(View view) {
            startActivity(new Intent(context, DashboardActivity.class));
        }
    }

    class C03102 implements OnItemSelectedListener {
        C03102() {
        }

        @RequiresApi(api = 24)
        public void onItemSelected(AdapterView<?> adapterView, View v, int position, long id) {
            i = 0;
            if (dbhelper.getTankNames().size() > 0) {
                String tankUniqueId = dbhelper.getTankUniqueIndex(position);
                Prefs.writeString(context, "uId", dbhelper.getTankUniqueIndex(position));
                redrawGraph(tankUniqueId, i);
            }
           btnBack.setText("Today");
        }

        public void onNothingSelected(AdapterView<?> adapterView) {
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_graph);
        initWidgets();
        loadSpinnerData();
    }

    private void initWidgets() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle((CharSequence) "Daily Water Levels");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setHomeAsUpIndicator(getResources().getDrawable(R.drawable.ic_action_back));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(new C03091());
        mChart = (LineChart) findViewById(R.id.linechart);
        spinner = (Spinner) findViewById(R.id.spinner);
        btnBack = (Button) findViewById(R.id.back_btn);
        btnPrev = (Button) findViewById(R.id.prev_btn);
        btnBack.setOnClickListener(this);
        btnPrev.setOnClickListener(this);
        graphText = (TextView) findViewById(R.id.graph_text);
    }

    private void loadSpinnerData() {
        dbhelper = new DBHelper(context);
        if (dbhelper.getTankNames().size() > 0) {
            dataAdapter = new ArrayAdapter(context, android.R.layout.simple_list_item_1, dbhelper.getTankNames());
            dataAdapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
            dataAdapter.notifyDataSetChanged();
            spinner.setAdapter(dataAdapter);
        }
        spinner.setOnItemSelectedListener(new C03102());
    }

    public void redrawGraph(final String tankUniqueId, final int i) {
        mChart.clear();
        if (dbhelper.drawGraph(tankUniqueId,i).getDataSetCount() != 0) {
            mChart.setData(dbhelper.drawGraph(tankUniqueId, i));
            mChart.getDescription().setEnabled(false);
            mChart.getXAxis().setPosition(XAxisPosition.BOTTOM);
            mChart.getXAxis().setCenterAxisLabels(false);
            mChart.getXAxis().setValueFormatter(new IAxisValueFormatter() {
                public String getFormattedValue(float value, AxisBase axis) {
                    return dbhelper.LineDataXLabels(tankUniqueId, i).get(Integer.valueOf((int) value));
                }
            });
            mChart.getAxisLeft().setDrawZeroLine(true);
            mChart.getAxisRight().setEnabled(false);
            mChart.getAxisLeft().setLabelCount(6, true);
            mChart.getAxisLeft().setAxisMaxValue(2.5f);
            mChart.getAxisLeft().setAxisMinValue(0.0f);
            mChart.invalidate();
        }
    }

    public void onClick(View v) {
        String tankUniqueId = Prefs.readString(context, "uId", "");
        switch (v.getId()) {
            case R.id.prev_btn:
                i--;
                if (i > -8) {
                    redrawGraph(tankUniqueId, i);
                    getGraphText(i);
                    break;
                }
                i = -7;
                Utils.showShortToast(context, "Last 7th Day").show();
                break;
            case R.id.back_btn:
                i++;
                if (i <= 0) {
                    redrawGraph(tankUniqueId, i);
                    getGraphText(i);
                    break;
                }
                Utils.showShortToast(context, "Today").show();
                i = 0;
                break;
            default:
                return;
        }
    }

    public void getGraphText(int interval) {
        Calendar cal = new GregorianCalendar();
        cal.add(Calendar.DAY_OF_MONTH, interval);
        Date dayOfmonth = cal.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("E dd MMMM yyyy");
        btnBack.setText(getResources().getText(R.string.back_button));
        switch (interval) {
            case -8:
            case -7:
            case -6:
            case -5:
            case -4:
            case -3:
            case -2:
                graphText.setText(String.valueOf(sdf.format(dayOfmonth)));
                return;
            case -1:
                graphText.setText("Yesterday");
                return;
            case 0:
                btnBack.setText("Today");
                graphText.setText("Today");
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
        startActivity(new Intent(context, DashboardActivity.class));
    }
}
