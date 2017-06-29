package com.planet.wondering.chemi.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.planet.wondering.chemi.R;
import com.planet.wondering.chemi.view.fragment.SearchDetailFragment;

/**
 * Created by yoon on 2017. 6. 29..
 */

public class SearchActivity extends AppBaseActivity {

    private static final String TAG = SearchActivity.class.getSimpleName();

    public static Intent newIntent(Context packageContext) {
        Intent intent = new Intent(packageContext, SearchActivity.class);
        return intent;
    }

    private FragmentManager mFragmentManager;
    private Fragment mFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);

        mFragmentManager = getSupportFragmentManager();
        mFragment = mFragmentManager.findFragmentById(R.id.pure_fragment_container);

        if (mFragment == null) {
            mFragment = SearchDetailFragment.newInstance();
            mFragmentManager.beginTransaction()
                    .add(R.id.pure_fragment_container, mFragment)
                    .commit();
        }
    }


}
