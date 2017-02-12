package com.planet.wondering.chemi.view.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.planet.wondering.chemi.R;
import com.planet.wondering.chemi.view.fragment.MemberStartFragment;

/**
 * Created by yoon on 2017. 2. 12..
 */

public class MemberStartActivity extends AppCompatActivity {

    private static final String TAG = MemberStartActivity.class.getSimpleName();
    private FragmentManager mFragmentManager;
    private Fragment mFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_start);

        mFragmentManager = getSupportFragmentManager();
        mFragment = mFragmentManager.findFragmentById(R.id.member_start_fragment_container);

        if (mFragment == null) {
            mFragment = MemberStartFragment.newInstance();
            mFragmentManager.beginTransaction()
                    .add(R.id.member_start_fragment_container, mFragment)
                    .commit();
        }
    }
}
