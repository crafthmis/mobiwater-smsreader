package com.techbridge.smsreader.views.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import com.techbridge.smsreader.R;
import com.techbridge.smsreader.db.DBHelper;
import com.techbridge.smsreader.models.Month;
import com.techbridge.smsreader.models.MonthDetails;
import com.techbridge.smsreader.utils.Prefs;
import com.techbridge.smsreader.utils.Utils;
import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;
import java.util.ArrayList;
import java.util.List;

public class MonthAdapter extends ExpandableRecyclerViewAdapter<MonthParentViewHolder, MonthChildViewHolder> {
    public static final String PREF_FILE_NAME = "testPref";
    private static final String TAG = "LOGD";
    private DBHelper dbHelper;
    private Context mContext;
    private LayoutInflater mInflator;

    public MonthAdapter(Context context, List<Month> groups) {
        super(groups);
        this.mContext = context;
        this.mInflator = LayoutInflater.from(context);
        this.dbHelper = new DBHelper(context);
    }

    public MonthParentViewHolder onCreateGroupViewHolder(ViewGroup parentViewGroup, int viewType) {
        return new MonthParentViewHolder(this.mInflator.inflate(R.layout.month_list_parent_item, parentViewGroup, false));
    }

    public MonthChildViewHolder onCreateChildViewHolder(ViewGroup childViewGroup, int viewType) {
        return new MonthChildViewHolder(this.mInflator.inflate(R.layout.month_list_child_item, childViewGroup, false));
    }

    public void onBindGroupViewHolder(MonthParentViewHolder parentViewHolder, final int position, ExpandableGroup group) {
        parentViewHolder.mMonthName.setText(group.getTitle());
        parentViewHolder.btnDownload.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                dbHelper.exportDB(Prefs.readString(MonthAdapter.this.mContext, "tankUniqueId", ""), position, 2017);
                Utils.showShortToast(MonthAdapter.this.mContext, "Download Complete");
            }
        });
    }

    public void onBindChildViewHolder(MonthChildViewHolder childViewHolder, int position, ExpandableGroup group, int childIndex) {
        MonthDetails mDetails = (MonthDetails) ((Month) group).getItems().get(childIndex);
        childViewHolder.mDayLabel.setText(mDetails.getDayString());
        childViewHolder.mTimeLabel.setText(mDetails.getTimeString());
        childViewHolder.mDayReading.setText(mDetails.getDayreading());
    }
}
