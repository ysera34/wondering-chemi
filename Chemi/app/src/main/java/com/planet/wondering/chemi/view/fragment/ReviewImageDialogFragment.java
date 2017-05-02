package com.planet.wondering.chemi.view.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.planet.wondering.chemi.R;

import java.util.ArrayList;

/**
 * Created by yoon on 2017. 5. 1..
 */

public class ReviewImageDialogFragment extends DialogFragment implements View.OnClickListener {

    private static final String TAG = ReviewImageDialogFragment.class.getSimpleName();

    private static final String ARG_REVIEW_IMAGE_PATHS = "review_image_paths";
    private static final String ARG_REVIEW_IMAGE_INDEX = "review_image_index";

    public static ReviewImageDialogFragment newInstance() {

        Bundle args = new Bundle();

        ReviewImageDialogFragment fragment = new ReviewImageDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static ReviewImageDialogFragment newInstance(ArrayList<String> reviewImagePaths, int reviewImageIndex) {

        Bundle args = new Bundle();
        args.putStringArrayList(ARG_REVIEW_IMAGE_PATHS, reviewImagePaths);
        args.putInt(ARG_REVIEW_IMAGE_INDEX, reviewImageIndex);

        ReviewImageDialogFragment fragment = new ReviewImageDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private ArrayList<String> mReviewImagePaths;
    private int mReviewImageIndex;

    private AlertDialog.Builder mBuilder;

    private ViewPager mReviewImageViewPager;
    private TextView mReviewImageIndicatorTextView;
    private ImageView mReviewImageCancelImageView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mReviewImagePaths = getArguments().getStringArrayList(ARG_REVIEW_IMAGE_PATHS);
        mReviewImageIndex = getArguments().getInt(ARG_REVIEW_IMAGE_INDEX);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity())
                .inflate(R.layout.fragment_review_image_dialog, null);

        mReviewImageIndicatorTextView = (TextView) view.findViewById(R.id.review_image_indicator_text_view);

        mReviewImageCancelImageView = (ImageView) view.findViewById(R.id.review_image_cancel_image_view);
        mReviewImageCancelImageView.setOnClickListener(this);

        mReviewImageViewPager = (ViewPager) view.findViewById(R.id.review_image_view_pager);
        mReviewImageViewPager.setAdapter(new ReviewImageViewPagerAdapter(mReviewImagePaths));
        mReviewImageViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mReviewImageIndicatorTextView.setText(getString(R.string.review_image_indicator_format,
                        String.valueOf(position + 1), String.valueOf(mReviewImagePaths.size())));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mReviewImageIndicatorTextView.setText(getString(R.string.review_image_indicator_format,
                String.valueOf(mReviewImageIndex), String.valueOf(mReviewImagePaths.size())));
        mReviewImageViewPager.setCurrentItem(mReviewImageIndex - 1);


        mBuilder = new AlertDialog.Builder(getActivity(), android.R.style.Theme_Translucent_NoTitleBar)
                .setView(view);
        return mBuilder.create();
    }
//    android.R.style.Theme_Black_NoTitleBar_Fullscreen
//    android.R.style.Theme_Material_NoActionBar_TranslucentDecor


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.review_image_cancel_image_view:
                getDialog().dismiss();
                break;
        }
    }

    private class ReviewImageViewPagerAdapter extends PagerAdapter {

        private ArrayList<String> mReviewImagePaths;
        private LayoutInflater mLayoutInflater;

        public ReviewImageViewPagerAdapter(ArrayList<String> reviewImagePaths) {
            mReviewImagePaths = reviewImagePaths;
            mLayoutInflater = LayoutInflater.from(getContext());
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = mLayoutInflater.inflate(R.layout.list_item_review_image, container, false);

            ImageView reviewImageView = (ImageView) view.findViewById(R.id.list_item_review_image_view);
            Glide.with(getActivity())
                    .load(mReviewImagePaths.get(position))
                    .into(reviewImageView);
            container.addView(view);
            return view;
        }

        @Override
        public int getCount() {
            return mReviewImagePaths.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((RelativeLayout) object);
        }
    }
}

