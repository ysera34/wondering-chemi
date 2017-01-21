package com.planet.wondering.chemi.view.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.planet.wondering.chemi.R;

/**
 * Created by yoon on 2017. 1. 21..
 */

public class HexagonFilterLayout extends LinearLayout {

    public HexagonFilterLayout(Context context) {
        super(context);
        init(context);
    }

    public HexagonFilterLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.HexagonFilterLayout,
                0,0
        );

        try {
            mHexagonImageSrc = a.getResourceId(R.styleable.HexagonFilterLayout_hexagonImageSrc, 0);
            mHexagonCount = a.getInt(R.styleable.HexagonFilterLayout_hexagonCount, 0);
            mHexagonLabel = a.getString(R.styleable.HexagonFilterLayout_hexagonLabel);
        } finally {
            a.recycle();
        }

        init(context);
    }

    private View mRootView;
    private RelativeLayout mHexagonFilterImageLayout;
    private ImageView mHexagonFilterImageView;
    private TextView mCountTextView;
    private TextView mUnitTextView;
    private TextView mLabelTextView;

    private int mHexagonImageSrc;
    private int mHexagonCount;
    private String mHexagonLabel;

    private boolean mScaleState = false;

    private void init(Context context) {
        mRootView = inflate(context, R.layout.layout_hexagon_filter, this);
        mHexagonFilterImageLayout = (RelativeLayout) mRootView.findViewById(R.id.hexagon_filter_image_layout);
        mHexagonFilterImageView = (ImageView) mRootView.findViewById(R.id.hexagon_filter_image_view);
        mHexagonFilterImageView.setImageResource(mHexagonImageSrc);
        mCountTextView = (TextView) mRootView.findViewById(R.id.count_text_view);
        mCountTextView.setText(String.valueOf(mHexagonCount));
        mUnitTextView = (TextView) mRootView.findViewById(R.id.unit_text_view);
        mLabelTextView = (TextView) mRootView.findViewById(R.id.label_text_view);
        mLabelTextView.setText(mHexagonLabel);
        invalidate();
    }

    public void scaleUpAnimate() {
        if (mScaleState) {
            return;
        }
        ScaleAnimation scale = new ScaleAnimation((float) 1.0, (float) 1.2, (float) 1.0, (float) 1.2,
                Animation.RELATIVE_TO_SELF, (float) 0.5, Animation.RELATIVE_TO_SELF, (float) 0.5);
        scale.setFillAfter(true);
        scale.setDuration(500);
        mHexagonFilterImageLayout.startAnimation(scale);
        mScaleState = true;
    }

    public void scaleDownAnimate() {
        if (!mScaleState) {
            return;
        }
        ScaleAnimation scale = new ScaleAnimation((float) 1.2, (float) 1.0, (float) 1.2, (float) 1.0,
                Animation.RELATIVE_TO_SELF, (float) 0.5, Animation.RELATIVE_TO_SELF, (float) 0.5);
        scale.setFillAfter(true);
        scale.setDuration(500);
        mHexagonFilterImageLayout.startAnimation(scale);
        mScaleState = false;
    }

    public int getHexagonImageSrc() {
        return mHexagonImageSrc;
    }

    public void setHexagonImageSrc(int hexagonImageSrc) {
        mHexagonImageSrc = hexagonImageSrc;
        invalidate();
    }

    public int getHexagonCount() {
        return mHexagonCount;
    }

    public void setHexagonCount(int hexagonCount) {
        mHexagonCount = hexagonCount;
        invalidate();
    }

    public String getHexagonLabel() {
        return mHexagonLabel;
    }

    public void setHexagonLabel(String hexagonLabel) {
        mHexagonLabel = hexagonLabel;
        invalidate();
    }
}
