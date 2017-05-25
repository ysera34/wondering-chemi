package com.planet.wondering.chemi.view.fragment;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.planet.wondering.chemi.R;
import com.planet.wondering.chemi.model.config.UserConfig;
import com.planet.wondering.chemi.network.AppSingleton;
import com.planet.wondering.chemi.network.Parser;
import com.planet.wondering.chemi.util.helper.UserSharedPreferences;
import com.planet.wondering.chemi.util.listener.OnMenuSelectedListener;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.planet.wondering.chemi.network.Config.NUMBER_OF_RETRIES;
import static com.planet.wondering.chemi.network.Config.SOCKET_TIMEOUT_GET_REQ;
import static com.planet.wondering.chemi.network.Config.SOCKET_TIMEOUT_POST_REQ;
import static com.planet.wondering.chemi.network.Config.URL_HOST;
import static com.planet.wondering.chemi.network.Config.User.Key.GET_EMAIL;
import static com.planet.wondering.chemi.network.Config.User.Key.GET_PUSH;
import static com.planet.wondering.chemi.network.Config.User.Key.TOKEN;
import static com.planet.wondering.chemi.network.Config.User.PATH;
import static com.planet.wondering.chemi.network.Config.User.SETTING_PATH;

/**
 * Created by yoon on 2017. 2. 10..
 */

public class MemberConfigFragment extends Fragment
        implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private static final String TAG = MemberConfigFragment.class.getSimpleName();

    public static MemberConfigFragment newInstance() {

        Bundle args = new Bundle();

        MemberConfigFragment fragment = new MemberConfigFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private int[] mConfigLayoutIds;
    private RelativeLayout[] mConfigLayouts;
    private ImageView mMemberConfigProfileArrowImageView;
    private TextView mMemberConfigProfileTextView;
    private TextView mMemberConfigVersionTextView;
    private Switch mPushSwitch;
    private Switch mEmailSwitch;

    private UserConfig mUserConfig;
    private String mCurrentAppVersion;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mConfigLayoutIds = new int[]{
                R.id.member_config_profile_layout, R.id.member_config_notice_layout,
                R.id.member_config_request_product_layout,
                R.id.member_config_faq_layout, R.id.member_config_version_layout,
                R.id.member_config_push_notification_layout, R.id.member_config_email_layout,
                R.id.member_config_privacy_layout, R.id.member_config_collaboration_layout};
        mConfigLayouts = new RelativeLayout[mConfigLayoutIds.length];
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_member_config, container, false);

        for (int i = 0; i < mConfigLayoutIds.length; i++) {
            mConfigLayouts[i] = (RelativeLayout) view.findViewById(mConfigLayoutIds[i]);
            mConfigLayouts[i].setOnClickListener(this);
        }
        mMemberConfigProfileArrowImageView = (ImageView) view.findViewById(R.id.member_config_profile_arrow_image_view);
        mMemberConfigProfileTextView = (TextView) view.findViewById(R.id.member_config_profile_text_view);
        mMemberConfigVersionTextView = (TextView) view.findViewById(R.id.member_config_version_text_view);

        mPushSwitch = (Switch) view.findViewById(R.id.member_config_push_notification_switch);
        mEmailSwitch = (Switch) view.findViewById(R.id.member_config_email_switch);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        /**
         * 1. anonymous user
         * 2. local user
         * 3. google or naver user
         */

        requestGetUserConfig(); /* only for app version */
        if (UserSharedPreferences.getStoredToken(getActivity()) == null) {
            mMemberConfigProfileTextView.setText("회원 가입 및 로그인");
            mMemberConfigProfileArrowImageView.setRotation(180f);
            mConfigLayouts[6].setVisibility(View.GONE);
        }

        try {
            PackageInfo packageInfo = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);
            mCurrentAppVersion = packageInfo.versionName;
