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
import com.techbridge.smsreader.views.adapters.SettingAdapter;

public class SettingActivity extends BaseActivity implements OnClickListener {
    private Context context = this;
    private DBHelper dbhelper;
    private Toolbar mToolbar;
    private FloatingActionButton myFab;
    private Paint f67p = new Paint();
    private RecyclerView recyclerView;
    private View view;

    class C03211 implements OnClickListener {
        C03211() {
        }

        public void onClick(View view) {
            SettingActivity.this.startActivity(new Intent(SettingActivity.this.context, DashboardActivity.class));
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_main);
        initWidgets();
        loadView();
        setSwipeForRecyclerView();
    }

    private void initWidgets() {
        this.mToolbar = (Toolbar) findViewById(R.id.toolbar);
        this.mToolbar.setTitle((CharSequence) "Tanks");
        setSupportActionBar(this.mToolbar);
        getSupportActionBar().setHomeAsUpIndicator(getResources().getDrawable(R.drawable.ic_action_back));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.mToolbar.setNavigationOnClickListener(new C03211());
        this.myFab = (FloatingActionButton) findViewById(R.id.fab);
        this.recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        this.dbhelper = new DBHelper(this);
        this.myFab.setOnClickListener(this);
    }

    public void loadView() {
        if (this.dbhelper.getSettingsData().size() > 0) {
            try {
                this.recyclerView.setAdapter(new SettingAdapter(this.dbhelper.getSettingsData(), this.context));
                this.recyclerView.setLayoutManager(new LinearLayoutManager(this.context));
            } catch (Exception e) {
                Log.e("DB Error", e.toString());
                e.printStackTrace();
            }
        }
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab:
                startActivity(new Intent(this.context, AddsettingActivity.class));
                return;
            default:
                return;
        }
    }

    private void setSwipeForRecyclerView() {
        SwipeUtil swipeHelper = new SwipeUtil(0, 4, this.context) {
            public void onSwiped(ViewHolder viewHolder, int direction) {
                ((SettingAdapter) SettingActivity.this.recyclerView.getAdapter()).pendingRemoval(viewHolder.getAdapterPosition());
            }

            public int getSwipeDirs(RecyclerView recyclerView, ViewHolder viewHolder) {
                if (((SettingAdapter) recyclerView.getAdapter()).isPendingRemoval(viewHolder.getAdapterPosition())) {
                    return 0;
                }
                return super.getSwipeDirs(recyclerView, viewHolder);
            }
        };
        new ItemTouchHelper(swipeHelper).attachToRecyclerView(this.recyclerView);
        swipeHelper.setLeftSwipeLable("");
        swipeHelper.setLeftcolorCode(ContextCompat.getColor(this.context, R.color.colorRed));
    }

    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this.context, DashboardActivity.class));
    }
}
