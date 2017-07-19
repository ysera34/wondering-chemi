package com.planet.wondering.chemi.view.custom;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Handler;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.planet.wondering.chemi.R;
import com.planet.wondering.chemi.util.animator.WidthResizeAnimation;

/**
 * Created by yoon on 2017. 7. 17..
 */

public class BarGraphView extends LinearLayout {

    public BarGraphView(Context context, int barNameResId,
                        int barGraphValue, int barGraphTargetWidth, int barColorResId) {
        super(context);
        mContext = context;
        mBarNameResId = barNameResId;
        mBarGraphValue = barGraphValue;
        mBarGraphTargetWidth = barGraphTargetWidth;
        mBarColorResId = barColorResId;
        initializeView();
    }

    public BarGraphView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initializeView();
    }

    private Context mContext;
    private LinearLayout.LayoutParams mLayoutParams;
    private TextView mBarNameTextView;
    private View mBarGraphView;
    private TextView mCountTextView;
    private WidthResizeAnimation mWidthResizeAnimation;
    private int mBarNameResId;
    private int mBarGraphValue;
    private int mBarGraphTargetWidth;
    private int mBarColorResId;

    private void initializeView() {
        setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getPixelFromDp(30)));
        setOrientation(HORIZONTAL);
        setBaselineAligned(false);

        mBarNameTextView = new TextView(mContext);
        mBarGraphView = new View(mContext);
        mCountTextView = new TextView(mContext);
        mWidthResizeAnimation = new WidthResizeAnimation(mBarGraphView, 0, mBarGraphTargetWidth);
        mWidthResizeAnimation.setDuration(1000);

        mBarNameTextView.setLayoutParams(new LayoutParams(getPixelFromDp(35), ViewGroup.LayoutParams.MATCH_PARENT));
        mBarNameTextView.setMaxLines(2);
        mBarNameTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
        mBarNameTextView.setText(getResources().getString(mBarNameResId));
        mBarNameTextView.setGravity(Gravity.CENTER | Gravity.START);
        mBarNameTextView.setTextColor(getResources().getColor(mBarColorResId));
        mBarNameTextView.setTypeface(Typeface.DEFAULT);
        addView(mBarNameTextView);
        mLayoutParams = (LinearLayout.LayoutParams) mBarNameTextView.getLayoutParams();
        mLayoutParams.setMargins(0, 0, getPixelFromDp(4), 0);

        mBarGraphView.setLayoutParams(new LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT));
//        mLayoutParams = (LinearLayout.LayoutParams) mBarGraphView.getLayoutParams();
//        mLayoutParams.gravity = Gravity.CENTER_VERTICAL;
        mBarGraphView.setBackgroundColor(getResources().getColor(mBarColorResId));
        addView(mBarGraphView);

        mCountTextView.setLayoutParams(new LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mCountTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
        mCountTextView.setGravity(Gravity.CENTER);
        addView(mCountTextView);
        mLayoutParams = (LinearLayout.LayoutParams) mCountTextView.getLayoutParams();
        mLayoutParams.setMargins(getPixelFromDp(4), 0, 0, 0);

        if (mBarGraphValue > 0) {
            animateTextView(0, mBarGraphValue, mCountTextView);
        } else if (mBarGraphValue == 0) {
            mWidthResizeAnimation.setTargetWidth((int) (1 * getResources().getDisplayMetrics().density));
            mCountTextView.setText(highlightText(mBarGraphValue, mBarColorResId));
        }
        mBarGraphView.startAnimation(mWidthResizeAnimation);
    }

    private void animateTextView(int initialValue, final int finalValue, final TextView textView) {
        DecelerateInterpolator decelerateInterpolator = new DecelerateInterpolator(0.8f);
        int start = Math.min(initialValue, finalValue);
        int end = Math.max(initialValue, finalValue);
        int difference = Math.abs(finalValue - initialValue);
        Handler handler = new Handler();
        for (int i = start; i <= end; i++) {
//            int time = Math.round(decelerateInterpolator.getInterpolation((((float) i) / difference)) * 50) * i;
            int time = (1000 / finalValue) * i;
            final int finalCount = ((initialValue > finalValue) ? initialValue - i : i);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    textView.setText(highlightText(finalCount, mBarColorResId));
                    textView.setTypeface(Typeface.DEFAULT);
                }
            }, time);
        }
    }

    private SpannableString highlightText(int count, int colorResId) {
        SpannableString spannableString = new SpannableString(
                getContext().getString(R.string.total_count_format, String.valueOf(count)));
        int index = String.valueOf(count).length();
        spannableString.setSpan(new ForegroundColorSpan(getResources().getColor(colorResId)),
                0, index, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new RelativeSizeSpan(1.5f),
                0, index, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new StyleSpan(Typeface.NORMAL),
                0, index + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableString;
    }

    private int getPixelFromDp(int dp) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }
}
