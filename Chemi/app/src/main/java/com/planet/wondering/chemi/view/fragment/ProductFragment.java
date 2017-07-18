package com.planet.wondering.chemi.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;

import com.planet.wondering.chemi.R;
import com.planet.wondering.chemi.model.Product;

import java.util.ArrayList;

import static com.planet.wondering.chemi.common.Common.REVIEW_READ_REQUEST_CODE;

/**
 * Created by yoon on 2017. 1. 18..
 */
public class ProductFragment extends Fragment {

    private static final String TAG = ProductFragment.class.getSimpleName();

    private static final String ARG_PRODUCT = "product";
    private static final String ARG_PRODUCT_ID = "product_id";

    public static ProductFragment newInstance() {
        
        Bundle args = new Bundle();
        
        ProductFragment fragment = new ProductFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static ProductFragment newInstance(int productId) {

        Bundle args = new Bundle();
        args.putInt(ARG_PRODUCT_ID, productId);

        ProductFragment fragment = new ProductFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static ProductFragment newInstance(Product product) {

        Bundle args = new Bundle();
        args.putSerializable(ARG_PRODUCT, product);

        ProductFragment fragment = new ProductFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private int mProductId;
    private Product mProduct;

    private TabLayout mProductDetailTabLayout;
    private ViewPager mProductDetailViewPager;
    private ArrayList<Fragment> mProductDetailListFragments;
    private ArrayList<String> mProductDetailListFragmentTitles;

    private ProductFragmentPagerAdapter mFragmentPagerAdapter;

    private LinearLayout mReviewCreateLayout;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mProductId = getArguments().getInt(ARG_PRODUCT_ID, 0);
        mProduct = (Product) getArguments().getSerializable(ARG_PRODUCT);

        mProductDetailListFragments = new ArrayList<>();
        mProductDetailListFragmentTitles = new ArrayList<>();

        addProductDetailFragment(ReviewListFragment.newInstance(mProduct),
                getString(R.string.product_detail_tab_title2));
        addProductDetailFragment(ChemicalListFragment.newInstance(mProduct),
                getString(R.string.product_detail_tab_title1));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product, container, false);

        mProductDetailTabLayout = (TabLayout) view.findViewById(R.id.product_detail_tab_layout);
        mProductDetailViewPager = (ViewPager) view.findViewById(R.id.product_detail_view_pager);
        mReviewCreateLayout = (LinearLayout) view.findViewById(R.id.review_create_layout);

//        mChildFragment = mChildFragmentManager.findFragmentById(R.id.product_fragment_container);
//        if (mChildFragment == null) {
//            mChildFragmentManager.beginTransaction()
//                    .add(R.id.product_fragment_container, ChemicalChartFragment.newInstance())
//                    .commit();
//        }

        mFragmentPagerAdapter = new ProductFragmentPagerAdapter(getChildFragmentManager());
        mProductDetailViewPager.setAdapter(mFragmentPagerAdapter);
        mProductDetailViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    showReviewCreateLayout();
                } else if (position == 1) {
                    hideReviewCreateLayout();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mProductDetailTabLayout.setupWithViewPager(mProductDetailViewPager);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void addProductDetailFragment(Fragment fragment, String title) {
        mProductDetailListFragments.add(fragment);
        mProductDetailListFragmentTitles.add(title);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REVIEW_READ_REQUEST_CODE:
            for (Fragment fragment : getChildFragmentManager().getFragments()) {
                fragment.onActivityResult(requestCode, resultCode, data);
            }
        }
    }

    public void updateFragmentTitles(Product product) {
        mFragmentPagerAdapter.setFragmentTitles(1, getString(R.string.product_detail_tab_title2) +
                getString(R.string.product_detail_tab_title2_description, String.valueOf(product.getRatingCount())));
        mFragmentPagerAdapter.notifyDataSetChanged();
    }

    private class ProductFragmentPagerAdapter extends FragmentPagerAdapter {

        public ProductFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mProductDetailListFragments.get(position);
        }

        @Override
        public int getCount() {
            return mProductDetailListFragmentTitles.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mProductDetailListFragmentTitles.get(position);
        }

        public void setFragmentTitles(int position, String title) {
            mProductDetailListFragmentTitles.set(position, title);
        }
    }

    public void showReviewCreateLayout() {
        mProductDetailViewPager.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 0));
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mProductDetailViewPager.getLayoutParams();
        params.height = 0;
        params.weight = 1;
        mProductDetailViewPager.setLayoutParams(params);
        mReviewCreateLayout.animate().translationY(0)
                .setInterpolator(new DecelerateInterpolator(2));
    }

    public void hideReviewCreateLayout() {

        mProductDetailViewPager.postDelayed(new Runnable() {
            @Override
            public void run() {
                mReviewCreateLayout.animate().translationY(mReviewCreateLayout.getHeight())
                        .setInterpolator(new AccelerateInterpolator(2));
            }
        }, 200);
        mProductDetailViewPager.postDelayed(new Runnable() {
            @Override
            public void run() {
                mProductDetailViewPager.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
            }
        }, 500);
    }
}
