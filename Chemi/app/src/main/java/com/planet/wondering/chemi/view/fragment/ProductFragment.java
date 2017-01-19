package com.planet.wondering.chemi.view.fragment;

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

import com.planet.wondering.chemi.R;
import com.planet.wondering.chemi.model.Product;
import com.planet.wondering.chemi.model.storage.ProductStorage;

import java.util.ArrayList;

/**
 * Created by yoon on 2017. 1. 18..
 */
public class ProductFragment extends Fragment {

    private static final String TAG = ProductFragment.class.getSimpleName();

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

    private int mProductId;
    private Product mProduct;

    private TabLayout mProductDetailTabLayout;
    private ViewPager mProductDetailViewPager;
    private ArrayList<Fragment> mProductDetailListFragments;
    private ArrayList<String> mProductDetailListFragmentTitles;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mProductId = getArguments().getInt(ARG_PRODUCT_ID, 0);
        mProduct = ProductStorage.getStorage(getActivity()).getProduct(mProductId);

        mProductDetailListFragments = new ArrayList<>();
        mProductDetailListFragmentTitles = new ArrayList<>();

        addProductDetailFragment(ChemicalListFragment.newInstance(mProduct.getId()),
                getString(R.string.product_detail_tab_title1));
        addProductDetailFragment(ReviewListFragment.newInstance(mProduct.getId()),
                getString(R.string.product_detail_tab_title2));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product, container, false);

        mProductDetailTabLayout = (TabLayout) view.findViewById(R.id.product_detail_tab_layout);
        mProductDetailViewPager = (ViewPager) view.findViewById(R.id.product_detail_view_pager);

        FragmentManager fm = getChildFragmentManager();
        mProductDetailViewPager.setAdapter(new FragmentPagerAdapter(fm) {
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
}
