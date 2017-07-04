package com.planet.wondering.chemi.view.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.planet.wondering.chemi.R;

/**
 * Created by yoon on 2017. 6. 30..
 */

public class CategoryGroupView extends LinearLayout {

    public CategoryGroupView(Context context) {
        super(context);
        initializeView(context);
    }

    public CategoryGroupView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.CategoryGroupView,
                0, 0
        );

        try {
            mCategoryResId = a.getResourceId(R.styleable.CategoryGroupView_iconResId, 0);
            mCategoryName = a.getString(R.styleable.CategoryGroupView_name);
        } finally {
            a.recycle();
        }
        initializeView(context);
    }

    public CategoryGroupView(Context context, AttributeSet attrs, int categoryResId, String categoryName) {
        super(context, attrs);
        mCategoryResId = categoryResId;
        mCategoryName = categoryName;
        initializeView(context);
    }

    private int mCategoryResId;
    private String mCategoryName;

    private LinearLayout mCategoryGroupLayout;
    private ImageView mCategoryIconImageView;
    private TextView mCategoryNameTextView;

    private void initializeView(Context context) {

        setOrientation(VERTICAL);
        setGravity(Gravity.CENTER);
        int paddingDp = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 4, getResources().getDisplayMetrics());
        setPadding(paddingDp, 0, paddingDp, 0);
        mCategoryGroupLayout = (LinearLayout) inflate(context, R.layout.layout_category_group_view, this);
        mCategoryIconImageView = (ImageView) mCategoryGroupLayout.findViewById(R.id.category_group_icon_image_view);
        mCategoryNameTextView = (TextView) mCategoryGroupLayout.findViewById(R.id.category_group_name_text_view);
        bindCategory();
    }

    private void bindCategory() {
        mCategoryIconImageView.setImageResource(mCategoryResId);
        mCategoryNameTextView.setText(String.valueOf(mCategoryName));
    }

    public int getCategoryResId() {
        return mCategoryResId;
    }

    public void setCategoryResId(int categoryResId) {
        mCategoryResId = categoryResId;
    }

    public String getCategoryName() {
        return mCategoryName;
    }

    public void setCategoryName(String categoryName) {
        mCategoryName = categoryName;
    }

    public void setCategoryIcon(int drawableResId) {
        mCategoryIconImageView.setImageResource(drawableResId);
    }

    public void setCategoryNameColor(int colorResId) {
        mCategoryNameTextView.setTextColor(getResources().getColor(colorResId));
    }
}
