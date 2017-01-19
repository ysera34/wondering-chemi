package com.planet.wondering.chemi.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;

import com.planet.wondering.chemi.R;
import com.planet.wondering.chemi.model.Product;
import com.planet.wondering.chemi.model.storage.ProductStorage;
import com.planet.wondering.chemi.view.fragment.ProductFragment;

/**
 * Created by yoon on 2017. 1. 18..
 */

public class ProductActivity extends BottomNavigationActivity {

    private static final String TAG = SearchActivity.class.getSimpleName();

    private static final String EXTRA_PRODUCT_ID = "com.planet.wondering.chemi.product_id";

    private FragmentManager mFragmentManager;
    private Fragment mFragment;

    private int mProductId;
    private Product mProduct;

    private Toolbar mProductToolbar;

    public static Intent newIntent(Context packageContext) {
        Intent intent = new Intent(packageContext, ProductActivity.class);
        return intent;
    }

    public static Intent newIntent(Context packageContext, int productId) {
        Intent intent = new Intent(packageContext, ProductActivity.class);
        intent.putExtra(EXTRA_PRODUCT_ID, productId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        mProductId = getIntent().getIntExtra(EXTRA_PRODUCT_ID, 0);
        mProduct = ProductStorage.getStorage(getApplicationContext()).getProduct(mProductId);

        mFragmentManager = getSupportFragmentManager();
        mFragment = mFragmentManager.findFragmentById(R.id.product_fragment_container);

        if (mFragment == null) {
            mFragment = ProductFragment.newInstance();
            mFragmentManager.beginTransaction()
                    .add(R.id.product_fragment_container, mFragment)
                    .commit();
        }

        mProductToolbar = (Toolbar) findViewById(R.id.product_detail_toolbar);
        setSupportActionBar(mProductToolbar);

        setTitle(mProduct.getName());
        mProductToolbar.setSubtitle(mProduct.getBrand());
    }

    @Override
    protected void onResume() {
        super.onResume();
//        if (mCategoryId > 0) {
//            setupBottomNavigation(1);
//        } else {
//            setupBottomNavigation(0);
//        }
    }
}
