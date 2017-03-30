package com.planet.wondering.chemi.view.activity;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
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
import com.planet.wondering.chemi.util.listener.OnUserInfoUpdateListener;
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

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.planet.wondering.chemi.network.Config.SOCKET_TIMEOUT_GET_REQ;
import static com.planet.wondering.chemi.network.Config.URL_HOST;
import static com.planet.wondering.chemi.network.Config.User.PATH;

/**
 * Created by yoon on 2016. 12. 31..
 */

public class MemberActivity extends BottomNavigationActivity
        implements OnMenuSelectedListener, OnDialogFinishedListener, OnUserInfoUpdateListener {

    private static final String TAG = MemberActivity.class.getSimpleName();
    private FragmentManager mFragmentManager;
    private Fragment mFragment;

    public static Intent newIntent(Context packageContext) {
        Intent intent = new Intent(packageContext, MemberActivity.class);
        return intent;
    }

    private String mAccessToken;
    private User mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAccessToken = UserSharedPreferences.getStoredToken(getApplicationContext());

        mFragmentManager = getSupportFragmentManager();
        mFragment = mFragmentManager.findFragmentById(R.id.fragment_container);

    }

    @Override
    protected void onResume() {
        super.onResume();
        setupBottomNavigation(4);

        if (mFragment == null) {
            if (mAccessToken != null) {
//                mFragment = MemberFragment.newInstance();
//                mFragmentManager.beginTransaction()
//                        .add(R.id.fragment_container, mFragment)
//                        .commit();
                requestMemberConfigUser();

            } else {
                mFragment = MemberConfigSignInFragment.newInstance();
                mFragmentManager.beginTransaction()
                        .add(R.id.fragment_container, mFragment)
                        .commit();
            }
        }
    }

    @Override
    public void onMenuSelected(int layoutIndex) {
        switch (layoutIndex) {
            case -1:
                mFragmentManager.beginTransaction()
                        .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left)
                        .replace(R.id.fragment_container, MemberConfigFragment.newInstance())
                        .commit();
                break;
            case 1:
                if (UserSharedPreferences.getStoredToken(getApplicationContext()) != null) {
                    mFragmentManager.beginTransaction()
                            .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left)
                            .replace(R.id.fragment_container, MemberConfigProfileFragment.newInstance(mUser))
                            .commit();
                } else {
                    startActivity(MemberStartActivity.newIntent(getApplicationContext()));
                }

                break;
            case 2:
                mFragmentManager.beginTransaction()
                        .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left)
                        .replace(R.id.fragment_container, MemberConfigNoticeFragment.newInstance())
                        .commit();
                break;
            case 3:
                mFragmentManager.beginTransaction()
                        .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left)
                        .replace(R.id.fragment_container, MemberConfigRequestFragment.newInstance())
                        .commit();
                break;
            case 4:
                mFragmentManager.beginTransaction()
                        .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left)
                        .replace(R.id.fragment_container, MemberConfigFAQFragment.newInstance())
                        .commit();
                break;
            case 5:
                mFragmentManager.beginTransaction()
                        .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left)
                        .replace(R.id.fragment_container, MemberConfigTermsFragment.newInstance())
                        .commit();
                break;
            case 6:
                mFragmentManager.beginTransaction()
                        .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left)
                        .replace(R.id.fragment_container, MemberConfigPartnerFragment.newInstance())
                        .commit();
                break;

            case 11:
                mFragmentManager.beginTransaction()
                        .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left)
                        .replace(R.id.fragment_container, MemberConfigChangeNameFragment.newInstance(mUser.getName()))
                        .commit();
                break;
            case 12:
                mFragmentManager.beginTransaction()
                        .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left)
                        .replace(R.id.fragment_container, MemberConfigChangePasswordFragment.newInstance())
                        .commit();
                break;
            case 13:
                mFragmentManager.beginTransaction()
                        .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left)
                        .replace(R.id.fragment_container, MemberAskInfoFragment.newInstance())
                        .commit();
                break;
        }
    }

    @Override
    public void onDialogFinished(boolean isChose) {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if (fragment instanceof MemberConfigProfileFragment) {
            ((MemberConfigProfileFragment) fragment).onDialogFinished(isChose);
        }
    }

    @Override
    public void onUserNameInfoUpdate(String userName) {
        mUser.setName(userName);
    }

    @Override
    public void onBackPressed() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
