package com.planet.wondering.chemi.view.activity;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
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
import com.planet.wondering.chemi.util.helper.UserSharedPreferences;
import com.planet.wondering.chemi.util.listener.OnDialogFinishedListener;
import com.planet.wondering.chemi.util.listener.OnMenuSelectedListener;
import com.planet.wondering.chemi.util.listener.OnSurveyCompletedListener;
import com.planet.wondering.chemi.util.listener.OnUserInfoUpdateListener;
import com.planet.wondering.chemi.view.custom.CustomProgressDialog;
import com.planet.wondering.chemi.view.fragment.MemberAskInfoFragment;
import com.planet.wondering.chemi.view.fragment.MemberConfigChangeNameFragment;
import com.planet.wondering.chemi.view.fragment.MemberConfigChangePasswordFragment;
import com.planet.wondering.chemi.view.fragment.MemberConfigFAQFragment;
import com.planet.wondering.chemi.view.fragment.MemberConfigFragment;
import com.planet.wondering.chemi.view.fragment.MemberConfigNoticeFragment;
import com.planet.wondering.chemi.view.fragment.MemberConfigPartnerFragment;
import com.planet.wondering.chemi.view.fragment.MemberConfigProfileFragment;
import com.planet.wondering.chemi.view.fragment.MemberConfigRequestFragment;
import com.planet.wondering.chemi.view.fragment.MemberConfigSignInFragment;
import com.planet.wondering.chemi.view.fragment.MemberConfigTermsFragment;
import com.planet.wondering.chemi.view.fragment.MemberFragment;
import com.planet.wondering.chemi.view.fragment.MemberStartNameFragment;
import com.planet.wondering.chemi.view.fragment.MemberSurveyInfoFragment;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.planet.wondering.chemi.common.Common.CONFIRM_EMAIL_REPETITION_FALSE_CODE;
import static com.planet.wondering.chemi.common.Common.CONFIRM_EMAIL_REPETITION_TRUE_CODE;
import static com.planet.wondering.chemi.common.Common.EXTRA_RESPONSE_USER;
import static com.planet.wondering.chemi.common.Common.EXTRA_RESPONSE_USER_CODE;
import static com.planet.wondering.chemi.common.Common.REVOKE_ACCESS_GOOGLE_REQUEST_CODE;
import static com.planet.wondering.chemi.common.Common.REVOKE_ACCESS_NAVER_REQUEST_CODE;
import static com.planet.wondering.chemi.common.Common.SIGN_IN_GOOGLE_REQUEST_CODE;
import static com.planet.wondering.chemi.common.Common.SIGN_IN_LOCAL_REQUEST_CODE;
import static com.planet.wondering.chemi.common.Common.SIGN_IN_LOCAL_USER_ERROR_CODE;
import static com.planet.wondering.chemi.common.Common.SIGN_IN_LOCAL_USER_FAIL_CODE;
import static com.planet.wondering.chemi.common.Common.SIGN_IN_NAVER_REQUEST_CODE;
import static com.planet.wondering.chemi.common.Common.SIGN_OUT_GOOGLE_REQUEST_CODE;
import static com.planet.wondering.chemi.common.Common.SIGN_OUT_LOCAL_REQUEST_CODE;
import static com.planet.wondering.chemi.common.Common.SIGN_OUT_NAVER_REQUEST_CODE;
import static com.planet.wondering.chemi.common.Common.SIGN_UP_FOR_PLATFORM_USER_REQUEST_CODE;
import static com.planet.wondering.chemi.common.Common.SIGN_UP_FOR_PLATFORM_USER_SUCCESS_CODE;
import static com.planet.wondering.chemi.common.Common.WITHDRAW_GOOGLE_USER_REQUEST_CODE;
import static com.planet.wondering.chemi.common.Common.WITHDRAW_LOCAL_USER_REQUEST_CODE;
import static com.planet.wondering.chemi.common.Common.WITHDRAW_NAVER_USER_REQUEST_CODE;
import static com.planet.wondering.chemi.network.Config.NUMBER_OF_RETRIES;
import static com.planet.wondering.chemi.network.Config.SOCKET_TIMEOUT_GET_REQ;
import static com.planet.wondering.chemi.network.Config.URL_HOST;
import static com.planet.wondering.chemi.network.Config.User.Key.TOKEN;
import static com.planet.wondering.chemi.network.Config.User.PATH;

