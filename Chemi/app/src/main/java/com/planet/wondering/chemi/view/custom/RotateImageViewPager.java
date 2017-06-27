package com.planet.wondering.chemi.view.custom;

import android.content.Context;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.planet.wondering.chemi.R;

import java.util.ArrayList;

/**
 * Created by yoon on 2017. 6. 22..
 */

public class RotateImageViewPager extends RotateViewPager {

    public RotateImageViewPager(Context context) {
        super(context);
    }

    public RotateImageViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private RotateImageViewPagerAdapter mRotateImageViewPagerAdapter;
    private RotateImageViewPagerChangeListener mRotateImageViewPagerChangeListener;

    public void setRotateViewPagerAdapter(ArrayList<String> imagePaths) {
        if (mRotateImageViewPagerAdapter == null) {
            mRotateImageViewPagerAdapter = new RotateImageViewPagerAdapter(imagePaths);
        }
        mRotateViewPager.setAdapter(mRotateImageViewPagerAdapter);
        mRotateViewPager.setCurrentItem(imagePaths.size() * 1000);
        setViewPagerIndicator(imagePaths.size());
    }

    ItemClickListener mItemClickListener;

    public interface ItemClickListener {
        void onClickListener();
    }

    public void setOnItemClickListener() {
        mItemClickListener.onClickListener();
    }

    public void addOnPageChangeListener() {
        if (mRotateImageViewPagerChangeListener == null) {
            mRotateImageViewPagerChangeListener = new RotateImageViewPagerChangeListener();
        }
        mRotateViewPager.addOnPageChangeListener(mRotateImageViewPagerChangeListener);
    }

    public void removeOnPageChangeListener() {
        if (mRotateImageViewPagerChangeListener != null) {
            mRotateViewPager.removeOnPageChangeListener(mRotateImageViewPagerChangeListener);
        }
    }

    public void startRotateViewPager() {
        mRotateHandler = new Handler();
        mRotateRunnable = new Runnable() {
            @Override
            public void run() {
                mRotateViewPager.setCurrentItem(mRotateViewPagerCurrentIndex++, true);
                mRotateHandler.postDelayed(mRotateRunnable, ROTATE_THRESHOLD_TIME);
            }
        };
        mRotateHandler.postDelayed(mRotateRunnable, ROTATE_THRESHOLD_TIME);
    }

    public void stopRotateViewPager() {
        if (mRotateHandler != null) {
            mRotateHandler.removeCallbacks(mRotateRunnable);
            mRotateHandler = null;
        }
    }

    private class RotateImageViewPagerAdapter extends PagerAdapter {

        private ArrayList<String> mImagePaths;
        private LayoutInflater mLayoutInflater;

        public RotateImageViewPagerAdapter(ArrayList<String> imagePaths) {
            mImagePaths = imagePaths;
            mLayoutInflater = LayoutInflater.from(getContext());
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            int realPosition = position % mImagePaths.size();
            View view = mLayoutInflater.inflate(R.layout.layout_rotate_image_view, container, false);
            ImageView imageView = (ImageView) view;
            imageView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    setOnItemClickListener();
                }
            });

            Glide.with(getContext())
                    .load(mImagePaths.get(position))
                    .into(imageView);
            container.addView(imageView);
            return view;
        }

        @Override
        public int getCount() {
            return Integer.MAX_VALUE;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((LinearLayout) object);
        }
    }

    private class RotateImageViewPagerChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            int realPosition = position % mIndicatorTextViews.size();
            setIndicatorBackground(realPosition);
            mRotateViewPagerCurrentIndex = position;
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }
}
