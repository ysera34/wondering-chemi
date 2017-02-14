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
import com.planet.wondering.chemi.util.helper.TextValidator;
import com.planet.wondering.chemi.view.activity.MemberStartActivity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by yoon on 2017. 2. 14..
 */

public class MemberStartLocalFragment extends Fragment
        implements View.OnClickListener {

    public static final String TAG = MemberStartLocalFragment.class.getSimpleName();

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

    private TextView mEmailValidationMessageTextView;
    private TextView mNameValidationMessageTextView;
    private TextView mPasswordValidationMessageTextView;
    private TextView mConfirmValidationessageTextView;

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

        mEmailValidationMessageTextView =
                (TextView) view.findViewById(R.id.member_start_local_email_validation_text_view);
        mNameValidationMessageTextView =
                (TextView) view.findViewById(R.id.member_start_local_name_validation_text_view);
        mPasswordValidationMessageTextView =
                (TextView) view.findViewById(R.id.member_start_local_pw_validation_text_view);
        mConfirmValidationessageTextView =
                (TextView) view.findViewById(R.id.member_start_local_pw_confirm_validation_text_view);

        mMemberStartLocalPrivacyInfoTextView =
                (TextView) view.findViewById(R.id.member_start_local_privacy_info_text_view);
        mMemberStartLocalSubmitButtonTextView =
                (TextView) view.findViewById(R.id.member_start_local_submit_button_text_view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        validateEditText();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.member_start_local_cancel_layout:
                ((MemberStartActivity) getActivity()).cancelSignInLocal();
                break;
        }
    }

    private void validateEditText() {
        mMemberStartLocalEmailEditText.addTextChangedListener(
                new TextValidator(mMemberStartLocalEmailEditText) {
            @Override
            public void validate(TextView textView, String text) {
                if (validateEmail(text)) {
                    mMemberStartLocalEmailEditText.setBackgroundResource(R.drawable.edit_text_under_line_correct);
                } else {
                    mMemberStartLocalEmailEditText.setBackgroundResource(R.drawable.edit_text_under_line_focus_true);
                }
            }
        });
        mMemberStartLocalNameEditText.addTextChangedListener(
                new TextValidator(mMemberStartLocalNameEditText) {
            @Override
            public void validate(TextView textView, String text) {
                if (validateName(text)) {
                    mMemberStartLocalNameEditText.setBackgroundResource(R.drawable.edit_text_under_line_correct);
                } else {
                    mMemberStartLocalNameEditText.setBackgroundResource(R.drawable.edit_text_under_line_focus_true);
                }
            }
        });
        mMemberStartLocalPasswordEditText.addTextChangedListener(
                new TextValidator(mMemberStartLocalPasswordEditText) {
            @Override
            public void validate(TextView textView, String text) {
                if (validatePassword(text)) {
                    mMemberStartLocalPasswordEditText.setBackgroundResource(R.drawable.edit_text_under_line_correct);
                } else {
                    mMemberStartLocalPasswordEditText.setBackgroundResource(R.drawable.edit_text_under_line_focus_true);
                }
            }
        });
        mMemberStartLocalConfirmEditText.addTextChangedListener(
                new TextValidator(mMemberStartLocalConfirmEditText) {
            @Override
            public void validate(TextView textView, String text) {
                if (validatePassword(text)) {
                    mMemberStartLocalConfirmEditText.setBackgroundResource(R.drawable.edit_text_under_line_correct);
                } else {
                    mMemberStartLocalConfirmEditText.setBackgroundResource(R.drawable.edit_text_under_line_focus_true);
                }
            }
        });
    }

    private boolean validateEmail(String emailText) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(emailText);
        return matcher.matches();
    }

    private boolean validateName(String nameText) {
        if (nameText != null && nameText.length() > 2 && nameText.length() < 11) {
            return true;
        }
        return false;
    }

    private boolean validatePassword(String passwordText) {
        if (passwordText != null && passwordText.length() > 6) {
            return true;
        }
        return false;
    }
}
