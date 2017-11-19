package com.techbridge.smsreader.views.adapters;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.techbridge.smsreader.R;
import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder;

public class MonthParentViewHolder extends GroupViewHolder {
    private static final float INITIAL_POSITION = 0.0f;
    private static final float ROTATED_POSITION = 90.0f;
    public ImageView btnDownload;
    public boolean collapsed = true;
    public TextView mMonthName;
    public ImageView mParentDropDownArrow;

    public MonthParentViewHolder(View itemView) {
        super(itemView);
        this.mMonthName = (TextView) itemView.findViewById(R.id.month_name);
        this.mParentDropDownArrow = (ImageView) itemView.findViewById(R.id.dropdown_arrow);
        this.btnDownload = (ImageView) itemView.findViewById(R.id.btnDownload);
        if (this.collapsed) {
            this.mParentDropDownArrow.setRotation(0.0f);
        } else {
            this.mParentDropDownArrow.setRotation(ROTATED_POSITION);
        }
    }

    public void expand() {
        animateExpand();
        this.collapsed = false;
    }

    public void collapse() {
        animateCollapse();
        this.collapsed = true;
    }

    private void animateExpand() {
        this.mParentDropDownArrow.setRotation(ROTATED_POSITION);
    }

    private void animateCollapse() {
        this.mParentDropDownArrow.setRotation(0.0f);
    }
}
