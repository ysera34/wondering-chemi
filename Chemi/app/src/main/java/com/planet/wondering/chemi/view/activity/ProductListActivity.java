package com.planet.wondering.chemi.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import java.util.ArrayList;

/**
 * Created by yoon on 2017. 1. 17..
 */

public class ProductListActivity extends BottomNavigationActivity {

    private static final String EXTRA_PRODUCT_ID = "com.planet.wondering.chemi.product_id";
    private static final String EXTRA_PRODUCT_IDS = "com.planet.wondering.chemi.product_ids";
    private static final String EXTRA_CATEGORY_ID = "com.planet.wondering.chemi.category_id";

    private int mProductId;
    private ArrayList<Integer> mProductIds;
    private byte mCategoryId;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mProductId = getIntent().getIntExtra(EXTRA_PRODUCT_ID, 0);
        mProductIds = getIntent().getIntegerArrayListExtra(EXTRA_PRODUCT_IDS);
        mCategoryId = getIntent().getByteExtra(EXTRA_CATEGORY_ID, (byte) 0);
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