/**
 * Created by yoon on 2016. 12. 31..
 */

public class MemberActivity extends BottomNavigationActivity implements OnMenuSelectedListener,
        OnDialogFinishedListener, OnUserInfoUpdateListener, OnSurveyCompletedListener {

    private static final String TAG = MemberActivity.class.getSimpleName();

    private static final String EXTRA_REQUEST_ID = "com.planet.wondering.chemi.request_id";
    private static final String EXTRA_IS_FIRST_USER = "com.planet.wondering.chemi.is_first_user";

    private FragmentManager mFragmentManager;
    private Fragment mFragment;

    public static Intent newIntent(Context packageContext) {
        Intent intent = new Intent(packageContext, MemberActivity.class);
        return intent;
    }

    public static Intent newIntent(Context packageContext, int requestId) {
        Intent intent = new Intent(packageContext, MemberActivity.class);
        intent.putExtra(EXTRA_REQUEST_ID, requestId);
        return intent;
    }

    public static Intent newIntent(Context packageContext, boolean isFirstUser) {
        Intent intent = new Intent(packageContext, MemberActivity.class);
        intent.putExtra(EXTRA_IS_FIRST_USER, isFirstUser);
        return intent;
    }

    private String mAccessToken;
    private User mUser;
    private int mRequestId;
    private boolean mIsFirstUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAccessToken = UserSharedPreferences.getStoredToken(getApplicationContext());

        mRequestId = getIntent().getIntExtra(EXTRA_REQUEST_ID, -1);
        mIsFirstUser = getIntent().getBooleanExtra(EXTRA_IS_FIRST_USER, false);

        mFragmentManager = getSupportFragmentManager();
        mFragment = mFragmentManager.findFragmentById(R.id.main_fragment_container);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setupBottomNavigation(4);

        if (mFragment == null) {
            if (mRequestId == -1) {
                if (mAccessToken != null) {
    //                mFragment = MemberFragment.newInstance();
    //                mFragmentManager.beginTransaction()
    //                        .add(R.id.fragment_container, mFragment)
    //                        .commit();
                    requestMemberConfigUser();

                } else {
                    mFragment = MemberConfigSignInFragment.newInstance();
                    mFragmentManager.beginTransaction()
                            .add(R.id.main_fragment_container, mFragment)
                            .commit();
                }
            } else if (mRequestId == 4) {
                mBottomNavigationLayout.setVisibility(View.GONE);
//                hideBottomNavigationView();
                mFragmentManager.beginTransaction()
//                        .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left)
                        .replace(R.id.main_fragment_container, MemberConfigFAQFragment.newInstance())
                        .commit();
            } else if (mRequestId == 5) {
                mBottomNavigationLayout.setVisibility(View.GONE);
                mFragmentManager.beginTransaction()
                        .replace(R.id.main_fragment_container, MemberAskInfoFragment.newInstance(null, 5))
                        .commit();
            } else if (mRequestId == 6) {
                mBottomNavigationLayout.setVisibility(View.GONE);
                mFragmentManager.beginTransaction()
                        .replace(R.id.main_fragment_container, MemberConfigRequestFragment.newInstance())
                        .commit();
            }
        } else if (mFragment instanceof MemberFragment) {
            /* refresh fragment */
            if (mAccessToken != null) {
                requestMemberConfigUser();
            }
        }
    }

    private int mPlatformId;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case SIGN_IN_GOOGLE_REQUEST_CODE:
            case SIGN_IN_NAVER_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    int responseCode = data.getIntExtra(EXTRA_RESPONSE_USER_CODE, -1);
                    switch (responseCode) {
                        case CONFIRM_EMAIL_REPETITION_TRUE_CODE:
                            Toast.makeText(getApplicationContext(), "로그인 하였습니다.", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), MemberActivity.class);
                            finish();
                            startActivity(intent);
                            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

                            break;
                        case CONFIRM_EMAIL_REPETITION_FALSE_CODE:
                            hideBottomNavigationView();
                            User anonymousUser = (User) data.getSerializableExtra(EXTRA_RESPONSE_USER);
                            mFragmentManager.beginTransaction()
                                    .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left)
                                    .replace(R.id.main_fragment_container,
                                            MemberStartNameFragment.newInstance(anonymousUser))
                                    .commit();
                            mPlatformId = anonymousUser.getPlatformId();
                            break;
                    }
                }
                break;
            case SIGN_UP_FOR_PLATFORM_USER_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    int responseCode = data.getIntExtra(EXTRA_RESPONSE_USER_CODE, -1);
                    switch (responseCode) {
                        case SIGN_UP_FOR_PLATFORM_USER_SUCCESS_CODE:
                            mFragmentManager.beginTransaction()
                                    .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left)
                                    .replace(R.id.main_fragment_container, MemberSurveyInfoFragment.newInstance())
                                    .commit();
                            break;
                    }
                }
                break;
            case SIGN_IN_LOCAL_REQUEST_CODE:
                int responseCode = data.getIntExtra(EXTRA_RESPONSE_USER_CODE, -1);
                if (resultCode == RESULT_OK) {
                    Toast.makeText(getApplicationContext(), "로그인 하였습니다.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), MemberActivity.class);
                    finish();
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                } else if (resultCode == RESULT_CANCELED) {
                    switch (responseCode) {
                        case SIGN_IN_LOCAL_USER_FAIL_CODE:
                            Toast.makeText(getApplicationContext(),
                                    "가입된 이메일이 아니거나, 비밀번호가 일치하지 않아요.", Toast.LENGTH_SHORT).show();
                            break;
                        case SIGN_IN_LOCAL_USER_ERROR_CODE:
//                            Toast.makeText(getApplicationContext(), R.string.progress_dialog_message_error,
//                                    Toast.LENGTH_SHORT).show();
                            break;
                    }
                }
                break;

            case SIGN_OUT_LOCAL_REQUEST_CODE:
            case SIGN_OUT_GOOGLE_REQUEST_CODE:
            case SIGN_OUT_NAVER_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    Toast.makeText(getApplicationContext(), "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show();
                    UserSharedPreferences.removeStoredToken(getApplicationContext());
                    UserSharedPreferences.removeStoredUserName(getApplicationContext());
                    mFragmentManager.beginTransaction()
                            .setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right)
                            .replace(R.id.main_fragment_container, MemberConfigFragment.newInstance())
                            .commit();
                }

                break;

            case REVOKE_ACCESS_GOOGLE_REQUEST_CODE:
            case REVOKE_ACCESS_NAVER_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    Toast.makeText(getApplicationContext(), "연동해제 하였습니다.", Toast.LENGTH_SHORT).show();
                    UserSharedPreferences.removeStoredToken(getApplicationContext());
                    UserSharedPreferences.removeStoredUserName(getApplicationContext());
                    mFragmentManager.beginTransaction()
                            .setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right)
                            .replace(R.id.main_fragment_container, MemberConfigFragment.newInstance())
                            .commit();
                }
                break;

            case WITHDRAW_GOOGLE_USER_REQUEST_CODE:
            case WITHDRAW_NAVER_USER_REQUEST_CODE:
            case WITHDRAW_LOCAL_USER_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    Toast.makeText(getApplicationContext(), "탈퇴 하였습니다.", Toast.LENGTH_SHORT).show();
                    UserSharedPreferences.removeStoredToken(getApplicationContext());
                    UserSharedPreferences.removeStoredUserName(getApplicationContext());
                    mFragmentManager.beginTransaction()
                            .setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right)
                            .replace(R.id.main_fragment_container, MemberConfigFragment.newInstance())
                            .commit();
                }
                break;
        }
    }

    @Override
    public void onMenuSelected(int layoutIndex) {
        switch (layoutIndex) {
            case -1:
                mFragmentManager.beginTransaction()
                        .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left)
                        .replace(R.id.main_fragment_container, MemberConfigFragment.newInstance())
                        .commit();
                break;
            case 1:
                if (UserSharedPreferences.getStoredToken(getApplicationContext()) != null) {
                    mFragmentManager.beginTransaction()
                            .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left)
                            .replace(R.id.main_fragment_container, MemberConfigProfileFragment.newInstance(mUser))
                            .commit();
                } else {
//                    startActivity(MemberStartActivity.newIntent(getApplicationContext()));
//                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
//                    finish();
                    mFragmentManager.beginTransaction()
                            .setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right)
                            .replace(R.id.main_fragment_container, MemberConfigSignInFragment.newInstance())
                            .commit();
                }

                break;
            case 2:
                hideBottomNavigationView();
                mFragmentManager.beginTransaction()
                        .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left)
                        .replace(R.id.main_fragment_container, MemberConfigNoticeFragment.newInstance())
                        .commit();
                break;
            case 3:
                hideBottomNavigationView();
                mFragmentManager.beginTransaction()
                        .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left)
                        .replace(R.id.main_fragment_container, MemberConfigRequestFragment.newInstance())
                        .commit();
                break;
            case 4:
                hideBottomNavigationView();
                mFragmentManager.beginTransaction()
                        .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left)
                        .replace(R.id.main_fragment_container, MemberConfigFAQFragment.newInstance())
                        .commit();
                break;
            case 5:
