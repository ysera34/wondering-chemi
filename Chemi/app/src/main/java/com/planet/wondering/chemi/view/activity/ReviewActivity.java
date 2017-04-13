package com.planet.wondering.chemi.view.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;

import com.planet.wondering.chemi.R;
import com.planet.wondering.chemi.common.Common;
import com.planet.wondering.chemi.model.Product;
import com.planet.wondering.chemi.model.Review;
import com.planet.wondering.chemi.util.listener.OnReviewEditListener;
import com.planet.wondering.chemi.view.fragment.ReviewCreateFragment;
import com.planet.wondering.chemi.view.fragment.ReviewEditFragment;
import com.planet.wondering.chemi.view.fragment.ReviewReadFragment;

/**
 * Created by yoon on 2017. 2. 23..
 */

public class ReviewActivity extends BottomNavigationActivity implements OnReviewEditListener {

    private static final String TAG = ReviewActivity.class.getSimpleName();

    private static final String EXTRA_PRODUCT = "com.planet.wondering.chemi.product";
    private static final String EXTRA_REVIEW = "com.planet.wondering.chemi.review";
    private static final String EXTRA_REQUEST_ID = "com.planet.wondering.chemi.request_id";

    public static Intent newIntent(Context packageContext) {
        Intent intent = new Intent(packageContext, ReviewActivity.class);
        return intent;
    }

    public static Intent newIntent(Context packageContext, Product product, int requestId) {
        Intent intent = new Intent(packageContext, ReviewActivity.class);
        intent.putExtra(EXTRA_PRODUCT, product);
        intent.putExtra(EXTRA_REQUEST_ID, requestId);
        return intent;
    }

    public static Intent newIntent(Context packageContext, Review review, int requestId) {
        Intent intent = new Intent(packageContext, ReviewActivity.class);
        intent.putExtra(EXTRA_REVIEW, review);
        intent.putExtra(EXTRA_REQUEST_ID, requestId);
        return intent;
    }

    private FragmentManager mFragmentManager;
    private Fragment mFragment;

    private Product mProduct;
    private Review mReview;
    private int mRequestId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBottomNavigationLayout.setVisibility(View.GONE);

        mProduct = (Product) getIntent().getSerializableExtra(EXTRA_PRODUCT);
        mReview = (Review) getIntent().getSerializableExtra(EXTRA_REVIEW);
        mRequestId = getIntent().getIntExtra(EXTRA_REQUEST_ID, -1);

        mFragmentManager = getSupportFragmentManager();
        mFragment = mFragmentManager.findFragmentById(R.id.main_fragment_container);

        if (mRequestId == Common.REVIEW_CREATE_REQUEST_CODE) {
            mFragment = ReviewCreateFragment.newInstance(mProduct);
            mFragmentManager.beginTransaction()
                    .add(R.id.main_fragment_container, mFragment)
                    .commit();
        } else if (mRequestId == Common.REVIEW_READ_REQUEST_CODE) {
            mFragment = ReviewReadFragment.newInstance();
            mFragmentManager.beginTransaction()
                    .add(R.id.main_fragment_container, mFragment)
                    .commit();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
//        setupBottomNavigation(0);
    }

    private String mReviewContent;

    @Override
    public void onReviewEdit(String reviewContent, boolean isEdit) {
        if (isEdit) {
            mReviewContent = reviewContent;
            mFragmentManager.beginTransaction()
//                    .replace(R.id.fragment_container, ReviewEditFragment.newInstance(reviewContent))
                    .add(R.id.main_fragment_container, ReviewEditFragment.newInstance(reviewContent))
                    .addToBackStack(null)
                    .commit();
        } else {
            mReviewContent = reviewContent;
//            mFragmentManager.beginTransaction()
//                    .replace(R.id.fragment_container, ReviewCreateFragment.newInstance(reviewContent))

//                    .replace(R.id.fragment_container, ReviewCreateFragment.newInstance(mProduct, reviewContent))
//                    .commit();

            mFragmentManager.popBackStackImmediate();
            Fragment fragment = mFragmentManager.findFragmentById(R.id.main_fragment_container);
            ((ReviewCreateFragment) fragment).updateContentTextView(mReviewContent);
//            mFragmentManager.beginTransaction()
//                    .replace(R.id.fragment_container, ReviewCreateFragment.newInstance(reviewContent))
//                    .addToBackStack(null)
//                    .commit();
        }
    }

    @Override
    public void onBackPressed() {
        Fragment fragment = mFragmentManager.findFragmentById(R.id.main_fragment_container);
//        MemberConfigFragment memberConfigFragment = MemberConfigFragment.newInstance();
        if (fragment instanceof ReviewEditFragment) {
//            mFragmentManager.beginTransaction()
//                    .replace(R.id.fragment_container, ReviewCreateFragment.newInstance(mProduct, mReviewContent))
//                    .commit();

            mFragmentManager.popBackStackImmediate();

        } else {
            setResult(Activity.RESULT_OK, new Intent());
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        ReviewSharedPreferences preferences = new ReviewSharedPreferences();
//        preferences.removeStoredRatingValue(getApplicationContext());
//        preferences.removeStoredImage1Path(getApplicationContext());
//        preferences.removeStoredImage2Path(getApplicationContext());
//        preferences.removeStoredImage3Path(getApplicationContext());
    }
}
