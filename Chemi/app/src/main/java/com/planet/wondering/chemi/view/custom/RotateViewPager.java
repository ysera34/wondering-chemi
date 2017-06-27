package com.planet.wondering.chemi.view.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.planet.wondering.chemi.R;

import java.util.ArrayList;

/**
 * Created by yoon on 2017. 6. 22..
 */

public class RotateViewPager extends RelativeLayout {

    public RotateViewPager(Context context) {
        super(context);
        initializeView(context);
    }

    public RotateViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.RotateViewPager,
                0, 0
        );

        try {
            mTitleIconResId = a.getResourceId(R.styleable.RotateViewPager_titleIconSrc, 0);
            mTitleText = a.getString(R.styleable.RotateViewPager_titleText);
            mIndicatorType = a.getInt(R.styleable.RotateViewPager_indicatorType, -1);
        } finally {
            a.recycle();
        }
        initializeView(context);
    }

    private int mTitleIconResId;
    private String mTitleText;
    private int mIndicatorType;

    private View mRootView;
    protected ViewPager mRotateViewPager;
    private LinearLayout mTitleLayout;
    private ImageView mTitleIconImageView;
    private TextView mTitleTextView;
    private LinearLayout mIndicatorLayout;
    protected ArrayList<TextView> mIndicatorTextViews;

    protected static final long ROTATE_THRESHOLD_TIME = 5000;
    protected int mRotateViewPagerCurrentIndex;
    protected Handler mRotateHandler;
    protected Runnable mRotateRunnable;

    private void initializeView(Context context) {
        mRootView = inflate(context, R.layout.layout_rotate_view_pager, this);
        mRotateViewPager = (ViewPager) mRootView.findViewById(R.id.rotate_view_pager);
        mTitleLayout = (LinearLayout) mRootView.findViewById(R.id.title_layout);
        mTitleIconImageView = (ImageView) mRootView.findViewById(R.id.title_icon_image_view);
        mTitleTextView = (TextView) mRootView.findViewById(R.id.title_text_view);
        if (mTitleText != null) {
            mTitleTextView.setText(String.valueOf(mTitleText));
        } else {
            mTitleTextView.setVisibility(View.GONE);
        }
        mIndicatorLayout = (LinearLayout) mRootView.findViewById(R.id.indicator_layout);
    }

    protected void setViewPagerIndicator(int size) {
        mIndicatorTextViews = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            TextView indicatorTextView = new TextView(getContext());
            specifyIndicatorSize(indicatorTextView);

            if (i != size - 1) {
                LinearLayout.LayoutParams params =
                        (LinearLayout.LayoutParams) indicatorTextView.getLayoutParams();
                params.setMarginEnd(20);
                indicatorTextView.setLayoutParams(params);
            }
            mIndicatorLayout.addView(indicatorTextView);
            mIndicatorTextViews.add(indicatorTextView);
        }
        setIndicatorBackground(0);
        setIndicatorLayoutType(mIndicatorType);
    }

    private void specifyIndicatorSize(TextView indicatorTextView) {
        indicatorTextView.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        indicatorTextView.getLayoutParams().width =
                (int) getResources().getDimension(R.dimen.rotate_view_pager_indicator_width);
        indicatorTextView.getLayoutParams().height =
                (int) getResources().getDimension(R.dimen.rotate_view_pager_indicator_height);
    }

    protected void setIndicatorBackground(int position) {
        for (int i = 0; i < mIndicatorTextViews.size(); i++) {
            if (i == position) {
                mIndicatorTextViews.get(i)
                        .setBackgroundResource(R.drawable.widget_home_indicator_circle_true);
            } else {
                mIndicatorTextViews.get(i)
                        .setBackgroundResource(R.drawable.widget_home_indicator_circle_false);
            }
        }
    }

    private void setIndicatorLayoutType(int layoutType) {
        RelativeLayout.LayoutParams params =
                (RelativeLayout.LayoutParams) mIndicatorLayout.getLayoutParams();
        switch (layoutType) {
            case -1:
                break;
            case 1:
                params.addRule(RelativeLayout.ALIGN_PARENT_END);
                params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                params.setMargins(0, 30, 30, 0);
                mIndicatorLayout.setLayoutParams(params);
                break;
        }
    }

}
