package com.planet.wondering.chemi.view.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.iid.FirebaseInstanceId;
import com.nhn.android.naverlogin.OAuthLogin;
import com.nhn.android.naverlogin.OAuthLoginHandler;
import com.planet.wondering.chemi.R;
import com.planet.wondering.chemi.model.User;
import com.planet.wondering.chemi.network.AppSingleton;
import com.planet.wondering.chemi.network.Parser;
import com.planet.wondering.chemi.util.helper.UserSharedPreferences;
import com.planet.wondering.chemi.view.custom.CustomProgressDialog;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import static com.planet.wondering.chemi.common.Common.CONFIRM_EMAIL_ANOTHER_PLATFORM_USER_CODE;
import static com.planet.wondering.chemi.common.Common.CONFIRM_EMAIL_REPETITION_ERROR_CODE;
import static com.planet.wondering.chemi.common.Common.CONFIRM_EMAIL_REPETITION_FALSE_CODE;
import static com.planet.wondering.chemi.common.Common.CONFIRM_EMAIL_REPETITION_TRUE_CODE;
import static com.planet.wondering.chemi.common.Common.EXTRA_COMPLICATED_REVOKE;
import static com.planet.wondering.chemi.common.Common.EXTRA_REQUEST_USER;
import static com.planet.wondering.chemi.common.Common.EXTRA_REQUEST_USER_CODE;
import static com.planet.wondering.chemi.common.Common.EXTRA_RESPONSE_USER;
import static com.planet.wondering.chemi.common.Common.EXTRA_RESPONSE_USER_CODE;
import static com.planet.wondering.chemi.common.Common.GOOGLE_USER_PLATFORM_ID;
import static com.planet.wondering.chemi.common.Common.LOCAL_USER_PLATFORM_ID;
import static com.planet.wondering.chemi.common.Common.NAVER_USER_PLATFORM_ID;
import static com.planet.wondering.chemi.common.Common.REVOKE_ACCESS_GOOGLE_REQUEST_CODE;
import static com.planet.wondering.chemi.common.Common.REVOKE_ACCESS_GOOGLE_RESULT_CODE;
import static com.planet.wondering.chemi.common.Common.REVOKE_ACCESS_NAVER_REQUEST_CODE;
import static com.planet.wondering.chemi.common.Common.REVOKE_ACCESS_NAVER_RESULT_CODE;
import static com.planet.wondering.chemi.common.Common.SIGN_IN_GOOGLE_REQUEST_CODE;
import static com.planet.wondering.chemi.common.Common.SIGN_IN_LOCAL_REQUEST_CODE;
import static com.planet.wondering.chemi.common.Common.SIGN_IN_LOCAL_USER_ERROR_CODE;
import static com.planet.wondering.chemi.common.Common.SIGN_IN_LOCAL_USER_FAIL_CODE;
import static com.planet.wondering.chemi.common.Common.SIGN_IN_LOCAL_USER_SUCCESS_CODE;
import static com.planet.wondering.chemi.common.Common.SIGN_IN_NAVER_REQUEST_CODE;
import static com.planet.wondering.chemi.common.Common.SIGN_OUT_GOOGLE_REQUEST_CODE;
import static com.planet.wondering.chemi.common.Common.SIGN_OUT_GOOGLE_RESPONSE_CODE;
import static com.planet.wondering.chemi.common.Common.SIGN_OUT_LOCAL_REQUEST_CODE;
import static com.planet.wondering.chemi.common.Common.SIGN_OUT_LOCAL_RESPONSE_CODE;
import static com.planet.wondering.chemi.common.Common.SIGN_OUT_NAVER_REQUEST_CODE;
import static com.planet.wondering.chemi.common.Common.SIGN_OUT_NAVER_RESPONSE_CODE;
import static com.planet.wondering.chemi.common.Common.SIGN_UP_FOR_PLATFORM_USER_ERROR_CODE;
import static com.planet.wondering.chemi.common.Common.SIGN_UP_FOR_PLATFORM_USER_REQUEST_CODE;
import static com.planet.wondering.chemi.common.Common.SIGN_UP_FOR_PLATFORM_USER_SUCCESS_CODE;
import static com.planet.wondering.chemi.common.Common.WITHDRAW_GOOGLE_USER_REQUEST_CODE;
import static com.planet.wondering.chemi.common.Common.WITHDRAW_GOOGLE_USER_RESULT_CODE;
import static com.planet.wondering.chemi.common.Common.WITHDRAW_LOCAL_USER_REQUEST_CODE;
import static com.planet.wondering.chemi.common.Common.WITHDRAW_LOCAL_USER_RESULT_CODE;
import static com.planet.wondering.chemi.common.Common.WITHDRAW_NAVER_USER_REQUEST_CODE;
import static com.planet.wondering.chemi.common.Common.WITHDRAW_NAVER_USER_RESULT_CODE;
import static com.planet.wondering.chemi.network.Config.NUMBER_OF_RETRIES;
import static com.planet.wondering.chemi.network.Config.SOCKET_TIMEOUT_POST_REQ;
import static com.planet.wondering.chemi.network.NetworkConfig.URL_HOST;
import static com.planet.wondering.chemi.network.Config.User.EMAIL_STRING_PATH;
import static com.planet.wondering.chemi.network.Config.User.Key.ACCESS_TOKEN;
import static com.planet.wondering.chemi.network.Config.User.Key.EMAIL;
import static com.planet.wondering.chemi.network.Config.User.Key.EMAIL_STRING;
import static com.planet.wondering.chemi.network.Config.User.Key.NAME;
import static com.planet.wondering.chemi.network.Config.User.Key.PASSWORD;
import static com.planet.wondering.chemi.network.Config.User.Key.PLATFORM;
import static com.planet.wondering.chemi.network.Config.User.Key.PUSH_TOKEN;
import static com.planet.wondering.chemi.network.Config.User.Key.TOKEN;
import static com.planet.wondering.chemi.network.Config.User.LOGIN_PARAMS;
import static com.planet.wondering.chemi.network.Config.User.PATH;

