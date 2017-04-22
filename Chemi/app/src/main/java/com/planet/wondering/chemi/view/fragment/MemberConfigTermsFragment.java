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

import java.util.ArrayList;

/**
 * Created by yoon on 2017. 3. 20..
 */

public class MemberConfigTermsFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = MemberConfigTermsFragment.class.getSimpleName();

    public static MemberConfigTermsFragment newInstance() {

        Bundle args = new Bundle();

        MemberConfigTermsFragment fragment = new MemberConfigTermsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private LinearLayout mBackLayout;

    private TabLayout mMemberConfigTermsTabLayout;
    private ViewPager mMemberConfigTermsViewPager;
    private ArrayList<Fragment> mMemberConfigTermsFragments;
    private ArrayList<String> mMemberConfigTermsFragmentTitles;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mMemberConfigTermsFragments = new ArrayList<>();
        mMemberConfigTermsFragmentTitles = new ArrayList<>();

        addFragment(MemberConfigTermsAgreementFragment.newInstance(), "이용약관");
        addFragment(MemberConfigTermsPrivacyFragment.newInstance(), "개인정보 취급방침");

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_member_config_terms, container, false);

        mBackLayout = (LinearLayout) view.findViewById(R.id.member_config_terms_back_layout);
        mBackLayout.setOnClickListener(this);

        mMemberConfigTermsTabLayout = (TabLayout) view.findViewById(R.id.member_config_terms_tab_layout);
        mMemberConfigTermsViewPager = (ViewPager) view.findViewById(R.id.member_config_terms_view_pager);

        mMemberConfigTermsViewPager.setAdapter(new FragmentPagerAdapter(getChildFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mMemberConfigTermsFragments.get(position);
            }

            @Override
            public int getCount() {
                return mMemberConfigTermsFragmentTitles.size();
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return mMemberConfigTermsFragmentTitles.get(position);
            }
        });
        mMemberConfigTermsTabLayout.setupWithViewPager(mMemberConfigTermsViewPager);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.member_config_terms_back_layout:
                getActivity().onBackPressed();
                break;
        }
    }

    private void addFragment(Fragment fragment, String title) {
        mMemberConfigTermsFragments.add(fragment);
        mMemberConfigTermsFragmentTitles.add(title);
    }
}
