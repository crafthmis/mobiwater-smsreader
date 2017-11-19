package com.techbridge.smsreader.views.fragments;

import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import com.techbridge.smsreader.R;
import com.techbridge.smsreader.db.DBHelper;
import com.techbridge.smsreader.utils.Prefs;
import com.techbridge.smsreader.utils.Utils;

public class Graph1Fragment extends Fragment implements OnClickListener {
    private Button btnBack;
    private Button btnNext;
    private ArrayAdapter<String> dataAdapter;
    private DBHelper dbhelper;
    private int f59i = 0;
    private LinearLayout mChart;
    private Spinner spinner;
    String title;

    class C03321 implements OnItemSelectedListener {
        C03321() {
        }

        @RequiresApi(api = 24)
        public void onItemSelected(AdapterView<?> adapterView, View v, int position, long id) {
            String tankUniqueId = dbhelper.getTankUniqueIndex(position);
            Prefs.writeString(getActivity(), "uId", dbhelper.getTankUniqueIndex(position));
            mChart.removeAllViews();
            mChart.addView(dbhelper.drawGraph1(tankUniqueId, 0));
            f59i = 0;
        }

        public void onNothingSelected(AdapterView<?> adapterView) {
        }
    }

    public static Graph1Fragment newInstance(String title) {
        Graph1Fragment fragment = new Graph1Fragment();
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
        View view = inflater.inflate(R.layout.fragment_graph1, container, false);
        initWidgets(view);
        loadSpinnerData();
        return view;
    }

    private void initWidgets(View v) {
        this.mChart = (LinearLayout) v.findViewById(R.id.linechart);
        this.spinner = (Spinner) v.findViewById(R.id.spinner);
        this.btnBack = (Button) v.findViewById(R.id.back_btn);
        this.btnNext = (Button) v.findViewById(R.id.next_btn);
        this.btnBack.setOnClickListener(this);
        this.btnNext.setOnClickListener(this);
    }

    private void loadSpinnerData() {
        this.dbhelper = new DBHelper(getActivity());
        this.dataAdapter = new ArrayAdapter(getActivity(), 17367048, this.dbhelper.getTankNames());
        this.dataAdapter.setDropDownViewResource(17367049);
        this.dataAdapter.notifyDataSetChanged();
        this.spinner.setAdapter(this.dataAdapter);
        this.spinner.setOnItemSelectedListener(new C03321());
    }

    public void onClick(View v) {
        String tankUniqueId = Prefs.readString(getActivity(), "uId", "");
        switch (v.getId()) {
            case R.id.back_btn:
                if (this.f59i <= 0) {
                    this.mChart.removeAllViews();
                    this.mChart.addView(this.dbhelper.drawGraph1(tankUniqueId, this.f59i));
                    Utils.showShortToast(getActivity(), this.f59i + "").show();
                    this.f59i++;
                    return;
                }
                Utils.showShortToast(getActivity(), "Today").show();
                return;
            case R.id.next_btn:
                if (this.f59i > -6) {
                    this.mChart.removeAllViews();
                    this.mChart.addView(this.dbhelper.drawGraph1(tankUniqueId, this.f59i));
                    Utils.showShortToast(getActivity(), this.f59i + "").show();
                    this.f59i--;
                    return;
                }
                Utils.showShortToast(getActivity(), "Last 7th Day").show();
                return;
            default:
                return;
        }
    }
}
