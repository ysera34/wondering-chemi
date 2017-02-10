package com.planet.wondering.chemi.view.activity;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.planet.wondering.chemi.R;
import com.planet.wondering.chemi.util.listener.OnMenuSelectedListener;
import com.planet.wondering.chemi.view.fragment.MemberConfigFragment;
import com.planet.wondering.chemi.view.fragment.MemberFragment;

/**
 * Created by yoon on 2016. 12. 31..
 */

public class MemberActivity extends BottomNavigationActivity implements OnMenuSelectedListener {

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

    @Override
    public void onMenuSelected() {
        mFragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left)
                .replace(R.id.fragment_container, MemberConfigFragment.newInstance())
                .commit();
    }

    @Override
    public void onBackPressed() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
//        MemberConfigFragment memberConfigFragment = MemberConfigFragment.newInstance();
        if (fragment instanceof MemberConfigFragment) {
            mFragmentManager.beginTransaction()
                    .setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right)
                    .replace(R.id.fragment_container, MemberFragment.newInstance())
                    .commit();
        } else {
            super.onBackPressed();
        }
    }
}
