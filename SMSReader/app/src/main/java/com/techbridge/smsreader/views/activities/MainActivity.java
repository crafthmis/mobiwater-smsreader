package com.techbridge.smsreader.views.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import com.techbridge.smsreader.R;
import com.techbridge.smsreader.db.DBHelper;
import com.techbridge.smsreader.views.adapters.TankAdapter;

public class MainActivity extends AppCompatActivity {
    private Context context = this;
    private DBHelper dbhelper;
    private Toolbar mToolbar;
    private Paint f66p = new Paint();
    private ProgressDialog pDialog;
    private RecyclerView recyclerView;
    private View view;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_alarm);
        initWidgets();
        loadView();
    }

    private void initWidgets() {
        this.mToolbar = (Toolbar) findViewById(R.id.toolbar);
        this.mToolbar.setTitle((CharSequence) "Test SMS Reader");
        setSupportActionBar(this.mToolbar);
        getSupportActionBar().setHomeAsUpIndicator(getResources().getDrawable(R.drawable.ic_action_back));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        this.dbhelper = new DBHelper(this);
        this.pDialog = new ProgressDialog(this.context);
    }

    public void loadView() {
        if (this.dbhelper.getWaterLevelData().size() > 0) {
            try {
                this.recyclerView.setAdapter(new TankAdapter(this.dbhelper.getWaterLevelData()));
                this.recyclerView.setLayoutManager(new LinearLayoutManager(this.context));
            } catch (Exception e) {
                Log.e("DB Error", e.toString());
                e.printStackTrace();
            }
        }
    }
}
