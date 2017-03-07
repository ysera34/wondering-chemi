package com.planet.wondering.chemi.view.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.iid.FirebaseInstanceId;
import com.planet.wondering.chemi.R;
import com.planet.wondering.chemi.model.User;
import com.planet.wondering.chemi.network.AppSingleton;
import com.planet.wondering.chemi.network.Parser;
import com.planet.wondering.chemi.util.helper.TextValidator;
import com.planet.wondering.chemi.util.helper.UserSharedPreferences;
import com.planet.wondering.chemi.view.activity.MemberStartActivity;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.planet.wondering.chemi.network.Config.SOCKET_TIMEOUT_POST_REQ;
import static com.planet.wondering.chemi.network.Config.URL_HOST;
import static com.planet.wondering.chemi.network.Config.User.EMAIL_PATH;
import static com.planet.wondering.chemi.network.Config.User.EMAIL_STRING;
import static com.planet.wondering.chemi.network.Config.User.Key.EMAIL;
import static com.planet.wondering.chemi.network.Config.User.Key.NAME;
import static com.planet.wondering.chemi.network.Config.User.NAME_STRING;
import static com.planet.wondering.chemi.network.Config.User.PATH;

/**
 * Created by yoon on 2017. 2. 14..
 */

public class MemberStartLocalFragment extends Fragment
        implements View.OnClickListener {

    private static final String TAG = MemberStartLocalFragment.class.getSimpleName();

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

    private LinearLayout mEmailAuthButtonLayout;
    private TextView mEmailAuthButtonTextView;
    private TextView mEmailResultTextView;
    private InputMethodManager mInputMethodManager;
    private LinearLayout mBodyLayout;
    private LinearLayout mFooterLayout;

    private String[] mValidationMessages;

    private TextView mEmailValidationMessageTextView;
    private TextView mNameValidationMessageTextView;
    private TextView mPasswordValidationMessageTextView;
    private TextView mConfirmValidationMessageTextView;

    private TextView mMemberStartLocalPrivacyInfoTextView;
    private TextView mMemberStartLocalSubmitButtonTextView;

    private User mUser;

    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mValidationMessages = getResources().getStringArray(R.array.validation_message_array);
        mInputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Log.d(TAG, "onAuthStateChanged:signed_in: " + user.getUid());
                } else {
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };
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

        mEmailAuthButtonLayout = (LinearLayout)
                view.findViewById(R.id.member_start_local_email_auth_button_layout);
        mEmailAuthButtonTextView = (TextView)
                view.findViewById(R.id.member_start_local_email_auth_button_text_view);
        mEmailAuthButtonTextView.setOnClickListener(this);
        mEmailResultTextView = (TextView)
                view.findViewById(R.id.member_start_local_email_result_text_view);
        mBodyLayout = (LinearLayout)
                view.findViewById(R.id.member_start_local_body_layout);
        mFooterLayout = (LinearLayout)
                view.findViewById(R.id.member_start_local_footer);

        mEmailValidationMessageTextView =
                (TextView) view.findViewById(R.id.member_start_local_email_validation_text_view);
        mNameValidationMessageTextView =
                (TextView) view.findViewById(R.id.member_start_local_name_validation_text_view);
        mPasswordValidationMessageTextView =
                (TextView) view.findViewById(R.id.member_start_local_pw_validation_text_view);
        mConfirmValidationMessageTextView =
                (TextView) view.findViewById(R.id.member_start_local_pw_confirm_validation_text_view);

        mMemberStartLocalPrivacyInfoTextView =
                (TextView) view.findViewById(R.id.member_start_local_privacy_info_text_view);
        mMemberStartLocalPrivacyInfoTextView.setOnClickListener(this);
        mMemberStartLocalSubmitButtonTextView =
                (TextView) view.findViewById(R.id.member_start_local_submit_button_text_view);
        mMemberStartLocalSubmitButtonTextView.setOnClickListener(this);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        validateEditText();
    }

    @Override
    public void onResume() {
        super.onResume();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthStateListener != null) {
            mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.member_start_local_cancel_layout:
                mInputMethodManager.hideSoftInputFromWindow(mMemberStartLocalEmailEditText.getWindowToken(), 0);
                ((MemberStartActivity) getActivity()).cancelSignUpForLocal();
                break;
            case R.id.member_start_local_email_auth_button_text_view:
                mInputMethodManager.hideSoftInputFromWindow(mMemberStartLocalEmailEditText.getWindowToken(), 0);
                if (isAuthEmailValidation) {
                    requestConfirmEmailRepetition(mMemberStartLocalEmailEditText.getText().toString());
//                    requestSendAuthEmail(mMemberStartLocalEmailEditText.getText().toString());
                } else {
                    Toast.makeText(getActivity(), mValidationMessages[0], Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.member_start_local_privacy_info_text_view:
                break;
            case R.id.member_start_local_submit_button_text_view:
                if (isAuthEmailResult && isConfirmPassword) {
//                    requestSubmitUserInfo();
                    requestConfirmNameRepetition(mMemberStartLocalNameEditText.getText().toString());
                } else {
                    Toast.makeText(getActivity(),
                            "이메일 인증이 완료 되지 않았거나, 비밀번호가 일치하지 않아요.", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private boolean isAuthEmailValidation = false;
    private boolean isAuthEmailResult = false;
    private boolean isConfirmName = false;
    private boolean isConfirmPassword = false;
    private String mAccessToken;

    public void updateUIByAuthEmail(String accessToken) {
        if (isAuthEmailValidation && isAuthEmailResult) {
            mEmailValidationMessageTextView.setText(getString(R.string.email_validation_message_correct));
            mEmailAuthButtonLayout.setVisibility(View.GONE);
            mBodyLayout.setVisibility(View.VISIBLE);
            mFooterLayout.setVisibility(View.VISIBLE);
            mMemberStartLocalEmailEditText.setEnabled(false);
            Log.i(TAG, "accessToken : " + accessToken);
            mAccessToken = accessToken;
        }
    }

    private void validateEditText() {
        mMemberStartLocalEmailEditText.addTextChangedListener(
                new TextValidator(mMemberStartLocalEmailEditText) {
            @Override
            public void validate(TextView textView, String text) {
                if (validateEmail(text) == null) {
                    mMemberStartLocalEmailEditText.setBackgroundResource(R.drawable.edit_text_under_line_correct);
                    mEmailValidationMessageTextView.setText(mValidationMessages[2]);
                    mEmailValidationMessageTextView.setTextColor(getResources().getColor(R.color.colorPrimary));

//                    mEmailAuthButtonTextView.setTextColor(getResources().getColor(R.color.colorWhite));
//                    mEmailAuthButtonTextView.setBackgroundResource(R.drawable.widget_solid_oval_rectangle_primary);
                    mEmailAuthButtonTextView.setTextColor(getResources().getColorStateList(R.color.color_selector_white_primary));
                    mEmailAuthButtonTextView.setBackgroundResource(R.drawable.selector_opaque_primary_transparent_white);
                    isAuthEmailValidation = true;
                } else {
                    mMemberStartLocalEmailEditText.setBackgroundResource(R.drawable.edit_text_under_line_focus_true_accent);
                    mEmailValidationMessageTextView.setText(validateEmail(text));
                    mEmailValidationMessageTextView.setTextColor(getResources().getColor(R.color.colorAccent));

                    mEmailAuthButtonTextView.setTextColor(getResources().getColor(R.color.colorPrimary));
                    mEmailAuthButtonTextView.setBackgroundResource(R.drawable.widget_border_oval_rectangle_primary);
                    isAuthEmailValidation = false;
                }
            }
        });
        mMemberStartLocalNameEditText.addTextChangedListener(
                new TextValidator(mMemberStartLocalNameEditText) {
            @Override
            public void validate(TextView textView, String text) {
                if (validateName(text) == null) {
                    mMemberStartLocalNameEditText.setBackgroundResource(R.drawable.edit_text_under_line_correct);
                    mNameValidationMessageTextView.setText(mValidationMessages[6]);
                    mNameValidationMessageTextView.setTextColor(getResources().getColor(R.color.colorPrimary));
                } else {
                    mMemberStartLocalNameEditText.setBackgroundResource(R.drawable.edit_text_under_line_focus_true_accent);
                    mNameValidationMessageTextView.setText(validateName(text));
                    mNameValidationMessageTextView.setTextColor(getResources().getColor(R.color.colorAccent));
                }
            }
        });
        mMemberStartLocalPasswordEditText.addTextChangedListener(
                new TextValidator(mMemberStartLocalPasswordEditText) {
            @Override
            public void validate(TextView textView, String text) {
                if (validatePassword(text) == null) {
                    mMemberStartLocalPasswordEditText.setBackgroundResource(R.drawable.edit_text_under_line_correct);
                    mPasswordValidationMessageTextView.setText("");
                } else {
                    mMemberStartLocalPasswordEditText.setBackgroundResource(R.drawable.edit_text_under_line_focus_true_accent);
                    mPasswordValidationMessageTextView.setText(validatePassword(text));
                    mPasswordValidationMessageTextView.setTextColor(getResources().getColor(R.color.colorAccent));
                }
            }
        });
        mMemberStartLocalConfirmEditText.addTextChangedListener(
                new TextValidator(mMemberStartLocalConfirmEditText) {
            @Override
            public void validate(TextView textView, String text) {
                if (validateConfirm(text) == null) {
                    mMemberStartLocalConfirmEditText.setBackgroundResource(R.drawable.edit_text_under_line_correct);
                    mConfirmValidationMessageTextView.setText(mValidationMessages[10]);
                    mConfirmValidationMessageTextView.setTextColor(getResources().getColor(R.color.colorPrimary));
                    isConfirmPassword = true;
                } else {
                    mMemberStartLocalConfirmEditText.setBackgroundResource(R.drawable.edit_text_under_line_focus_true_accent);
                    mConfirmValidationMessageTextView.setText(validateConfirm(text));
                    mConfirmValidationMessageTextView.setTextColor(getResources().getColor(R.color.colorAccent));
                    isConfirmPassword = false;
                }
            }
        });
    }

    private String validateEmail(String emailText) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(emailText);
        if (!matcher.matches()) {
            return mValidationMessages[0];
        } else {
            return null; // emailText request url ~~>
        }
    }

    private String validateName(String nameText) {
        if (nameText != null) {
            if (nameText.length() < 2) {
                return mValidationMessages[3];
            } else if (nameText.length() >= 2 && nameText.length() < 11) {
                return null; // nameText request url ~~>
            } else if (nameText.length() >= 11) {
                return mValidationMessages[4];
            }
        }
        return mValidationMessages[5];
    }

    private String validatePassword(String passwordText) {
        if (passwordText != null) {
            if (passwordText.length() < 6) {
                return mValidationMessages[7];
            } else if (passwordText.length() >= 6 && passwordText.length() < 13) {
                return null;
            } else if (passwordText.length() >= 13) {
                return mValidationMessages[8];
            }
        }
        return null;
    }

    private String validateConfirm(String confirmText) {
        if (confirmText.length() == 0) {
            return "";
        }
        if (confirmText.length() > 0
                && !mMemberStartLocalPasswordEditText.getText().toString().equals(confirmText)) {
            return mValidationMessages[9];
        }
        return null;
    }

    private void requestConfirmEmailRepetition(String emailAddress) {

        Map<String, String> params = new HashMap<>();
        params.put("emailString", emailAddress);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST, URL_HOST + PATH + EMAIL_STRING, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (Parser.parseSimpleResult(response)) {
                            mEmailValidationMessageTextView.setText(getString(R.string.email_submit_message_correct));
                            mEmailAuthButtonTextView.setEnabled(false);
                            requestSendAuthEmail(mMemberStartLocalEmailEditText.getText().toString());
                        } else {
                            mMemberStartLocalEmailEditText.setBackgroundResource(R.drawable.edit_text_under_line_focus_true_accent);
                            mEmailValidationMessageTextView.setText(getString(R.string.email_submit_message_incorrect));
                            mEmailValidationMessageTextView.setTextColor(getResources().getColor(R.color.colorAccent));
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, String.valueOf(error.getMessage()));
                        Toast.makeText(getActivity(),
                                "메일 중복 확인 중 오류가 발생하였습니다. 잠시 후 다시 요청해주세요", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(SOCKET_TIMEOUT_POST_REQ,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        AppSingleton.getInstance(getActivity()).addToRequestQueue(jsonObjectRequest, TAG);
    }

    private void requestSendAuthEmail(String emailAddress) {

        Map<String, String> params = new HashMap<>();
        params.put(EMAIL, emailAddress);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST, URL_HOST + PATH + EMAIL_PATH, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(getActivity(),
                                "메일 발송이 완료 되었습니다.\n메일함에서 확인해주세요.", Toast.LENGTH_SHORT).show();
                        isAuthEmailResult = true;
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, String.valueOf(error.getMessage()));
                        Toast.makeText(getActivity(),
                                "메일 발송 중 오류가 발생하였습니다. 잠시 후 다시 요청해주세요.", Toast.LENGTH_SHORT).show();
                        isAuthEmailResult = false;
                    }
                }
        );

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(SOCKET_TIMEOUT_POST_REQ,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        AppSingleton.getInstance(getActivity()).addToRequestQueue(jsonObjectRequest, TAG);
    }

    private void requestConfirmNameRepetition(String name) {

        Map<String, String> params = new HashMap<>();
        params.put("nameString", name);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST, URL_HOST + PATH + NAME_STRING, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (Parser.parseSimpleResult(response)) {
                            isConfirmName = true;
                        } else {
                            isConfirmName = false;
                        }

                        if (isAuthEmailResult && isConfirmPassword && isConfirmName) {
                            requestSubmitUserInfo();
                        } else {
                            mMemberStartLocalNameEditText.setBackgroundResource(R.drawable.edit_text_under_line_focus_true_accent);
                            mNameValidationMessageTextView.setText(getString(R.string.name_submit_message_incorrect));
                            mNameValidationMessageTextView.setTextColor(getResources().getColor(R.color.colorAccent));
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, String.valueOf(error.getMessage()));
                        Toast.makeText(getActivity(),
                                "닉네임 중복 확인 중 오류가 발생하였습니다. 잠시 후 다시 요청해주세요", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(SOCKET_TIMEOUT_POST_REQ,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        AppSingleton.getInstance(getActivity()).addToRequestQueue(jsonObjectRequest, TAG);
    }

    private void requestSubmitUserInfo() {

        final ProgressDialog progressDialog;
        progressDialog = ProgressDialog.show(getActivity(), "회원가입을 요청하고 있습니다.",
                    getString(R.string.progress_dialog_message_wait), false, false);

        Map<String, String> params = new HashMap<>();
        params.put(EMAIL, mMemberStartLocalEmailEditText.getText().toString());
        params.put(NAME, mMemberStartLocalNameEditText.getText().toString());
        params.put("password", mMemberStartLocalConfirmEditText.getText().toString());
        params.put("accessToken", mAccessToken);
        params.put("pushToken", FirebaseInstanceId.getInstance().getToken());
        params.put("platform", "0");

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST, URL_HOST + PATH, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressDialog.dismiss();
                        Toast.makeText(getActivity(),
                                "회원 가입이 완료되었습니다.", Toast.LENGTH_SHORT).show();
                        mUser = Parser.parseUser(response);

                        createFirebaseAccount(mMemberStartLocalEmailEditText.getText().toString(),
                                mMemberStartLocalConfirmEditText.getText().toString());

                        if (UserSharedPreferences.getStoredToken(getActivity()) != null) {
                            UserSharedPreferences.removeStoredToken(getActivity());
                        }
                        UserSharedPreferences.setStoreToken(getActivity(), mUser.getToken());
                        Log.d(TAG, "user token : " + UserSharedPreferences.getStoredToken(getActivity()));
                        ((MemberStartActivity) getActivity()).replaceFragment();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Log.e(TAG, String.valueOf(error.getMessage()));
                        Toast.makeText(getActivity(),
                                "회원 가입 중 문제가 발생하였습니다.", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(SOCKET_TIMEOUT_POST_REQ,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        AppSingleton.getInstance(getActivity()).addToRequestQueue(jsonObjectRequest, TAG);
    }

    private void createFirebaseAccount(String email, String password) {
        Log.d(TAG, "createFirebaseAccount with Firebase: " + email);

        mFirebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "createUserWithEmail:onComplete: " + task.isSuccessful());

                        if (!task.isSuccessful()) {
                            Toast.makeText(getActivity(),
                                    "fail to create firebase user", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}
