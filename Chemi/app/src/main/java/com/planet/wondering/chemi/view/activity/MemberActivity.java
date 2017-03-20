package com.planet.wondering.chemi.view.activity;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.planet.wondering.chemi.R;
import com.planet.wondering.chemi.util.listener.OnMenuSelectedListener;
import com.planet.wondering.chemi.view.fragment.MemberAskInfoFragment;
import com.planet.wondering.chemi.view.fragment.MemberConfigChangeNameFragment;
import com.planet.wondering.chemi.view.fragment.MemberConfigChangePasswordFragment;
import com.planet.wondering.chemi.view.fragment.MemberConfigFAQFragment;
import com.planet.wondering.chemi.view.fragment.MemberConfigFragment;
import com.planet.wondering.chemi.view.fragment.MemberConfigNoticeFragment;
import com.planet.wondering.chemi.view.fragment.MemberConfigPartnerFragment;
import com.planet.wondering.chemi.view.fragment.MemberConfigProfileFragment;
import com.planet.wondering.chemi.view.fragment.MemberConfigRequestFragment;
import com.planet.wondering.chemi.view.fragment.MemberConfigTermsFragment;
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
    public void onMenuSelected(int layoutIndex) {
        switch (layoutIndex) {
            case -1:
                mFragmentManager.beginTransaction()
                        .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left)
                        .replace(R.id.fragment_container, MemberConfigFragment.newInstance())
                        .commit();
                break;
            case 1:
                mFragmentManager.beginTransaction()
                        .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left)
                        .replace(R.id.fragment_container, MemberConfigProfileFragment.newInstance())
                        .commit();
                break;
            case 2:
                mFragmentManager.beginTransaction()
                        .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left)
                        .replace(R.id.fragment_container, MemberConfigNoticeFragment.newInstance())
                        .commit();
                break;
            case 3:
                mFragmentManager.beginTransaction()
                        .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left)
                        .replace(R.id.fragment_container, MemberConfigRequestFragment.newInstance())
                        .commit();
                break;
            case 4:
                mFragmentManager.beginTransaction()
                        .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left)
                        .replace(R.id.fragment_container, MemberConfigFAQFragment.newInstance())
                        .commit();
                break;
            case 5:
                mFragmentManager.beginTransaction()
                        .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left)
                        .replace(R.id.fragment_container, MemberConfigTermsFragment.newInstance())
                        .commit();
                break;
            case 6:
                mFragmentManager.beginTransaction()
                        .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left)
                        .replace(R.id.fragment_container, MemberConfigPartnerFragment.newInstance())
                        .commit();
                break;

            case 11:
                mFragmentManager.beginTransaction()
                        .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left)
                        .replace(R.id.fragment_container, MemberConfigChangeNameFragment.newInstance())
                        .commit();
                break;
            case 12:
                mFragmentManager.beginTransaction()
                        .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left)
                        .replace(R.id.fragment_container, MemberConfigChangePasswordFragment.newInstance())
                        .commit();
                break;
            case 13:
                mFragmentManager.beginTransaction()
                        .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left)
                        .replace(R.id.fragment_container, MemberAskInfoFragment.newInstance())
                        .commit();
                break;

        }
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
        } else if (fragment instanceof MemberConfigProfileFragment) {
            mFragmentManager.beginTransaction()
                    .setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right)
                    .replace(R.id.fragment_container, MemberConfigFragment.newInstance())
                    .commit();
        } else if (fragment instanceof MemberConfigNoticeFragment) {
            mFragmentManager.beginTransaction()
                    .setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right)
                    .replace(R.id.fragment_container, MemberConfigFragment.newInstance())
                    .commit();
        } else if (fragment instanceof MemberConfigRequestFragment) {
            mFragmentManager.beginTransaction()
                    .setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right)
                    .replace(R.id.fragment_container, MemberConfigFragment.newInstance())
                    .commit();
        } else if (fragment instanceof MemberConfigFAQFragment) {
            mFragmentManager.beginTransaction()
                    .setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right)
                    .replace(R.id.fragment_container, MemberConfigFragment.newInstance())
                    .commit();
        } else if (fragment instanceof MemberConfigTermsFragment) {
            mFragmentManager.beginTransaction()
                    .setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right)
                    .replace(R.id.fragment_container, MemberConfigFragment.newInstance())
                    .commit();
        } else if (fragment instanceof MemberConfigPartnerFragment) {
            mFragmentManager.beginTransaction()
                    .setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right)
                    .replace(R.id.fragment_container, MemberConfigFragment.newInstance())
                    .commit();
        } else if (fragment instanceof MemberConfigChangeNameFragment) {
            mFragmentManager.beginTransaction()
                    .setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right)
                    .replace(R.id.fragment_container, MemberConfigProfileFragment.newInstance())
                    .commit();
        } else if (fragment instanceof MemberConfigChangePasswordFragment) {
            mFragmentManager.beginTransaction()
                    .setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right)
                    .replace(R.id.fragment_container, MemberConfigProfileFragment.newInstance())
                    .commit();
        } else if (fragment instanceof MemberAskInfoFragment) {
            mFragmentManager.beginTransaction()
                    .setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right)
                    .replace(R.id.fragment_container, MemberConfigProfileFragment.newInstance())
                    .commit();
        } else {
            super.onBackPressed();
        }
    }
}
