package com.techbridge.smsreader.views.adapters;

import android.view.View;
import android.widget.TextView;
import com.techbridge.smsreader.R;
import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder;

public class MonthChildViewHolder extends ChildViewHolder {
    public TextView mDayLabel;
    public TextView mDayReading;
    public TextView mTimeLabel;

    public MonthChildViewHolder(View itemView) {
        super(itemView);
        this.mDayLabel = (TextView) itemView.findViewById(R.id.day_label);
        this.mTimeLabel = (TextView) itemView.findViewById(R.id.time_label);
        this.mDayReading = (TextView) itemView.findViewById(R.id.reading_label);
    }

    public void setDayLabel(String name) {
        this.mDayLabel.setText(name);
    }

    public void setTimeLabel(String name) {
        this.mDayReading.setText(name);
    }

    public void setDayReading(String name) {
        this.mDayReading.setText(name);
    }
}
