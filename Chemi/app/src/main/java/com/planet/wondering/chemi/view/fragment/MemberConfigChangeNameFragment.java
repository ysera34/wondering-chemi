package com.planet.wondering.chemi.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
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
import com.planet.wondering.chemi.network.AppSingleton;
import com.planet.wondering.chemi.network.Parser;
import com.planet.wondering.chemi.util.helper.TextValidator;
import com.planet.wondering.chemi.util.helper.UserSharedPreferences;
import com.planet.wondering.chemi.util.listener.OnUserInfoUpdateListener;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.planet.wondering.chemi.network.Config.SOCKET_TIMEOUT_GET_REQ;
import static com.planet.wondering.chemi.network.Config.SOCKET_TIMEOUT_POST_REQ;
import static com.planet.wondering.chemi.network.Config.URL_HOST;
import static com.planet.wondering.chemi.network.Config.User.Key.NAME;
import static com.planet.wondering.chemi.network.Config.User.Key.TOKEN;
import static com.planet.wondering.chemi.network.Config.User.NAME_PATH;
import static com.planet.wondering.chemi.network.Config.User.NAME_STRING_PATH;
import static com.planet.wondering.chemi.network.Config.User.PATH;

/**
 * Created by yoon on 2017. 3. 20..
 */

public class MemberConfigChangeNameFragment extends Fragment
        implements View.OnClickListener, View.OnFocusChangeListener {

    private static final String TAG = MemberConfigChangeNameFragment.class.getSimpleName();

    private static final String ARG_USER_NAME = "user_name";

    public static MemberConfigChangeNameFragment newInstance() {

        Bundle args = new Bundle();

        MemberConfigChangeNameFragment fragment = new MemberConfigChangeNameFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static MemberConfigChangeNameFragment newInstance(String userName) {

        Bundle args = new Bundle();
        args.putString(ARG_USER_NAME, userName);

        MemberConfigChangeNameFragment fragment = new MemberConfigChangeNameFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private LinearLayout mBackLayout;

    private InputMethodManager mInputMethodManager;
    private EditText mMemberConfigChangeNameEditText;
    private TextView mMemberConfigChangeNameValidationTextView;
    private TextView mMemberConfigChangeNameAuthButtonTextView;

    private String mUserName;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mInputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

        mUserName = getArguments().getString(ARG_USER_NAME, null);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_member_config_change_name, container, false);
        mBackLayout = (LinearLayout) view.findViewById(R.id.member_config_change_name_back_layout);
        mBackLayout.setOnClickListener(this);

        mMemberConfigChangeNameEditText = (EditText) view.findViewById(R.id.member_config_change_name_edit_text);
        mMemberConfigChangeNameEditText.setOnFocusChangeListener(this);
        mMemberConfigChangeNameEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE) {
                    mInputMethodManager.hideSoftInputFromWindow(mMemberConfigChangeNameEditText.getWindowToken(), 0);
                    if (isAuthNameValidation) {
                        requestConfirmNameRepetition(mMemberConfigChangeNameEditText.getText().toString());
                    } else {
                        Toast.makeText(getActivity(), getString(R.string.name_validation_message_incorrect),
                                Toast.LENGTH_SHORT).show();
                    }
                }
                return false;
            }
        });
        mMemberConfigChangeNameValidationTextView =
                (TextView) view.findViewById(R.id.member_config_change_name_validation_text_view);
        mMemberConfigChangeNameAuthButtonTextView =
                (TextView) view.findViewById(R.id.member_config_change_name_auth_button_text_view);
        mMemberConfigChangeNameAuthButtonTextView.setOnClickListener(this);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mMemberConfigChangeNameEditText.setText(mUserName);
        mMemberConfigChangeNameAuthButtonTextView.setEnabled(false);
        validateEditText();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.member_config_change_name_back_layout:
                mInputMethodManager.hideSoftInputFromWindow(mMemberConfigChangeNameEditText.getWindowToken(), 0);
                getActivity().onBackPressed();
                break;

            case R.id.member_config_change_name_auth_button_text_view:
                mInputMethodManager.hideSoftInputFromWindow(mMemberConfigChangeNameEditText.getWindowToken(), 0);
                if (isAuthNameValidation) {
                    requestConfirmNameRepetition(mMemberConfigChangeNameEditText.getText().toString());
                } else {
                    Toast.makeText(getActivity(), getString(R.string.name_validation_message_incorrect),
                            Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        switch (v.getId()) {
            case R.id.member_config_change_name_edit_text:
                mMemberConfigChangeNameEditText.getText().clear();
                mMemberConfigChangeNameEditText.setText(mUserName);
                mMemberConfigChangeNameEditText.setSelection(mUserName.length());
                mMemberConfigChangeNameAuthButtonTextView.setEnabled(true);
                break;
        }
    }

    OnUserInfoUpdateListener mUserInfoUpdateListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mUserInfoUpdateListener = (OnUserInfoUpdateListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnUserInfoUpdateListener");
        }
    }

    private boolean isAuthNameValidation = false;
    private boolean isConfirmName = false;

    private void validateEditText() {
        mMemberConfigChangeNameEditText.addTextChangedListener(
                new TextValidator(mMemberConfigChangeNameEditText) {
                    @Override
                    public void validate(TextView textView, String text) {
                        if (validateName(text) == null) {
                            if (text.equals(mUserName)) {
                                mMemberConfigChangeNameValidationTextView.setText("현재 사용중인 닉네임입니다.");
                            } else {
                                mMemberConfigChangeNameValidationTextView.setText(getString(R.string.name_validation_message_correct));
                            }
                                mMemberConfigChangeNameEditText.setBackgroundResource(R.drawable.edit_text_under_line_correct);
                                mMemberConfigChangeNameValidationTextView.setTextColor(getResources().getColor(R.color.colorPrimary));

                                mMemberConfigChangeNameAuthButtonTextView.setTextColor(getResources().getColorStateList(R.color.color_selector_button_white_primary));
                                mMemberConfigChangeNameAuthButtonTextView.setBackgroundResource(R.drawable.selector_opaque_primary);
                                isAuthNameValidation = true;
                        } else {
                            mMemberConfigChangeNameEditText.setBackgroundResource(R.drawable.edit_text_under_line_focus_true_accent);
                            mMemberConfigChangeNameValidationTextView.setText(validateName(text));
                            mMemberConfigChangeNameValidationTextView.setTextColor(getResources().getColor(R.color.colorAccent));

                            mMemberConfigChangeNameAuthButtonTextView.setTextColor(getResources().getColor(R.color.colorWhite));
                            mMemberConfigChangeNameAuthButtonTextView.setBackgroundResource(R.drawable.widget_solid_oval_rectangle_iron);
                            isAuthNameValidation = false;
                        }
                    }
                }
        );
    }

    private String validateName(String nameText) {
        if (nameText != null) {
            if (nameText.length() < 2) {
                return getString(R.string.name_validation_message_short);
            } else if (nameText.length() >= 2 && nameText.length() < 11) {
                return null; // nameText request url ~~>
            } else if (nameText.length() >= 11) {
                return getString(R.string.name_validation_message_long);
            }
        }
        return getString(R.string.name_validation_message_incorrect);
    }

    private void requestConfirmNameRepetition(final String name) {

        if (name.equals(mUserName)) {
            Toast.makeText(getActivity(), "사용 중인 닉네임을 계속 사용합니다.", Toast.LENGTH_SHORT).show();
            getActivity().onBackPressed();
            return;
        }

        Map<String, String> params = new HashMap<>();
        params.put("nameString", name);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST, URL_HOST + PATH + NAME_STRING_PATH, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (Parser.parseSimpleResult(response)) {
                            isConfirmName = true;
                        } else {
                            isConfirmName = false;
                        }

                        if (isAuthNameValidation && isConfirmName) {
//                            requestSubmitUserInfo();
//                            ((MemberStartActivity) getActivity()).requestSubmitUserInfo(mMemberConfigChangeNameEditText.getText().toString());
//                            Toast.makeText(getActivity(), "회원 수정 리퀘스트.", Toast.LENGTH_SHORT).show();
                            requestUpdateUserName(name);
                        } else {
                            mMemberConfigChangeNameEditText.setBackgroundResource(R.drawable.edit_text_under_line_focus_true_accent);
                            mMemberConfigChangeNameValidationTextView.setText(getString(R.string.name_submit_message_incorrect));
                            mMemberConfigChangeNameValidationTextView.setTextColor(getResources().getColor(R.color.colorAccent));
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, String.valueOf(error.toString()));
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

    private void requestUpdateUserName(final String userName) {

        Map<String, String> params = new HashMap<>();
        params.put(NAME, String.valueOf(userName));

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.PUT, URL_HOST + PATH + NAME_PATH, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i(TAG, response.toString());
                        Toast.makeText(getActivity(),
                                "회원님의 닉네임이 수정되었어요.", Toast.LENGTH_SHORT).show();
                        mUserInfoUpdateListener.onUserNameInfoUpdate(Parser.parseUpdatedUserName(response));
                        getActivity().onBackPressed();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, error.toString());
                        Toast.makeText(getActivity(),
                                "닉네임 수정중에 오류가 발생하였어요. 잠시후 다시 요쳥해주세요", Toast.LENGTH_SHORT).show();
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

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(SOCKET_TIMEOUT_GET_REQ,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        AppSingleton.getInstance(getActivity()).addToRequestQueue(jsonObjectRequest, TAG);
    }
}
