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
import android.widget.EditText;

import com.planet.wondering.chemi.R;

import java.util.ArrayList;

/**
 * Created by yoon on 2017. 1. 5..
 */

public class SearchDetailFragment extends Fragment {

    private static final String TAG = SearchDetailFragment.class.getSimpleName();

    private EditText mSearchEditText;

    private TabLayout mSearchTabLayout;
    private ViewPager mSearchViewPager;
    private ArrayList<Fragment> mSearchListFragments;
    private ArrayList<String> mSearchListFragmentsTitles;

    public static SearchDetailFragment newInstance() {

        Bundle args = new Bundle();

        SearchDetailFragment fragment = new SearchDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mSearchListFragments = new ArrayList<>();
        mSearchListFragmentsTitles = new ArrayList<>();

        addSearchFragment(SearchPopularListFragment.newInstance(),
                getString(R.string.search_popular_fragment_title));
        addSearchFragment(SearchLatestListFragment.newInstance(),
                getString(R.string.search_latest_fragment_title));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_detail, container, false);
        mSearchEditText = (EditText) view.findViewById(R.id.search_edit_text);

        mSearchTabLayout = (TabLayout) view.findViewById(R.id.search_tabLayout);
        mSearchViewPager = (ViewPager) view.findViewById(R.id.search_viewPager);

        FragmentManager fm = getChildFragmentManager();
        mSearchViewPager.setAdapter(new FragmentPagerAdapter(fm) {
            @Override
            public Fragment getItem(int position) {
                return mSearchListFragments.get(position);
            }

            @Override
            public int getCount() {
                return mSearchListFragments.size();
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return mSearchListFragmentsTitles.get(position);
            }
        });

        mSearchTabLayout.setupWithViewPager(mSearchViewPager);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void addSearchFragment(Fragment fragment, String title) {
        mSearchListFragments.add(fragment);
        mSearchListFragmentsTitles.add(title);
    }

    public void updateSearchEditText(String searchWord) {
        mSearchEditText.setText(String.valueOf(searchWord));
    }
}
