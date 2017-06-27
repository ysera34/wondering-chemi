package com.planet.wondering.chemi.view.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

/**
 * Created by yoon on 2017. 6. 22..
 */

public class HomeScrollView extends ScrollView {

    public interface OnScrollChangedListener {
        void onScrollChanged(HomeScrollView view, int l, int t);
    }

    private OnScrollChangedListener mScrollChangedListener;

    public HomeScrollView(Context context) {
        super(context);
    }

    public HomeScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HomeScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setScrollChangedListener(OnScrollChangedListener scrollChangedListener) {
        mScrollChangedListener = scrollChangedListener;
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (mScrollChangedListener != null) {
            mScrollChangedListener.onScrollChanged(this, l, t);
        }
    }
}
