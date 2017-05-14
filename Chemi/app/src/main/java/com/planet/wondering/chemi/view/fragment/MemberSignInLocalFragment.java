package com.planet.wondering.chemi.view.fragment;

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
import com.planet.wondering.chemi.R;
import com.planet.wondering.chemi.network.AppSingleton;
import com.planet.wondering.chemi.network.Parser;
import com.planet.wondering.chemi.util.helper.TextValidator;
import com.planet.wondering.chemi.util.helper.UserSharedPreferences;
import com.planet.wondering.chemi.util.listener.OnMenuSelectedListener;
import com.planet.wondering.chemi.view.activity.SearchActivity;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.planet.wondering.chemi.network.Config.SOCKET_TIMEOUT_POST_REQ;
import static com.planet.wondering.chemi.network.Config.URL_HOST;
import static com.planet.wondering.chemi.network.Config.User.Key.EMAIL;
import static com.planet.wondering.chemi.network.Config.User.Key.PASSWORD;
import static com.planet.wondering.chemi.network.Config.User.Key.PLATFORM;
import static com.planet.wondering.chemi.network.Config.User.LOGIN_PARAMS;
import static com.planet.wondering.chemi.network.Config.User.PATH;

/**
 * Created by yoon on 2017. 2. 18..
 */

