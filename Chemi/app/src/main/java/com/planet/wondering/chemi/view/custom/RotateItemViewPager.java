package com.planet.wondering.chemi.view.custom;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by yoon on 2017. 6. 22..
 */

public class RotateItemViewPager extends RotateViewPager {

    public RotateItemViewPager(Context context) {
        super(context);
    }

    public RotateItemViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private RotateItemInfiniteViewPagerAdapter mRotateItemInfiniteViewPagerAdapter;
    private RotateItemInfiniteViewPagerChangeListener mRotateItemInfiniteViewPagerChangeListener;

    private class RotateItemInfiniteViewPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return 0;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return false;
        }
    }

    private class RotateItemInfiniteViewPagerChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

}
