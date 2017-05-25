package com.planet.wondering.chemi.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.planet.wondering.chemi.R;
import com.planet.wondering.chemi.model.User;
import com.planet.wondering.chemi.util.helper.TextValidator;
import com.planet.wondering.chemi.util.listener.OnMenuSelectedListener;
import com.planet.wondering.chemi.view.activity.BottomNavigationActivity;
import com.planet.wondering.chemi.view.activity.MemberActivity;
import com.planet.wondering.chemi.view.activity.MemberStartActivity;
import com.planet.wondering.chemi.view.activity.UserActivity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.planet.wondering.chemi.common.Common.SIGN_IN_GOOGLE_REQUEST_CODE;
import static com.planet.wondering.chemi.common.Common.SIGN_IN_LOCAL_REQUEST_CODE;
import static com.planet.wondering.chemi.common.Common.SIGN_IN_NAVER_REQUEST_CODE;

/**
 * Created by yoon on 2017. 3. 25..
 */

public class MemberConfigSignInFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = MemberConfigSignInFragment.class.getSimpleName();

    public static MemberConfigSignInFragment newInstance() {

        Bundle args = new Bundle();

        MemberConfigSignInFragment fragment = new MemberConfigSignInFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private AppBarLayout mMemberConfigSignInAppBarLayout;
    private Toolbar mMemberConfigSignInToolbar;

    private InputMethodManager mInputMethodManager;
    private EditText mMemberSignInEmailEditText;
    private TextView mEmailValidationMessageTextView;
    private EditText mMemberSignInPasswordEditText;
    private TextView mPasswordValidationMessageTextView;

    private TextView mMemberConfigSignInSubmitButtonTextView;
    private TextView mMemberConfigSignInForgetPasswordTextView;

    private RelativeLayout mMemberConfigSignInGoogleLayout;
    private RelativeLayout mMemberConfigSignInNaverLayout;
    private RelativeLayout mMemberConfigSignInLocalLayout;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        mInputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_member_config_sign_in, container, false);
        mMemberConfigSignInToolbar = (Toolbar) view.findViewById(R.id.member_config_sign_in_toolbar);
        ((MemberActivity) getActivity()).setSupportActionBar(mMemberConfigSignInToolbar);
        mMemberConfigSignInAppBarLayout = (AppBarLayout) view.findViewById(R.id.member_config_sign_in_app_bar_layout);
        mMemberConfigSignInAppBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, final int verticalOffset) {
//                if (Math.abs(verticalOffset)-appBarLayout.getTotalScrollRange() == 0) {
//                if (verticalOffset == -mMemberAppBarLayout.getHeight() + mMemberToolbar.getHeight()) {
                if (verticalOffset < -appBarLayout.getTotalScrollRange() / 2) {
                    ((MemberActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(true);
                    ((BottomNavigationActivity) getActivity()).hideBottomNavigationView();
                } else {
                    ((MemberActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
                    ((BottomNavigationActivity) getActivity()).showBottomNavigationView();
                }
            }
        });

        mMemberSignInEmailEditText = (EditText)
                view.findViewById(R.id.member_config_sign_in_email_edit_text);
        mEmailValidationMessageTextView = (TextView)
                view.findViewById(R.id.member_config_sign_in_email_validation_text_view);
        mMemberSignInPasswordEditText = (EditText)
                view.findViewById(R.id.member_config_sign_in_password_edit_text);
        mPasswordValidationMessageTextView = (TextView)
                view.findViewById(R.id.member_config_sign_in_password_validation_text_view);

        mMemberConfigSignInSubmitButtonTextView = (TextView)
                view.findViewById(R.id.member_config_sign_in_submit_button_text_view);
        mMemberConfigSignInSubmitButtonTextView.setOnClickListener(this);
        mMemberConfigSignInForgetPasswordTextView = (TextView)
                view.findViewById(R.id.member_config_sign_in_forget_password_text_view);
        mMemberConfigSignInForgetPasswordTextView.setOnClickListener(this);

        mMemberConfigSignInGoogleLayout = (RelativeLayout)
                view.findViewById(R.id.member_config_sign_in_google_layout);
        mMemberConfigSignInGoogleLayout.setOnClickListener(this);
        mMemberConfigSignInNaverLayout = (RelativeLayout)
                view.findViewById(R.id.member_config_sign_in_naver_layout);
        mMemberConfigSignInNaverLayout.setOnClickListener(this);
        mMemberConfigSignInLocalLayout = (RelativeLayout)
                view.findViewById(R.id.member_config_sign_in_local_layout);
        mMemberConfigSignInLocalLayout.setOnClickListener(this);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        validationEditText();
    }

    @Override
    public void onClick(View v) {
        mInputMethodManager.hideSoftInputFromWindow(mMemberSignInEmailEditText.getWindowToken(), 0);
        mInputMethodManager.hideSoftInputFromWindow(mMemberSignInPasswordEditText.getWindowToken(), 0);
        switch (v.getId()) {
            case R.id.member_config_sign_in_submit_button_text_view:
                if (isValidatedEmail && isValidatedPassword) {
                    User anonymousUser = new User();
                    anonymousUser.setEmail(mMemberSignInEmailEditText.getText().toString());
                    anonymousUser.setPassword(mMemberSignInPasswordEditText.getText().toString());
                    getActivity().startActivityForResult(UserActivity.newIntent(getActivity(),
                            SIGN_IN_LOCAL_REQUEST_CODE, anonymousUser), SIGN_IN_LOCAL_REQUEST_CODE);
                } else {
                    Toast.makeText(getActivity(), "이메일과 비밀번호를 확인해주세요.", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.member_config_sign_in_forget_password_text_view:
                startActivity(MemberStartActivity.newIntent(getActivity(), 2));
                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
            case R.id.member_config_sign_in_google_layout:
                getActivity().startActivityForResult(UserActivity.newIntent(getActivity(),
                        SIGN_IN_GOOGLE_REQUEST_CODE), SIGN_IN_GOOGLE_REQUEST_CODE);
                break;
            case R.id.member_config_sign_in_naver_layout:
                getActivity().startActivityForResult(UserActivity.newIntent(getActivity(),
                        SIGN_IN_NAVER_REQUEST_CODE), SIGN_IN_NAVER_REQUEST_CODE);
                break;
            case R.id.member_config_sign_in_local_layout:
                startActivity(MemberStartActivity.newIntent(getActivity(), 3));
                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
        }
    }

    private boolean isValidatedEmail = false;
    private boolean isValidatedPassword = false;

    private void validationEditText() {
        mMemberSignInEmailEditText.addTextChangedListener(
                new TextValidator(mMemberSignInEmailEditText) {
                    @Override
                    public void validate(TextView textView, String text) {
                        if (validateEmail(text) == null) {
                            mMemberSignInEmailEditText.setBackgroundResource(R.drawable.edit_text_under_line_correct);
                            mEmailValidationMessageTextView.setTextColor(getResources().getColor(R.color.colorPrimary));
                            mEmailValidationMessageTextView.setText("");

                            mMemberConfigSignInSubmitButtonTextView.setTextColor(getResources().getColorStateList(R.color.color_selector_button_white_primary));
                            mMemberConfigSignInSubmitButtonTextView.setBackgroundResource(R.drawable.selector_opaque_primary);
                            isValidatedEmail = true;
                        } else {
                            mMemberSignInEmailEditText.setBackgroundResource(R.drawable.edit_text_under_line_focus_true_accent);
                            mEmailValidationMessageTextView.setText(validateEmail(text));
                            mEmailValidationMessageTextView.setTextColor(getResources().getColor(R.color.colorAccent));

                            mMemberConfigSignInSubmitButtonTextView.setTextColor(getResources().getColor(R.color.colorWhite));
                            mMemberConfigSignInSubmitButtonTextView.setBackgroundResource(R.drawable.widget_solid_oval_rectangle_iron);
                            isValidatedEmail = false;
                        }
                    }
                }
        );
        mMemberSignInPasswordEditText.addTextChangedListener(
                new TextValidator(mMemberSignInPasswordEditText) {
                    @Override
                    public void validate(TextView textView, String text) {
                        if (validatePassword(text) == null) {
                            mMemberSignInPasswordEditText.setBackgroundResource(R.drawable.edit_text_under_line_correct);
                            mPasswordValidationMessageTextView.setText("");
                            isValidatedPassword = true;
                        } else {
                            mMemberSignInPasswordEditText.setBackgroundResource(R.drawable.edit_text_under_line_focus_true_accent);
                            mPasswordValidationMessageTextView.setText(validatePassword(text));
                            mPasswordValidationMessageTextView.setTextColor(getResources().getColor(R.color.colorAccent));
                            isValidatedPassword = false;
                        }
                    }
                }
        );
    }

    private String validateEmail(String emailText) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(emailText);
        if (!matcher.matches()) {
            return getString(R.string.email_validation_message_wrong_pattern);
        } else {
            return null; // emailText request url ~~>
        }
    }

    private String validatePassword(String passwordText) {
        if (passwordText != null) {
            if (passwordText.length() < 6) {
                return getString(R.string.password_validation_message_short);
            } else if (passwordText.length() >= 6 && passwordText.length() < 13) {
                return null;
            } else if (passwordText.length() >= 13) {
                return getString(R.string.password_validation_message_long);
            }
        }
        return null;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_toolbar_member, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_config:
                mMenuSelectedListener.onMenuSelected(-1);
                return true;
            default:
                return super.onOptionsItemSelected(item);
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
