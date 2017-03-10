package com.planet.wondering.chemi.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.planet.wondering.chemi.R;
import com.planet.wondering.chemi.view.activity.MemberStartActivity;

/**
 * Created by yoon on 2017. 3. 8..
 */

public class MemberSendEmailFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = MemberSendEmailFragment.class.getSimpleName();

    private static final String ARG_EMAIL = "email";

    public static MemberSendEmailFragment newInstance() {

        Bundle args = new Bundle();

        MemberSendEmailFragment fragment = new MemberSendEmailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static MemberSendEmailFragment newInstance(String email) {

        Bundle args = new Bundle();
        args.putString(ARG_EMAIL, email);

        MemberSendEmailFragment fragment = new MemberSendEmailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private TextView mMemberSendEmailEmailTextView;
    private TextView mMemberSendEmailConfirmButtonTextView;

    private String mEmail;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mEmail = getArguments().getString(ARG_EMAIL, null);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_member_send_email, container, false);
        mMemberSendEmailEmailTextView = (TextView)
                view.findViewById(R.id.member_send_email_email_text_view);
        mMemberSendEmailConfirmButtonTextView = (TextView)
                view.findViewById(R.id.member_send_email_confirm_button_text_view);
        mMemberSendEmailConfirmButtonTextView.setOnClickListener(this);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (mEmail != null) {
            mMemberSendEmailEmailTextView.setText(getString(R.string.member_send_email_format, mEmail));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.member_send_email_confirm_button_text_view:
                Toast.makeText(getActivity(), "where i am", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    public void updateUIByAuthEmail(String accessToken) {
        if (mEmail != null) {
            Log.i(TAG, "accessToken : " + accessToken);
            ((MemberStartActivity) getActivity()).replaceFragment(accessToken);
        }
    }
}
