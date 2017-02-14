package com.planet.wondering.chemi.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.planet.wondering.chemi.R;
import com.planet.wondering.chemi.view.activity.MemberStartActivity;

/**
 * Created by yoon on 2017. 2. 14..
 */

public class MemberStartLocalFragment extends Fragment
        implements View.OnClickListener {

    public static MemberStartLocalFragment newInstance() {

        Bundle args = new Bundle();

        MemberStartLocalFragment fragment = new MemberStartLocalFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private RelativeLayout mMemberStartLocalCancelLayout;
    private EditText mMemberStartLocalEmailEditText;
    private EditText mMemberStartLocalNameEditText;
    private EditText mMemberStartLocalPasswordEditText;
    private EditText mMemberStartLocalConfirmEditText;
    private TextView mMemberStartLocalPrivacyInfoTextView;
    private TextView mMemberStartLocalSubmitButtonTextView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_member_start_local, container, false);
        mMemberStartLocalCancelLayout = (RelativeLayout) view.findViewById(R.id.member_start_local_cancel_layout);
        mMemberStartLocalCancelLayout.setOnClickListener(this);
        mMemberStartLocalEmailEditText =
                (EditText) view.findViewById(R.id.member_start_local_email_edit_text);
        mMemberStartLocalNameEditText =
                (EditText) view.findViewById(R.id.member_start_local_name_edit_text);
        mMemberStartLocalPasswordEditText =
                (EditText) view.findViewById(R.id.member_start_local_pw_edit_text);
        mMemberStartLocalConfirmEditText =
                (EditText) view.findViewById(R.id.member_start_local_pw_confirm_edit_text);
        mMemberStartLocalPrivacyInfoTextView =
                (TextView) view.findViewById(R.id.member_start_local_privacy_info_text_view);
        mMemberStartLocalSubmitButtonTextView =
                (TextView) view.findViewById(R.id.member_start_local_submit_button_text_view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.member_start_local_cancel_layout:
                ((MemberStartActivity) getActivity()).cancelSignInLocal();
                break;
        }
    }
}
