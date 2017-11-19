package com.techbridge.smsreader.utils.wheelpicker;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

class AnimationView extends View {
    private static final float RADIUS = 20.0f;
    private Paint backgroundPaint = new Paint();
    private float initialX;
    private float initialY;
    private Paint myPaint;
    private float offsetX;
    private float offsetY;
    private float f34x = 30.0f;
    private float f35y = 30.0f;

    public AnimationView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.backgroundPaint.setColor(-16776961);
        this.myPaint = new Paint();
        this.myPaint.setColor(-1);
        this.myPaint.setAntiAlias(true);
    }

    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case 0:
                this.initialX = this.f34x;
                this.initialY = this.f35y;
                this.offsetX = event.getX();
                this.offsetY = event.getY();
                break;
            case 1:
            case 2:
            case 3:
                this.f34x = (this.initialX + event.getX()) - this.offsetX;
                this.f35y = (this.initialY + event.getY()) - this.offsetY;
                break;
        }
        return true;
    }

    public void draw(Canvas canvas) {
        Canvas canvas2 = canvas;
        float f = 0.0f;
        canvas2.drawRect(0.0f, f, (float) canvas.getWidth(), (float) canvas.getHeight(), this.backgroundPaint);
        canvas.drawCircle(this.f34x, this.f35y, RADIUS, this.myPaint);
        invalidate();
    }
}
