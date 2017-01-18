package com.planet.wondering.chemi.view.activity;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.planet.wondering.chemi.R;
import com.planet.wondering.chemi.view.fragment.MemberFragment;

/**
 * Created by yoon on 2016. 12. 31..
 */

public class MemberActivity extends BottomNavigationActivity {

    private static final String TAG = MemberActivity.class.getSimpleName();
    private FragmentManager mFragmentManager;
    private Fragment mFragment;

    public static Intent newIntent(Context packageContext) {
        Intent intent = new Intent(packageContext, MemberActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mFragmentManager = getSupportFragmentManager();
        mFragment = mFragmentManager.findFragmentById(R.id.fragment_container);

        if (mFragment == null) {
            mFragment = MemberFragment.newInstance();
            mFragmentManager.beginTransaction()
                    .add(R.id.fragment_container, mFragment)
                    .commit();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setupBottomNavigation(4);
    }
}
