package com.planet.wondering.chemi.view.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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

    private static final String EXTRA_CHEMICAL_ID = "com.planet.wondering.chemi.chemical_id";

    public static Intent newIntent(Context packageContext) {
        Intent intent = new Intent(packageContext, DictionaryActivity.class);
        return intent;
    }

    public static Intent newIntent(Context packageContext, int chemicalId) {
        Intent intent = new Intent(packageContext, DictionaryActivity.class);
        intent.putExtra(EXTRA_CHEMICAL_ID, chemicalId);
        return intent;
    }

    private FragmentManager mFragmentManager;
    private Fragment mFragment;

    private int mChemicalId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        if (Intent.ACTION_VIEW.equals(intent.getAction())) {
            Uri uri = intent.getData();
            mChemicalId = Integer.valueOf(uri.getQueryParameter("chemical_id"));
        } else {
            mChemicalId = getIntent().getIntExtra(EXTRA_CHEMICAL_ID, -1);
        }

        mFragmentManager = getSupportFragmentManager();
        mFragment = mFragmentManager.findFragmentById(R.id.main_fragment_container);

        if (mFragment == null) {
            if (mChemicalId > 0) {
                mFragment = DictionaryFragment.newInstance(mChemicalId);
            } else {
                mFragment = DictionaryFragment.newInstance();
            }
            mFragmentManager.beginTransaction()
                    .add(R.id.main_fragment_container, mFragment)
                    .commit();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setupBottomNavigation(2);
    }

    public void resizeFrameLayout(int resize) {
        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.main_fragment_container);
        ViewGroup.LayoutParams layoutParams1 = frameLayout.getLayoutParams();
        layoutParams1.height = frameLayout.getHeight() + resize;
        frameLayout.setLayoutParams(layoutParams1);
    }

    @Override
    public void onChemicalSelected(Chemical chemical) {
        DictionaryFragment fragment = (DictionaryFragment) getSupportFragmentManager()
                .findFragmentById(R.id.main_fragment_container);
        fragment.updateSearchEditText(chemical);
    }

    @Override
    public void onDialogFinished(boolean isChose, int requestCode) {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.main_fragment_container);
        if (fragment instanceof DictionaryFragment) {
            ((DictionaryFragment) fragment).onDialogFinished(isChose, requestCode);
        }
    }

    @Override
    public void onBackPressed() {
        if (mChemicalId > 0) {
            super.onBackPressed();
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        } else {
            if (mFragment instanceof DictionaryFragment) {
                ((DictionaryFragment) mFragment).onBackPressed();
            } else {
                super.onBackPressed();
            }
        }
    }
}
