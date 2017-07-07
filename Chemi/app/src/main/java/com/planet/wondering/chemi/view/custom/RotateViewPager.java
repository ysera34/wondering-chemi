package com.planet.wondering.chemi.view.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.planet.wondering.chemi.R;
import com.planet.wondering.chemi.model.home.BestReview;
import com.planet.wondering.chemi.model.home.PromoteContent;

import java.util.ArrayList;

/**
 * Created by yoon on 2017. 6. 22..
 */

public class RotateViewPager extends RelativeLayout {

    private static final String TAG = RotateViewPager.class.getSimpleName();

    public RotateViewPager(Context context) {
        super(context);
        initializeView(context);
    }

    public RotateViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.RotateViewPager,
                0, 0
        );

        try {
            mTitleIconResId = a.getResourceId(R.styleable.RotateViewPager_titleIconSrc, 0);
            mTitleText = a.getString(R.styleable.RotateViewPager_titleText);
            mIndicatorType = a.getInteger(R.styleable.RotateViewPager_indicatorType, -1);
        } finally {
            a.recycle();
        }
        initializeView(context);
    }

    private int mTitleIconResId;
    private String mTitleText;
    private int mIndicatorType;

    private View mRootView;
    protected ViewPager mRotateViewPager;
    private LinearLayout mTitleLayout;
    private ImageView mTitleIconImageView;
    private TextView mTitleTextView;
    private LinearLayout mIndicatorLayout;
    private IndicatorTextView mIndicatorTextView;
    protected ArrayList<TextView> mIndicatorTextViews;
    private int mPagerItemSize;

    protected static final long ROTATE_THRESHOLD_TIME = 5000;
    protected int mRotateViewPagerCurrentIndex;
    protected Handler mRotateHandler;
    protected Runnable mRotateRunnable;

    String[] mReviewMessageArray = getResources().getStringArray(R.array.review_rating_message_array);

    private void initializeView(Context context) {
        mRootView = inflate(context, R.layout.layout_rotate_view_pager, this);
        mRotateViewPager = (ViewPager) mRootView.findViewById(R.id.rotate_view_pager);
        mTitleLayout = (LinearLayout) mRootView.findViewById(R.id.title_layout);
        mTitleIconImageView = (ImageView) mRootView.findViewById(R.id.title_icon_image_view);
        mTitleTextView = (TextView) mRootView.findViewById(R.id.title_text_view);
        if (mTitleText != null) {
            mTitleTextView.setText(String.valueOf(mTitleText));
        } else {
            mTitleTextView.setVisibility(View.GONE);
        }
        mIndicatorLayout = (LinearLayout) mRootView.findViewById(R.id.indicator_layout);
        mIndicatorTextView = new IndicatorTextView(getContext());
        mIndicatorTextViews = new ArrayList<>();
    }

    private RotateViewPagerAdapter mRotateViewPagerAdapter;
    private RotateViewPagerChangeListener mRotateViewPagerChangeListener;

    public void setRotateViewPagerAdapter(ArrayList objects) {
        if (mRotateViewPagerAdapter == null) {
            mRotateViewPagerAdapter = new RotateViewPagerAdapter(objects);
        }
        mRotateViewPager.setAdapter(mRotateViewPagerAdapter);
        mRotateViewPager.setCurrentItem(objects.size() * 1000);
        setViewPagerIndicator(objects.size());
        mPagerItemSize = objects.size();
    }

    private class RotateViewPagerAdapter extends PagerAdapter {

        private ArrayList<PromoteContent> mPromoteContents;
        private ArrayList<BestReview> mBestReviews;
        private LayoutInflater mLayoutInflater;

        public RotateViewPagerAdapter(ArrayList objects) {
            if (objects.get(0) instanceof PromoteContent) {
                mPromoteContents = objects;
            } else if (objects.get(0) instanceof BestReview) {
                mBestReviews = objects;
            }
            mLayoutInflater = LayoutInflater.from(getContext());
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            if (mPromoteContents != null) {
                int restPosition = position % mPromoteContents.size();
                final PromoteContent promoteContent = mPromoteContents.get(restPosition);
                View view = mLayoutInflater.inflate(R.layout.layout_rotate_image_view, container, false);
                ImageView imageView = (ImageView) view;

                Glide.with(getContext())
                        .load(promoteContent.getImagePath())
                        .into(imageView);
                imageView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        OnItemClickListener itemClickListener = getItemClickListener();
                        if (itemClickListener != null) {
                            itemClickListener.onItemClick(promoteContent.getId());
                        }
                    }
                });
                container.addView(imageView);
                return imageView;
            } else if (mBestReviews != null) {
                int restPosition = position % mBestReviews.size();
                final BestReview bestReview = mBestReviews.get(restPosition);
                View view = mLayoutInflater.inflate(R.layout.layout_rotate_review, container, false);
                TextView ratingValueTextView = (TextView) view.findViewById(R.id.review_rating_value_text_view);
                TextView ratingMessageTextView = (TextView) view.findViewById(R.id.review_rating_message_text_view);
                TextView contentTextView = (TextView) view.findViewById(R.id.review_content_text_view);
                TextView parentTextView = (TextView) view.findViewById(R.id.review_parent_text_view);
                TextView childTextView = (TextView) view.findViewById(R.id.review_child_text_view);

                ratingValueTextView.setText(String.valueOf(bestReview.getRatingValue()));
                ratingMessageTextView.setText(String.valueOf(getReviewMessage(bestReview.getRatingValue())));
                contentTextView.setText(String.valueOf(bestReview.getReviewContent()));

                parentTextView.setText(bestReview.getParentTextBuilder());
                childTextView.setText(bestReview.getChildTextBuilder());

                TextView productBrandTextView = (TextView) view.findViewById(R.id.review_product_brand_text_view);
                TextView productNameTextView = (TextView) view.findViewById(R.id.review_product_name_text_view);
                ImageView productImageView = (ImageView) view.findViewById(R.id.review_product_image_view);

                productBrandTextView.setText(String.valueOf(bestReview.getProductBrand()));
                productNameTextView.setText(String.valueOf(bestReview.getProductName()));
                Glide.with(getContext())
                        .load(bestReview.getProductImagePath())
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .override(getPixelFromDp(120), getPixelFromDp(90))
                        .centerCrop()
                        .crossFade()
                        .into(productImageView);
                view.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        OnItemClickListener itemClickListener = getItemClickListener();
                        if (itemClickListener != null) {
                            itemClickListener.onItemClick(bestReview.getId());
                        }
                    }
                });
                container.addView(view);
                return view;
            }
            return null;
        }

        @Override
        public int getCount() {
            return Integer.MAX_VALUE;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            if (mPromoteContents != null) {
                container.removeView((ImageView) object);
            } else if (mBestReviews != null) {
                container.removeView((LinearLayout) object);
            }
        }
    }

    private OnItemClickListener mItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(int itemId);
    }

    public OnItemClickListener getItemClickListener() {
        return mItemClickListener;
    }

    public void setItemClickListener(OnItemClickListener itemClickListener) {
        mItemClickListener = itemClickListener;
    }

    public void addOnPageChangeListener() {
        if (mRotateViewPagerChangeListener == null) {
            mRotateViewPagerChangeListener = new RotateViewPagerChangeListener();
        }
        mRotateViewPager.addOnPageChangeListener(mRotateViewPagerChangeListener);
    }

    public void removeOnPageChangeListener() {
        if (mRotateViewPagerChangeListener != null) {
            mRotateViewPager.removeOnPageChangeListener(mRotateViewPagerChangeListener);
        }
    }

    private class RotateViewPagerChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            if (mPagerItemSize != 0) {
                int restPosition = position % mPagerItemSize;
                switch (mIndicatorType) {
                    case 0:
                        mIndicatorTextView.setCurrentPage(restPosition + 1);
                        break;
                    default:
                        setIndicatorBackground(restPosition);
                        break;
                }
                mRotateViewPagerCurrentIndex = position;
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    public void startRotateViewPager() {
        mRotateHandler = new Handler();
        mRotateRunnable = new Runnable() {
            @Override
            public void run() {
                if (mRotateViewPagerAdapter != null) {
                    mRotateViewPager.setCurrentItem(mRotateViewPagerCurrentIndex++, true);
                    mRotateHandler.postDelayed(mRotateRunnable, ROTATE_THRESHOLD_TIME);
                }
            }
        };
        mRotateHandler.postDelayed(mRotateRunnable, ROTATE_THRESHOLD_TIME);
    }

    public void stopRotateViewPager() {
        if (mRotateHandler != null) {
            mRotateHandler.removeCallbacks(mRotateRunnable);
            mRotateHandler = null;
        }
    }

    protected void setViewPagerIndicator(int size) {

        switch (mIndicatorType) {
            case 0:
                mIndicatorTextView.setCurrentPage(1);
                mIndicatorTextView.setPageSize(size);
                mIndicatorLayout.addView(mIndicatorTextView);
                break;
            case 1:
                for (int i = 0; i < size; i++) {
                    TextView indicatorTextView = new TextView(getContext());
                    specifyIndicatorSize(indicatorTextView);

                    if (i != size - 1) {
                        LinearLayout.LayoutParams params =
                                (LinearLayout.LayoutParams) indicatorTextView.getLayoutParams();
                        params.setMarginEnd(20);
                        indicatorTextView.setLayoutParams(params);
                    }
                    mIndicatorLayout.addView(indicatorTextView);
                    mIndicatorTextViews.add(indicatorTextView);
                }
                setIndicatorBackground(0);
                break;
        }

        setIndicatorLayoutType(mIndicatorType);
        mRotateViewPagerCurrentIndex = mRotateViewPager.getCurrentItem();
    }

    private void specifyIndicatorSize(TextView indicatorTextView) {
        indicatorTextView.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        indicatorTextView.getLayoutParams().width =
                (int) getResources().getDimension(R.dimen.rotate_view_pager_indicator_width);
        indicatorTextView.getLayoutParams().height =
                (int) getResources().getDimension(R.dimen.rotate_view_pager_indicator_height);
    }

    protected void setIndicatorBackground(int position) {
        for (int i = 0; i < mIndicatorTextViews.size(); i++) {
            if (i == position) {
                mIndicatorTextViews.get(i)
                        .setBackgroundResource(R.drawable.widget_home_indicator_circle_true);
            } else {
                mIndicatorTextViews.get(i)
                        .setBackgroundResource(R.drawable.widget_home_indicator_circle_false);
            }
        }
    }

    private void setIndicatorLayoutType(int layoutType) {
        RelativeLayout.LayoutParams params =
                (RelativeLayout.LayoutParams) mIndicatorLayout.getLayoutParams();
        switch (layoutType) {
            case -1:
                break;
            case 0:
                params.addRule(RelativeLayout.ALIGN_PARENT_END);
                params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                params.setMargins(0, 0, 40, 40);
                mIndicatorLayout.setLayoutParams(params);
                break;
            case 1:
                params.addRule(RelativeLayout.ALIGN_PARENT_END);
                params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                params.setMargins(0, 40, 70, 0);
                mIndicatorLayout.setLayoutParams(params);
                break;
        }
    }

    private int getPixelFromDp(int dp) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    private String getReviewMessage(float reviewRatingValue) {
        for (int i = 0; i < mReviewMessageArray.length; i++) {
            if (reviewRatingValue == 0.5f * i) {
                return mReviewMessageArray[i];
            }
        }
        return "";
    }
}
