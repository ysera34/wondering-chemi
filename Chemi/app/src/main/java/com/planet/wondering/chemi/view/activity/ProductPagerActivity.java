package com.planet.wondering.chemi.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.planet.wondering.chemi.R;
import com.planet.wondering.chemi.view.fragment.ProductPagerFragment;

import java.util.ArrayList;

/**
 * Created by yoon on 2017. 2. 5..
 */

public class ProductPagerActivity extends BottomNavigationActivity {

    private static final String TAG = ProductPagerActivity.class.getSimpleName();

    private static final String EXTRA_PRODUCT_ID = "com.planet.wondering.chemi.product_id";
    private static final String EXTRA_PRODUCT_IDS = "com.planet.wondering.chemi_product_ids";

    public static Intent newIntent(Context packageContext, int productId) {
        Intent intent = new Intent(packageContext, ProductPagerActivity.class);
        intent.putExtra(EXTRA_PRODUCT_ID, productId);
        return intent;
    }

    public static Intent newIntent(Context packageContext, ArrayList<Integer> productIds, int productId) {
        Intent intent = new Intent(packageContext, ProductPagerActivity.class);
        intent.putIntegerArrayListExtra(EXTRA_PRODUCT_IDS, productIds);
        intent.putExtra(EXTRA_PRODUCT_ID, productId);
        return intent;
    }

    private FragmentManager mFragmentManager;
    private Fragment mFragment;

//    private ViewPager mProductViewPager;
    private ArrayList<Integer> mProductIds;
    private int mProductId;
    private byte mCategoryId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_product_pager);
//        mProductViewPager = (ViewPager) findViewById(R.id.activity_product_pager_view_pager);
//        mProductIds = new ArrayList<>();
        mProductIds = getIntent().getIntegerArrayListExtra(EXTRA_PRODUCT_IDS);
        mProductId = getIntent().getIntExtra(EXTRA_PRODUCT_ID, 0);

        mFragmentManager = getSupportFragmentManager();
        mFragment = mFragmentManager.findFragmentById(R.id.fragment_container);

        if (mFragment == null) {
            mFragment = ProductPagerFragment.newInstance(mProductIds, mProductId);
            mFragmentManager.beginTransaction()
//                    .setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
                    .add(R.id.fragment_container, mFragment)
                    .commit();
        }

//        mProductViewPager.setAdapter(new FragmentStatePagerAdapter(fm) {
//            @Override
//            public Fragment getItem(int position) {
//                return ProductFragment.newInstance(mProductIds.get(position));
//            }
//
//            @Override
//            public int getCount() {
//                return mProductIds.size();
//            }
//        });
//
//        for (int i = 0; i < mProductIds.size(); i++) {
//            if (mProductIds.get(i) == mProductId) {
//                mProductViewPager.setCurrentItem(i);
//                break;
//            }
//        }
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
