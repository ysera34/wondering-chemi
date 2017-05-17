package com.planet.wondering.chemi.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.planet.wondering.chemi.R;
import com.planet.wondering.chemi.util.listener.OnMenuSelectedListener;
import com.planet.wondering.chemi.view.activity.MemberStartActivity;

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

    private ImageView mMemberConfigSettingImageView;
    private TextView mMemberConfigSignInTextView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_member_config_sign_in, container, false);
        mMemberConfigSettingImageView = (ImageView) view.findViewById(R.id.member_config_setting_image_view);
        mMemberConfigSettingImageView.setOnClickListener(this);
        mMemberConfigSignInTextView = (TextView) view.findViewById(R.id.member_config_sign_in_text_view);
        mMemberConfigSignInTextView.setOnClickListener(this);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.member_config_setting_image_view:
                mMenuSelectedListener.onMenuSelected(-1);
                break;
            case R.id.member_config_sign_in_text_view:
                startActivity(MemberStartActivity.newIntent(getActivity()));
                getActivity().overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                getActivity().finish();
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
