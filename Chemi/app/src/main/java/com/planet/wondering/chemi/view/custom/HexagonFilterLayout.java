package com.planet.wondering.chemi.view.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.Gravity;
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
        setWillNotDraw(false);
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        mRootView = inflate(context, R.layout.layout_hexagon_filter, this);
        setGravity(Gravity.CENTER_HORIZONTAL);
        setOrientation(VERTICAL);
        mHexagonFilterImageLayout = (RelativeLayout) mRootView.findViewById(R.id.hexagon_filter_image_layout);
        mHexagonFilterImageView = (ImageView) mRootView.findViewById(R.id.hexagon_filter_image_view);
        mHexagonFilterImageView.setImageResource(mHexagonImageSrc);
        mCountTextView = (TextView) mRootView.findViewById(R.id.count_text_view);
        mCountTextView.setText(String.valueOf(mHexagonCount));
        mCountTextView.setTypeface(Typeface.DEFAULT);
        mUnitTextView = (TextView) mRootView.findViewById(R.id.unit_text_view);
        mUnitTextView.setTypeface(Typeface.DEFAULT);
        mLabelTextView = (TextView) mRootView.findViewById(R.id.label_text_view);
        mLabelTextView.setText(mHexagonLabel);
        mLabelTextView.setTypeface(Typeface.DEFAULT);
//        invalidate();
    }

    public void scaleUpAnimate() {
        if (mScaleState) {
            return;
        }
        ScaleAnimation scale = new ScaleAnimation((float) 1.0, (float) 1.2, (float) 1.0, (float) 1.2,
                Animation.RELATIVE_TO_SELF, (float) 0.5, Animation.RELATIVE_TO_SELF, (float) 0.5);
        scale.setFillAfter(true);
        scale.setDuration(250);
        mHexagonFilterImageLayout.startAnimation(scale);
        mLabelTextView.setTextColor(Color.parseColor("#757575"));
        mLabelTextView.setTypeface(Typeface.DEFAULT_BOLD);
        mScaleState = true;
    }

    public void scaleDownAnimate() {
        if (!mScaleState) {
            return;
        }
        ScaleAnimation scale = new ScaleAnimation((float) 1.2, (float) 1.0, (float) 1.2, (float) 1.0,
                Animation.RELATIVE_TO_SELF, (float) 0.5, Animation.RELATIVE_TO_SELF, (float) 0.5);
        scale.setFillAfter(true);
        scale.setDuration(250);
        mHexagonFilterImageLayout.startAnimation(scale);
        mLabelTextView.setTextColor(Color.parseColor("#9b9b9b"));
        mLabelTextView.setTypeface(Typeface.DEFAULT);
        mScaleState = false;
    }

    public int getHexagonImageSrc() {
        return mHexagonImageSrc;
    }

    public void setHexagonImageSrc(int hexagonImageSrc) {
        mHexagonImageSrc = hexagonImageSrc;
        invalidate();
        requestLayout();
    }

    public int getHexagonCount() {
        return mHexagonCount;
    }

    public void setHexagonCount(int hexagonCount) {
        mHexagonCount = hexagonCount;
        invalidate();
        requestLayout();
    }

    public String getHexagonLabel() {
        return mHexagonLabel;
    }

    public void setHexagonLabel(String hexagonLabel) {
        mHexagonLabel = hexagonLabel;
        invalidate();
        requestLayout();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mCountTextView.setText(String.valueOf(mHexagonCount));
    }
}
