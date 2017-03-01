package com.planet.wondering.chemi.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;

import com.planet.wondering.chemi.R;
import com.planet.wondering.chemi.util.listener.OnReviewEditListener;
import com.planet.wondering.chemi.view.fragment.ReviewCreateFragment;
import com.planet.wondering.chemi.view.fragment.ReviewEditFragment;

/**
 * Created by yoon on 2017. 2. 23..
 */

public class ReviewActivity extends BottomNavigationActivity implements OnReviewEditListener {

    private static final String TAG = ReviewActivity.class.getSimpleName();
    private FragmentManager mFragmentManager;
    private Fragment mFragment;

    public static Intent newIntent(Context packageContext) {
        Intent intent = new Intent(packageContext, ReviewActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBottomNavigationLayout.setVisibility(View.GONE);

        mFragmentManager = getSupportFragmentManager();
        mFragment = mFragmentManager.findFragmentById(R.id.fragment_container);

        if (mFragment == null) {
            mFragment = ReviewCreateFragment.newInstance();
            mFragmentManager.beginTransaction()
                    .add(R.id.fragment_container, mFragment)
                    .commit();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
//        setupBottomNavigation(0);
    }

    @Override
    public void onReviewEdit(String reviewContent, boolean isEdit) {
        if (isEdit) {
            mFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, ReviewEditFragment.newInstance(reviewContent))
                    .commit();
        } else {
            mFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, ReviewCreateFragment.newInstance(reviewContent))
                    .commit();
//            mFragmentManager.beginTransaction()
//                    .replace(R.id.fragment_container, ReviewCreateFragment.newInstance(reviewContent))
//                    .addToBackStack(null)
//                    .commit();
        }
    }
}
