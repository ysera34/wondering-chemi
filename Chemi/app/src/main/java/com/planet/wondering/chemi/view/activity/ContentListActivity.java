package com.planet.wondering.chemi.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.planet.wondering.chemi.R;
import com.planet.wondering.chemi.view.fragment.ContentPagerFragment;

/**
 * Created by yoon on 2016. 12. 31..
 */

public class ContentListActivity extends BottomNavigationActivity {

    private static final String TAG = ContentListActivity.class.getSimpleName();

    private FragmentManager mFragmentManager;
    private Fragment mFragment;

    public static Intent newIntent(Context packageContext) {
        Intent intent = new Intent(packageContext, ContentListActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mFragmentManager = getSupportFragmentManager();
        mFragment = mFragmentManager.findFragmentById(R.id.main_fragment_container);

        if (mFragment == null) {
            mFragment = ContentPagerFragment.newInstance();
            mFragmentManager.beginTransaction()
                    .add(R.id.main_fragment_container, mFragment)
                    .commit();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setupBottomNavigation(1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }
}
