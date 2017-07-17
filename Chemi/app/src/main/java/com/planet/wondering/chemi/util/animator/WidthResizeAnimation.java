package com.planet.wondering.chemi.util.animator;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;

/**
 * Created by yoon on 2017. 7. 17..
 */

public class WidthResizeAnimation extends Animation {

    private View mView;
    private int mStartWidth;
    private int mTargetWidth;

    public WidthResizeAnimation(View view, int startWidth, int targetWidth) {
        mView = view;
        mStartWidth = startWidth;
        mTargetWidth = targetWidth;
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
//        super.applyTransformation(interpolatedTime, t);
        int newWidth = (int) (mStartWidth + mTargetWidth * interpolatedTime);
        mView.getLayoutParams().width = newWidth;
        mView.requestLayout();
    }

    @Override
    public void initialize(int width, int height, int parentWidth, int parentHeight) {
        super.initialize(width, height, parentWidth, parentHeight);
    }

    @Override
    public boolean willChangeBounds() {
//        return super.willChangeBounds();
        return true;
    }
}
