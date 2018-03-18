package com.techbridge.smsreader.views.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import com.techbridge.smsreader.R;
import com.techbridge.smsreader.db.DBHelper;
import com.techbridge.smsreader.utils.SwipeUtil;
import com.techbridge.smsreader.views.adapters.CtrlsettingAdapter;
import com.techbridge.smsreader.views.adapters.SettingAdapter;

public class CtrlsettingsActivity extends BaseActivity implements OnClickListener {
    private Context context = CtrlsettingsActivity.this;
    private DBHelper dbhelper;
    private Toolbar mToolbar;
    private FloatingActionButton myFab;
    private Paint f61p = new Paint();
    private RecyclerView recyclerView;
    private View view;

    class C02971 implements OnClickListener {
        C02971() {
        }

        public void onClick(View view) {
            startActivity(new Intent(context, DashboardActivity.class));
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_main);
        initWidgets();
        loadView();
    }

    private void initWidgets() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle((CharSequence) "Controllers");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setHomeAsUpIndicator(getResources().getDrawable(R.drawable.ic_action_back));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(new C02971());
        myFab = (FloatingActionButton) findViewById(R.id.fab);
        myFab.setVisibility(View.INVISIBLE);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        dbhelper = new DBHelper(this);
    }

    public void loadView() {
        if (dbhelper.getSettingsData().size() > 0) {
            try {
                recyclerView.setAdapter(new CtrlsettingAdapter(dbhelper.getSettingsData(), context));
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } catch (Exception e) {
                Log.e("DB Error", e.toString());
                e.printStackTrace();
            }
        }
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab:
                startActivity(new Intent(context, AddsettingActivity.class));
                return;
            default:
                return;
        }
    }

    private void setSwipeForRecyclerView() {
        SwipeUtil swipeHelper = new SwipeUtil(0, 4, context) {
            public void onSwiped(ViewHolder viewHolder, int direction) {
                ((SettingAdapter)recyclerView.getAdapter()).pendingRemoval(viewHolder.getAdapterPosition());
            }

            public int getSwipeDirs(RecyclerView recyclerView, ViewHolder viewHolder) {
                if (((SettingAdapter)recyclerView.getAdapter()).isPendingRemoval(viewHolder.getAdapterPosition())) {
                    return 0;
                }
                return super.getSwipeDirs(recyclerView, viewHolder);
            }
        };
        new ItemTouchHelper(swipeHelper).attachToRecyclerView(recyclerView);
        swipeHelper.setLeftSwipeLable("");
        swipeHelper.setLeftcolorCode(ContextCompat.getColor(context, R.color.colorRed));
    }

    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(context, DashboardActivity.class));
    }
}
