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

import com.planet.wondering.chemi.R;

import java.util.ArrayList;

/**
 * Created by yoon on 2016. 12. 31..
 */

public class ContentPagerFragment extends Fragment {

    public static final String TAG = ContentPagerFragment.class.getSimpleName();

    public static ContentPagerFragment newInstance() {

        Bundle args = new Bundle();

        ContentPagerFragment fragment = new ContentPagerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private TabLayout mContentTabLayout;
    private ViewPager mContentViewPager;
    private ArrayList<Fragment> mContentFragments;
    private ArrayList<String> mContentFragmentTitles;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContentFragments = new ArrayList<>();
        mContentFragmentTitles = new ArrayList<>();

        addFragment(ContentListFragment.newInstance(), "케미데스크");
        addFragment(ContentListFragment.newInstance(), "케미 PICK");
        addFragment(ContentListFragment.newInstance(), "케미라이프");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_content_pager, container, false);

        mContentTabLayout = (TabLayout) view.findViewById(R.id.content_tab_layout);
        mContentViewPager = (ViewPager) view.findViewById(R.id.content_view_pager);

        mContentViewPager.setOffscreenPageLimit(mContentFragments.size() - 1);
        mContentViewPager.setAdapter(new FragmentPagerAdapter(getChildFragmentManager()) {

            @Override
            public Fragment getItem(int position) {
                Bundle bundle = new Bundle();
                bundle.putInt("category_id", position + 1);
                mContentFragments.get(position).setArguments(bundle);
                return mContentFragments.get(position);
            }

            @Override
            public int getCount() {
                return mContentFragmentTitles.size();
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return mContentFragmentTitles.get(position);
            }
        });
        mContentTabLayout.setupWithViewPager(mContentViewPager);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void addFragment(Fragment fragment, String title) {
        mContentFragments.add(fragment);
        mContentFragmentTitles.add(title);
    }
}
