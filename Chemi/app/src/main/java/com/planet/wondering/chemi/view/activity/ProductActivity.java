package com.planet.wondering.chemi.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.planet.wondering.chemi.R;
import com.planet.wondering.chemi.common.AppBaseActivity;
import com.planet.wondering.chemi.model.Product;
import com.planet.wondering.chemi.model.storage.ProductStorage;
import com.planet.wondering.chemi.util.helper.BottomNavigationViewHelper;
import com.planet.wondering.chemi.view.fragment.ProductFragment;

/**
 * Created by yoon on 2017. 1. 18..
 */

public class ProductActivity extends AppBaseActivity
        implements BottomNavigationView.OnNavigationItemSelectedListener{

    private static final String TAG = SearchActivity.class.getSimpleName();

    private static final String EXTRA_PRODUCT_ID = "com.planet.wondering.chemi.product_id";

    private FragmentManager mFragmentManager;
    private Fragment mFragment;

    private int mProductId;
    private Product mProduct;

    private Toolbar mProductToolbar;

    protected BottomNavigationView mBottomNavigationView;
    public RelativeLayout mBottomNavigationLayout;

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

        mBottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation_view);
        BottomNavigationViewHelper.disableShiftMode(mBottomNavigationView);
        mBottomNavigationView.setOnNavigationItemSelectedListener(this);
        mBottomNavigationLayout = (RelativeLayout) findViewById(R.id.bottom_navigation_layout);
    }

    @Override
    protected void onResume() {
        super.onResume();
//        if (mCategoryId > 0) {
//            setupBottomNavigation(1);
//        } else {
            setupBottomNavigation(0);
//        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar_product, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_faq:
                Toast.makeText(getApplicationContext(), "action_faq", Toast.LENGTH_SHORT).show();
                break;
            case R.id.action_archive:
                Toast.makeText(getApplicationContext(), "action_archive", Toast.LENGTH_SHORT).show();
                break;
            case R.id.action_share:
                Toast.makeText(getApplicationContext(), "action_share", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                startActivity(SearchActivity.newIntent(getApplicationContext()));
                break;
            case R.id.action_category:
                startActivity(CategoryActivity.newIntent(getApplicationContext()));
                break;
            case R.id.action_content:
                startActivity(ContentActivity.newIntent(getApplicationContext()));
                break;
            case R.id.action_dictionary:
                startActivity(DictionaryActivity.newIntent(getApplicationContext()));
                break;
            case R.id.action_member:
                startActivity(MemberActivity.newIntent(getApplicationContext()));
                break;
        }
        return true;
    }

    private void setupBottomNavigation(int menuIndex) {
        mBottomNavigationView.getMenu().getItem(menuIndex).setChecked(true);
        mBottomNavigationView.getMenu().getItem(menuIndex).setEnabled(false);
    }

    private void showBottomNavigationView() {
        mBottomNavigationLayout.animate().translationY(0)
                .setInterpolator(new DecelerateInterpolator(2));
    }

    private void hideBottomNavigationView() {
        mBottomNavigationLayout.animate().translationY(mBottomNavigationLayout.getHeight())
                .setInterpolator(new AccelerateInterpolator(2));
    }

}
