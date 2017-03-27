package com.planet.wondering.chemi.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.planet.wondering.chemi.R;

/**
 * Created by yoon on 2017. 3. 20..
 */

public class MemberConfigChangePasswordFragment extends Fragment implements View.OnClickListener {

    public static MemberConfigChangePasswordFragment newInstance() {

        Bundle args = new Bundle();

        MemberConfigChangePasswordFragment fragment = new MemberConfigChangePasswordFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private LinearLayout mBackLayout;

    private InputMethodManager mInputMethodManager;
    private EditText mMemberConfigChangePasswordEditText;
    private TextView mMemberConfigChangePasswordValidationTextView;
    private EditText mMemberConfigChangeNewPasswordEditText;
    private TextView mMemberConfigChangeNewPasswordValidationTextView;
    private EditText mMemberConfigChangeConfirmNewPasswordEditText;
    private TextView mMemberConfigChangeConfirmNewPasswordValidationTextView;
    private TextView mMemberConfigChangePasswordAuthButtonTextView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mInputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_member_config_change_password, container, false);
        mBackLayout = (LinearLayout) view.findViewById(R.id.member_config_change_password_back_layout);
        mBackLayout.setOnClickListener(this);

        mMemberConfigChangePasswordEditText = (EditText) view.findViewById(R.id.member_config_change_password_edit_text);
        mMemberConfigChangePasswordValidationTextView = (TextView) view.findViewById(R.id.member_config_change_password_validation_text_view);
        mMemberConfigChangeNewPasswordEditText = (EditText) view.findViewById(R.id.member_config_change_new_password_edit_text);
        mMemberConfigChangeNewPasswordValidationTextView = (TextView) view.findViewById(R.id.member_config_change_new_password_validation_text_view);
        mMemberConfigChangeConfirmNewPasswordEditText = (EditText) view.findViewById(R.id.member_config_change_confirm_new_password_edit_text);
        mMemberConfigChangeConfirmNewPasswordValidationTextView = (TextView) view.findViewById(R.id.member_config_change_confirm_new_password_validation_text_view);
        mMemberConfigChangePasswordAuthButtonTextView = (TextView) view.findViewById(R.id.member_config_change_password_auth_button_text_view);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.member_config_change_password_back_layout:
                mInputMethodManager.hideSoftInputFromWindow(mMemberConfigChangePasswordEditText.getWindowToken(), 0);
                mInputMethodManager.hideSoftInputFromWindow(mMemberConfigChangeNewPasswordEditText.getWindowToken(), 0);
                mInputMethodManager.hideSoftInputFromWindow(mMemberConfigChangeConfirmNewPasswordEditText.getWindowToken(), 0);
                getActivity().onBackPressed();
                break;
        }
    }
}
