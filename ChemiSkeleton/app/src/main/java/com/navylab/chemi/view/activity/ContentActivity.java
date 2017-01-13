package com.navylab.chemi.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.navylab.chemi.view.BottomNavigationActivity;
import com.navylab.chemi.R;
import com.navylab.chemi.view.fragment.ContentFragment;

/**
 * Created by yoon on 2016. 12. 31..
 */

public class ContentActivity extends BottomNavigationActivity {

    private static final String TAG = ContentActivity.class.getSimpleName();
    private FragmentManager mFragmentManager;
    private Fragment mFragment;

    public static Intent newIntent(Context packageContext) {
        Intent intent = new Intent(packageContext, ContentActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mFragmentManager = getSupportFragmentManager();
        mFragment = mFragmentManager.findFragmentById(R.id.fragment_container);

        if (mFragment == null) {
            mFragment = ContentFragment.newInstance();
            mFragmentManager.beginTransaction()
                    .add(R.id.fragment_container, mFragment)
                    .commit();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        mBottomNavigationView.getMenu().getItem(2).setChecked(true);
    }
}
