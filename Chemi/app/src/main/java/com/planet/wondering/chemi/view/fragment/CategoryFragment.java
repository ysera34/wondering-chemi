package com.planet.wondering.chemi.view.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.planet.wondering.chemi.R;
import com.planet.wondering.chemi.view.activity.ProductListActivity;

/**
 * Created by yoon on 2016. 12. 31..
 */

public class CategoryFragment extends Fragment implements View.OnClickListener {

    public static final String TAG = CategoryFragment.class.getSimpleName();

    public static CategoryFragment newInstance() {

        Bundle args = new Bundle();

        CategoryFragment fragment = new CategoryFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private int[] mCategoryImageViewIds;
    private int[] mCategoryNameTextViewIds;
    private ImageView[] mCategoryImageViews;
    private TextView[] mCategoryNameTextViews;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCategoryImageViewIds = new int[]{
                R.id.category_baby_wet_tissue_image_view, R.id.category_baby_hair_image_view,
                R.id.category_baby_lotion_image_view, R.id.category_baby_tooth_paste_image_view,
                R.id.category_baby_body_wash_image_view, R.id.category_adult_etc_image_view,};
        mCategoryNameTextViewIds = new int[]{
                R.id.category_baby_wet_tissue_text_view, R.id.category_baby_hair_text_view,
                R.id.category_baby_lotion_text_view, R.id.category_baby_tooth_paste_text_view,
                R.id.category_baby_body_wash_text_view, R.id.category_adult_etc_text_view,};
        mCategoryImageViews = new ImageView[mCategoryImageViewIds.length];
        mCategoryNameTextViews = new TextView[mCategoryNameTextViewIds.length];
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category, container, false);

        for (int i = 0; i < mCategoryImageViewIds.length; i++) {
            mCategoryImageViews[i] = (ImageView) view.findViewById(mCategoryImageViewIds[i]);
            mCategoryImageViews[i].setOnClickListener(this);
        }
        for (int i = 0; i < mCategoryNameTextViewIds.length; i++) {
            mCategoryNameTextViews[i] = (TextView) view.findViewById(mCategoryNameTextViewIds[i]);
            mCategoryNameTextViews[i].setOnClickListener(this);
        }
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onClick(View v) {
        byte categoryId = -1;
        switch (v.getId()) {
            case R.id.category_baby_wet_tissue_image_view:
            case R.id.category_baby_wet_tissue_text_view:
                categoryId = 1;
                break;
            case R.id.category_baby_hair_image_view:
            case R.id.category_baby_hair_text_view:
                categoryId = 2;
                break;
            case R.id.category_baby_lotion_image_view:
            case R.id.category_baby_lotion_text_view:
                categoryId = 4;
                break;
            case R.id.category_baby_tooth_paste_image_view:
            case R.id.category_baby_tooth_paste_text_view:
                categoryId = 5;
                break;
            case R.id.category_baby_body_wash_image_view:
            case R.id.category_baby_body_wash_text_view:
                categoryId = 3;
                break;
            case R.id.category_adult_etc_image_view:
            case R.id.category_adult_etc_text_view:
                categoryId = 10;
                break;
        }
        startActivity(ProductListActivity.newIntent(getActivity(), categoryId));
    }
}