public class MemberSignInLocalFragment extends Fragment
        implements View.OnClickListener {

    private static final String TAG = MemberSignInLocalFragment.class.getSimpleName();

    public static MemberSignInLocalFragment newInstance() {

        Bundle args = new Bundle();

        MemberSignInLocalFragment fragment = new MemberSignInLocalFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private RelativeLayout mMemberSignInCancelLayout;

    private InputMethodManager mInputMethodManager;
    private EditText mMemberSignInEmailEditText;
    private TextView mEmailValidationMessageTextView;
    private EditText mMemberSignInPasswordEditText;
    private TextView mPasswordValidationMessageTextView;

    private TextView mMemberSignInSubmitButtonTextView;
    private TextView mMemberSignInSubmitResultTextView;
    private TextView mMemberSignInForgetPasswordTextView;
    private TextView mMemberSignInRecommendUserTextView;

    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        View view = inflater.inflate(R.layout.fragment_member_sign_in_local, container, false);
//        View view = inflater.inflate(R.layout.fragment_member_sign_in_local_transparent, container, false);
        mMemberSignInCancelLayout = (RelativeLayout) view.findViewById(R.id.member_sign_in_cancel_layout);
        mMemberSignInCancelLayout.setOnClickListener(this);

        mMemberSignInEmailEditText = (EditText)
                view.findViewById(R.id.member_sign_in_email_edit_text);
        mEmailValidationMessageTextView = (TextView)
                view.findViewById(R.id.member_sign_in_email_validation_text_view);
        mMemberSignInPasswordEditText = (EditText)
                view.findViewById(R.id.member_sign_in_password_edit_text);
        mPasswordValidationMessageTextView = (TextView)
                view.findViewById(R.id.member_sign_in_password_validation_text_view);

        mMemberSignInSubmitButtonTextView = (TextView)
                view.findViewById(R.id.member_sign_in_submit_button_text_view);
        mMemberSignInSubmitButtonTextView.setOnClickListener(this);
        mMemberSignInSubmitResultTextView = (TextView)
                view.findViewById(R.id.member_sign_in_submit_result_text_view);

        mMemberSignInForgetPasswordTextView = (TextView)
                view.findViewById(R.id.member_sign_in_forget_password_text_view);
        mMemberSignInForgetPasswordTextView.setOnClickListener(this);
        mMemberSignInRecommendUserTextView = (TextView)
                view.findViewById(R.id.member_sign_in_recommend_user_text_view);
        mMemberSignInRecommendUserTextView.setOnClickListener(this);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        validationEditText();
    }

    @Override
    public void onStart() {
        super.onStart();
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
            case R.id.member_sign_in_cancel_layout:
//                ((MemberStartActivity) getActivity()).cancelSignInLocal();
                mInputMethodManager.hideSoftInputFromWindow(mMemberSignInEmailEditText.getWindowToken(), 0);
                mInputMethodManager.hideSoftInputFromWindow(mMemberSignInPasswordEditText.getWindowToken(), 0);
                mMenuSelectedListener.onMenuSelected(7004);
                break;
            case R.id.member_sign_in_submit_button_text_view:
                mInputMethodManager.hideSoftInputFromWindow(mMemberSignInEmailEditText.getWindowToken(), 0);
                mInputMethodManager.hideSoftInputFromWindow(mMemberSignInPasswordEditText.getWindowToken(), 0);
                if (isValidatedEmail && isValidatedPassword) {
                    requestSignInLocal(mMemberSignInEmailEditText.getText().toString(),
                            mMemberSignInPasswordEditText.getText().toString());
                } else {
                    Toast.makeText(getActivity(), "이메일과 비밀번호를 확인해주세요.", Toast.LENGTH_SHORT).show();
                    mMemberSignInSubmitResultTextView.setText("이메일과 비밀번호를 확인해주세요.");
                }

                break;
            case R.id.member_sign_in_forget_password_text_view:
                mInputMethodManager.hideSoftInputFromWindow(mMemberSignInEmailEditText.getWindowToken(), 0);
                mInputMethodManager.hideSoftInputFromWindow(mMemberSignInPasswordEditText.getWindowToken(), 0);
//                ((MemberStartActivity) getActivity()).findPassword();
                mMenuSelectedListener.onMenuSelected(7005);
                break;
            case R.id.member_sign_in_recommend_user_text_view:
                mInputMethodManager.hideSoftInputFromWindow(mMemberSignInEmailEditText.getWindowToken(), 0);
                mInputMethodManager.hideSoftInputFromWindow(mMemberSignInPasswordEditText.getWindowToken(), 0);
//                ((MemberStartActivity) getActivity()).cancelSignInLocal();
                mMenuSelectedListener.onMenuSelected(7004);
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

                            mMemberSignInSubmitButtonTextView.setTextColor(getResources().getColorStateList(R.color.color_selector_button_white_primary));
                            mMemberSignInSubmitButtonTextView.setBackgroundResource(R.drawable.selector_opaque_primary);
                            isValidatedEmail = true;
                        } else {
                            mMemberSignInEmailEditText.setBackgroundResource(R.drawable.edit_text_under_line_focus_true_accent);
                            mEmailValidationMessageTextView.setText(validateEmail(text));
                            mEmailValidationMessageTextView.setTextColor(getResources().getColor(R.color.colorAccent));

                            mMemberSignInSubmitButtonTextView.setTextColor(getResources().getColor(R.color.colorWhite));
                            mMemberSignInSubmitButtonTextView.setBackgroundResource(R.drawable.widget_solid_oval_rectangle_iron);
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

    private void requestSignInLocal(final String email, final String password) {

        Map<String, String> params = new HashMap<>();
        params.put(EMAIL, email);
        params.put(PASSWORD, password);
        params.put(PLATFORM, "0");

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST, URL_HOST + PATH + LOGIN_PARAMS, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (Parser.parseSimpleResult(response)) {

                            if (UserSharedPreferences.getStoredToken(getActivity()) != null) {
                                UserSharedPreferences.removeStoredToken(getActivity());
                            }
                            UserSharedPreferences.setStoreToken(getActivity(), Parser.parseSignInUserToken(response));
                            Log.d(TAG, "user token : " + UserSharedPreferences.getStoredToken(getActivity()));

//                            if (UserSharedPreferences.getStoredUserName(getActivity()) != null) {
//                                UserSharedPreferences.removeStoredUserName(getActivity());
//                            }
//                            UserSharedPreferences.setStoreUserName(getActivity(), user.getName());
//                            Log.d(TAG, "user name : " + UserSharedPreferences.getStoredUserName(getActivity()));

                            // sign in firebase user
                            signInFirebaseAccount(email, email);

                            Toast.makeText(getActivity(), "로그인 하였습니다.", Toast.LENGTH_SHORT).show();
                            startActivity(SearchActivity.newIntent(getActivity()));
                            getActivity().finish();
                        } else {
                            Toast.makeText(getActivity(),
                                    "가입된 이메일이 아니거나, 비밀번호가 일치하지 않아요.", Toast.LENGTH_SHORT).show();
                            mMemberSignInSubmitResultTextView.setText("가입된 이메일이 아니거나, 비밀번호가 일치하지 않아요.");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, String.valueOf(error.toString()));
//                        Toast.makeText(getActivity(),
//                                "로그인 중 오류가 발생했어요.", Toast.LENGTH_SHORT).show();
                        Toast.makeText(getActivity(), R.string.progress_dialog_message_error,
                                Toast.LENGTH_SHORT).show();
                    }
                }
        );

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(SOCKET_TIMEOUT_POST_REQ,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        AppSingleton.getInstance(getActivity()).addToRequestQueue(jsonObjectRequest, TAG);
    }

    private void signInFirebaseAccount(String email, String password) {
        Log.d(TAG, "sign in firebase account :" + email);

        mFirebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithEmail:onCompleted:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled i the listener.
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithEmail:failed", task.getException());
                            Toast.makeText(getActivity(),
                                        "fail to sign in firebase user", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
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
