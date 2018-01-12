package com.techbridge.smsreader.views.fragments;

import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.techbridge.smsreader.utils.graphutils.HourAxisValueFormatter;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DayFragment extends Fragment implements View.OnClickListener  {
    private Button btnBack;
    private Button btnPrev;
    private ArrayAdapter<String> dataAdapter;
    private DBHelper dbhelper;
    private TextView graphText;
    private int i = 0;
    private LineChart mChart;
    private Toolbar mToolbar;
    private Spinner spinner;
    String title;


    class C03331 implements OnItemSelectedListener {
        C03331() {
        }

        @RequiresApi(api = 24)
        public void onItemSelected(AdapterView<?> adapterView, View v, int position, long id) {
            i = 0;
            if (dbhelper.getTankNames().size() > 0) {
                String tankUniqueId = dbhelper.getTankUniqueIndex(position);
                Prefs.writeString(getActivity(), "uId", dbhelper.getTankUniqueIndex(position));
                redrawGraph(tankUniqueId, i);
            }
            btnBack.setText("Today");
        }

        public void onNothingSelected(AdapterView<?> adapterView) {
        }
    }

    public static DayFragment newInstance(String title) {
        DayFragment fragment = new DayFragment();
        new Bundle(1).putString("EXTRA_TITLE", title);
        return fragment;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            title = getArguments().getString("EXTRA_TITLE");
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_graph, container, false);
        initWidgets(view);
        loadSpinnerData();
        return view;
    }

    private void initWidgets(View v) {
        this.mChart = (LineChart) v.findViewById(R.id.linechart);
        this.spinner = (Spinner) v.findViewById(R.id.spinner);
        this.btnBack = (Button) v.findViewById(R.id.back_btn);
        this.btnPrev = (Button) v.findViewById(R.id.prev_btn);
        this.dbhelper = new DBHelper(getActivity());
        this.btnBack.setOnClickListener(this);
        this.btnPrev.setOnClickListener(this);
        graphText = (TextView) v.findViewById(R.id.graph_text);
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
            //HourAxisValueFormatter xAxisFormatter = new HourAxisValueFormatter(Long.parseLong(DBHelper.getDayRange(i).split("-")[0]));
            //mChart.getXAxis().setValueFormatter(xAxisFormatter);
            //mChart.getXAxis().setAxisMaxValue(Float.parseFloat(DBHelper.getDayRange(i).split("-")[1]));
            //mChart.getXAxis().setAxisMinValue(Float.parseFloat(DBHelper.getDayRange(i).split("-")[0]));
            mChart.getAxisLeft().setDrawZeroLine(true);
            mChart.getAxisRight().setEnabled(false);
            //mChart.getXAxis().setLabelCount(12);
            mChart.getAxisLeft().setLabelCount(6, true);
            mChart.getAxisLeft().setAxisMaxValue(dbhelper.getTankHeightByUid(tankUniqueId));
            mChart.getAxisLeft().setAxisMinValue(0.0f);
            mChart.invalidate();
        }
    }

    private void loadSpinnerData() {
        dbhelper = new DBHelper(getActivity());
        if (dbhelper.getTankNames().size() > 0) {
            dataAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, dbhelper.getTankNames());
            dataAdapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
            dataAdapter.notifyDataSetChanged();
            spinner.setAdapter(dataAdapter);
        }
        spinner.setOnItemSelectedListener(new C03331());
    }


    public void onClick(View v) {
        String tankUniqueId = Prefs.readString(getActivity(), "uId", "");
        switch (v.getId()) {
            case R.id.prev_btn:
                i--;
                if (i > -8) {
                    redrawGraph(tankUniqueId, i);
                    getGraphText(i);
                    break;
                }
                i = -7;
                Utils.showShortToast(getActivity(), "Last 7th Day").show();
                break;
            case R.id.back_btn:
                i++;
                if (i <= 0) {
                    redrawGraph(tankUniqueId, i);
                    getGraphText(i);
                    break;
                }
                Utils.showShortToast(getActivity(), "Today").show();
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
                graphText.setText(DBHelper.getDayRange(-2).split("-")[0]+" Today "+DBHelper.getDayRange(-2).split("-")[1]);
                return;
            default:
                return;
        }
    }
}
