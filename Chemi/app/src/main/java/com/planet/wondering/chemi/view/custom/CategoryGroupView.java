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

//        LinearLayout.LayoutParams params = (LayoutParams) getLayoutParams();
//
//        params.height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 80, getResources().getDisplayMetrics());
//        params.width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, getResources().getDisplayMetrics());
//        getLayoutParams().width =
//                (int) getResources().getDimension(R.dimen.category_group_view_width);
//        getLayoutParams().height =
//                (int) getResources().getDimension(R.dimen.category_group_view_height);
//        getLayoutParams().height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 80, getResources().getDisplayMetrics());
//        getLayoutParams().width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, getResources().getDisplayMetrics());
//
//        setLayoutParams(new LinearLayout.LayoutParams(
//                (int) (50 * getContext().getResources().getDisplayMetrics().density + 0.5f),
//                (int) (80 * getContext().getResources().getDisplayMetrics().density + 0.5f)));

//        mCategoryGroupLayout.setLayoutParams(params);

//        indicatorTextView.setLayoutParams(new LinearLayout.LayoutParams(
//                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT
//        ));
//        indicatorTextView.getLayoutParams().width =
//                (int) getResources().getDimension(R.dimen.rotate_view_pager_indicator_width);
//        indicatorTextView.getLayoutParams().height =
//                (int) getResources().getDimension(R.dimen.rotate_view_pager_indicator_height);

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
