package com.planet.wondering.chemi.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.planet.wondering.chemi.R;
import com.planet.wondering.chemi.util.helper.UserSharedPreferences;
import com.planet.wondering.chemi.util.listener.OnMenuSelectedListener;

/**
 * Created by yoon on 2017. 2. 10..
 */

public class MemberConfigFragment extends Fragment
        implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    public static MemberConfigFragment newInstance() {

        Bundle args = new Bundle();

        MemberConfigFragment fragment = new MemberConfigFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private int[] mConfigLayoutIds;
    private RelativeLayout[] mConfigLayouts;
    private TextView mMemeberConfigProfileTextView;
    private Switch mPushSwitch;
    private Switch mEmailSwitch;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mConfigLayoutIds = new int[]{
                R.id.member_config_profile_layout, R.id.member_config_notice_layout,
                R.id.member_config_request_product_layout,
                R.id.member_config_faq_layout, R.id.member_config_version_layout,
                R.id.member_config_push_notification_layout, R.id.member_config_email_layout,
                R.id.member_config_privacy_layout, R.id.member_config_collaboration_layout};
        mConfigLayouts = new RelativeLayout[mConfigLayoutIds.length];
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_member_config, container, false);

        for (int i = 0; i < mConfigLayoutIds.length; i++) {
            mConfigLayouts[i] = (RelativeLayout) view.findViewById(mConfigLayoutIds[i]);
            mConfigLayouts[i].setOnClickListener(this);
        }
        mMemeberConfigProfileTextView = (TextView) view.findViewById(R.id.member_config_profile_text_view);

        mPushSwitch = (Switch) view.findViewById(R.id.member_config_push_notification_switch);
        mPushSwitch.setOnCheckedChangeListener(this);
        mEmailSwitch = (Switch) view.findViewById(R.id.member_config_email_switch);
        mEmailSwitch.setOnCheckedChangeListener(this);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (UserSharedPreferences.getStoredToken(getActivity()) == null) {
            mMemeberConfigProfileTextView.setText("회원 가입 및 로그인");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.member_config_profile_layout:
                mMenuSelectedListener.onMenuSelected(1);
                break;
            case R.id.member_config_notice_layout:
                mMenuSelectedListener.onMenuSelected(2);
                break;
            case R.id.member_config_request_product_layout:
                mMenuSelectedListener.onMenuSelected(3);
                break;
            case R.id.member_config_faq_layout:
                mMenuSelectedListener.onMenuSelected(4);
                break;
            case R.id.member_config_version_layout:
                break;
            case R.id.member_config_privacy_layout:
                mMenuSelectedListener.onMenuSelected(5);
                break;
            case R.id.member_config_collaboration_layout:
                mMenuSelectedListener.onMenuSelected(6);
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.member_config_push_notification_switch:
                if (isChecked) {
                    Toast.makeText(getActivity(), "push true", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "push false", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.member_config_email_switch:
                if (isChecked) {
                    Toast.makeText(getActivity(), "email true", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "email false", Toast.LENGTH_SHORT).show();
                }
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
