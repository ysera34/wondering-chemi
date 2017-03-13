package com.planet.wondering.chemi.view.custom;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by yoon on 2017. 3. 13..
 */

public class SwipeableViewPager extends ViewPager {

    private boolean isSwipeable;

    public SwipeableViewPager(Context context) {
        super(context);
    }

    public SwipeableViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public boolean isSwipeable() {
        return isSwipeable;
    }

    public void setSwipeable(boolean swipeable) {
        isSwipeable = swipeable;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return isSwipeable && super.onTouchEvent(event);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        return isSwipeable && super.onInterceptTouchEvent(event);
    }

    @Override
    public boolean canScrollHorizontally(int direction) {
        return isSwipeable && super.canScrollHorizontally(direction);
    }

}
