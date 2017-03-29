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
import com.planet.wondering.chemi.network.AppSingleton;
import com.planet.wondering.chemi.network.Parser;
import com.planet.wondering.chemi.util.helper.TextValidator;
import com.planet.wondering.chemi.util.listener.OnMenuSelectedListener;
import com.planet.wondering.chemi.view.activity.MemberStartActivity;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.planet.wondering.chemi.network.Config.SOCKET_TIMEOUT_POST_REQ;
import static com.planet.wondering.chemi.network.Config.URL_HOST;
import static com.planet.wondering.chemi.network.Config.User.EMAIL_PATH;
import static com.planet.wondering.chemi.network.Config.User.Key.EMAIL;
import static com.planet.wondering.chemi.network.Config.User.PATH;

/**
 * Created by yoon on 2017. 2. 18..
 */

public class MemberForgetPasswordFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = MemberForgetPasswordFragment.class.getSimpleName();

    public static MemberForgetPasswordFragment newInstance() {

        Bundle args = new Bundle();

        MemberForgetPasswordFragment fragment = new MemberForgetPasswordFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private RelativeLayout mMemberForgetPasswordCancelLayout;

    private InputMethodManager mInputMethodManager;
    private EditText mMemberForgetPasswordEmailEditText;
    private TextView mEmailValidationMessageTextView;
    private TextView mMemberForgetPasswordSubmitButtonTextView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mInputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_member_forget_password, container, false);
        mMemberForgetPasswordCancelLayout = (RelativeLayout)
                view.findViewById(R.id.member_forget_password_cancel_layout);
        mMemberForgetPasswordCancelLayout.setOnClickListener(this);

        mMemberForgetPasswordEmailEditText = (EditText)
                view.findViewById(R.id.member_forget_password_email_edit_text);
        mEmailValidationMessageTextView = (TextView)
                view.findViewById(R.id.member_forget_password_email_validation_text_view);
        mMemberForgetPasswordSubmitButtonTextView = (TextView)
                view.findViewById(R.id.member_forget_password_email_submit_button_text_view);
        mMemberForgetPasswordSubmitButtonTextView.setOnClickListener(this);

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
            case R.id.member_forget_password_cancel_layout:
                mMenuSelectedListener.onMenuSelected(7006);
                break;
            case R.id.member_forget_password_email_submit_button_text_view:
                mInputMethodManager.hideSoftInputFromWindow(mMemberForgetPasswordEmailEditText.getWindowToken(), 0);
                if (isAuthEmailValidation) {
                    requestSendAuthEmail(mMemberForgetPasswordEmailEditText.getText().toString());
                } else {
                    Toast.makeText(getActivity(), getString(R.string.email_validation_message_wrong_pattern),
                            Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private boolean isAuthEmailValidation = false;

    private void validateEditText() {
        mMemberForgetPasswordEmailEditText.addTextChangedListener(
                new TextValidator(mMemberForgetPasswordEmailEditText) {
            @Override
            public void validate(TextView textView, String text) {
                if (validateEmail(text) == null) {
                    mMemberForgetPasswordEmailEditText.setBackgroundResource(R.drawable.edit_text_under_line_correct);
                    mEmailValidationMessageTextView.setText("");
                    mEmailValidationMessageTextView.setTextColor(getResources().getColor(R.color.colorPrimary));

                    mMemberForgetPasswordSubmitButtonTextView.setTextColor(getResources().getColorStateList(R.color.color_selector_button_white_primary));
                    mMemberForgetPasswordSubmitButtonTextView.setBackgroundResource(R.drawable.selector_opaque_primary);
                    isAuthEmailValidation = true;
                } else {
                    mMemberForgetPasswordEmailEditText.setBackgroundResource(R.drawable.edit_text_under_line_focus_true_accent);
                    mEmailValidationMessageTextView.setText(validateEmail(text));
                    mEmailValidationMessageTextView.setTextColor(getResources().getColor(R.color.colorAccent));

                    mMemberForgetPasswordSubmitButtonTextView.setTextColor(getResources().getColor(R.color.colorWhite));
                    mMemberForgetPasswordSubmitButtonTextView.setBackgroundResource(R.drawable.widget_solid_oval_rectangle_iron);
                    isAuthEmailValidation = false;
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
            return getString(R.string.email_validation_message_wrong_pattern);
        } else {
            return null;
        }
    }

    private void requestSendAuthEmail(final String emailAddress) {

        Map<String, String> params = new HashMap<>();
        params.put(EMAIL, emailAddress);
        params.put("resetsPassword", "1");

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST, URL_HOST + PATH + EMAIL_PATH, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (Parser.parseSimpleResult(response)) {
                            ((MemberStartActivity) getActivity()).replaceFragment(emailAddress);
                            Toast.makeText(getActivity(),
                                    "메일 발송이 완료 되었습니다.\n메일함에서 확인해주세요.", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getActivity(),
                                    "사용 할 수 없는 메일 주소예요. 메일 주소를 확인해주세요.", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, String.valueOf(error.getMessage()));
                        Toast.makeText(getActivity(),
                                "메일 발송 중 오류가 발생하였습니다. 잠시 후 다시 요청해주세요.", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(SOCKET_TIMEOUT_POST_REQ,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        AppSingleton.getInstance(getActivity()).addToRequestQueue(jsonObjectRequest, TAG);
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
