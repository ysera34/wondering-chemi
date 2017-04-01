package com.planet.wondering.chemi.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.planet.wondering.chemi.R;
import com.planet.wondering.chemi.common.AppBaseActivity;
import com.planet.wondering.chemi.view.fragment.ContentFragment;

/**
 * Created by yoon on 2017. 3. 31..
 */

public class ContentActivity extends AppBaseActivity {

    private static final String TAG = ContentActivity.class.getSimpleName();

    private FragmentManager mFragmentManager;
    private Fragment mFragment;

    public static Intent newIntent(Context packageContext) {
        Intent intent = new Intent(packageContext, ContentActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);

        mFragmentManager = getSupportFragmentManager();
        mFragment = mFragmentManager.findFragmentById(R.id.pure_fragment_container);

        if (mFragment == null) {
            mFragment = ContentFragment.newInstance();
            mFragmentManager.beginTransaction()
                    .add(R.id.pure_fragment_container, mFragment)
                    .commit();
        }
    }
}
