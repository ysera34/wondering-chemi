package com.planet.wondering.chemi.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.planet.wondering.chemi.R;
import com.planet.wondering.chemi.network.AppSingleton;
import com.planet.wondering.chemi.network.Parser;
import com.planet.wondering.chemi.util.helper.TextValidator;
import com.planet.wondering.chemi.util.helper.UserSharedPreferences;
import com.planet.wondering.chemi.view.activity.SearchActivity;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.planet.wondering.chemi.network.Config.SOCKET_TIMEOUT_POST_REQ;
import static com.planet.wondering.chemi.network.Config.URL_HOST;
import static com.planet.wondering.chemi.network.Config.User.Key.ACCESS_TOKEN;
import static com.planet.wondering.chemi.network.Config.User.Key.PASSWORD;
import static com.planet.wondering.chemi.network.Config.User.PASSWORD_PATH;
import static com.planet.wondering.chemi.network.Config.User.PATH;

/**
 * Created by yoon on 2017. 3. 10..
 */

public class MemberChangePasswordFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = MemberChangePasswordFragment.class.getSimpleName();
    private static final String ARG_EMAIL = "email";
    private static final String ARG_ACCESS_TOKEN = "access_token";

    public static MemberChangePasswordFragment newInstance() {

        Bundle args = new Bundle();

        MemberChangePasswordFragment fragment = new MemberChangePasswordFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static MemberChangePasswordFragment newInstance(String accessToken) {

        Bundle args = new Bundle();
        args.putString(ARG_ACCESS_TOKEN, accessToken);

        MemberChangePasswordFragment fragment = new MemberChangePasswordFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static MemberChangePasswordFragment newInstance(String email, String accessToken) {

        Bundle args = new Bundle();
        args.putString(ARG_EMAIL, email);
        args.putString(ARG_ACCESS_TOKEN, accessToken);

        MemberChangePasswordFragment fragment = new MemberChangePasswordFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private EditText mMemberChangePasswordPWEditText;
    private TextView mPasswordValidationMessageTextView;
    private TextView mMemberChangePasswordSubmitButtonTextView;

    private String mEmail;
    private String mAccessToken;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mEmail = getArguments().getString(ARG_EMAIL, null);
        mAccessToken = getArguments().getString(ARG_ACCESS_TOKEN, null);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_member_change_password, container, false);
        mMemberChangePasswordPWEditText = (EditText)
                view.findViewById(R.id.member_change_password_pw_edit_text);
        mPasswordValidationMessageTextView = (TextView)
                view.findViewById(R.id.member_change_password_pw_validation_text_view);
        mMemberChangePasswordSubmitButtonTextView = (TextView)
                view.findViewById(R.id.member_change_password_submit_button_text_view);
        mMemberChangePasswordSubmitButtonTextView.setOnClickListener(this);
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
            case R.id.member_change_password_submit_button_text_view:
                if (isPasswordValidation) {
                    requestChangePassword(mAccessToken, mMemberChangePasswordPWEditText.getText().toString());
                } else {
                    Toast.makeText(getActivity(), mPasswordValidationMessage, Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private boolean isPasswordValidation = false;
    private String mPasswordValidationMessage;

    private void validateEditText() {
        mMemberChangePasswordPWEditText.addTextChangedListener(
                new TextValidator(mMemberChangePasswordPWEditText) {
                    @Override
                    public void validate(TextView textView, String text) {
                        if (validatePassword(text) == null) {
                            mMemberChangePasswordPWEditText.setBackgroundResource(R.drawable.edit_text_under_line_correct);
                            mPasswordValidationMessageTextView.setText("");
                            mPasswordValidationMessageTextView.setTextColor(getResources().getColor(R.color.colorPrimary));

                            mMemberChangePasswordSubmitButtonTextView.setTextColor(getResources().getColorStateList(R.color.color_selector_button_white_primary));
                            mMemberChangePasswordSubmitButtonTextView.setBackgroundResource(R.drawable.selector_opaque_primary);
                            isPasswordValidation = true;
                        } else {
                            mMemberChangePasswordPWEditText.setBackgroundResource(R.drawable.edit_text_under_line_focus_true_accent);
                            mPasswordValidationMessageTextView.setText(validatePassword(text));
                            mPasswordValidationMessageTextView.setTextColor(getResources().getColor(R.color.colorAccent));

                            mMemberChangePasswordSubmitButtonTextView.setTextColor(getResources().getColor(R.color.colorWhite));
                            mMemberChangePasswordSubmitButtonTextView.setBackgroundResource(R.drawable.widget_solid_oval_rectangle_iron);
                            isPasswordValidation = false;
                            mPasswordValidationMessage = validatePassword(text);
                        }
                    }
                }
        );
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

    private void requestChangePassword(String accessToken, String password) {

        Map<String, String> params = new HashMap<>();
        params.put(ACCESS_TOKEN, accessToken);
        params.put(PASSWORD, password);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST, URL_HOST + PATH + PASSWORD_PATH, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        if (UserSharedPreferences.getStoredToken(getActivity()) != null) {
                            UserSharedPreferences.removeStoredToken(getActivity());
                        }
                        UserSharedPreferences.setStoreToken(getActivity(), Parser.parseSignInUserToken(response));
                        Log.d(TAG, "user token after changing password: " + UserSharedPreferences.getStoredToken(getActivity()));

                        // change password and sign in firebase user

                        startActivity(SearchActivity.newIntent(getActivity()));
                        getActivity().finish();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, String.valueOf(error.getMessage()));
                        Toast.makeText(getActivity(),
                                "비밀 번호 변경 중 오류가 발생하였습니다. 잠시 후 다시 요청해주세요.", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(SOCKET_TIMEOUT_POST_REQ,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        AppSingleton.getInstance(getActivity()).addToRequestQueue(jsonObjectRequest, TAG);
    }
}
