package com.planet.wondering.chemi.view.activity;

import android.content.Context;
import android.content.Intent;
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
    private String mTag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mProductId = getIntent().getIntExtra(EXTRA_PRODUCT_ID, 0);
        mProductIds = getIntent().getIntegerArrayListExtra(EXTRA_PRODUCT_IDS);
        mCategoryId = getIntent().getByteExtra(EXTRA_CATEGORY_ID, (byte) 0);
        mTag = getIntent().getStringExtra(EXTRA_TAG_NAME);
        Log.i(TAG, mTag);

        mFragmentManager = getSupportFragmentManager();
        mFragment = mFragmentManager.findFragmentById(R.id.fragment_container);

        if (mFragment == null) {
            mFragment = ProductListFragment.newInstance();
            mFragmentManager.beginTransaction()
//                    .setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
                    .add(R.id.fragment_container, mFragment)
                    .commit();
        }
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
