package com.planet.wondering.chemi.view.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
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

    private int mCategoryResId;
    private String mCategoryName;

    private LinearLayout mCategoryGroupLayout;
    private ImageView mCategoryIconImageView;
    private TextView mCategoryNameTextView;

    private void initializeView(Context context) {

        setOrientation(VERTICAL);
        mCategoryGroupLayout = (LinearLayout) inflate(context, R.layout.layout_category_group_view, this);
        mCategoryIconImageView = (ImageView) mCategoryGroupLayout.findViewById(R.id.category_group_icon_image_view);
        mCategoryNameTextView = (TextView) mCategoryGroupLayout.findViewById(R.id.category_group_name_text_view);
        bindCategory();
    }

    private void bindCategory() {
        mCategoryIconImageView.setImageResource(mCategoryResId);
        mCategoryNameTextView.setText(String.valueOf(mCategoryName));
    }
}
