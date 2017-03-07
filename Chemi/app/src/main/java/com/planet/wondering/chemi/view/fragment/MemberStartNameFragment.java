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
import com.planet.wondering.chemi.view.activity.MemberStartActivity;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;
import static com.planet.wondering.chemi.network.Config.SOCKET_TIMEOUT_POST_REQ;
import static com.planet.wondering.chemi.network.Config.URL_HOST;
import static com.planet.wondering.chemi.network.Config.User.NAME_STRING;
import static com.planet.wondering.chemi.network.Config.User.PATH;

/**
 * Created by yoon on 2017. 2. 17..
 */

public class MemberStartNameFragment extends Fragment implements View.OnClickListener {

    public static MemberStartNameFragment newInstance() {

        Bundle args = new Bundle();

        MemberStartNameFragment fragment = new MemberStartNameFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private InputMethodManager mInputMethodManager;
    private EditText mMemberStartNameNameEditText;
    private TextView mMemberStartNameNameValidationTextView;
    private TextView mMemberStartNameNameAuthButtonTextView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mInputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_member_start_name, container, false);

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

                            mMemberStartNameNameAuthButtonTextView.setTextColor(getResources().getColorStateList(R.color.color_selector_white_primary));
                            mMemberStartNameNameAuthButtonTextView.setBackgroundResource(R.drawable.selector_opaque_primary_transparent_white);
                            isAuthNameValidation = true;
                        } else {
                            mMemberStartNameNameEditText.setBackgroundResource(R.drawable.edit_text_under_line_focus_true_accent);
                            mMemberStartNameNameValidationTextView.setText(validateName(text));
                            mMemberStartNameNameValidationTextView.setTextColor(getResources().getColor(R.color.colorAccent));

                            mMemberStartNameNameAuthButtonTextView.setTextColor(getResources().getColor(R.color.colorPrimary));
                            mMemberStartNameNameAuthButtonTextView.setBackgroundResource(R.drawable.widget_border_oval_rectangle_primary);
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

                        if (isAuthNameValidation && isConfirmName) {
//                            requestSubmitUserInfo();
                            ((MemberStartActivity) getActivity()).requestSubmitUserInfo(mMemberStartNameNameEditText.getText().toString());
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
}
