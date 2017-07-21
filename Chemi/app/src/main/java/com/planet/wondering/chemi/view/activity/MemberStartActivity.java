package com.planet.wondering.chemi.view.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.widget.Toast;

import com.planet.wondering.chemi.R;
import com.planet.wondering.chemi.model.User;
import com.planet.wondering.chemi.util.helper.UserSharedPreferences;
import com.planet.wondering.chemi.util.listener.OnMenuSelectedListener;
import com.planet.wondering.chemi.util.listener.OnSurveyCompletedListener;
import com.planet.wondering.chemi.view.fragment.MemberAskInfoFragment;
import com.planet.wondering.chemi.view.fragment.MemberChangePasswordFragment;
import com.planet.wondering.chemi.view.fragment.MemberConfigTermsFragment;
import com.planet.wondering.chemi.view.fragment.MemberForgetPasswordFragment;
import com.planet.wondering.chemi.view.fragment.MemberSendEmailFragment;
import com.planet.wondering.chemi.view.fragment.MemberSignInLocalFragment;
import com.planet.wondering.chemi.view.fragment.MemberStartFragment;
import com.planet.wondering.chemi.view.fragment.MemberStartLocalFragment;
import com.planet.wondering.chemi.view.fragment.MemberStartNameFragment;
import com.planet.wondering.chemi.view.fragment.MemberSurveyInfoFragment;

import static com.planet.wondering.chemi.common.Common.CONFIRM_EMAIL_REPETITION_FALSE_CODE;
import static com.planet.wondering.chemi.common.Common.CONFIRM_EMAIL_REPETITION_TRUE_CODE;
import static com.planet.wondering.chemi.common.Common.EXTRA_RESPONSE_USER;
import static com.planet.wondering.chemi.common.Common.EXTRA_RESPONSE_USER_CODE;
import static com.planet.wondering.chemi.common.Common.GOOGLE_USER_PLATFORM_ID;
import static com.planet.wondering.chemi.common.Common.IS_NOW_USED_USER_REQUEST_CODE;
import static com.planet.wondering.chemi.common.Common.NAVER_USER_PLATFORM_ID;
import static com.planet.wondering.chemi.common.Common.REVOKE_ACCESS_GOOGLE_REQUEST_CODE;
import static com.planet.wondering.chemi.common.Common.REVOKE_ACCESS_NAVER_REQUEST_CODE;
import static com.planet.wondering.chemi.common.Common.SIGN_IN_GOOGLE_REQUEST_CODE;
import static com.planet.wondering.chemi.common.Common.SIGN_IN_LOCAL_REQUEST_CODE;
import static com.planet.wondering.chemi.common.Common.SIGN_IN_LOCAL_USER_ERROR_CODE;
import static com.planet.wondering.chemi.common.Common.SIGN_IN_LOCAL_USER_FAIL_CODE;
import static com.planet.wondering.chemi.common.Common.SIGN_IN_NAVER_REQUEST_CODE;
import static com.planet.wondering.chemi.common.Common.SIGN_UP_FOR_PLATFORM_USER_REQUEST_CODE;
import static com.planet.wondering.chemi.common.Common.SIGN_UP_FOR_PLATFORM_USER_SUCCESS_CODE;

/**
 * Created by yoon on 2017. 2. 12..
 */

