package com.planet.wondering.chemi.view.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.planet.wondering.chemi.R;
import com.planet.wondering.chemi.view.activity.BottomNavigationActivity;
import com.planet.wondering.chemi.view.activity.MemberActivity;

/**
 * Created by yoon on 2016. 12. 31..
 */

public class MemberFragment extends Fragment {

    private static final String TAG = MemberFragment.class.getSimpleName();

    public static MemberFragment newInstance() {

        Bundle args = new Bundle();

        MemberFragment fragment = new MemberFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private AppBarLayout mMemberAppBarLayout;
    private Toolbar mMemberToolbar;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_member, container, false);

        mMemberToolbar = (Toolbar) view.findViewById(R.id.member_toolbar);
        ((MemberActivity) getActivity()).setSupportActionBar(mMemberToolbar);
        mMemberAppBarLayout = (AppBarLayout) view.findViewById(R.id.member_app_bar_layout);
        mMemberAppBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, final int verticalOffset) {
//                if (Math.abs(verticalOffset)-appBarLayout.getTotalScrollRange() == 0) {
//                if (verticalOffset == -mMemberAppBarLayout.getHeight() + mMemberToolbar.getHeight()) {
                if (verticalOffset < -appBarLayout.getTotalScrollRange() / 2) {
                    ((MemberActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(true);
                    ((BottomNavigationActivity) getActivity()).hideBottomNavigationView();
                } else {
                    ((MemberActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
                    ((BottomNavigationActivity) getActivity()).showBottomNavigationView();
                }
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
