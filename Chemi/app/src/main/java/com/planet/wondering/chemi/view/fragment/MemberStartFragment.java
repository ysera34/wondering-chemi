package com.planet.wondering.chemi.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.planet.wondering.chemi.R;
import com.planet.wondering.chemi.util.listener.OnMenuSelectedListener;

/**
 * Created by yoon on 2017. 2. 12..
 */

public class MemberStartFragment extends Fragment
        implements View.OnClickListener {

    private static final String TAG = MemberStartFragment.class.getSimpleName();

    public static MemberStartFragment newInstance() {

        Bundle args = new Bundle();

        MemberStartFragment fragment = new MemberStartFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private RelativeLayout mStartNaverLayout;
    private RelativeLayout mStartGoogleLayout;
    private RelativeLayout mStartLocalLayout;

    private TextView mStartBrowseTextView;
    private TextView mStartAlreadyUserTextView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_member_start, container, false);

        mStartNaverLayout = (RelativeLayout) view.findViewById(R.id.member_start_naver_layout);
        mStartNaverLayout.setOnClickListener(this);
        mStartGoogleLayout = (RelativeLayout) view.findViewById(R.id.member_start_google_layout);
        mStartGoogleLayout.setOnClickListener(this);
        mStartLocalLayout = (RelativeLayout) view.findViewById(R.id.member_start_local_layout);
        mStartLocalLayout.setOnClickListener(this);
        mStartBrowseTextView = (TextView) view.findViewById(R.id.member_start_browse_text_view);
        mStartBrowseTextView.setOnClickListener(this);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.member_start_naver_layout:
                mMenuSelectedListener.onMenuSelected(7020);
                break;
            case R.id.member_start_google_layout:
                mMenuSelectedListener.onMenuSelected(7010);
                break;
            case R.id.member_start_local_layout:
                mMenuSelectedListener.onMenuSelected(7002);
                break;
            case R.id.member_start_browse_text_view:
                mMenuSelectedListener.onMenuSelected(7000);
                break;
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