//        MemberConfigFragment memberConfigFragment = MemberConfigFragment.newInstance();
        if (fragment instanceof MemberConfigSignInFragment) {
            finish();
        } else if (fragment instanceof MemberConfigFragment) {
            if (UserSharedPreferences.getStoredToken(getApplicationContext()) != null) {
                mFragmentManager.beginTransaction()
                        .setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right)
                        .replace(R.id.fragment_container, MemberFragment.newInstance(mUser))
                        .commit();
            } else {
//                finish();
                mFragmentManager.beginTransaction()
                        .setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right)
                        .replace(R.id.fragment_container, MemberConfigSignInFragment.newInstance())
                        .commit();
            }
        } else if (fragment instanceof MemberConfigProfileFragment) {
            mFragmentManager.beginTransaction()
                    .setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right)
                    .replace(R.id.fragment_container, MemberConfigFragment.newInstance())
                    .commit();

        } else if (fragment instanceof MemberConfigNoticeFragment) {
            mFragmentManager.beginTransaction()
                    .setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right)
                    .replace(R.id.fragment_container, MemberConfigFragment.newInstance())
                    .commit();
        } else if (fragment instanceof MemberConfigRequestFragment) {
            mFragmentManager.beginTransaction()
                    .setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right)
                    .replace(R.id.fragment_container, MemberConfigFragment.newInstance())
                    .commit();
        } else if (fragment instanceof MemberConfigFAQFragment) {
            mFragmentManager.beginTransaction()
                    .setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right)
                    .replace(R.id.fragment_container, MemberConfigFragment.newInstance())
                    .commit();
        } else if (fragment instanceof MemberConfigTermsFragment) {
            mFragmentManager.beginTransaction()
                    .setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right)
                    .replace(R.id.fragment_container, MemberConfigFragment.newInstance())
                    .commit();
        } else if (fragment instanceof MemberConfigPartnerFragment) {
            mFragmentManager.beginTransaction()
                    .setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right)
                    .replace(R.id.fragment_container, MemberConfigFragment.newInstance())
                    .commit();
        } else if (fragment instanceof MemberConfigChangeNameFragment) {
            mFragmentManager.beginTransaction()
                    .setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right)
                    .replace(R.id.fragment_container, MemberConfigProfileFragment.newInstance(mUser))
                    .commit();
        } else if (fragment instanceof MemberConfigChangePasswordFragment) {
            mFragmentManager.beginTransaction()
                    .setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right)
                    .replace(R.id.fragment_container, MemberConfigProfileFragment.newInstance(mUser))
                    .commit();
        } else if (fragment instanceof MemberAskInfoFragment) {
            mFragmentManager.beginTransaction()
                    .setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right)
                    .replace(R.id.fragment_container, MemberConfigProfileFragment.newInstance(mUser))
                    .commit();
        } else {
            super.onBackPressed();
        }
    }

    private void requestMemberConfigUser() {

        final ProgressDialog progressDialog =
                ProgressDialog.show(MemberActivity.this,
                        "회원님의 정보를 가져오고 있습니다.",
                        getString(R.string.progress_dialog_message_wait), false, false);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET, URL_HOST + PATH,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i(TAG, response.toString());
                        mUser = Parser.parseMemberConfigUser(response);
                        progressDialog.dismiss();

                        Log.i(TAG, mUser.toString());

                        mFragment = MemberFragment.newInstance(mUser);
                        mFragmentManager.beginTransaction()
                                .add(R.id.fragment_container, mFragment)
                                .commit();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Log.e(TAG, error.toString());
                        if (error instanceof com.android.volley.TimeoutError) {
                            Toast.makeText(getApplicationContext(),
                                    "회원 정보 요청 중 문제가 발생하였습니다. 다시 요청 하겠습니다.", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(MemberActivity.this, MemberActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                            startActivity(intent);

                        } else if (error instanceof com.android.volley.NoConnectionError) {
                            Toast.makeText(getApplicationContext(),
                                    "네트워크에 문제가 발생하였습니다.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        )
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("token", mAccessToken);
                return params;
            }
        };

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(SOCKET_TIMEOUT_GET_REQ,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest, TAG);
    }
}