public class MemberStartActivity extends AppBaseActivity
        implements OnMenuSelectedListener, OnSurveyCompletedListener {

    private static final String TAG = MemberStartActivity.class.getSimpleName();
    private static final String EXTRA_REQUEST_ID = "com.planet.wondering.chemi.request_id";

    public static Intent newIntent(Context packageContext) {
        Intent intent = new Intent(packageContext, MemberStartActivity.class);
        return intent;
    }

    public static Intent newIntent(Context packageContext, int requestId) {
        Intent intent = new Intent(packageContext, MemberStartActivity.class);
        intent.putExtra(EXTRA_REQUEST_ID, requestId);
        return intent;
    }

    private FragmentManager mFragmentManager;
    private Fragment mFragment;

    private int mPlatformId;
    private int mRequestId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.i(TAG, "shared preferences accessToken : "
                + UserSharedPreferences.getStoredToken(getApplicationContext()));
        if (UserSharedPreferences.getStoredToken(getApplicationContext()) != null) {
            Intent intent = new Intent(MemberStartActivity.this, HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        }

        setContentView(R.layout.activity_member_start);

        mFragmentManager = getSupportFragmentManager();
        mFragment = mFragmentManager.findFragmentById(R.id.member_fragment_container);

        mRequestId = getIntent().getIntExtra(EXTRA_REQUEST_ID, -1);
        executeRequestId(mRequestId);
    }

    @Override
    protected void onResume() {
        super.onResume();

        Uri uriData = getIntent().getData();
        String accessToken = null;
        String resetsPassword = null;
        if (uriData != null) {
//            Log.i(TAG, String.valueOf(uriData.toString()));
            accessToken = uriData.getQueryParameter("accesstoken");
            resetsPassword = uriData.getQueryParameter("resetspassword");
        }
        if (accessToken != null) {
//            Log.i(TAG, "accesstoken : " + accessToken);
            if (resetsPassword.equals("0")) {
                try {
                    MemberStartLocalFragment fragment = (MemberStartLocalFragment) getSupportFragmentManager()
                            .findFragmentById(R.id.member_fragment_container);
                    fragment.updateUIByAuthEmail(accessToken);
                } catch (ClassCastException e) {
                    Toast.makeText(getApplicationContext(), "비정상 요청이예요.", Toast.LENGTH_SHORT).show();
                    startActivity(MemberStartActivity.newIntent(getApplication()));
                }
            } else if (resetsPassword.equals("1")) {
                try {
                    MemberSendEmailFragment fragment = (MemberSendEmailFragment) getSupportFragmentManager()
                            .findFragmentById(R.id.member_fragment_container);
                    fragment.updateUIByAuthEmail(accessToken);
                } catch (ClassCastException e) {
                    Toast.makeText(getApplicationContext(), "비정상 요청이예요.", Toast.LENGTH_SHORT).show();
                    startActivity(MemberStartActivity.newIntent(getApplication()));
                }
            }
        }
    }

    private void executeRequestId(int requestId) {
        if (mFragment == null) {
            if (mRequestId == -1 || mRequestId == IS_NOW_USED_USER_REQUEST_CODE) {
                mFragment = MemberStartFragment.newInstance();
                mFragmentManager.beginTransaction()
                        .add(R.id.member_fragment_container, mFragment)
                        .commit();
            } else if (mRequestId == 2) {
                mFragment = MemberForgetPasswordFragment.newInstance();
                mFragmentManager.beginTransaction()
                        .add(R.id.member_fragment_container, mFragment)
                        .commit();
            } else if (mRequestId == 3) {
                mFragment = MemberStartLocalFragment.newInstance();
                mFragmentManager.beginTransaction()
                        .add(R.id.member_fragment_container, mFragment)
                        .commit();
            }
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case SIGN_IN_GOOGLE_REQUEST_CODE:
            case SIGN_IN_NAVER_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    int responseCode = data.getIntExtra(EXTRA_RESPONSE_USER_CODE, -1);
                    switch (responseCode) {
                        case CONFIRM_EMAIL_REPETITION_TRUE_CODE:
                            Toast.makeText(getApplicationContext(), "로그인 하였습니다.", Toast.LENGTH_SHORT).show();
                            finish();
                            if (mRequestId != IS_NOW_USED_USER_REQUEST_CODE) {
                                startActivity(HomeActivity.newIntent(getApplicationContext()));
                                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                            }
                            break;
                        case CONFIRM_EMAIL_REPETITION_FALSE_CODE:
                            User anonymousUser = (User) data.getSerializableExtra(EXTRA_RESPONSE_USER);
                            mFragmentManager.beginTransaction()
                                    .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left)
                                    .replace(R.id.member_fragment_container,
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
                            if (mRequestId == IS_NOW_USED_USER_REQUEST_CODE) {
                                mFragmentManager.beginTransaction()
                                        .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left)
                                        .replace(R.id.member_fragment_container,
                                                MemberSurveyInfoFragment.newInstance(IS_NOW_USED_USER_REQUEST_CODE))
                                        .commit();
                            } else {
                                mFragmentManager.beginTransaction()
                                        .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left)
                                        .replace(R.id.member_fragment_container, MemberSurveyInfoFragment.newInstance())
                                        .commit();
                            }
                            break;
                    }
                }
                break;
            case SIGN_IN_LOCAL_REQUEST_CODE:
                int responseCode = data.getIntExtra(EXTRA_RESPONSE_USER_CODE, -1);
                if (resultCode == RESULT_OK) {
                    Toast.makeText(getApplicationContext(), "로그인 하였습니다.", Toast.LENGTH_SHORT).show();
                    finish();
                    if (mRequestId != IS_NOW_USED_USER_REQUEST_CODE) {
                        startActivity(HomeActivity.newIntent(getApplicationContext()));
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    }
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

            case REVOKE_ACCESS_GOOGLE_REQUEST_CODE:
            case REVOKE_ACCESS_NAVER_REQUEST_CODE:

                break;

        }
    }

    public void replaceFragment(String email) {
        Fragment fragment = getSupportFragmentManager()
                .findFragmentById(R.id.member_fragment_container);
        if (fragment instanceof MemberStartFragment) {
            mFragmentManager.beginTransaction()
                    .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left)
                    .replace(R.id.member_fragment_container, MemberStartNameFragment.newInstance())
                    .commit();
        } else if (fragment instanceof MemberStartLocalFragment) {
            if (mRequestId == IS_NOW_USED_USER_REQUEST_CODE) {
                mFragmentManager.beginTransaction()
                        .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left)
                        .replace(R.id.member_fragment_container,
                                MemberSurveyInfoFragment.newInstance(IS_NOW_USED_USER_REQUEST_CODE))
                        .commit();
            } else {
                mFragmentManager.beginTransaction()
                        .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left)
                        .replace(R.id.member_fragment_container, MemberSurveyInfoFragment.newInstance())
                        .commit();
            }

        } else if (fragment instanceof MemberForgetPasswordFragment) {
            mFragmentManager.beginTransaction()
                    .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left)
                    .replace(R.id.member_fragment_container, MemberSendEmailFragment.newInstance(email))
                    .commit();
        } else if (fragment instanceof MemberSendEmailFragment) {
            mFragmentManager.beginTransaction()
                    .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left)
                    .replace(R.id.member_fragment_container, MemberChangePasswordFragment.newInstance(email))
                    .commit();
        }
    }

    @Override
    public void onMenuSelected(int layoutIndex) {
        Fragment fragment;
        switch (layoutIndex) {
            case 7000: /* browsing user  */
                startActivity(HomeActivity.newIntent(getApplicationContext()));
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                finish();
                break;
            case 7001: /* signUpForLocal  */
                mFragmentManager.beginTransaction()
                        .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left)
                        .replace(R.id.member_fragment_container, MemberStartLocalFragment.newInstance())
//                        .addToBackStack(null)
                        .commit();
                break;
            case 7002: /* signInLocal  */
                mFragmentManager.beginTransaction()
                        .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left)
                        .replace(R.id.member_fragment_container, MemberSignInLocalFragment.newInstance())
