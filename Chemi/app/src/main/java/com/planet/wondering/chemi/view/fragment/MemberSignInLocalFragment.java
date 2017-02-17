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

/**
 * Created by yoon on 2017. 2. 18..
 */

public class MemberSignInLocalFragment extends Fragment
        implements View.OnClickListener {

    public static MemberSignInLocalFragment newInstance() {

        Bundle args = new Bundle();

        MemberSignInLocalFragment fragment = new MemberSignInLocalFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private RelativeLayout mMemberSignInCancelLayout;

    private TextView mMemberSignInForgetPasswordTextView;
    private TextView mMemberSignInRecommendUserTextView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_member_sign_in_local, container, false);
        mMemberSignInCancelLayout = (RelativeLayout) view.findViewById(R.id.member_sign_in_cancel_layout);
        mMemberSignInCancelLayout.setOnClickListener(this);
        mMemberSignInForgetPasswordTextView = (TextView)
                view.findViewById(R.id.member_sign_in_forget_password_text_view);
        mMemberSignInForgetPasswordTextView.setOnClickListener(this);
        mMemberSignInRecommendUserTextView = (TextView)
                view.findViewById(R.id.member_sign_in_recommend_user_text_view);
        mMemberSignInRecommendUserTextView.setOnClickListener(this);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.member_sign_in_cancel_layout:
                ((MemberStartActivity) getActivity()).cancelSignInLocal();
                break;
            case R.id.member_sign_in_forget_password_text_view:
                ((MemberStartActivity) getActivity()).findPassword();
                break;
            case R.id.member_sign_in_recommend_user_text_view:
                ((MemberStartActivity) getActivity()).cancelSignInLocal();
                break;
        }
    }
}
