package com.techbridge.smsreader.views.fragments;

import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis.XAxisPosition;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.techbridge.smsreader.R;
import com.techbridge.smsreader.db.DBHelper;
import com.techbridge.smsreader.utils.Prefs;

public class WeekFragment extends Fragment {
    private ArrayAdapter<String> dataAdapter;
    private DBHelper dbhelper;
    private int i = -7;
    private LineChart mChart;
    private Spinner spinner;
    String title;


    class C03331 implements OnItemSelectedListener {
        C03331() {
        }

        @RequiresApi(api = 24)
        public void onItemSelected(AdapterView<?> adapterView, View v, int position, long id) {
            if (dbhelper.getTankNames().size() > 0) {
                String tankUniqueId = dbhelper.getTankUniqueIndex(position);
                Prefs.writeString(getActivity(), "uId", dbhelper.getTankUniqueIndex(position));
                redrawGraph(tankUniqueId, i);
            }
        }

        public void onNothingSelected(AdapterView<?> adapterView) {
        }
    }

    public static WeekFragment newInstance(String title) {
        WeekFragment fragment = new WeekFragment();
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
        View view = inflater.inflate(R.layout.fragment_week, container, false);
        initWidgets(view);
        loadSpinnerData();
        return view;
    }

    private void initWidgets(View v) {
        this.mChart = (LineChart) v.findViewById(R.id.linechart);
        this.spinner = (Spinner) v.findViewById(R.id.spinner);
        this.dbhelper = new DBHelper(getActivity());

    }

    public void redrawGraph(final String tankUniqueId, final int i) {
        mChart.clear();
        if (dbhelper.drawWeekGraph(tankUniqueId, i).getDataSetCount() != 0) {
            mChart.setData(dbhelper.drawWeekGraph(tankUniqueId, i));
            mChart.getDescription().setEnabled(false);
            mChart.getXAxis().setPosition(XAxisPosition.BOTTOM);
            mChart.getXAxis().setCenterAxisLabels(false);
            mChart.getXAxis().setValueFormatter(new IAxisValueFormatter() {
                public String getFormattedValue(float value, AxisBase axis) {
                    return dbhelper.LineDataXWeekLabels(tankUniqueId, i).get(Integer.valueOf((int) value));
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
}
