package com.planet.wondering.chemi.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.planet.wondering.chemi.R;
import com.planet.wondering.chemi.view.custom.CategoryGroupView;

import java.util.ArrayList;

/**
 * Created by yoon on 2017. 7. 1..
 */

public class CategoryFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = CategoryFragment.class.getSimpleName();

    private static final String ARG_CATEGORY_GROUP_ID = "category_group_id";

    public static CategoryFragment newInstance() {

        Bundle args = new Bundle();

        CategoryFragment fragment = new CategoryFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static CategoryFragment newInstance(int categoryGroupId) {

        Bundle args = new Bundle();
        args.putInt(ARG_CATEGORY_GROUP_ID, categoryGroupId);

        CategoryFragment fragment = new CategoryFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private int mCategoryGroupId;
    private LinearLayout mCategoryHeaderLayout;
    private TabLayout mCategoryGroupTabLayout;
    private ViewPager mCategoryGroupViewPager;
    private ArrayList<Fragment> mCategoryDetailFragments;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCategoryGroupId = getArguments().getInt(ARG_CATEGORY_GROUP_ID, -1);

        mCategoryDetailFragments = new ArrayList<>();
        mCategoryDetailFragments.add(CategoryDetailFragment.newInstance(0));
        mCategoryDetailFragments.add(CategoryDetailFragment.newInstance(1));
        mCategoryDetailFragments.add(CategoryDetailFragment.newInstance(2));
        mCategoryDetailFragments.add(CategoryDetailFragment.newInstance(3));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category, container, false);
        mCategoryHeaderLayout = (LinearLayout) view.findViewById(R.id.category_header_layout);
        mCategoryHeaderLayout.setOnClickListener(this);
        mCategoryGroupTabLayout = (TabLayout) view.findViewById(R.id.category_group_tab_layout);
        mCategoryGroupViewPager = (ViewPager) view.findViewById(R.id.category_group_view_pager);

        mCategoryGroupViewPager.setOffscreenPageLimit(mCategoryDetailFragments.size() - 1);
        mCategoryGroupViewPager.setAdapter(new FragmentPagerAdapter(getChildFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mCategoryDetailFragments.get(position);
            }

            @Override
            public int getCount() {
                return mCategoryDetailFragments.size();
            }
        });
        mCategoryGroupTabLayout.setupWithViewPager(mCategoryGroupViewPager);
        setCategoryGroupTabLayout();
        mCategoryGroupTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                mCategoryGroupViews[position].setCategoryIcon(mTabSelectedIconResIds[position]);
                mCategoryGroupViews[position].setCategoryNameColor(R.color.colorPrimary);
                tab.setCustomView(mCategoryGroupViews[position]);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                mCategoryGroupViews[position].setCategoryIcon(mTabUnSelectedIconResIds[position]);
                mCategoryGroupViews[position].setCategoryNameColor(R.color.colorArmadillo);
                tab.setCustomView(mCategoryGroupViews[position]);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mCategoryGroupViewPager.setCurrentItem(1);
        if (mCategoryGroupId > -1) {
            mCategoryGroupViewPager.setCurrentItem(mCategoryGroupId);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.category_header_layout:
                getActivity().onBackPressed();
                break;
        }
    }

    private CategoryGroupView[] mCategoryGroupViews;
    private int[] mTabUnSelectedIconResIds = {
            R.drawable.ic_category_baby_circle_false, R.drawable.ic_category_female_circle_false,
            R.drawable.ic_category_general_circle_false, R.drawable.ic_category_living_circle_false,};
    private int[] mTabSelectedIconResIds = {
            R.drawable.ic_category_baby_circle_true, R.drawable.ic_category_female_circle_true,
            R.drawable.ic_category_general_circle_true, R.drawable.ic_category_living_circle_true,};
    private int[] mTabTitleResIds = {
            R.string.category_group_0_name, R.string.category_group_1_name,
            R.string.category_group_2_name, R.string.category_group_3_name,};

    private void setCategoryGroupTabLayout() {

        mCategoryGroupViews = new CategoryGroupView[mTabUnSelectedIconResIds.length];
        for (int i = 0; i < mCategoryGroupViews.length; i++) {
            CategoryGroupView groupView = new CategoryGroupView(getContext(), null,
                    mTabUnSelectedIconResIds[i], getString(mTabTitleResIds[i]));
            mCategoryGroupViews[i] = groupView;
        }
//        CategoryGroupView groupView0 = new CategoryGroupView(getContext(), null,
//                R.drawable.ic_category_baby_circle_false, getString(R.string.category_group_name_1));
//        CategoryGroupView groupView1 = new CategoryGroupView(getContext(), null,
//                R.drawable.ic_category_female_circle_false, getString(R.string.category_group_name_2));
//        CategoryGroupView groupView2 = new CategoryGroupView(getContext(), null,
//                R.drawable.ic_category_general_circle_false, getString(R.string.category_group_name_3));
//        CategoryGroupView groupView3 = new CategoryGroupView(getContext(), null,
//                R.drawable.ic_category_living_circle_false, getString(R.string.category_group_name_4));
//        mCategoryGroupTabLayout.getTabAt(0).setCustomView(groupView0);
//        mCategoryGroupTabLayout.getTabAt(1).setCustomView(groupView1);
//        mCategoryGroupTabLayout.getTabAt(2).setCustomView(groupView2);
//        mCategoryGroupTabLayout.getTabAt(3).setCustomView(groupView3);

//        mCategoryGroupTabLayout.getTabAt(0).setIcon(getResources().getDrawable(mTabUnSelectedIconResIds[0]));
//        mCategoryGroupTabLayout.getTabAt(0).setText(getString(mTabTitleResIds[0]));
//        mCategoryGroupTabLayout.getTabAt(1).setIcon(getResources().getDrawable(mTabUnSelectedIconResIds[1]));
//        mCategoryGroupTabLayout.getTabAt(1).setText(getString(mTabTitleResIds[1]));
//        mCategoryGroupTabLayout.getTabAt(2).setIcon(getResources().getDrawable(mTabUnSelectedIconResIds[2]));
//        mCategoryGroupTabLayout.getTabAt(2).setText(getString(mTabTitleResIds[2]));
//        mCategoryGroupTabLayout.getTabAt(3).setIcon(getResources().getDrawable(mTabUnSelectedIconResIds[3]));
//        mCategoryGroupTabLayout.getTabAt(3).setText(getString(mTabTitleResIds[3]));


        for (int i = 0; i < mTabUnSelectedIconResIds.length; i++) {
//            mCategoryGroupTabLayout.getTabAt(i).setIcon(getResources().getDrawable(mTabUnSelectedIconResIds[i]));
//            mCategoryGroupTabLayout.getTabAt(i).setText(getString(mTabTitleResIds[i]));

            mCategoryGroupTabLayout.getTabAt(i).setCustomView(mCategoryGroupViews[i]);
        }

    }
}
