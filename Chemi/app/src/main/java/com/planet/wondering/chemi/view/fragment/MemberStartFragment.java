package com.planet.wondering.chemi.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.planet.wondering.chemi.R;
import com.planet.wondering.chemi.view.activity.MemberStartActivity;
import com.planet.wondering.chemi.view.activity.SearchActivity;

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

    private TextView mStartNaverTextView;
    private TextView mStartGoogleTextView;
    private TextView mStartLocalTextView;
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
        mStartNaverTextView = (TextView) view.findViewById(R.id.member_start_naver_text_view);
//        mStartNaverTextView.setOnClickListener(this);
        mStartGoogleLayout = (RelativeLayout) view.findViewById(R.id.member_start_google_layout);
        mStartGoogleLayout.setOnClickListener(this);
        mStartGoogleTextView = (TextView) view.findViewById(R.id.member_start_google_text_view);
//        mStartGoogleTextView.setOnClickListener(this);
        mStartLocalLayout = (RelativeLayout) view.findViewById(R.id.member_start_local_layout);
        mStartLocalLayout.setOnClickListener(this);
        mStartLocalTextView = (TextView) view.findViewById(R.id.member_start_local_text_view);
//        mStartLocalTextView.setOnClickListener(this);
        mStartBrowseTextView = (TextView) view.findViewById(R.id.member_start_browse_text_view);
        mStartBrowseTextView.setOnClickListener(this);
        mStartAlreadyUserTextView = (TextView) view.findViewById(R.id.member_start_already_user_text_view);
        mStartAlreadyUserTextView.setOnClickListener(this);

        view.findViewById(R.id.member_start_info_button).setOnClickListener(this);
        view.findViewById(R.id.member_survey_info_button).setOnClickListener(this);

        view.findViewById(R.id.naver_sign_out_button).setOnClickListener(this);
        view.findViewById(R.id.google_sign_out_button).setOnClickListener(this);
        view.findViewById(R.id.google_revoke_button).setOnClickListener(this);
        view.findViewById(R.id.naver_revoke_button).setOnClickListener(this);
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
            case R.id.member_start_naver_text_view:
                ((MemberStartActivity) getActivity()).signInNaver();
                break;
            case R.id.member_start_google_layout:
            case R.id.member_start_google_text_view:
                ((MemberStartActivity) getActivity()).signInGoogle();
                break;
            case R.id.member_start_local_layout:
            case R.id.member_start_local_text_view:
                ((MemberStartActivity) getActivity()).signUpForLocal();
                break;
            case R.id.member_start_browse_text_view:
                startActivity(SearchActivity.newIntent(getActivity()));
                getActivity().finish();
                break;
            case R.id.member_start_already_user_text_view:
                ((MemberStartActivity) getActivity()).signInLocal();
                break;

            case R.id.member_start_info_button:
                ((MemberStartActivity) getActivity()).moveToFragment(2);
                break;
            case R.id.member_survey_info_button:
                ((MemberStartActivity) getActivity()).moveToFragment(3);

            case R.id.naver_sign_out_button:
                ((MemberStartActivity) getActivity()).signOutNaver();
                break;
            case R.id.naver_revoke_button:
                ((MemberStartActivity) getActivity()).revokeAccessNaver();
                break;
            case R.id.google_sign_out_button:
                ((MemberStartActivity) getActivity()).signOutGoogle();
                break;
            case R.id.google_revoke_button:
                ((MemberStartActivity) getActivity()).revokeAccessGoogle();
                break;
        }
    }
}
