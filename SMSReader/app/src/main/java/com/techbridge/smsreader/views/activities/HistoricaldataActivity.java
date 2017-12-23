package com.techbridge.smsreader.views.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import com.techbridge.smsreader.R;
import com.techbridge.smsreader.db.DBHelper;
import com.techbridge.smsreader.utils.Prefs;
import com.techbridge.smsreader.views.adapters.MonthAdapter;

public class HistoricaldataActivity extends BaseActivity implements OnClickListener {
    private Context context = HistoricaldataActivity.this;
    public ArrayAdapter<String> dataAdapter;
    public ArrayAdapter<String> dataAdapter2;
    private DBHelper dbhelper;
    private Toolbar mToolbar;
    private FloatingActionButton myFab;
    private Paint f65p = new Paint();
    private RecyclerView recyclerView;
    private Spinner spinner2;
    private Spinner spinner3;
    private View view;

    class C03131 implements OnClickListener {
        C03131() {
        }

        public void onClick(View view) {
            startActivity(new Intent(context, DashboardActivity.class));
        }
    }

    class C03142 implements OnItemSelectedListener {
        C03142() {
        }

        public void onItemSelected(AdapterView<?> adapterView, View v, int position, long id) {
            String tankUniqueId = dbhelper.getTankUniqueIndex(position);
            Prefs.writeString(context, "tankUniqueId", dbhelper.getTankUniqueIndex(position));
            int year = Integer.parseInt((String) dbhelper.getYearsList().get(Prefs.readInteger(context, "yearPosition", 0)));
            if (dbhelper.getMonthsList(tankUniqueId, year).size() > 0) {
                try {
                    MonthAdapter dataAdapter = new MonthAdapter(context, dbhelper.getMonthsList(tankUniqueId, year));
                    dataAdapter.notifyDataSetChanged();
                    recyclerView.setAdapter(dataAdapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(context));
                    return;
                } catch (Exception e) {
                    Log.e("DB Error", e.toString());
                    e.printStackTrace();
                    return;
                }
            }
            recyclerView.removeAllViewsInLayout();
        }

        public void onNothingSelected(AdapterView<?> adapterView) {
        }
    }

    class C03153 implements OnItemSelectedListener {
        C03153() {
        }

        public void onItemSelected(AdapterView<?> adapterView, View v, int position, long id) {
            String tankUniqueId = Prefs.readString(context, "tankUniqueId", "");
            int year = Integer.parseInt((String) dbhelper.getYearsList().get(position));
            Prefs.writeInteger(context, "yearPosition", position);
            if (dbhelper.getMonthsList(tankUniqueId, year).size() > 0) {
                try {
                    MonthAdapter dataAdapter = new MonthAdapter(context, dbhelper.getMonthsList(tankUniqueId, year));
                    dataAdapter.notifyDataSetChanged();
                    recyclerView.setAdapter(dataAdapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(context));
                    return;
                } catch (Exception e) {
                    Log.e("DB Error", e.toString());
                    e.printStackTrace();
                    return;
                }
            }
            recyclerView.removeAllViewsInLayout();
        }

        public void onNothingSelected(AdapterView<?> adapterView) {
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_history);
        initWidgets();
        loadSpinnerData();
        loadView();
    }

    private void initWidgets() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle((CharSequence) "Historical Data");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setHomeAsUpIndicator(getResources().getDrawable(R.drawable.ic_action_back));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(new C03131());
        myFab = (FloatingActionButton) findViewById(R.id.fab);
        myFab.setVisibility(View.INVISIBLE);
        spinner2 = (Spinner) findViewById(R.id.spinner2);
        spinner3 = (Spinner) findViewById(R.id.spinner3);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        dbhelper = new DBHelper(context);
        Prefs.writeString(context, "tankUniqueId", dbhelper.getTankUniqueIndex(0));
    }

    public void loadView() {
        String tankUniqueId = dbhelper.getTankUniqueIndex(0);
        if (dbhelper.getMonthsList(tankUniqueId, 2017).size() > 0) {
            try {
                MonthAdapter dataAdapter = new MonthAdapter(context, dbhelper.getMonthsList(tankUniqueId, 2017));
                dataAdapter.notifyDataSetChanged();
                recyclerView.setAdapter(dataAdapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } catch (Exception e) {
                Log.e("DB Error", e.toString());
                e.printStackTrace();
            }
        }
    }

    public void loadSpinnerData() {
        if (dbhelper.getTankNames().size() > 0) {
            dataAdapter = new ArrayAdapter(context, android.R.layout.simple_list_item_1, dbhelper.getTankNames());
            dataAdapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
            dataAdapter.notifyDataSetChanged();
            spinner2.setAdapter(dataAdapter);
        }
        if (dbhelper.getYearsList().size() > 0) {
            dataAdapter2 = new ArrayAdapter(context, android.R.layout.simple_list_item_1, dbhelper.getYearsList());
            dataAdapter2.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
            dataAdapter2.notifyDataSetChanged();
            spinner3.setAdapter(dataAdapter2);
        }
        spinner2.setOnItemSelectedListener(new C03142());
        spinner3.setOnItemSelectedListener(new C03153());
    }

    public void onClick(View v) {
        int id = v.getId();
    }

    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(context, DashboardActivity.class));
    }
}
