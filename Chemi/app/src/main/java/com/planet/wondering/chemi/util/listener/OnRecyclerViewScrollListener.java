package com.planet.wondering.chemi.util.listener;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * Created by yoon on 2017. 1. 18..
 */

public abstract class OnRecyclerViewScrollListener extends RecyclerView.OnScrollListener {

    private static final int REACTION_THRESHOLD = 200;

    private boolean mControlsVisible = true;
    private int mScrolledDistance;

    public abstract void onShowView();
    public abstract void onHideView();

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        int firstVisibleItem = ((LinearLayoutManager)
                recyclerView.getLayoutManager()).findFirstVisibleItemPosition();

        if (firstVisibleItem == 0) {
            if (!mControlsVisible) {
                onShowView();
                mControlsVisible = true;
            }
        } else {
            if (mScrolledDistance > REACTION_THRESHOLD && mControlsVisible) {
                onHideView();
                mControlsVisible = false;
                mScrolledDistance = 0;
            } else if (mScrolledDistance < -REACTION_THRESHOLD && !mControlsVisible) {
                onShowView();
                mControlsVisible = true;
                mScrolledDistance = 0;
            }
        }
        if ((mControlsVisible && dy > 0) || (!mControlsVisible && dy < 0)) {
            mScrolledDistance += dy;
        }
    }
}
