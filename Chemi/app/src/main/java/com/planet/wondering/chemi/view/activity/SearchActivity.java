package com.planet.wondering.chemi.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.planet.wondering.chemi.R;
import com.planet.wondering.chemi.util.listener.OnTagSelectedListener;
import com.planet.wondering.chemi.view.fragment.SearchDetailFragment;

/**
 * Created by yoon on 2017. 6. 29..
 */

public class SearchActivity extends AppBaseActivity implements OnTagSelectedListener {

    private static final String TAG = SearchActivity.class.getSimpleName();

    private static final String EXTRA_CATEGORY_GROUP_ID = "com.planet.wondering.chemi.category_group_id";

    public static Intent newIntent(Context packageContext) {
        Intent intent = new Intent(packageContext, SearchActivity.class);
        return intent;
    }

    public static Intent newIntent(Context packageContext, int categoryId) {
        Intent intent = new Intent(packageContext, SearchActivity.class);
        intent.putExtra(EXTRA_CATEGORY_GROUP_ID, categoryId);
        return intent;
    }

    private FragmentManager mFragmentManager;
    private Fragment mFragment;
    private int mCategoryGroupId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);
        mCategoryGroupId = getIntent().getIntExtra(EXTRA_CATEGORY_GROUP_ID, -1);

        mFragmentManager = getSupportFragmentManager();
        mFragment = mFragmentManager.findFragmentById(R.id.pure_fragment_container);

        if (mFragment == null) {
            mFragment = SearchDetailFragment.newInstance(mCategoryGroupId);
            mFragmentManager.beginTransaction()
                    .add(R.id.pure_fragment_container, mFragment)
                    .commit();
        }
    }

    @Override
    public void onTagSelected(String tag) {
        mFragment = mFragmentManager.findFragmentById(R.id.pure_fragment_container);
        if (mFragment instanceof SearchDetailFragment) {
            ((SearchDetailFragment) mFragment).updateSearchEditText(tag);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