//                mBottomNavigationLayout.setVisibility(View.GONE);
                hideBottomNavigationView();
                mFragmentManager.beginTransaction()
                        .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left)
                        .replace(R.id.main_fragment_container, MemberConfigTermsFragment.newInstance())
                        .commit();
                break;
            case 6:
                mFragmentManager.beginTransaction()
                        .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left)
                        .replace(R.id.main_fragment_container, MemberConfigPartnerFragment.newInstance())
                        .commit();
                break;

            case 11:
                mFragmentManager.beginTransaction()
                        .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left)
                        .replace(R.id.main_fragment_container, MemberConfigChangeNameFragment.newInstance(mUser.getName()))
                        .commit();
                break;
            case 12:
                mFragmentManager.beginTransaction()
                        .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left)
                        .replace(R.id.main_fragment_container, MemberConfigChangePasswordFragment.newInstance())
                        .commit();
                break;
            case 13:
//                mBottomNavigationLayout.setVisibility(View.GONE);
                hideBottomNavigationView();
                mFragmentManager.beginTransaction()
                        .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left)
                        .replace(R.id.main_fragment_container, MemberAskInfoFragment.newInstance(mUser))
                        .commit();
                break;
        }
    }

    @Override
    public void onDialogFinished(boolean isChose, int requestCode) {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.main_fragment_container);
        if (fragment instanceof MemberConfigProfileFragment) {
            ((MemberConfigProfileFragment) fragment).onDialogFinished(isChose, requestCode);
        }
    }

    @Override
    public void onUserNameInfoUpdate(String userName) {
        mUser.setName(userName);
    }

    @Override
    public void onUserImagePathUpdate(String userImagePath) {
//        Log.d(TAG, userImagePath);
        mUser.setImagePath(userImagePath);
//        Log.d(TAG, mUser.toString());
    }

    private boolean isInfoValueUpdate = false;

    @Override
    public void onUserInfoValueUpdate() {

        isInfoValueUpdate = true;
        requestMemberConfigUser();
    }

    @Override
    public void onUserInfoValueUpdate(User user) {

        mUser.setBirthYear(user.getBirthYear());
        mUser.setGender(user.isGender());
        mUser.setHasDrySkin(user.isHasDrySkin());
        mUser.setHasOilySkin(user.isHasOilySkin());
        mUser.setHasAllergy(user.isHasAllergy());
        mUser.setHasChild(user.isHasChild());
        mUser.setChildHasDrySkin(user.isChildHasDrySkin());
        mUser.setChildHasAllergy(user.isChildHasAllergy());
    }

    @Override
    public void onSurveyCompleted(int stageNumber, boolean isCompleted) {
        MemberSurveyInfoFragment surveyInfoFragment = (MemberSurveyInfoFragment) getSupportFragmentManager()
                .findFragmentById(R.id.main_fragment_container);
        surveyInfoFragment.updateConfirmButtonTextView(stageNumber, isCompleted);
    }

    @Override
    public void onSurveyValueSubmit(int stageNumber, int value) {
        MemberSurveyInfoFragment surveyInfoFragment = (MemberSurveyInfoFragment) getSupportFragmentManager()
                .findFragmentById(R.id.main_fragment_container);
        surveyInfoFragment.updateUserInfo(stageNumber, value);
    }

    @Override
    public void onBackPressed() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.main_fragment_container);
