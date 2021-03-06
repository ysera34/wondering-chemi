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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import com.planet.wondering.chemi.view.activity.UserActivity;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.planet.wondering.chemi.common.Common.SIGN_UP_FOR_PLATFORM_USER_REQUEST_CODE;
import static com.planet.wondering.chemi.network.Config.NUMBER_OF_RETRIES;
import static com.planet.wondering.chemi.network.Config.SOCKET_TIMEOUT_POST_REQ;
import static com.planet.wondering.chemi.network.NetworkConfig.URL_HOST;
import static com.planet.wondering.chemi.network.Config.User.Key.NAME_STRING;
import static com.planet.wondering.chemi.network.Config.User.NAME_STRING_PATH;
import static com.planet.wondering.chemi.network.Config.User.PATH;

/**
 * Created by yoon on 2017. 2. 17..
 */

public class MemberStartNameFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = MemberStartNameFragment.class.getSimpleName();

    private static final String ARG_ANONYMOUS_USER = "anonymous_user";

    public static MemberStartNameFragment newInstance() {

        Bundle args = new Bundle();

        MemberStartNameFragment fragment = new MemberStartNameFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static MemberStartNameFragment newInstance(User user) {

        Bundle args = new Bundle();
        args.putSerializable(ARG_ANONYMOUS_USER, user);

        MemberStartNameFragment fragment = new MemberStartNameFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private InputMethodManager mInputMethodManager;
    private RelativeLayout mMemberStartNameCancelLayout;
    private EditText mMemberStartNameNameEditText;
    private TextView mMemberStartNameNameValidationTextView;
    private TextView mMemberStartNameNameAuthButtonTextView;

    private User mAnonymousUser;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mInputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        mAnonymousUser = (User) getArguments().getSerializable(ARG_ANONYMOUS_USER);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_member_start_name, container, false);

        mMemberStartNameCancelLayout = (RelativeLayout) view.findViewById(R.id.member_start_name_cancel_layout);
        mMemberStartNameCancelLayout.setOnClickListener(this);
        mMemberStartNameNameEditText = (EditText) view.findViewById(R.id.member_start_name_name_edit_text);
        mMemberStartNameNameValidationTextView = (TextView)
                view.findViewById(R.id.member_start_name_name_validation_text_view);
        mMemberStartNameNameAuthButtonTextView = (TextView)
                view.findViewById(R.id.member_start_name_name_auth_button_text_view);
        mMemberStartNameNameAuthButtonTextView.setOnClickListener(this);
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
            case R.id.member_start_name_cancel_layout:
                mInputMethodManager.hideSoftInputFromWindow(mMemberStartNameNameEditText.getWindowToken(), 0);
                getActivity().onBackPressed();
                break;
            case R.id.member_start_name_name_auth_button_text_view:
                mInputMethodManager.hideSoftInputFromWindow(mMemberStartNameNameEditText.getWindowToken(), 0);
                if (isAuthNameValidation) {
                    requestConfirmNameRepetition(mMemberStartNameNameEditText.getText().toString());
                } else {
                    Toast.makeText(getActivity(), getString(R.string.name_validation_message_incorrect),
                            Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private boolean isAuthNameValidation = false;
    private boolean isConfirmName = false;

    private void validateEditText() {
        mMemberStartNameNameEditText.addTextChangedListener(
                new TextValidator(mMemberStartNameNameEditText) {
                    @Override
                    public void validate(TextView textView, String text) {
                        if (validateName(text) == null) {
                            mMemberStartNameNameEditText.setBackgroundResource(R.drawable.edit_text_under_line_correct);
                            mMemberStartNameNameValidationTextView.setText(getString(R.string.name_validation_message_correct));
                            mMemberStartNameNameValidationTextView.setTextColor(getResources().getColor(R.color.colorPrimary));

                            mMemberStartNameNameAuthButtonTextView.setTextColor(getResources().getColorStateList(R.color.color_selector_button_white_primary));
                            mMemberStartNameNameAuthButtonTextView.setBackgroundResource(R.drawable.selector_opaque_primary);
                            isAuthNameValidation = true;
                        } else {
                            mMemberStartNameNameEditText.setBackgroundResource(R.drawable.edit_text_under_line_focus_true_accent);
                            mMemberStartNameNameValidationTextView.setText(validateName(text));
                            mMemberStartNameNameValidationTextView.setTextColor(getResources().getColor(R.color.colorAccent));

                            mMemberStartNameNameAuthButtonTextView.setTextColor(getResources().getColor(R.color.colorWhite));
                            mMemberStartNameNameAuthButtonTextView.setBackgroundResource(R.drawable.widget_solid_oval_rectangle_iron);
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
            } else if (nameText.length() >= 2 && nameText.length() < 7) {
                return null; // nameText request url ~~>
            } else if (nameText.length() >= 7) {
                return getString(R.string.name_validation_message_long);
            }
        }
        return getString(R.string.name_validation_message_incorrect);
    }

    private void requestConfirmNameRepetition(String name) {

        Map<String, String> params = new HashMap<>();
        params.put(NAME_STRING, name);

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
//                            ((MemberStartActivity) getActivity()).requestSubmitUserInfo(mMemberStartNameNameEditText.getText().toString());
                            mAnonymousUser.setName(mMemberStartNameNameEditText.getText().toString());
                            getActivity().startActivityForResult(UserActivity.newIntent(getActivity(),
                                    SIGN_UP_FOR_PLATFORM_USER_REQUEST_CODE, mAnonymousUser), SIGN_UP_FOR_PLATFORM_USER_REQUEST_CODE);
                        } else {
                            mMemberStartNameNameEditText.setBackgroundResource(R.drawable.edit_text_under_line_focus_true_accent);
                            mMemberStartNameNameValidationTextView.setText(getString(R.string.name_submit_message_incorrect));
                            mMemberStartNameNameValidationTextView.setTextColor(getResources().getColor(R.color.colorAccent));
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, String.valueOf(error.toString()));
//                        Toast.makeText(getActivity(),
//                                "닉네임 중복 확인 중 오류가 발생하였습니다. 잠시 후 다시 요청해주세요", Toast.LENGTH_SHORT).show();
                        Toast.makeText(getActivity(), R.string.progress_dialog_message_error,
                                Toast.LENGTH_SHORT).show();
                    }
                }
        );

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(SOCKET_TIMEOUT_POST_REQ,
                NUMBER_OF_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        AppSingleton.getInstance(getActivity()).addToRequestQueue(jsonObjectRequest, TAG);
    }
}
