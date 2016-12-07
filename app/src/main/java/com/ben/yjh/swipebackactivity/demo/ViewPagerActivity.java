package com.ben.yjh.swipebackactivity.demo;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ben.yjh.swipebackactivity.R;
import com.ben.yjh.swipebackactivity.library.SwipeBackActivity;

public class ViewPagerActivity extends SwipeBackActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setShadowRes(R.drawable.shadow);
        setShadowWidth(10);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pager);

        setTitle(getClass().getSimpleName());
        setTranslationX(300);
        setSwipeSize(60);
        ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return new ScreenSlidePageFragment().newInstance(position);
            }

            @Override
            public int getCount() {
                return 4;
            }
        });
    }
}
