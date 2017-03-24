package com.planet.wondering.chemi.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.planet.wondering.chemi.R;
import com.planet.wondering.chemi.model.Chemical;
import com.planet.wondering.chemi.util.listener.OnChemicalSelectedListener;
import com.planet.wondering.chemi.util.listener.OnDialogFinishedListener;
import com.planet.wondering.chemi.view.fragment.DictionaryFragment;

/**
 * Created by yoon on 2016. 12. 31..
 */

public class DictionaryActivity extends BottomNavigationActivity
        implements OnChemicalSelectedListener, OnDialogFinishedListener {

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
        setupBottomNavigation(3);
    }

    public void resizeFrameLayout(int resize) {
        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.fragment_container);
        ViewGroup.LayoutParams layoutParams1 = frameLayout.getLayoutParams();
        layoutParams1.height = frameLayout.getHeight() + resize;
        frameLayout.setLayoutParams(layoutParams1);
    }

    @Override
    public void onChemicalSelected(Chemical chemical) {
        DictionaryFragment fragment = (DictionaryFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragment_container);
        fragment.updateSearchEditText(chemical);
    }

    @Override
    public void onDialogFinished(boolean isChose) {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if (fragment instanceof DictionaryFragment) {
            ((DictionaryFragment) fragment).onDialogFinished(isChose);
        }
    }

    @Override
    public void onBackPressed() {
        if (mFragment instanceof DictionaryFragment) {
            ((DictionaryFragment) mFragment).onBackPressed();
        } else {
            super.onBackPressed();
        }
    }
}
