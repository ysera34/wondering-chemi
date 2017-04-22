package com.planet.wondering.chemi.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.planet.wondering.chemi.R;

/**
 * Created by yoon on 2017. 3. 20..
 */

public class MemberConfigRequestFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = MemberConfigRequestFragment.class.getSimpleName();

    public static MemberConfigRequestFragment newInstance() {

        Bundle args = new Bundle();

        MemberConfigRequestFragment fragment = new MemberConfigRequestFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private LinearLayout mBackLayout;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_member_config_request, container, false);
        mBackLayout = (LinearLayout) view.findViewById(R.id.member_config_request_back_layout);
        mBackLayout.setOnClickListener(this);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.member_config_request_back_layout:
                getActivity().onBackPressed();
                break;
        }
    }
}