//                        .addToBackStack(null)
                        .commit();
                break;
            case 7003: /* cancel signUpForLocal  */
                fragment = getSupportFragmentManager().findFragmentById(R.id.member_fragment_container);
                if (fragment instanceof MemberStartLocalFragment) {
                    if (mRequestId == 3) {
                        finish();
                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                    } else {
                        mFragmentManager.beginTransaction()
                                .setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right)
                                .replace(R.id.member_fragment_container, MemberSignInLocalFragment.newInstance())
                                .commit();
                    }
                }
                break;
            case 7004: /* cancel signInLocal  */
                fragment = getSupportFragmentManager().findFragmentById(R.id.member_fragment_container);
                if (fragment instanceof MemberSignInLocalFragment) {
                    mFragmentManager.beginTransaction()
                            .setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right)
                            .replace(R.id.member_fragment_container, MemberStartFragment.newInstance())
                            .commit();
                }
                break;
            case 7005: /* find password local  */
                mFragmentManager.beginTransaction()
                        .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left)
                        .replace(R.id.member_fragment_container, MemberForgetPasswordFragment.newInstance())
//                        .addToBackStack(null)
                        .commit();
                break;
            case 7006: /* cancel forget password  */
                fragment = getSupportFragmentManager().findFragmentById(R.id.member_fragment_container);
                if (fragment instanceof MemberForgetPasswordFragment) {
                    mFragmentManager.beginTransaction()
                            .setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right)
                            .replace(R.id.member_fragment_container, MemberSignInLocalFragment.newInstance())
                            .commit();
                }
                break;
            case 7007: /* watch terms */
                mFragmentManager.beginTransaction()
                        .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left)
                        .add(R.id.member_fragment_container, MemberConfigTermsFragment.newInstance())
                        .addToBackStack("terms_fragment")
                        .commit();
                break;
            case 7010: /* signInGoogle  */
                startActivityForResult(UserActivity.newIntent(getApplicationContext(),
                        SIGN_IN_GOOGLE_REQUEST_CODE), SIGN_IN_GOOGLE_REQUEST_CODE);
                break;
            case 7020: /* signInNaver */
                startActivityForResult(UserActivity.newIntent(getApplicationContext(),
                        SIGN_IN_NAVER_REQUEST_CODE), SIGN_IN_NAVER_REQUEST_CODE);
                break;
        }
    }

    @Override
    public void onSurveyCompleted(int stageNumber, boolean isCompleted) {
        MemberSurveyInfoFragment surveyInfoFragment = (MemberSurveyInfoFragment) getSupportFragmentManager()
                .findFragmentById(R.id.member_fragment_container);
        surveyInfoFragment.updateConfirmButtonTextView(stageNumber, isCompleted);
    }

    @Override
    public void onSurveyValueSubmit(int stageNumber, int value) {
        MemberSurveyInfoFragment surveyInfoFragment = (MemberSurveyInfoFragment) getSupportFragmentManager()
                .findFragmentById(R.id.member_fragment_container);
        surveyInfoFragment.updateUserInfo(stageNumber, value);
    }

    @Override
    public void onBackPressed() {
//        MemberSurveyInfoFragment surveyInfoFragment = (MemberSurveyInfoFragment) getSupportFragmentManager()
//                .findFragmentById(R.id.member_start_fragment_container);
//        surveyInfoFragment.onBackPressed();
        Fragment fragment = getSupportFragmentManager()
                .findFragmentById(R.id.member_fragment_container);
        if (fragment instanceof MemberStartLocalFragment) {
//            mFragmentManager.beginTransaction()
//                    .setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right)
//                    .replace(R.id.member_fragment_container, MemberSignInLocalFragment.newInstance())
//                    .commit();
        } else if (fragment instanceof MemberConfigTermsFragment) {
            mFragmentManager.popBackStackImmediate();
        } else if (fragment instanceof MemberStartNameFragment) {
            mFragmentManager.beginTransaction()
                    .setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right)
                    .replace(R.id.member_fragment_container, MemberStartFragment.newInstance())
                    .commit();
//            if (mPlatformId == 1) {
//                startActivityForResult(UserActivity.newIntent(getApplicationContext(),
//                        REVOKE_ACCESS_GOOGLE_REQUEST_CODE), REVOKE_ACCESS_GOOGLE_REQUEST_CODE);
//            } else if (mPlatformId == 2) {
//                startActivityForResult(UserActivity.newIntent(getApplicationContext(),
//                        REVOKE_ACCESS_NAVER_REQUEST_CODE), REVOKE_ACCESS_NAVER_REQUEST_CODE);
//            }
            if (mPlatformId == GOOGLE_USER_PLATFORM_ID) {
                startActivity(UserActivity.newIntent(getApplicationContext(), REVOKE_ACCESS_GOOGLE_REQUEST_CODE));
            } else if (mPlatformId == NAVER_USER_PLATFORM_ID) {
                startActivity(UserActivity.newIntent(getApplicationContext(), REVOKE_ACCESS_NAVER_REQUEST_CODE));
            }

        } else if (fragment instanceof MemberSignInLocalFragment) {
            mFragmentManager.beginTransaction()
                    .setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right)
                    .replace(R.id.member_fragment_container, MemberStartFragment.newInstance())
                    .commit();
        } else if (fragment instanceof MemberForgetPasswordFragment) {
            if (mRequestId == -1) {
                mFragmentManager.beginTransaction()
                        .setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right)
                        .replace(R.id.member_fragment_container, MemberSignInLocalFragment.newInstance())
                        .commit();
            } else if (mRequestId == 2) {
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        } else if (fragment instanceof MemberSendEmailFragment) {
//            Toast.makeText(getApplicationContext(), "비밀번호 변경이 취소 되었습니다.", Toast.LENGTH_SHORT).show();
//            mFragmentManager.beginTransaction()
//                    .setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right)
//                    .replace(R.id.pure_fragment_container, MemberSignInLocalFragment.newInstance())
//                    .commit();
        } else if (fragment instanceof MemberChangePasswordFragment) {
            mFragmentManager.beginTransaction()
                    .setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right)
                    .replace(R.id.member_fragment_container, MemberSignInLocalFragment.newInstance())
                    .commit();
        } else if (fragment instanceof MemberAskInfoFragment) {
            startActivity(HomeActivity.newIntent(getApplicationContext()));
            finish();
        } else if (fragment instanceof MemberSurveyInfoFragment) {
//            startActivity(HomeActivity.newIntent(getApplicationContext()));
//            finish();
            super.onBackPressed();
        } else {
            super.onBackPressed();
//            mBackPressCloseHandler.onBackPressed();
        }
    }
}
