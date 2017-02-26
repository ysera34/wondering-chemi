package com.planet.wondering.chemi.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.Toast;

import com.planet.wondering.chemi.R;
import com.planet.wondering.chemi.view.activity.MemberActivity;

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

    private Toolbar mConfigToolbar;
    private int[] mConfigLayoutIds;
    private RelativeLayout[] mConfigLayouts;
    private Switch mPushSwitch;
    private Switch mEmailSwitch;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mConfigLayoutIds = new int[]{
                R.id.member_config_profile_layout, R.id.member_config_request_product_layout,
                R.id.member_config_faq_layout, R.id.member_config_version_layout,
                R.id.member_config_push_notification_layout, R.id.member_config_email_layout,
                R.id.member_config_privacy_layout, R.id.member_config_collaboration_layout};
        mConfigLayouts = new RelativeLayout[8];
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_member_config, container, false);

        mConfigToolbar = (Toolbar) view.findViewById(R.id.member_config_toolbar);
        ((MemberActivity) getActivity()).setSupportActionBar(mConfigToolbar);
//        mConfigToolbar.setTitle("설정");
        ((MemberActivity) getActivity()).getSupportActionBar().setTitle("설정");
        for (int i = 0; i < mConfigLayoutIds.length; i++) {
            mConfigLayouts[i] = (RelativeLayout) view.findViewById(mConfigLayoutIds[i]);
            mConfigLayouts[i].setOnClickListener(this);
        }
        mPushSwitch = (Switch) view.findViewById(R.id.member_config_push_notification_switch);
        mPushSwitch.setOnCheckedChangeListener(this);
        mEmailSwitch = (Switch) view.findViewById(R.id.member_config_email_switch);
        mEmailSwitch.setOnCheckedChangeListener(this);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.member_config_profile_layout:
                break;
            case R.id.member_config_request_product_layout:
                break;
            case R.id.member_config_faq_layout:
                break;
            case R.id.member_config_version_layout:
                break;
            case R.id.member_config_privacy_layout:
                break;
            case R.id.member_config_collaboration_layout:
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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_toolbar_member_config, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_config_confirm:
                Toast.makeText(getActivity(), "action_config_confirm", Toast.LENGTH_SHORT).show();
//                mMenuSelectedListener.onMenuSelected();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
