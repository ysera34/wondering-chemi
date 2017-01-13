package com.navylab.chemi.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.navylab.chemi.view.BottomNavigationActivity;
import com.navylab.chemi.R;
import com.navylab.chemi.view.fragment.DictionaryFragment;

/**
 * Created by yoon on 2016. 12. 31..
 */

public class DictionaryActivity extends BottomNavigationActivity {

    private static final String TAG = DictionaryActivity.class.getSimpleName();
    private FragmentManager mFragmentManager;
    private Fragment mFragment;

    public static Intent newIntent(Context packageContext) {
        Intent intent = new Intent(packageContext, DictionaryActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mFragmentManager = getSupportFragmentManager();
        mFragment = mFragmentManager.findFragmentById(R.id.fragment_container);

        if (mFragment == null) {
            mFragment = DictionaryFragment.newInstance();
            mFragmentManager.beginTransaction()
                    .add(R.id.fragment_container, mFragment)
                    .commit();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        mBottomNavigationView.getMenu().getItem(3).setChecked(true);
    }
}