//        MemberConfigFragment memberConfigFragment = MemberConfigFragment.newInstance();
        if (fragment instanceof MemberConfigSignInFragment) {
            finish();
        } else if (fragment instanceof MemberConfigFragment) {
            if (UserSharedPreferences.getStoredToken(getApplicationContext()) != null) {
                mFragmentManager.beginTransaction()
                        .setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right)
                        .replace(R.id.main_fragment_container, MemberFragment.newInstance(mUser))
                        .commit();
            } else {
//                finish();
                mFragmentManager.beginTransaction()
                        .setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right)
                        .replace(R.id.main_fragment_container, MemberConfigSignInFragment.newInstance())
                        .commit();
            }
        } else if (fragment instanceof MemberConfigProfileFragment) {
            mFragmentManager.beginTransaction()
                    .setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right)
                    .replace(R.id.main_fragment_container, MemberConfigFragment.newInstance())
                    .commit();
        } else if (fragment instanceof MemberConfigNoticeFragment) {
            showBottomNavigationView();
            mFragmentManager.beginTransaction()
                    .setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right)
                    .replace(R.id.main_fragment_container, MemberConfigFragment.newInstance())
                    .commit();
        } else if (fragment instanceof MemberConfigRequestFragment) {
            showBottomNavigationView();
            if (mRequestId == -1) {
                mFragmentManager.beginTransaction()
                        .setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right)
                        .replace(R.id.main_fragment_container, MemberConfigFragment.newInstance())
                        .commit();
            } else if (mRequestId == 6) {
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        } else if (fragment instanceof MemberConfigFAQFragment) {
            showBottomNavigationView();
            if (mRequestId == -1) {
                mFragmentManager.beginTransaction()
                        .setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right)
                        .replace(R.id.main_fragment_container, MemberConfigFragment.newInstance())
                        .commit();
            } else if (mRequestId == 4){
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        } else if (fragment instanceof MemberConfigTermsFragment) {
//            mBottomNavigationLayout.setVisibility(View.VISIBLE);
            showBottomNavigationView();
            mFragmentManager.beginTransaction()
                    .setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right)
                    .replace(R.id.main_fragment_container, MemberConfigFragment.newInstance())
                    .commit();
        } else if (fragment instanceof MemberConfigPartnerFragment) {
            mFragmentManager.beginTransaction()
                    .setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right)
                    .replace(R.id.main_fragment_container, MemberConfigFragment.newInstance())
                    .commit();
        } else if (fragment instanceof MemberConfigChangeNameFragment) {
            mFragmentManager.beginTransaction()
                    .setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right)
                    .replace(R.id.main_fragment_container, MemberConfigProfileFragment.newInstance(mUser))
                    .commit();
        } else if (fragment instanceof MemberConfigChangePasswordFragment) {
            mFragmentManager.beginTransaction()
                    .setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right)
                    .replace(R.id.main_fragment_container, MemberConfigProfileFragment.newInstance(mUser))
                    .commit();
        } else if (fragment instanceof MemberAskInfoFragment) {
//            mBottomNavigationLayout.setVisibility(View.VISIBLE);
            showBottomNavigationView();
            if (mRequestId == -1) {
                mFragmentManager.beginTransaction()
                        .setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right)
                        .replace(R.id.main_fragment_container, MemberConfigProfileFragment.newInstance(mUser))
                        .commit();
            } else if (mRequestId == 5){
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        } else {
            super.onBackPressed();
        }
    }

    private void requestMemberConfigUser() {

//        final ProgressDialog progressDialog =
//                ProgressDialog.show(MemberActivity.this,
//                        "회원님의 정보를 가져오고 있습니다.",
//                        getString(R.string.progress_dialog_message_wait), false, false);
//        final ProgressDialog progressDialog = new ProgressDialog(MemberActivity.this);
//        progressDialog.setIndeterminate(true);
//        progressDialog.setIndeterminateDrawable(getResources().getDrawable(R.drawable.progress_bar_animation));
//        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        progressDialog.show();

        final CustomProgressDialog progressDialog = new CustomProgressDialog(MemberActivity.this);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progressDialog.show();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET, URL_HOST + PATH,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
//                        Log.i(TAG, response.toString());
                        mUser = Parser.parseMemberConfigUser(response);
                        progressDialog.dismiss();

//                        Log.i(TAG, mUser.toString());

                        if (!isInfoValueUpdate) {
                            mFragment = mFragmentManager.findFragmentById(R.id.main_fragment_container);
                            if (mFragment == null) {
                                mFragmentManager.beginTransaction()
                                        .add(R.id.main_fragment_container, MemberFragment.newInstance(mUser, mIsFirstUser))
                                        .commit();
                            } else if (mFragment instanceof MemberFragment) {
                                mFragmentManager.beginTransaction()
                                        .replace(R.id.main_fragment_container, MemberFragment.newInstance(mUser, mIsFirstUser))
                                        .commit();
                            }
//                            mFragment = MemberFragment.newInstance(mUser);
//                            mFragmentManager.beginTransaction()
//                                    .add(R.id.main_fragment_container, mFragment)
//                                    .commit();
                        } else {
                            isInfoValueUpdate = false;
                            mBottomNavigationLayout.setVisibility(View.VISIBLE);
                            mFragmentManager.beginTransaction()
                                    .setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right)
                                    .replace(R.id.main_fragment_container, MemberConfigProfileFragment.newInstance(mUser))
                                    .commit();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Log.e(TAG, error.toString());
                        Toast.makeText(getApplicationContext(), R.string.progress_dialog_message_error,
                                Toast.LENGTH_SHORT).show();
//                        if (error instanceof com.android.volley.TimeoutError) {
//                            Toast.makeText(getApplicationContext(),
//                                    "회원 정보 요청 중 문제가 발생하였습니다. 다시 요청 하겠습니다.", Toast.LENGTH_SHORT).show();
//
//                            Intent intent = new Intent(MemberActivity.this, MemberActivity.class);
//                            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                            startActivity(intent);
//
//                        } else if (error instanceof com.android.volley.NoConnectionError) {
//                            Toast.makeText(getApplicationContext(),
//                                    "네트워크에 문제가 발생하였습니다.", Toast.LENGTH_SHORT).show();
//                        }
                    }
                }
        )
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put(TOKEN, mAccessToken);
                return params;
            }
        };

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(SOCKET_TIMEOUT_GET_REQ,
                NUMBER_OF_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest, TAG);
    }
}
