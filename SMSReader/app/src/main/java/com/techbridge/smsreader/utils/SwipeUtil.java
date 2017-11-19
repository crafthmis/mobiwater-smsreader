package com.techbridge.smsreader.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.support.v7.widget.helper.ItemTouchHelper.SimpleCallback;
import android.view.View;
import com.techbridge.smsreader.R;

public abstract class SwipeUtil extends SimpleCallback {
    private Drawable background;
    private Context context;
    private Drawable deleteIcon;
    private boolean initiated;
    private String leftSwipeLable;
    private int leftcolorCode;
    private int xMarkMargin;

    public abstract void onSwiped(ViewHolder viewHolder, int i);

    public SwipeUtil(int dragDirs, int swipeDirs, Context context) {
        super(dragDirs, swipeDirs);
        this.context = context;
    }

    private void init() {
        this.background = new ColorDrawable();
        this.xMarkMargin = (int) this.context.getResources().getDimension(R.dimen.ic_clear_margin);
        this.deleteIcon = ContextCompat.getDrawable(context, R.drawable.ic_delete_white_24dp);
        this.deleteIcon.setColorFilter(-1, Mode.SRC_ATOP);
        this.initiated = true;
    }

    public boolean onMove(RecyclerView recyclerView, ViewHolder viewHolder, ViewHolder target) {
        return false;
    }

    public int getSwipeDirs(RecyclerView recyclerView, ViewHolder viewHolder) {
        return super.getSwipeDirs(recyclerView, viewHolder);
    }

    public void onChildDraw(Canvas c, RecyclerView recyclerView, ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        View itemView = viewHolder.itemView;
        if (!this.initiated) {
            init();
        }
        int itemHeight = itemView.getBottom() - itemView.getTop();
        ((ColorDrawable) this.background).setColor(getLeftcolorCode());
        this.background.setBounds(itemView.getRight() + ((int) dX), itemView.getTop(), itemView.getRight(), itemView.getBottom());
        this.background.draw(c);
        int intrinsicWidth = this.deleteIcon.getIntrinsicWidth();
        int intrinsicHeight = this.deleteIcon.getIntrinsicWidth();
        int xMarkLeft = (itemView.getRight() - this.xMarkMargin) - intrinsicWidth;
        int xMarkTop = itemView.getTop() + ((itemHeight - intrinsicHeight) / 2);
        this.deleteIcon.setBounds(xMarkLeft, xMarkTop + 16, itemView.getRight() - this.xMarkMargin, xMarkTop + intrinsicHeight);
        this.deleteIcon.draw(c);
        Paint paint = new Paint();
        paint.setColor(-1);
        paint.setTextSize(48.0f);
        paint.setTextAlign(Align.CENTER);
        c.drawText(getLeftSwipeLable(), (float) (xMarkLeft + 40), (float) (xMarkTop + 10), paint);
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
    }

    public String getLeftSwipeLable() {
        return this.leftSwipeLable;
    }

    public void setLeftSwipeLable(String leftSwipeLable) {
        this.leftSwipeLable = leftSwipeLable;
    }

    public int getLeftcolorCode() {
        return this.leftcolorCode;
    }

    public void setLeftcolorCode(int leftcolorCode) {
        this.leftcolorCode = leftcolorCode;
    }
}
