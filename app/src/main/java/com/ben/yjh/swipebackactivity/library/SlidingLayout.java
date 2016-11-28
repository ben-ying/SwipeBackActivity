package com.ben.yjh.swipebackactivity.library;

import android.content.Context;
import android.graphics.Point;
import android.support.v4.widget.SlidingPaneLayout;
import android.util.AttributeSet;
import android.view.Display;
import android.view.MotionEvent;
import android.view.WindowManager;

class SlidingLayout extends SlidingPaneLayout {

    private static final int MIN_SWIPE_SIZE = 30;

    private int mSwipeSize = 60;
    private int mScreenWidth;
    private boolean mSlideEnable = true;

    public int getSwipeSize() {
        return mSwipeSize;
    }

    public void setSwipeSize(int dp) {
        if (dp >= 0 && dp < MIN_SWIPE_SIZE) {
            this.mSwipeSize = MIN_SWIPE_SIZE;
        } else if (dp < 0) {
            this.mSwipeSize = mScreenWidth;
        } else {
            this.mSwipeSize = dp;
        }
    }

    public void setSlide(boolean slide) {
        this.mSlideEnable = slide;
    }

    public SlidingLayout(Context context) {
        super(context);
        init(context, null, 0);
    }

    public SlidingLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public SlidingLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs, defStyle);
    }

    private void init(Context context, AttributeSet attrs, int defStyle) {
        if (isInEditMode()) return;

        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        mScreenWidth = size.x;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return mSlideEnable && ev.getX() < dip2px(mSwipeSize) && super.onInterceptTouchEvent(ev);
    }

    public int dip2px(float dpValue) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
