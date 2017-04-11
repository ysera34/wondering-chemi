package com.planet.wondering.chemi.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;

import com.planet.wondering.chemi.R;
import com.planet.wondering.chemi.model.Tag;
import com.planet.wondering.chemi.util.helper.TagSharedPreferences;
import com.planet.wondering.chemi.view.fragment.ProductListFragment;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by yoon on 2017. 1. 17..
 */

public class ProductListActivity extends BottomNavigationActivity {

    private static final String TAG = ProductListActivity.class.getSimpleName();

    private static final String EXTRA_PRODUCT_ID = "com.planet.wondering.chemi.product_id";
    private static final String EXTRA_PRODUCT_IDS = "com.planet.wondering.chemi.product_ids";
    private static final String EXTRA_CATEGORY_ID = "com.planet.wondering.chemi.category_id";
    private static final String EXTRA_TAG_NAME = "com.planet.wondering.chemi.tag_name";

    public static Intent newIntent(Context packageContext) {
        Intent intent = new Intent(packageContext, ProductListActivity.class);
        return intent;
    }

    public static Intent newIntent(Context packageContext, int productId) {
        Intent intent = new Intent(packageContext, ProductListActivity.class);
        intent.putExtra(EXTRA_PRODUCT_ID, productId);
        return intent;
    }

    public static Intent newIntent(Context packageContext, ArrayList<Integer> productIds) {
        Intent intent = new Intent(packageContext, ProductListActivity.class);
        intent.putIntegerArrayListExtra(EXTRA_PRODUCT_IDS, productIds);
        return intent;
    }

    public static Intent newIntent(Context packageContext, byte categoryId) {
        Intent intent = new Intent(packageContext, ProductListActivity.class);
        intent.putExtra(EXTRA_CATEGORY_ID, categoryId);
        return intent;
    }

    public static Intent newIntent(Context packageContext, String tagName) {
        Intent intent = new Intent(packageContext, ProductListActivity.class);
        intent.putExtra(EXTRA_TAG_NAME, tagName);
        Tag tag = new Tag();
        tag.setName(tagName);
        tag.setRankDate(new Date());
        TagSharedPreferences.addStoreTag(packageContext, tag);
        return intent;
    }

    private FragmentManager mFragmentManager;
    private Fragment mFragment;

    private int mProductId;
    private ArrayList<Integer> mProductIds;
    private byte mCategoryId;
    private short mShortCategoryId;
    private String mTagName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mProductId = getIntent().getIntExtra(EXTRA_PRODUCT_ID, 0);
        mProductIds = getIntent().getIntegerArrayListExtra(EXTRA_PRODUCT_IDS);
        Log.i(TAG, "mCategoryId " + String.valueOf(mCategoryId));
        mCategoryId = getIntent().getByteExtra(EXTRA_CATEGORY_ID, (byte) -1);
        Log.i(TAG, "mShortCategoryId " + String.valueOf(mShortCategoryId));
        mShortCategoryId = getIntent().getShortExtra("short_category_id", (short) -1);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Log.i(TAG, "mCategoryId " + String.valueOf(mCategoryId));
            Log.i(TAG, "mShortCategoryId " + String.valueOf(mShortCategoryId));

            Log.i(TAG, "mCategoryId + 1 = " + String.valueOf(mCategoryId + 1));
            Log.i(TAG, "mShortCategoryId + 1 = " + String.valueOf(mShortCategoryId + 1));

            if (mCategoryId > 30) {
                mCategoryId = -1;
                Log.i(TAG, "mCategoryId " + String.valueOf(mCategoryId));
            }

            if (mShortCategoryId > 30) {
                mShortCategoryId = -1;
                Log.i(TAG, "mShortCategoryId " + String.valueOf(mShortCategoryId));
            }
        }
        Log.i(TAG, "mCategoryId " + String.valueOf(mCategoryId));
        Log.i(TAG, "mShortCategoryId " + String.valueOf(mShortCategoryId));
        mTagName = getIntent().getStringExtra(EXTRA_TAG_NAME);


        mFragmentManager = getSupportFragmentManager();
        mFragment = mFragmentManager.findFragmentById(R.id.main_fragment_container);

        if (mFragment == null && mCategoryId == -1) {
            mFragment = ProductListFragment.newInstance(mTagName);
        } else if (mCategoryId != -1) {
            mFragment = ProductListFragment.newInstance(mCategoryId);
        }
            mFragmentManager.beginTransaction()
//                    .setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
                    .add(R.id.main_fragment_container, mFragment)
                    .commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mCategoryId > 0) {
            setupBottomNavigation(1);
        } else {
            setupBottomNavigation(0);
        }
    }
}
