package com.planet.wondering.chemi.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.planet.wondering.chemi.R;

import java.util.ArrayList;

/**
 * Created by yoon on 2017. 2. 5..
 */

public class ProductPagerFragment extends Fragment {

    private static final String ARG_PRODUCT_ID = "product_id";
    private static final String ARG_PRODUCT_IDS = "product_ids";

    public static ProductPagerFragment newInstance() {

        Bundle args = new Bundle();

        ProductPagerFragment fragment = new ProductPagerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static ProductPagerFragment newInstance(ArrayList<Integer> productIds, int productId) {

        Bundle args = new Bundle();
        args.putIntegerArrayList(ARG_PRODUCT_IDS, productIds);
        args.putInt(ARG_PRODUCT_ID, productId);

        ProductPagerFragment fragment = new ProductPagerFragment();
        fragment.setArguments(args);

        return fragment;
    }

    private ViewPager mProductViewPager;
    private ArrayList<Integer> mProductIds;
    private int mProductId;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mProductIds = getArguments().getIntegerArrayList(ARG_PRODUCT_IDS);
        mProductId = getArguments().getInt(ARG_PRODUCT_ID);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product_pager, container, false);
        mProductViewPager = (ViewPager) view.findViewById(R.id.fragment_product_pager_view_pager);
        mProductViewPager.setAdapter(new FragmentStatePagerAdapter(getChildFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return ProductFragment.newInstance(mProductIds.get(position));
            }

            @Override
            public int getCount() {
                return mProductIds.size();
            }
        });
        for (int i = 0; i < mProductIds.size(); i++) {
            if (mProductIds.get(i) == mProductId) {
                mProductViewPager.setCurrentItem(i);
                break;
            }
        }
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
