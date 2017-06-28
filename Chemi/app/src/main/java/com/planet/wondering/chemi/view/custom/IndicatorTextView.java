package com.planet.wondering.chemi.view.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.planet.wondering.chemi.R;

/**
 * Created by yoon on 2017. 6. 28..
 */

public class IndicatorTextView extends LinearLayout {

    public IndicatorTextView(Context context) {
        super(context);
        initializeView();
    }

    public IndicatorTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initializeView();
    }

    private View mRootView;
    private TextView mCurrentPageTextView;
    private TextView mPageSizeTextView;
    private int mCurrentPage;
    private int mPageSize;

    private void initializeView() {
        mRootView = LayoutInflater.from(getContext()).inflate(R.layout.layout_indicator_text_view, this);
        mCurrentPageTextView = (TextView) mRootView.findViewById(R.id.current_page_text_view);
        mCurrentPageTextView.setText(String.valueOf(0));
        mPageSizeTextView = (TextView) mRootView.findViewById(R.id.page_size_text_view);
        mPageSizeTextView.setText(String.valueOf(0));
    }

    public int getCurrentPage() {
        return mCurrentPage;
    }

    public void setCurrentPage(int currentPage) {
        mCurrentPage = currentPage;
        mCurrentPageTextView.setText(String.valueOf(mCurrentPage));
    }

    public int getPageSize() {
        return mPageSize;
    }

    public void setPageSize(int pageSize) {
        mPageSize = pageSize;
        mPageSizeTextView.setText(String.valueOf(mPageSize));
    }
}
