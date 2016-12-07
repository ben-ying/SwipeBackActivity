package com.ben.yjh.swipebackactivity.library;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.SlidingPaneLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.ben.yjh.swipebackactivity.R;
import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Field;

public class SwipeBackActivity extends AppCompatActivity
        implements SlidingPaneLayout.PanelSlideListener {

    private static final String SCREENSHOT = "screenshot.jpg";

    private File mImageFile;
    private SlidingLayout mSlidingLayout;
    private FrameLayout mFrameLayout;
    private ImageView mBackImageView;
    private ImageView mShadowImageView;
    private int mTranslationX;
    private int mShadowWidth;
    private int mShadowRes = R.drawable.shadow;
    private boolean mSlideEnable = true;
    private int mWidth;
    private int mHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point(); display.getSize(size);
        mWidth = size.x;
        mHeight = size.y;
        mSlidingLayout = new SlidingLayout(this);
        mSlidingLayout.setPanelSlideListener(this);
        mSlidingLayout.setSliderFadeColor(getResources().getColor(android.R.color.transparent));
        try {
            Field overHang = SlidingPaneLayout.class.getDeclaredField("mOverhangSize");
            overHang.setAccessible(true);
            overHang.set(mSlidingLayout, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mImageFile = new File(getCacheDir(), SCREENSHOT);
        mShadowWidth = dip2px(mShadowWidth);

        mBackImageView = new ImageView(this);
        mBackImageView.setLayoutParams(new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT, mHeight));

        LinearLayout containerLayout = new LinearLayout(this);
        containerLayout.setOrientation(LinearLayout.HORIZONTAL);
        containerLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        containerLayout.setLayoutParams(new ViewGroup.LayoutParams(
                getWindowManager().getDefaultDisplay().getWidth() +
                        mShadowWidth, ViewGroup.LayoutParams.MATCH_PARENT));
        mFrameLayout = new FrameLayout(this);
        mFrameLayout.setBackgroundColor(getResources().getColor(android.R.color.white));
        mFrameLayout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));

        mShadowImageView = new ImageView(this);
        mShadowImageView.setBackgroundResource(mShadowRes);
        mShadowImageView.setLayoutParams(new LinearLayout.LayoutParams(
                mShadowWidth, LinearLayout.LayoutParams.MATCH_PARENT));
        containerLayout.addView(mShadowImageView);
        containerLayout.addView(mFrameLayout);
        containerLayout.setTranslationX(-mShadowWidth);
        mSlidingLayout.addView(mBackImageView);
        mSlidingLayout.addView(containerLayout);
    }

    // override before onCreate()
    public void setShadowRes(int res) {
        this.mShadowRes = res;
    }

    public int getShadowRes() {
        return mShadowRes <= 0 ? R.drawable.shadow : mShadowRes;
    }

    // override before onCreate()
    public void setShadowWidth(int dp) {
        this.mShadowWidth = dip2px(dp);
    }

    public float getShadowWidth() {
        return mShadowWidth <= 0 ? 30 : mShadowWidth;
    }

    public void setTranslationX(int dp) {
        this.mTranslationX = dip2px(dp);
    }

    public float getTranslationX() {
        return mTranslationX <= 0 ? dip2px(100) : mTranslationX;
    }

    public void setSwipeSize(int dp) {
        mSlidingLayout.setSwipeSize(dp);
    }

    public void setEnableSwipe(boolean enable) {
        mSlideEnable = enable;
        mSlidingLayout.setSlide(enable);
    }

    @Override
    public void setContentView(int id) {
        setContentView(getLayoutInflater().inflate(id, null));
    }

    @Override
    public void setContentView(View v) {
        setContentView(v, new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        try {
            mBackImageView.setScaleType(ImageView.ScaleType.FIT_XY);
            mBackImageView.setImageBitmap(getBitmap());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void setContentView(View v, ViewGroup.LayoutParams params) {
        super.setContentView(v, params);
        mFrameLayout.removeAllViews();

        ViewGroup parentView = (ViewGroup) getWindow().getDecorView();
        ViewGroup viewIncludingAction = (ViewGroup) parentView.getChildAt(0);
        parentView.removeView(viewIncludingAction);
        parentView.addView(mSlidingLayout);
        mFrameLayout.addView(viewIncludingAction, params);

        v.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getActionMasked();

                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        if (!mSlideEnable || event.getX() > dip2px(mSlidingLayout.getSwipeSize())) {
                            return true;
                        }
                        break;
                }
                return false;
            }
        });
    }

    @Override
    public void onPanelClosed(View view) {
        // panel closed
    }

    @Override
    public void onPanelOpened(View view) {
        finish();
        // remove default animation
        overridePendingTransition(0, 0);
    }

    @Override
    public void onPanelSlide(View view, float v) {
        if (mSlideEnable) {
            // add swipe effects
            mBackImageView.setTranslationX(v * mTranslationX - mTranslationX);
            float value = 1.5f - v;
            if (value > 1) {
                value = 1;
            }
            mShadowImageView.setAlpha(v < 0.8 ? 1 : value);
        }
    }

    public void startActivity(Intent intent, boolean isFullscreen) {
        screenshots(this, isFullscreen);
        super.startActivity(intent);
    }

    public Bitmap getBitmap() {
        return BitmapFactory.decodeFile(mImageFile.getAbsolutePath());
    }

    public void screenshots(final Activity activity, final boolean isFullScreen) {
        Bitmap b1 = null;
        Bitmap bitmap = null;
        try {
            View decorView = activity.getWindow().getDecorView();
            if (activity.getParent() != null &&
                    activity.getParent().getWindow()
                            .getDecorView().getHeight() > decorView.getHeight()) {
                decorView = activity.getParent().getWindow().getDecorView();
            }
            decorView.setDrawingCacheEnabled(true);
            decorView.buildDrawingCache();
            b1 = decorView.getDrawingCache();
            Rect frame = new Rect();
            activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
            int statusBarHeight = frame.top;

            try {
                if (isFullScreen) {
                    bitmap = Bitmap.createBitmap(b1, 0, 0, mWidth, mHeight);
                } else {
                    bitmap = Bitmap.createBitmap(b1, 0,
                            statusBarHeight, mWidth, mHeight - statusBarHeight);
                }
                decorView.destroyDrawingCache();
                FileOutputStream out = new FileOutputStream(mImageFile);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);

            } catch (OutOfMemoryError e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (b1 != null) {
                b1.recycle();
            }
            if (bitmap != null) {
                bitmap.recycle();
            }
        }
    }

    public int dip2px(float dpValue) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}