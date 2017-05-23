package com.planet.wondering.chemi.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.planet.wondering.chemi.R;
import com.planet.wondering.chemi.util.listener.OnMenuSelectedListener;
import com.planet.wondering.chemi.view.activity.BottomNavigationActivity;
import com.planet.wondering.chemi.view.activity.MemberActivity;

/**
 * Created by yoon on 2017. 3. 25..
 */

public class MemberConfigSignInFragment extends Fragment implements View.OnClickListener {

    public static MemberConfigSignInFragment newInstance() {

        Bundle args = new Bundle();

        MemberConfigSignInFragment fragment = new MemberConfigSignInFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private AppBarLayout mMemberConfigSignInAppBarLayout;
    private Toolbar mMemberConfigSignInToolbar;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_member_config_sign_in, container, false);
        mMemberConfigSignInToolbar = (Toolbar) view.findViewById(R.id.member_config_sign_in_toolbar);
        ((MemberActivity) getActivity()).setSupportActionBar(mMemberConfigSignInToolbar);
        mMemberConfigSignInAppBarLayout = (AppBarLayout) view.findViewById(R.id.member_config_sign_in_app_bar_layout);
        mMemberConfigSignInAppBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_toolbar_member, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_config:
                mMenuSelectedListener.onMenuSelected(-1);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    OnMenuSelectedListener mMenuSelectedListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mMenuSelectedListener = (OnMenuSelectedListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnMenuSelectedListener");
        }
    }
}
