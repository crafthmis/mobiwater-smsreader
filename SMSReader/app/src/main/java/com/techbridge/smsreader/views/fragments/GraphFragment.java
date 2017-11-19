package com.techbridge.smsreader.views.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import com.github.mikephil.charting.animation.Easing.EasingOption;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis.XAxisPosition;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.techbridge.smsreader.R;
import com.techbridge.smsreader.db.DBHelper;

public class GraphFragment extends Fragment {
    private ArrayAdapter<String> dataAdapter;
    private DBHelper dbhelper;
    private LineChart mChart;
    private Spinner spinner;
    String title;

    class C03331 implements OnItemSelectedListener {
        C03331() {
        }

        public void onItemSelected(AdapterView<?> adapterView, View v, int position, long id) {
            final String tankUniqueId = dbhelper.getTankUniqueIndex(position);
            mChart.setData(dbhelper.drawGraph(tankUniqueId, 0));
            mChart.getXAxis().setPosition(XAxisPosition.BOTTOM);
            mChart.getXAxis().setValueFormatter(new IAxisValueFormatter() {
                public String getFormattedValue(float value, AxisBase axis) {
                    return (String)dbhelper.LineDataXLabels(tankUniqueId, 0).get(Integer.valueOf((int) value));
                }
            });
            mChart.animateX(2000, EasingOption.EaseInOutQuart);
            mChart.invalidate();
        }

        public void onNothingSelected(AdapterView<?> adapterView) {
        }
    }

    public static GraphFragment newInstance(String title) {
        GraphFragment fragment = new GraphFragment();
        new Bundle(1).putString("EXTRA_TITLE", title);
        return fragment;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.title = getArguments().getString("EXTRA_TITLE");
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
    }

    private void loadSpinnerData() {
        this.dbhelper = new DBHelper(getActivity());
        this.dataAdapter = new ArrayAdapter(getActivity(), 17367048, this.dbhelper.getTankNames());
        this.dataAdapter.setDropDownViewResource(17367049);
        this.dataAdapter.notifyDataSetChanged();
        this.spinner.setAdapter(this.dataAdapter);
        this.spinner.setOnItemSelectedListener(new C03331());
    }
}