//            Log.i("version info", version);
            mMemberConfigVersionTextView.setText(mCurrentAppVersion);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }


        mPushSwitch.setChecked(UserSharedPreferences.getStoredGetPush(getActivity()));
        mEmailSwitch.setChecked(UserSharedPreferences.getStoredGetEmail(getActivity()));
        mPushSwitch.setOnCheckedChangeListener(this);
        mEmailSwitch.setOnCheckedChangeListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.member_config_profile_layout:
                mMenuSelectedListener.onMenuSelected(1);
                break;
            case R.id.member_config_notice_layout:
                mMenuSelectedListener.onMenuSelected(2);
                break;
            case R.id.member_config_request_product_layout:
                mMenuSelectedListener.onMenuSelected(3);
                break;
            case R.id.member_config_faq_layout:
                mMenuSelectedListener.onMenuSelected(4);
                break;
            case R.id.member_config_version_layout:
                if (UserSharedPreferences.getStoredToken(getActivity()) != null) {
                    if (mUserConfig != null) {
                        if (mCurrentAppVersion.equals(mUserConfig.getAppVersion())) {
                            Toast.makeText(getActivity(), "최신 버전 입니다.", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getActivity(),
                                    "최신 버전은 " + mUserConfig.getAppVersion() + "입니다. 업데이트가 필요합니다.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                break;
            case R.id.member_config_push_notification_layout:
                if (mPushSwitch.isChecked()) {
                    mPushSwitch.setChecked(false);
                } else {
                    mPushSwitch.setChecked(true);
                }
                break;
            case R.id.member_config_email_layout:
                if (mEmailSwitch.isChecked()) {
                    mEmailSwitch.setChecked(false);
                } else {
                    mEmailSwitch.setChecked(true);
                }
                break;
            case R.id.member_config_privacy_layout:
                mMenuSelectedListener.onMenuSelected(5);
                break;
            case R.id.member_config_collaboration_layout:
                mMenuSelectedListener.onMenuSelected(6);
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.member_config_push_notification_switch:
                if (isChecked) {
                    Toast.makeText(getActivity(), "푸쉬 알림 설정을 하셨어요.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "푸쉬 알림 설정을 해제하셨어요.", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.member_config_email_switch:
                if (isChecked) {
                    Toast.makeText(getActivity(), "이메일 수신 설정을 하셨어요.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "이메일 수신 설정을 해제하셨어요.", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void requestGetUserConfig() {

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET, URL_HOST + PATH + SETTING_PATH,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        mUserConfig = Parser.parseUserConfig(response);
//                        mPushSwitch.setChecked(mUserConfig.isGetPush());
//                        mEmailSwitch.setChecked(mUserConfig.isGetEmail());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, error.toString());
//                        Toast.makeText(getActivity(),
//                                "앱 정보를 가져오는 중에 오류가 발생하였습니다. 잠시후 다시 요쳥해주세요", Toast.LENGTH_SHORT).show();
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

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(SOCKET_TIMEOUT_GET_REQ,
                NUMBER_OF_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        AppSingleton.getInstance(getActivity()).addToRequestQueue(jsonObjectRequest, TAG);
    }

    private void requestPutUserConfig() {

        Map<String, Boolean> params = new HashMap<>();
        params.put(GET_PUSH, mPushSwitch.isChecked());
        params.put(GET_EMAIL, mEmailSwitch.isChecked());

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.PUT, URL_HOST + PATH + SETTING_PATH, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (Parser.parseSimpleResult(response)) {
                            UserSharedPreferences.setStoredGetPush(getActivity(), mPushSwitch.isChecked());
                            UserSharedPreferences.setStoredGetEmail(getActivity(), mEmailSwitch.isChecked());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, error.toString());
//                        Toast.makeText(getActivity(),
//                                "앱 정보 업데이트 중에 오류가 발생하였습니다. 잠시후 다시 요쳥해주세요", Toast.LENGTH_SHORT).show();
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

    private boolean determineRequestUserConfig() {

        boolean isCheckPush = mPushSwitch.isChecked();
        boolean isCheckEmail = mEmailSwitch.isChecked();

        if (isCheckPush != UserSharedPreferences.getStoredGetPush(getActivity())
                || isCheckEmail != UserSharedPreferences.getStoredGetEmail(getActivity())) {
            return true;
        }
        return false;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (determineRequestUserConfig()) {
            requestPutUserConfig();
        }
    }
}
