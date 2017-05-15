package com.planet.wondering.chemi.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.planet.wondering.chemi.R;
import com.planet.wondering.chemi.model.User;
import com.planet.wondering.chemi.network.AppSingleton;
import com.planet.wondering.chemi.network.Parser;
import com.planet.wondering.chemi.util.helper.TextValidator;
import com.planet.wondering.chemi.util.helper.UserSharedPreferences;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.planet.wondering.chemi.network.Config.SOCKET_TIMEOUT_POST_REQ;
import static com.planet.wondering.chemi.network.Config.URL_HOST;
import static com.planet.wondering.chemi.network.Config.User.Key.PASSWORD;
import static com.planet.wondering.chemi.network.Config.User.Key.TOKEN;
import static com.planet.wondering.chemi.network.Config.User.PASSWORD_PATH;
import static com.planet.wondering.chemi.network.Config.User.PATH;

/**
 * Created by yoon on 2017. 3. 20..
 */

public class MemberConfigChangePasswordFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = MemberConfigChangePasswordFragment.class.getSimpleName();

    public static MemberConfigChangePasswordFragment newInstance() {

        Bundle args = new Bundle();

        MemberConfigChangePasswordFragment fragment = new MemberConfigChangePasswordFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private LinearLayout mBackLayout;

    private InputMethodManager mInputMethodManager;
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

        mMemberConfigChangeNewPasswordEditText = (EditText) view.findViewById(R.id.member_config_change_new_password_edit_text);
        mMemberConfigChangeNewPasswordValidationTextView = (TextView) view.findViewById(R.id.member_config_change_new_password_validation_text_view);
        mMemberConfigChangeConfirmNewPasswordEditText = (EditText) view.findViewById(R.id.member_config_change_confirm_new_password_edit_text);
        mMemberConfigChangeConfirmNewPasswordValidationTextView = (TextView) view.findViewById(R.id.member_config_change_confirm_new_password_validation_text_view);
        mMemberConfigChangePasswordAuthButtonTextView = (TextView) view.findViewById(R.id.member_config_change_password_auth_button_text_view);
        mMemberConfigChangePasswordAuthButtonTextView.setOnClickListener(this);

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
            case R.id.member_config_change_password_back_layout:
                mInputMethodManager.hideSoftInputFromWindow(mMemberConfigChangeNewPasswordEditText.getWindowToken(), 0);
                mInputMethodManager.hideSoftInputFromWindow(mMemberConfigChangeConfirmNewPasswordEditText.getWindowToken(), 0);
                getActivity().onBackPressed();
                break;
            case R.id.member_config_change_password_auth_button_text_view:
                mInputMethodManager.hideSoftInputFromWindow(mMemberConfigChangeNewPasswordEditText.getWindowToken(), 0);
                mInputMethodManager.hideSoftInputFromWindow(mMemberConfigChangeConfirmNewPasswordEditText.getWindowToken(), 0);
                if (isPassword && isConfirmPassword) {
                    requestChangePassword();
                } else {
                    Toast.makeText(getActivity(), "비밀번호가 일치하지 않아요.", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private boolean isPassword = false;
    private String mPassword;
    private boolean isConfirmPassword = false;
    private String mConfirmPassword;

    private void validateEditText() {
        mMemberConfigChangeNewPasswordEditText.addTextChangedListener(
                new TextValidator(mMemberConfigChangeNewPasswordEditText) {
                    @Override
                    public void validate(TextView textView, String text) {
                        if (validatePassword(text) == null) {
                            mMemberConfigChangeNewPasswordEditText.setBackgroundResource(R.drawable.edit_text_under_line_correct);
                            mMemberConfigChangeNewPasswordValidationTextView.setText("");
                            isPassword = true;
                        } else {
                            mMemberConfigChangeNewPasswordEditText.setBackgroundResource(R.drawable.edit_text_under_line_focus_true_accent);
                            mMemberConfigChangeNewPasswordValidationTextView.setText(validatePassword(text));
                            mMemberConfigChangeNewPasswordValidationTextView.setTextColor(getResources().getColor(R.color.colorAccent));
                        }

                        if (mConfirmPassword != null && text.length() > 0 && text.equals(mConfirmPassword)) {
                            mMemberConfigChangeConfirmNewPasswordEditText.setBackgroundResource(R.drawable.edit_text_under_line_correct);
                            mMemberConfigChangeConfirmNewPasswordValidationTextView.setText(getString(R.string.confirm_validation_message_correct));
                            mMemberConfigChangeConfirmNewPasswordValidationTextView.setTextColor(getResources().getColor(R.color.colorPrimary));
                            isConfirmPassword = true;
                        } else if (mConfirmPassword != null && !text.equals(mConfirmPassword)) {
                            mMemberConfigChangeConfirmNewPasswordEditText.setBackgroundResource(R.drawable.edit_text_under_line_focus_true_accent);
                            mMemberConfigChangeConfirmNewPasswordValidationTextView.setText(validateConfirm(text));
                            mMemberConfigChangeConfirmNewPasswordValidationTextView.setTextColor(getResources().getColor(R.color.colorAccent));
                            isConfirmPassword = false;
                        }

                        if (isPassword && isConfirmPassword) {
                            mMemberConfigChangePasswordAuthButtonTextView.setTextColor(getResources().getColorStateList(R.color.color_selector_button_white_primary));
                            mMemberConfigChangePasswordAuthButtonTextView.setBackgroundResource(R.drawable.selector_opaque_primary);
                        } else {
                            mMemberConfigChangePasswordAuthButtonTextView.setTextColor(getResources().getColor(R.color.colorWhite));
                            mMemberConfigChangePasswordAuthButtonTextView.setBackgroundResource(R.drawable.widget_solid_oval_rectangle_iron);
                        }
                    }
                }
        );
        mMemberConfigChangeNewPasswordEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    mPassword = mMemberConfigChangeNewPasswordEditText.getText().toString();
                } else if (isPassword) {
                    if (mConfirmPassword != null && !mPassword.equals(mConfirmPassword)) {
                        mMemberConfigChangeConfirmNewPasswordEditText.setBackgroundResource(R.drawable.edit_text_under_line_focus_true_accent);
                        mMemberConfigChangeConfirmNewPasswordValidationTextView.setText("");
                        mMemberConfigChangeConfirmNewPasswordValidationTextView.setTextColor(getResources().getColor(R.color.colorAccent));
                        isConfirmPassword = false;
                    }
                }
            }
        });

        mMemberConfigChangeConfirmNewPasswordEditText.addTextChangedListener(
                new TextValidator(mMemberConfigChangeConfirmNewPasswordEditText) {
                    @Override
                    public void validate(TextView textView, String text) {
                        if (validateConfirm(text) == null) {
                            mMemberConfigChangeConfirmNewPasswordEditText.setBackgroundResource(R.drawable.edit_text_under_line_correct);
                            mMemberConfigChangeConfirmNewPasswordValidationTextView.setText(getString(R.string.confirm_validation_message_correct));
                            mMemberConfigChangeConfirmNewPasswordValidationTextView.setTextColor(getResources().getColor(R.color.colorPrimary));
                            isConfirmPassword = true;
                        } else {
                            mMemberConfigChangeConfirmNewPasswordEditText.setBackgroundResource(R.drawable.edit_text_under_line_focus_true_accent);
                            mMemberConfigChangeConfirmNewPasswordValidationTextView.setText(validateConfirm(text));
                            mMemberConfigChangeConfirmNewPasswordValidationTextView.setTextColor(getResources().getColor(R.color.colorAccent));
                            isConfirmPassword = false;
                        }
                        if (isPassword && isConfirmPassword) {
                            mMemberConfigChangePasswordAuthButtonTextView.setTextColor(getResources().getColorStateList(R.color.color_selector_button_white_primary));
                            mMemberConfigChangePasswordAuthButtonTextView.setBackgroundResource(R.drawable.selector_opaque_primary);
                        } else {
                            mMemberConfigChangePasswordAuthButtonTextView.setTextColor(getResources().getColor(R.color.colorWhite));
                            mMemberConfigChangePasswordAuthButtonTextView.setBackgroundResource(R.drawable.widget_solid_oval_rectangle_iron);
                        }
                    }
                }
        );
        mMemberConfigChangeConfirmNewPasswordEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    mConfirmPassword = mMemberConfigChangeConfirmNewPasswordEditText.getText().toString();
                }
            }
        });
    }

    private String validatePassword(String passwordText) {
        if (passwordText != null) {
            if (passwordText.length() == 0) {
                return "";
            } else if (passwordText.length() < 6) {
                return getString(R.string.password_validation_message_short);
            } else if (passwordText.length() >= 6 && passwordText.length() < 14) {
                return null;
            } else if (passwordText.length() >= 14) {
                return getString(R.string.password_validation_message_long);
            }
        }
        return null;
    }

    private String validateConfirm(String confirmText) {
        if (confirmText.length() == 0) {
            return "";
        }
        if (confirmText.length() < 6) {
            return "";
        } else if (confirmText.length() >= 6 && !mPassword.equals(confirmText)) {
            return getString(R.string.confirm_validation_message_incorrect);
        } else if (confirmText.length() >= 6 && mPassword.equals(confirmText)) {
            return null;
        } else {
            return "";
        }
    }

    private void requestChangePassword() {

        Map<String, String> params = new HashMap<>();
        params.put(PASSWORD, mMemberConfigChangeConfirmNewPasswordEditText.getText().toString());

        Log.i("password1", mMemberConfigChangeNewPasswordEditText.getText().toString());
        Log.i("password2", mMemberConfigChangeConfirmNewPasswordEditText.getText().toString());

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST, URL_HOST + PATH + PASSWORD_PATH, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
//                        Log.i(TAG, response.toString());
                        User user = Parser.parseSignInUserToken(response);

                        if (UserSharedPreferences.getStoredToken(getActivity()) != null) {
                            UserSharedPreferences.removeStoredToken(getActivity());
                        }
                        UserSharedPreferences.setStoreToken(getActivity(), user.getToken());
                        Log.d(TAG, "user token after changing password: " + UserSharedPreferences.getStoredToken(getActivity()));

                        if (UserSharedPreferences.getStoredUserName(getActivity()) != null) {
                            UserSharedPreferences.removeStoredUserName(getActivity());
                        }
                        UserSharedPreferences.setStoreUserName(getActivity(), user.getName());
                        Log.d(TAG, "user name after changing password: " + UserSharedPreferences.getStoredUserName(getActivity()));
                        Toast.makeText(getActivity(),
                                "비밀 번호 변경 되었습니다.", Toast.LENGTH_SHORT).show();
                        getActivity().onBackPressed();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, String.valueOf(error.toString()));
//                        Toast.makeText(getActivity(),
//                                "비밀 번호 변경 중 오류가 발생하였습니다. 잠시 후 다시 요청해주세요.", Toast.LENGTH_SHORT).show();
                        Toast.makeText(getActivity(), R.string.progress_dialog_message_error,
                                Toast.LENGTH_SHORT).show();
                    }
                }
        )
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put(TOKEN, UserSharedPreferences.getStoredToken(getActivity()));
                return params;
            }
        };

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(SOCKET_TIMEOUT_POST_REQ,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        AppSingleton.getInstance(getActivity()).addToRequestQueue(jsonObjectRequest, TAG);
    }
}
