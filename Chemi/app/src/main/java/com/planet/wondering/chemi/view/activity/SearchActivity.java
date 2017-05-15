package com.planet.wondering.chemi.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.planet.wondering.chemi.R;
import com.planet.wondering.chemi.util.helper.BackPressCloseHandler;
import com.planet.wondering.chemi.util.listener.OnTagSelectedListener;
import com.planet.wondering.chemi.view.fragment.SearchDetailFragment;
import com.planet.wondering.chemi.view.fragment.SearchFragment;

/**
 * Created by yoon on 2016. 12. 31..
 */

public class SearchActivity extends BottomNavigationActivity
        implements OnTagSelectedListener {

    private static final String TAG = SearchActivity.class.getSimpleName();

    private static final String EXTRA_IS_FIRST_USER = "com.planet.wondering.chemi.is_first_user";

    public static Intent newIntent(Context packageContext) {
        Intent intent = new Intent(packageContext, SearchActivity.class);
        return intent;
    }

    public static Intent newIntent(Context packageContext, boolean isFirstUser) {
        Intent intent = new Intent(packageContext, SearchActivity.class);
        intent.putExtra(EXTRA_IS_FIRST_USER, isFirstUser);
        return intent;
    }

    private FragmentManager mFragmentManager;
    private Fragment mFragment;
    private boolean mIsFirstUser;

    private BackPressCloseHandler mBackPressCloseHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mIsFirstUser = getIntent().getBooleanExtra(EXTRA_IS_FIRST_USER, false);

        mBackPressCloseHandler = new BackPressCloseHandler(SearchActivity.this);

        mFragmentManager = getSupportFragmentManager();
        mFragment = mFragmentManager.findFragmentById(R.id.main_fragment_container);

        if (mFragment == null) {
            mFragment = SearchFragment.newInstance(mIsFirstUser);
            mFragmentManager.beginTransaction()
                    .add(R.id.main_fragment_container, mFragment)
//                    .addToBackStack(null)
                    .commit();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setupBottomNavigation(0);
    }

    private SearchDetailFragment mSearchDetailFragment;

    @Override
    public void onTagSelected(String tag) {
        mSearchDetailFragment = (SearchDetailFragment) getSupportFragmentManager()
                .findFragmentById(R.id.main_fragment_container);
        mSearchDetailFragment.updateSearchEditText(tag);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        mBackPressCloseHandler.onBackPressed();
    }
}