/**
 * Created by yoon on 2017. 5. 23..
 */

public class UserActivity extends AppCompatActivity
        implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{

    private static final String TAG = UserActivity.class.getSimpleName();

    public static Intent newIntent(Context packageContext, int requestCode) {
        Intent intent = new Intent(packageContext, UserActivity.class);
        intent.putExtra(EXTRA_REQUEST_USER_CODE, requestCode);
        return intent;
    }

    public static Intent newIntent(Context packageContext, int requestCode, User anonymousUser) {
        Intent intent = new Intent(packageContext, UserActivity.class);
        intent.putExtra(EXTRA_REQUEST_USER_CODE, requestCode);
        intent.putExtra(EXTRA_REQUEST_USER, anonymousUser);
        return intent;
    }

    public static Intent newIntent(Context packageContext, int requestCode, boolean isUnComplicatedRevoke) {
        Intent intent = new Intent(packageContext, UserActivity.class);
        intent.putExtra(EXTRA_REQUEST_USER_CODE, requestCode);
        intent.putExtra(EXTRA_COMPLICATED_REVOKE, isUnComplicatedRevoke);
        return intent;
    }

    private GoogleApiClient mGoogleApiClient;
    private static final int RC_SIGN_IN = 9001;

    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    private OAuthLogin mNaverOAuthLogin;
    private Context mContext;
    private CustomProgressDialog mProgressDialog;

    private int mRequestUserCode;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_transparent);

        GoogleSignInOptions googleSignInOptions =
                new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken(getString(R.string.google_default_web_client_id))
                        .requestEmail()
                        .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions)
                .addConnectionCallbacks(this)
                .build();

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

        mContext = UserActivity.this;
        mNaverOAuthLogin = OAuthLogin.getInstance();
        mNaverOAuthLogin.init(this,
                getString(R.string.naver_oauth_client_id),
                getString(R.string.naver_oauth_client_secret),
                getString(R.string.app_name));

        mRequestUserCode = getIntent().getIntExtra(EXTRA_REQUEST_USER_CODE, -1);
        executeRequestCode(mRequestUserCode);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }

    private void executeRequestCode(int requestUserCode) {
        switch (requestUserCode) {
            case SIGN_IN_GOOGLE_REQUEST_CODE:
                signInGoogle();
                break;

            case SIGN_IN_NAVER_REQUEST_CODE:
                signInNaver();
                break;
            case SIGN_OUT_NAVER_REQUEST_CODE:
                signOutNaver();
                break;
            case REVOKE_ACCESS_NAVER_REQUEST_CODE:
            case WITHDRAW_NAVER_USER_REQUEST_CODE:
                revokeAccessNaver();
                break;
            case SIGN_UP_FOR_PLATFORM_USER_REQUEST_CODE:
                User anonymousUser = (User) getIntent().getSerializableExtra(EXTRA_REQUEST_USER);
                requestSignUpForPlatformUser(anonymousUser);
                break;

            case SIGN_IN_LOCAL_REQUEST_CODE:
                signInLocal();
                break;
            case SIGN_OUT_LOCAL_REQUEST_CODE:
                signOutLocal();
                break;
            case WITHDRAW_LOCAL_USER_REQUEST_CODE:
                requestWithdrawUser(LOCAL_USER_PLATFORM_ID);
                break;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthStateListener != null) {
            mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
        }
        hideProgressDialog();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                Log.d(TAG, "Google Sign in was successful, authenticate with Firebase");
                GoogleSignInAccount account = result.getSignInAccount();
                if (account != null) {
                    requestConfirmEmailRepetition(
                            account.getIdToken(), account.getEmail(), GOOGLE_USER_PLATFORM_ID);
                }
            } else {
                Log.w(TAG, "Google Sign in failed");
                finish();
            }
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.d(TAG, "GoogleApiClient:onConnected");
        switch (mRequestUserCode) {
            case SIGN_OUT_GOOGLE_REQUEST_CODE:
                signOutGoogle();
                break;
            case REVOKE_ACCESS_GOOGLE_REQUEST_CODE:
            case WITHDRAW_GOOGLE_USER_REQUEST_CODE:
                revokeAccessGoogle();
                break;
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.w(TAG, "GoogleApiClient:onConnectionSuspended");
    }

    public void signInGoogle() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    public void signOutGoogle() {
        mFirebaseAuth.signOut();

        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {
                        Log.d(TAG, "sign out GoogleApiClient.");
                        finishUserActivity(null, SIGN_OUT_GOOGLE_RESPONSE_CODE);
                    }
                });
    }

    public void revokeAccessGoogle() {
        mFirebaseAuth.signOut();

        Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {
                        Log.d(TAG, "revoke access GoogleApiClient.");
                        if (getIntent().getBooleanExtra(EXTRA_COMPLICATED_REVOKE, false)) {
                            requestWithdrawUser(GOOGLE_USER_PLATFORM_ID);
                        } else {
                            finishUserActivity(null, REVOKE_ACCESS_GOOGLE_RESULT_CODE);
                        }
                    }
                });
    }

    private void firebaseAuthWithGoogle(String accessToken, String email) {
        Log.d(TAG, "firebaseAuthWithGoogle: " + email);
        showProgressDialog();

        AuthCredential credential = GoogleAuthProvider.getCredential(accessToken, null);
        mFirebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithCredential:onComplete: " + task.isSuccessful());
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithCredential: ", task.getException());
                        }
                        hideProgressDialog();
                    }
                });
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed: " + connectionResult);
    }

    public void signInNaver() {
        mNaverOAuthLogin.startOauthLoginActivity(this, mOAuthLoginHandler);
    }

    public void signOutNaver() {
        mFirebaseAuth.signOut();
        mNaverOAuthLogin.logout(mContext);
        finishUserActivity(null, SIGN_OUT_NAVER_RESPONSE_CODE);
    }

    public void revokeAccessNaver() {
        mFirebaseAuth.signOut();
        new NaverMemberTokenDeleteTask().execute();
        if (getIntent().getBooleanExtra(EXTRA_COMPLICATED_REVOKE, false)) {
            requestWithdrawUser(NAVER_USER_PLATFORM_ID);
        } else {
            finishUserActivity(null, REVOKE_ACCESS_NAVER_RESULT_CODE);
        }
    }

    private void signInLocal() {
        User anonymousUser = (User) getIntent().getSerializableExtra(EXTRA_REQUEST_USER);
        requestSignInLocal(anonymousUser.getEmail(), anonymousUser.getPassword());
    }

    private void signOutLocal() {
        mFirebaseAuth.signOut();
        finishUserActivity(null, SIGN_OUT_LOCAL_RESPONSE_CODE);
    }

    private void createFirebaseAccount(String email, String password) {
        Log.d(TAG, "createFirebaseAccount with email: " + email);
        showProgressDialog();

        mFirebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "createUserWithEmail:onComplete: " + task.isSuccessful());
                        if (!task.isSuccessful()) {
                            Log.i(TAG, "createUserWithEmail:failed", task.getException());
                        }
                        hideProgressDialog();
                    }
                });
    }

    private void signInFirebaseAccount(String email, String password) {
        Log.d(TAG, "signInFirebaseAccout with email: " + email);
        showProgressDialog();

        mFirebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInFirebaseWithEmail:onComplete: " + task.isSuccessful());
                        if (!task.isSuccessful()) {
                            Log.i(TAG, "signInFirebaseWithEmail:failed", task.getException());
                        }
                        hideProgressDialog();
                    }
                });
    }

    private OAuthLoginHandler mOAuthLoginHandler = new OAuthLoginHandler() {
        @Override
        public void run(boolean success) {
            if (success) {
                String accessToken = mNaverOAuthLogin.getAccessToken(mContext);
                String refreshToken = mNaverOAuthLogin.getRefreshToken(mContext);
                String tokenType = mNaverOAuthLogin.getTokenType(mContext);
                String oauthState = mNaverOAuthLogin.getState(mContext).toString();
                Log.d(TAG, "Naver accessToken: " + accessToken);
                Log.d(TAG, "Naver refreshToken: " + refreshToken);
                Log.d(TAG, "Naver tokenType: " + tokenType);
                Log.d(TAG, "Naver oauthState: " + oauthState);

                String email = null;
                try {
                    email = new NaverMemberProfileTask().execute(accessToken).get();
                    requestConfirmEmailRepetition(accessToken, email, NAVER_USER_PLATFORM_ID);
                } catch (Exception e) {
                    Log.w(TAG, "NaverMemberProfileTask: " + e.getMessage());
                }


            } else {
                String errorCode = mNaverOAuthLogin.getLastErrorCode(mContext).toString();
                String errorDesc = mNaverOAuthLogin.getLastErrorDesc(mContext);
                Log.w(TAG, "Naver OAuthLogin: " + "errorCode: " + errorCode + ", errorDesc: " + errorDesc);
                if (errorCode.equals("user_cancel")) {
                    finish();
                }
            }
        }
    };

    private class NaverMemberProfileTask extends AsyncTask<String, Void, String> {

        private String email = null;
        @Override
        protected String doInBackground(String... params) {

            String token = params[0]; // YOUR_ACCESS_TOKEN
            String header = "Bearer " + token;
            try {
                String apiURL = "https://openapi.naver.com/v1/nid/me";
                URL url = new URL(apiURL);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("Authorization", header);
                int requestCode = connection.getResponseCode();
                BufferedReader br;
                if (requestCode == HttpURLConnection.HTTP_OK) {
                    br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                } else {
                    br = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
                }
                String inputLine;
                StringBuilder stringBuilder = new StringBuilder();
                while ((inputLine = br.readLine()) != null) {
                    stringBuilder.append(inputLine);
                }
                br.close();
                Log.i(TAG, "Naver member profile: " + stringBuilder.toString());
                JSONObject jsonObject = new JSONObject(stringBuilder.toString());
                email = Parser.parseNaverUser(jsonObject);
                Log.i(TAG, "Naver member profile email: " + email);
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }
            return email;
        }
    }

    private class NaverMemberTokenDeleteTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            boolean isSuccessDeleteToken = mNaverOAuthLogin.logoutAndDeleteToken(mContext);

            if (!isSuccessDeleteToken) {
                String errorCode = mNaverOAuthLogin.getLastErrorCode(mContext).toString();
                String errorDesc = mNaverOAuthLogin.getLastErrorDesc(mContext);
                Log.w(TAG, "Naver OAuthTokenDelete: " + "errorCode: " + errorCode + ", errorDesc: " + errorDesc);
            }
            return null;
        }
    }

    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new CustomProgressDialog(this);
            mProgressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
    }

    private void requestConfirmEmailRepetition(final String accessToken, final String email, final int platformId) {

        Log.d(TAG, "requestConfirmEmailRepetition:Email: " + email);
        showProgressDialog();

        Map<String, String> params = new HashMap<>();
        params.put(ACCESS_TOKEN, accessToken);
        params.put(EMAIL_STRING, email);
        params.put(PLATFORM, String.valueOf(platformId));

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST, URL_HOST + PATH + EMAIL_STRING_PATH, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        hideProgressDialog();
                        if (Parser.parseSimpleResult(response)) {

                            User user = Parser.parseEmailConfirm(response);

                            if (user == null) {
                                /* sign up for */
                                Log.i(TAG, "confirmEmailRepetition:onSuccess: " + email);
                                User anonymousUser = new User();
                                anonymousUser.setIdToken(accessToken);
                                anonymousUser.setEmail(email);
                                anonymousUser.setPlatformId((byte) platformId);
                                finishUserActivity(anonymousUser, CONFIRM_EMAIL_REPETITION_FALSE_CODE);
                            } else {
                                /* sign in */
                                setUserSharedPreferences(user);

                                if (platformId == GOOGLE_USER_PLATFORM_ID) {
                                    firebaseAuthWithGoogle(accessToken, email);
                                } else if (platformId == NAVER_USER_PLATFORM_ID) {
                                    signInFirebaseAccount(email, email);
                                }

                                finishUserActivity(user, CONFIRM_EMAIL_REPETITION_TRUE_CODE);
                            }
                        } else {
                            revokeAccessPlatform(platformId, true);
                            finishUserActivity(null, CONFIRM_EMAIL_ANOTHER_PLATFORM_USER_CODE);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        hideProgressDialog();
                        Log.e(TAG, String.valueOf(error.getMessage()));
                        Toast.makeText(getApplicationContext(), R.string.progress_dialog_message_error,
                                Toast.LENGTH_SHORT).show();
                        revokeAccessPlatform(platformId, false);
                        finishUserActivity(null, CONFIRM_EMAIL_REPETITION_ERROR_CODE);
                    }
                }
        );

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(SOCKET_TIMEOUT_POST_REQ,
                NUMBER_OF_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest, TAG);
    }

    private void revokeAccessPlatform(int platformId, boolean isRepetition) {
        switch (platformId) {
            case GOOGLE_USER_PLATFORM_ID:
                if (isRepetition) {
                    Toast.makeText(getApplicationContext(),
                            "이미 가입된 계정 입니다. 다른 계정으로 시도해주세요.",
//                            "동일한 이메일이 구글 인증 절차를 통하지 않고 가입 되었습니다. 다른 이메일로 가입해주세요.",
                        Toast.LENGTH_SHORT).show();
                }
                revokeAccessGoogle();
                break;
            case NAVER_USER_PLATFORM_ID:
                if (isRepetition) {
                    Toast.makeText(getApplicationContext(),
                            "이미 가입된 아이디 입니다. 다른 아이디로 시도해주세요.",
//                            "동일한 이메일이 네이버 인증 절차를 통하지 않고 가입 되었습니다. 다른 이메일로 가입해주세요.",
                            Toast.LENGTH_SHORT).show();
                }
                revokeAccessNaver();
                break;
        }
    }

    private void setUserSharedPreferences(User user) {
        if (UserSharedPreferences.getStoredToken(getApplicationContext()) != null) {
            UserSharedPreferences.removeStoredToken(getApplicationContext());
        }
        UserSharedPreferences.setStoreToken(getApplicationContext(), user.getToken());
        Log.d(TAG, "user token : " + UserSharedPreferences.getStoredToken(getApplicationContext()));

        if (UserSharedPreferences.getStoredUserName(getApplicationContext()) != null) {
            UserSharedPreferences.removeStoredUserName(getApplicationContext());
        }
        UserSharedPreferences.setStoreUserName(getApplicationContext(), user.getName());
        Log.d(TAG, "user name : " + UserSharedPreferences.getStoredUserName(getApplicationContext()));
    }

    private void removeUserSharedPreferences() {
        if (UserSharedPreferences.getStoredToken(getApplicationContext()) != null) {
            UserSharedPreferences.removeStoredToken(getApplicationContext());
        }
        Log.d(TAG, "user token : " + UserSharedPreferences.getStoredToken(getApplicationContext()));

        if (UserSharedPreferences.getStoredUserName(getApplicationContext()) != null) {
            UserSharedPreferences.removeStoredUserName(getApplicationContext());
        }
        Log.d(TAG, "user name : " + UserSharedPreferences.getStoredUserName(getApplicationContext()));
    }

    private void requestSignUpForPlatformUser(final User anonymousUser) {

        Log.d(TAG, "requestSignUpForPlatformUser:Name: " + anonymousUser.getName());
        showProgressDialog();

        Map<String, String> params = new HashMap<>();
        params.put(NAME, anonymousUser.getName());
        params.put(ACCESS_TOKEN, anonymousUser.getIdToken());
        params.put(PLATFORM, String.valueOf(anonymousUser.getPlatformId()));
        params.put(PUSH_TOKEN, FirebaseInstanceId.getInstance().getToken());

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST, URL_HOST + PATH, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        hideProgressDialog();
                        User user = Parser.parseSignUpForUser(response);
                        setUserSharedPreferences(user);
                        switch (anonymousUser.getPlatformId()) {
                            case GOOGLE_USER_PLATFORM_ID:
                                firebaseAuthWithGoogle(anonymousUser.getIdToken(), anonymousUser.getEmail());
                                break;
                            case NAVER_USER_PLATFORM_ID:
                                createFirebaseAccount(anonymousUser.getEmail(), anonymousUser.getEmail());
                                break;
                        }
                        finishUserActivity(user, SIGN_UP_FOR_PLATFORM_USER_SUCCESS_CODE);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        hideProgressDialog();
                        Log.e(TAG, String.valueOf(error.getMessage()));
                        Toast.makeText(getApplicationContext(), R.string.progress_dialog_message_error,
                                Toast.LENGTH_SHORT).show();
                        finishUserActivity(null, SIGN_UP_FOR_PLATFORM_USER_ERROR_CODE);
                    }
                }
        );

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(SOCKET_TIMEOUT_POST_REQ,
                NUMBER_OF_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest, TAG);

    }

    private void requestSignInLocal(final String email, final String password) {

        Log.d(TAG, "requestSignInLocal:Email: " + email);
        showProgressDialog();

        Map<String, String> params = new HashMap<>();
        params.put(EMAIL, email);
        params.put(PASSWORD, password);
        params.put(PLATFORM, String.valueOf(LOCAL_USER_PLATFORM_ID));

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST, URL_HOST + PATH + LOGIN_PARAMS, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (Parser.parseSimpleResult(response)) {
                            hideProgressDialog();
                            User user = Parser.parseSignInUserToken(response);

                            setUserSharedPreferences(user);
                            // sign in firebase user
                            signInFirebaseAccount(email, email);
                            finishUserActivity(null, SIGN_IN_LOCAL_USER_SUCCESS_CODE);
                        } else {
                            finishUserActivity(null, SIGN_IN_LOCAL_USER_FAIL_CODE);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        hideProgressDialog();
                        Log.e(TAG, String.valueOf(error.toString()));
                        Toast.makeText(getApplicationContext(), R.string.progress_dialog_message_error,
                                Toast.LENGTH_SHORT).show();
                        finishUserActivity(null, SIGN_IN_LOCAL_USER_ERROR_CODE);
                    }
                }
        );

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(SOCKET_TIMEOUT_POST_REQ,
                NUMBER_OF_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest, TAG);
    }

    private void requestWithdrawUser(final int platformId) {

        Log.d(TAG, "requestWithdrawUser:platformId: " + platformId);
        showProgressDialog();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.DELETE, URL_HOST + PATH,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        hideProgressDialog();
                        if (Parser.parseSimpleResult(response)) {
                            removeUserSharedPreferences();
                            switch (platformId) {
                                case  LOCAL_USER_PLATFORM_ID:
                                    finishUserActivity(null, WITHDRAW_LOCAL_USER_RESULT_CODE);
                                    break;
                                case GOOGLE_USER_PLATFORM_ID:
                                    finishUserActivity(null, WITHDRAW_GOOGLE_USER_RESULT_CODE);
                                    break;
                                case NAVER_USER_PLATFORM_ID:
                                    finishUserActivity(null, WITHDRAW_NAVER_USER_RESULT_CODE);
                                    break;
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        hideProgressDialog();
                        Log.e(TAG, String.valueOf(error.getMessage()));
                        Toast.makeText(getApplicationContext(), R.string.progress_dialog_message_error,
                                Toast.LENGTH_SHORT).show();
                    }
                }
        )
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put(TOKEN, UserSharedPreferences.getStoredToken(getApplicationContext()));
                return params;
            }
        };

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(SOCKET_TIMEOUT_POST_REQ,
                NUMBER_OF_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest, TAG);
    }

    private void finishUserActivity(User user, int resultCode) {

        Intent intent = new Intent();

        switch (resultCode) {
            case CONFIRM_EMAIL_REPETITION_TRUE_CODE:
                intent.putExtra(EXTRA_RESPONSE_USER_CODE, CONFIRM_EMAIL_REPETITION_TRUE_CODE);
                setResult(RESULT_OK, intent);
                finish();
                break;
            case CONFIRM_EMAIL_REPETITION_FALSE_CODE:
                intent.putExtra(EXTRA_RESPONSE_USER_CODE, CONFIRM_EMAIL_REPETITION_FALSE_CODE);
                intent.putExtra(EXTRA_RESPONSE_USER, user);
                setResult(RESULT_OK, intent);
                finish();
                break;
            case CONFIRM_EMAIL_ANOTHER_PLATFORM_USER_CODE:
                intent.putExtra(EXTRA_RESPONSE_USER_CODE, CONFIRM_EMAIL_ANOTHER_PLATFORM_USER_CODE);
                setResult(RESULT_OK, intent);
                finish();
                break;
            case CONFIRM_EMAIL_REPETITION_ERROR_CODE:
                intent.putExtra(EXTRA_RESPONSE_USER_CODE, CONFIRM_EMAIL_REPETITION_ERROR_CODE);
                setResult(RESULT_CANCELED, intent);
                finish();
                break;

            case SIGN_UP_FOR_PLATFORM_USER_SUCCESS_CODE:
                intent.putExtra(EXTRA_RESPONSE_USER_CODE, SIGN_UP_FOR_PLATFORM_USER_SUCCESS_CODE);
                setResult(RESULT_OK, intent);
                finish();
                break;
            case SIGN_UP_FOR_PLATFORM_USER_ERROR_CODE:
                intent.putExtra(EXTRA_RESPONSE_USER_CODE, SIGN_UP_FOR_PLATFORM_USER_ERROR_CODE);
                setResult(RESULT_CANCELED, intent);
                finish();
                break;

            case SIGN_OUT_GOOGLE_RESPONSE_CODE:
                intent.putExtra(EXTRA_RESPONSE_USER_CODE, SIGN_OUT_GOOGLE_RESPONSE_CODE);
                setResult(RESULT_OK, intent);
                finish();
                break;
            case SIGN_OUT_NAVER_RESPONSE_CODE:
                intent.putExtra(EXTRA_RESPONSE_USER_CODE, SIGN_OUT_NAVER_RESPONSE_CODE);
                setResult(RESULT_OK, intent);
                finish();
                break;
            case SIGN_OUT_LOCAL_RESPONSE_CODE:
                intent.putExtra(EXTRA_RESPONSE_USER_CODE, SIGN_OUT_LOCAL_RESPONSE_CODE);
                setResult(RESULT_OK, intent);
                finish();
                break;

            case REVOKE_ACCESS_GOOGLE_RESULT_CODE:
                intent.putExtra(EXTRA_RESPONSE_USER_CODE, REVOKE_ACCESS_GOOGLE_RESULT_CODE);
                setResult(RESULT_OK, intent);
                finish();
                break;
            case REVOKE_ACCESS_NAVER_RESULT_CODE:
                intent.putExtra(EXTRA_RESPONSE_USER_CODE, REVOKE_ACCESS_NAVER_RESULT_CODE);
                setResult(RESULT_OK, intent);
                finish();
                break;

            case WITHDRAW_GOOGLE_USER_RESULT_CODE:
                intent.putExtra(EXTRA_RESPONSE_USER_CODE, WITHDRAW_GOOGLE_USER_RESULT_CODE);
                setResult(RESULT_OK, intent);
                finish();
                break;
            case WITHDRAW_NAVER_USER_RESULT_CODE:
                intent.putExtra(EXTRA_RESPONSE_USER_CODE, WITHDRAW_NAVER_USER_RESULT_CODE);
                setResult(RESULT_OK, intent);
                finish();
                break;
            case WITHDRAW_LOCAL_USER_RESULT_CODE:
                intent.putExtra(EXTRA_RESPONSE_USER_CODE, WITHDRAW_LOCAL_USER_RESULT_CODE);
                setResult(RESULT_OK, intent);
                finish();
                break;

            case SIGN_IN_LOCAL_USER_SUCCESS_CODE:
                intent.putExtra(EXTRA_RESPONSE_USER_CODE, SIGN_IN_LOCAL_USER_SUCCESS_CODE);
                setResult(RESULT_OK, intent);
                finish();
                break;
            case SIGN_IN_LOCAL_USER_FAIL_CODE:
                intent.putExtra(EXTRA_RESPONSE_USER_CODE, SIGN_IN_LOCAL_USER_FAIL_CODE);
                setResult(RESULT_CANCELED, intent);
                finish();
                break;
            case SIGN_IN_LOCAL_USER_ERROR_CODE:
                intent.putExtra(EXTRA_RESPONSE_USER_CODE, SIGN_IN_LOCAL_USER_ERROR_CODE);
                setResult(RESULT_CANCELED, intent);
                finish();
                break;
        }
    }
}
