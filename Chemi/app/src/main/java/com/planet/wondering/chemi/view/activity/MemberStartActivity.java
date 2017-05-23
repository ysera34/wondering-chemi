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

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.nhn.android.naverlogin.OAuthLogin;
import com.planet.wondering.chemi.R;
import com.planet.wondering.chemi.model.User;
import com.planet.wondering.chemi.util.helper.BackPressCloseHandler;
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
import static com.planet.wondering.chemi.common.Common.SIGN_IN_GOOGLE_REQUEST_CODE;
import static com.planet.wondering.chemi.common.Common.SIGN_IN_NAVER_REQUEST_CODE;
import static com.planet.wondering.chemi.common.Common.SIGN_UP_FOR_PLATFORM_USER_REQUEST_CODE;
import static com.planet.wondering.chemi.common.Common.SIGN_UP_FOR_PLATFORM_USER_SUCCESS_CODE;

/**
 * Created by yoon on 2017. 2. 12..
 */

public class MemberStartActivity extends AppBaseActivity implements OnMenuSelectedListener,
        /*GoogleApiClient.OnConnectionFailedListener,*/ OnSurveyCompletedListener {

    private static final String TAG = MemberStartActivity.class.getSimpleName();
    public static final int RC_SIGN_IN = 9001;

    public static Intent newIntent(Context packageContext) {
        Intent intent = new Intent(packageContext, MemberStartActivity.class);
        return intent;
    }

    private BackPressCloseHandler mBackPressCloseHandler;

    private User mUser;

    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private GoogleApiClient mGoogleApiClient;

    private static OAuthLogin mNaverOAuthLogin;
    private static Context mContext;

    private FragmentManager mFragmentManager;
    private Fragment mFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.i(TAG, "shared preferences accessToken : "
                + UserSharedPreferences.getStoredToken(getApplicationContext()));
        if (UserSharedPreferences.getStoredToken(getApplicationContext()) != null) {
            Intent intent = new Intent(MemberStartActivity.this, SearchActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        }

        setContentView(R.layout.activity_member_start);

//        mBackPressCloseHandler = new BackPressCloseHandler(MemberStartActivity.this);
//
//        GoogleSignInOptions googleSignInOptions =
//                new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                        .requestIdToken(getString(R.string.google_default_web_client_id))
//                        .requestEmail()
//                        .build();
//
//        mGoogleApiClient = new GoogleApiClient.Builder(MemberStartActivity.this)
//                .enableAutoManage(MemberStartActivity.this, this)
//                .addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions)
//                .build();
//
//        mFirebaseAuth = FirebaseAuth.getInstance();
//
//        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
//            @Override
//            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
//                FirebaseUser user = firebaseAuth.getCurrentUser();
//                if (user != null) {
//                    Log.i(TAG, "onAuthStateChanged: signed_in:" + user.getUid());
//                } else {
//                    Log.i(TAG, "onAuthStateChanged: signed_out");
//                }
//                updateUI(user);
//            }
//        };

//        OAuthLoginDefine.DEVELOPER_VERSION = true;
//        mContext = this;
//        mNaverOAuthLogin = OAuthLogin.getInstance();
//        mNaverOAuthLogin.init(this,
//                getString(R.string.naver_oauth_client_id),
//                getString(R.string.naver_oauth_client_secret),
//                getString(R.string.app_name));
//
        mFragmentManager = getSupportFragmentManager();
        mFragment = mFragmentManager.findFragmentById(R.id.member_fragment_container);

        if (mFragment == null) {
            mFragment = MemberStartFragment.newInstance();
            mFragmentManager.beginTransaction()
                    .add(R.id.member_fragment_container, mFragment)
                    .commit();
        }
    }

//    @Override
//    public void onStart() {
//        super.onStart();
//        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
//    }

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
        } else {
//            Log.i(TAG, "uriData : did not get it");
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
        } else {
//            Log.e(TAG, "accesstoken : did not get it");
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

//    @Override
//    public void onStop() {
//        super.onStop();
//        if (mAuthStateListener != null) {
//            mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
//        }
//        hideProgressDialog();
//    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

//        if (requestCode == RC_SIGN_IN) {
//            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
//            if (result.isSuccess()) {
//                Log.i(TAG, "GoogleSignInResult" + " isSuccess");
//                GoogleSignInAccount account = result.getSignInAccount();
//
//                // TODO(user): send token to server and validate server-side
//                if (account != null) {
////                    requestConfirmEmailRepetition(account.getEmail(), account.getIdToken(), 1);
//                    requestConfirmEmailRepetition(account, null, null, 1);
////                    mGoogleSignInAccount = account;
//                }
////              requestSubmitUserInfo(account.getIdToken(), 1);
//            } else {
//                Log.i(TAG, "GoogleSignInResult" + " isFail");
//                updateUI(null);
//            }
//        }

        switch (requestCode) {
            case SIGN_IN_GOOGLE_REQUEST_CODE:
            case SIGN_IN_NAVER_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    int responseCode = data.getIntExtra(EXTRA_RESPONSE_USER_CODE, -1);
                    switch (responseCode) {
                        case CONFIRM_EMAIL_REPETITION_TRUE_CODE:
                            Toast.makeText(getApplicationContext(), "로그인 하였습니다.", Toast.LENGTH_SHORT).show();
                            startActivity(SearchActivity.newIntent(getApplicationContext()));
                            finish();
                            break;
                        case CONFIRM_EMAIL_REPETITION_FALSE_CODE:
                            User anonymousUser = (User) data.getSerializableExtra(EXTRA_RESPONSE_USER);
                            mFragmentManager.beginTransaction()
                                    .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left)
                                    .replace(R.id.member_fragment_container,
                                            MemberStartNameFragment.newInstance(anonymousUser))
                                    .commit();
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
                                    .replace(R.id.member_fragment_container, MemberSurveyInfoFragment.newInstance())
                                    .commit();
                            break;
                    }
                }
                break;
        }
    }

//    private void firebaseAuthGoogle(GoogleSignInAccount account) {
//        Log.d(TAG, "firebaseAuthWithGoogle: id : " + account.getId());
//        Log.d(TAG, "firebaseAuthWithGoogle: id token : " + account.getIdToken());
//        Log.d(TAG, "firebaseAuthWithGoogle: full name : " + account.getDisplayName());
//        Log.d(TAG, "firebaseAuthWithGoogle: email : " + account.getEmail());
//        Log.d(TAG, "firebaseAuthWithGoogle: server auth code : " + account.getServerAuthCode());
//        Log.d(TAG, "FirebaseInstanceId: token : " + FirebaseInstanceId.getInstance().getToken());
//        Log.d(TAG, "FirebaseInstanceId: token : " + FirebaseInstanceId.getInstance().getToken());
//        Log.d(TAG, "FirebaseInstanceId: token : " + FirebaseInstanceId.getInstance().getToken());
//
//        showProgressDialog();
//
//        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
//        mFirebaseAuth.signInWithCredential(credential)
//                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());
//
//                        if (!task.isSuccessful()) {
//                            Log.w(TAG, "signInWithCredential", task.getException());
//                            Toast.makeText(getApplicationContext(), "Authentication failed.",
//                                    Toast.LENGTH_SHORT).show();
//                        }
//                        hideProgressDialog();
//                    }
//                });
//    }
//
//    public void signInGoogle() {
//        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
//        startActivityForResult(signInIntent, RC_SIGN_IN);
//    }
//
//    public void signOutGoogle() {
////        FirebaseAuth.getInstance().signOut();
//        mFirebaseAuth.signOut();
//
//        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
//                new ResultCallback<Status>() {
//                    @Override
//                    public void onResult(@NonNull Status status) {
//                        updateUI(null);
//                    }
//                }
//        );
//    }
//
//    public void revokeAccessGoogle() {
//        mFirebaseAuth.signOut();
//
//        Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient).setResultCallback(
//                new ResultCallback<Status>() {
//                    @Override
//                    public void onResult(@NonNull Status status) {
//                        updateUI(null);
//                    }
//                }
//        );
//    }
//
//    private void updateUI(FirebaseUser user) {
//        hideProgressDialog();
//        if (user != null) {
//            Log.i(TAG, getString(R.string.google_status_fmt, user.getEmail()));
//            Log.i(TAG, getString(R.string.firebase_status_fmt, user.getUid()));
//        } else {
//            Log.i(TAG, "Google User, Firebase User is null");
//        }
//    }
//
//    @Override
//    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
//        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
//        // be available.
//        Log.d(TAG, "onConnectionFailed:" + connectionResult);
////        Toast.makeText(this, "Google Play Services error.", Toast.LENGTH_SHORT).show();
//    }
//
//    private void signInFirebase(String email, String password) {
//        Log.d(TAG, "signIn Firebase:" + email);
//
//        mFirebaseAuth.signInWithEmailAndPassword(email, password)
//                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        Log.d(TAG, "signInWIthEmail:onComplete: " + task.isSuccessful());
//
//                        if (!task.isSuccessful()) {
//                            Log.w(TAG, "signInWithEmail:failed", task.getException());
//                            Toast.makeText(MemberStartActivity.this, "signInWithEmail:failed", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });
//    }
//
//    public void signInNaver() {
//        mNaverOAuthLogin.startOauthLoginActivity(MemberStartActivity.this, mOAuthLoginHandler);
//    }
//
//    public void signOutNaver() {
//        mFirebaseAuth.signOut();
//
//        mNaverOAuthLogin.logout(mContext);
////        mNaverOAuthLogin.logoutAndDeleteToken(mContext);
//    }
//
//    public void revokeAccessNaver() {
//        mFirebaseAuth.signOut();
//
//        new DeleteTokenTask().execute();
//    }
//
//    private OAuthLoginHandler mOAuthLoginHandler = new OAuthLoginHandler() {
//        @Override
//        public void run(boolean success) {
//            if (success) {
//                String accessToken = mNaverOAuthLogin.getAccessToken(mContext);
//                String refreshToken = mNaverOAuthLogin.getRefreshToken(mContext);
//                long expiresAt = mNaverOAuthLogin.getExpiresAt(mContext);
//                String tokenType = mNaverOAuthLogin.getTokenType(mContext);
//                Log.i(TAG, "Naver accessToken: " + accessToken);
//                Log.i(TAG, "Naver refreshToken: " + refreshToken);
//                Log.i(TAG, "Naver expiresAt: " + String.valueOf(expiresAt));
//                Log.i(TAG, "Naver tokenType: " + tokenType);
//                Log.i(TAG, "Naver oauthState: " + mNaverOAuthLogin.getState(mContext).toString());
//
//                // TODO(user): send token to server and validate server-side
//                String email = null;
//                try {
//                    email = new NaverMemberProfileTask().execute(accessToken).get();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                requestConfirmEmailRepetition(null, email, accessToken, 2);
////                requestSubmitUserInfo(accessToken, 2);
//
//            } else {
//                String errorCode = mNaverOAuthLogin.getLastErrorCode(mContext).getCode();
//                String errorDesc = mNaverOAuthLogin.getLastErrorDesc(mContext);
//                Log.i(TAG, "naver OAuthLogin " + "errorCode:" + errorCode + ", errorDesc:" + errorDesc);
//            }
//        }
//    };

//    @VisibleForTesting
//    public ProgressDialog mProgressDialog;
//
//    public void showProgressDialog() {
//        if (mProgressDialog == null) {
//            mProgressDialog = new ProgressDialog(this);
////            mProgressDialog.setMessage(getString(R.string.loading));
//            mProgressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//            mProgressDialog.setIndeterminate(true);
//        }
//        mProgressDialog.show();
//    }
//
//    public void hideProgressDialog() {
//        if (mProgressDialog != null && mProgressDialog.isShowing()) {
//            mProgressDialog.dismiss();
//        }
//    }
//
//    private class DeleteTokenTask extends AsyncTask<Void, Void, Void> {
//        @Override
//        protected Void doInBackground(Void... params) {
//            boolean isSuccessDeleteToken = mNaverOAuthLogin.logoutAndDeleteToken(mContext);
//
//            if (!isSuccessDeleteToken) {
//                Log.d(TAG, "errorCode:" + mNaverOAuthLogin.getLastErrorCode(mContext));
//                Log.d(TAG, "errorDese:" + mNaverOAuthLogin.getLastErrorDesc(mContext));
//            }
//            return null;
//        }
//    }
//
//    private class NaverMemberProfileTask extends AsyncTask<String, Void, String> {
//
//        private String email = null;
//
//        @Override
//        protected String doInBackground(String... params) {
////            String token = "YOUR_ACCESS_TOKEN";// 네이버 로그인 접근 토큰;
//            String token = params[0];
//            String header = "Bearer " + token; // Bearer 다음에 공백 추가
//            try {
//                String apiURL = "https://openapi.naver.com/v1/nid/me";
//                URL url = new URL(apiURL);
//                HttpURLConnection con = (HttpURLConnection)url.openConnection();
//                con.setRequestMethod("GET");
//                con.setRequestProperty("Authorization", header);
//                int responseCode = con.getResponseCode();
//                BufferedReader br;
//                if(responseCode==200) { // 정상 호출
//                    br = new BufferedReader(new InputStreamReader(con.getInputStream()));
//                } else {  // 에러 발생
//                    br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
//                }
//                String inputLine;
//                StringBuilder stringBuilder = new StringBuilder();
//                while ((inputLine = br.readLine()) != null) {
//                    stringBuilder.append(inputLine);
//                }
//                br.close();
////                System.out.println(response.toString());
//                Log.i(TAG, "naver member profile : " + stringBuilder.toString());
//                JSONObject jsonObject = new JSONObject(stringBuilder.toString());
//                email = Parser.parseNaverUser(jsonObject);
//                Log.i(TAG, "naver member profile email : " + email);
//            } catch (Exception e) {
////                System.out.println(e);
//                Log.e(TAG, e.getMessage());
//            }
//            return email;
//        }
//    }

//    private GoogleSignInAccount mGoogleSignInAccount;
//    private String mNaverEmail = null;
//    private String mAccessToken = null;
//    private int mPlatformId = -1;

//    private void requestConfirmEmailRepetition(final GoogleSignInAccount googleSignInAccount,
//            final String emailAddress, final String accessToken, final int platformId) {
//
//        final CustomProgressDialog progressDialog = new CustomProgressDialog(MemberStartActivity.this);
//        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        progressDialog.show();
//
//        Map<String, String> params = new HashMap<>();
//        if (platformId == 1) {
//            params.put(ACCESS_TOKEN, googleSignInAccount.getIdToken());
//            params.put(EMAIL_STRING, googleSignInAccount.getEmail());
//            params.put(PLATFORM, String.valueOf(platformId));
//        } else if (platformId == 2) {
//            params.put(ACCESS_TOKEN, accessToken);
//            params.put(EMAIL_STRING, emailAddress);
//            params.put(PLATFORM, String.valueOf(platformId));
//        }
//
//        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
//                Request.Method.POST, URL_HOST + PATH + EMAIL_STRING_PATH, new JSONObject(params),
//                new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//
//                        progressDialog.dismiss();
//
//                        if (Parser.parseSimpleResult(response)) {
////                            requestSubmitUserInfo(accessToken, platformId);
//                            User user = Parser.parseEmailConfirm(response);
//
//
//                            if (user == null) { /*member sign up for*/
//                                mFragmentManager.beginTransaction()
//                                        .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left)
//                                        .replace(R.id.member_fragment_container, MemberStartNameFragment.newInstance())
//                                        .commit();
//
//                                if (platformId == 1) {
//                                    mGoogleSignInAccount = googleSignInAccount;
//                                } else if (platformId == 2) {
//                                    mNaverEmail = emailAddress;
//                                    mAccessToken = accessToken;
//                                }
//                                mPlatformId = platformId;
//
//                            } else {
//                                if (UserSharedPreferences.getStoredToken(getApplicationContext()) != null) {
//                                    UserSharedPreferences.removeStoredToken(getApplicationContext());
//                                }
//                                UserSharedPreferences.setStoreToken(getApplicationContext(), user.getToken());
//                                Log.d(TAG, "user token : " + UserSharedPreferences.getStoredToken(getApplicationContext()));
//
//                                if (UserSharedPreferences.getStoredUserName(getApplicationContext()) != null) {
//                                    UserSharedPreferences.removeStoredUserName(getApplicationContext());
//                                }
//                                UserSharedPreferences.setStoreUserName(getApplicationContext(), user.getName());
//                                Log.d(TAG, "user name : " + UserSharedPreferences.getStoredUserName(getApplicationContext()));
//
//                                if (platformId == 1) {
//                                    firebaseAuthGoogle(googleSignInAccount);
//                                } else if (platformId == 2) {
//                                    signInFirebase(emailAddress, emailAddress);
//                                }
//
//                                Toast.makeText(getApplicationContext(), "로그인 하였습니다.", Toast.LENGTH_SHORT).show();
//
//                                startActivity(SearchActivity.newIntent(getApplicationContext()));
//                                finish();
//                            }
//
//                        } else {
//                            if (platformId == 1) {
//                                Toast.makeText(getApplicationContext(),
//                                        "이미 가입된 계정 입니다. 다른 계정으로 시도해주세요.",
////                                        "동일한 이메일이 구글 인증 절차를 통하지 않고 가입 되었습니다. 다른 이메일로 가입해주세요.",
//                                        Toast.LENGTH_SHORT).show();
////                                signOutGoogle();
//                                revokeAccessGoogle();
//                            } else if (platformId == 2) {
//                                Toast.makeText(getApplicationContext(),
//                                        "이미 가입된 아이디 입니다. 다른 아이디로 시도해주세요.",
////                                        "동일한 이메일이 네이버 인증 절차를 통하지 않고 가입 되었습니다. 다른 이메일로 가입해주세요.",
//                                        Toast.LENGTH_SHORT).show();
////                                signOutNaver();
//                                revokeAccessNaver();
//                            }
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        progressDialog.dismiss();
//                        Log.e(TAG, String.valueOf(error.getMessage()));
////                        Toast.makeText(getApplicationContext(),
////                                "메일 중복 확인 중 오류가 발생하였습니다. 잠시 후 다시 요청해주세요", Toast.LENGTH_SHORT).show();
//                        Toast.makeText(getApplicationContext(), R.string.progress_dialog_message_error,
//                                Toast.LENGTH_SHORT).show();
//                        if (platformId == 1) {
//                            revokeAccessGoogle();
//                        } else if (platformId == 2) {
//                            revokeAccessNaver();
//                        }
//                    }
//                }
//        );
//
//        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(SOCKET_TIMEOUT_POST_REQ,
//                NUMBER_OF_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//
//        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest, TAG);
//    }

//    public void requestSubmitUserInfo(String name) {
//
////        if (mPlatformId == 1) {
////            Log.i(TAG, "requestSubmitUserInfo : mAccessToken :" + mGoogleSignInAccount.getIdToken());
////        } else if (mPlatformId == 2){
////            Log.i(TAG, "requestSubmitUserInfo : mAccessToken :" + mAccessToken);
////        }
////        Log.i(TAG, "requestSubmitUserInfo : name :" + name);
////        Log.i(TAG, "requestSubmitUserInfo : mPlatformId :" + String.valueOf(mPlatformId));
////        Log.i(TAG, "requestSubmitUserInfo : pushToken :" + FirebaseInstanceId.getInstance().getToken());
//
//        final CustomProgressDialog progressDialog = new CustomProgressDialog(MemberStartActivity.this);
//        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        progressDialog.show();
//
//        Map<String, String> params = new HashMap<>();
//        params.put(NAME, name);
//        if (mPlatformId == 1) {
//            params.put(ACCESS_TOKEN, mGoogleSignInAccount.getIdToken());
//        } else if (mPlatformId == 2) {
//            params.put(ACCESS_TOKEN, mAccessToken);
//        }
//        params.put(PLATFORM, String.valueOf(mPlatformId));
//        params.put(PUSH_TOKEN, FirebaseInstanceId.getInstance().getToken());
//
//        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
//                Request.Method.POST, URL_HOST + PATH, new JSONObject(params),
//                new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        progressDialog.dismiss();
////                        Toast.makeText(getApplicationContext(),
////                                "회원 가입이 완료되었습니다.", Toast.LENGTH_SHORT).show();
//                        Log.i(TAG, response.toString());
//                        mUser = Parser.parseSignUpForUser(response);
//
//                        /*firebase sign up for or sign in*/
//                        if (mPlatformId == 1) {
//                            firebaseAuthGoogle(mGoogleSignInAccount);
//                        } else if (mPlatformId == 2) {
//                            createFirebaseAccount(mNaverEmail, mNaverEmail);
//                        }
//
//                        if (UserSharedPreferences.getStoredToken(getApplicationContext()) != null) {
//                            UserSharedPreferences.removeStoredToken(getApplicationContext());
//                        }
//                        UserSharedPreferences.setStoreToken(getApplicationContext(), mUser.getToken());
//                        Log.d(TAG, "user token : " + UserSharedPreferences.getStoredToken(getApplicationContext()));
//
//                        if (UserSharedPreferences.getStoredUserName(getApplicationContext()) != null) {
//                            UserSharedPreferences.removeStoredUserName(getApplicationContext());
//                        }
//                        UserSharedPreferences.setStoreUserName(getApplicationContext(), mUser.getName());
//                        Log.d(TAG, "user name : " + UserSharedPreferences.getStoredUserName(getApplicationContext()));
//
//                        mFragmentManager.beginTransaction()
//                                .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left)
//                                .replace(R.id.member_fragment_container, MemberSurveyInfoFragment.newInstance())
//                                .commit();
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        progressDialog.dismiss();
//                        Log.e(TAG, String.valueOf(error.getMessage()));
////                        Toast.makeText(getApplicationContext(),
////                                "회원 가입 중 문제가 발생하였습니다.", Toast.LENGTH_SHORT).show();
//                        Toast.makeText(getApplicationContext(), R.string.progress_dialog_message_error,
//                                Toast.LENGTH_SHORT).show();
//                    }
//                }
//        );
//
//        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(SOCKET_TIMEOUT_POST_REQ,
//                NUMBER_OF_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//
//        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest, TAG);
//    }

//    private void createFirebaseAccount(String email, String password) {
//        Log.d(TAG, "createFirebaseAccount with Firebase: " + email);
//
//        mFirebaseAuth.createUserWithEmailAndPassword(email, password)
//                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        Log.d(TAG, "createUserWithEmail:onComplete: " + task.isSuccessful());
//
//                        if (!task.isSuccessful()) {
////                            Toast.makeText(MemberStartActivity.this,
////                                    "fail to create firebase user", Toast.LENGTH_SHORT).show();
//                            Log.i(TAG, "fail to create firebase user");
//                        }
//                    }
//                });
//    }

    public void replaceFragment(String email) {
        Fragment fragment = getSupportFragmentManager()
                .findFragmentById(R.id.member_fragment_container);
        if (fragment instanceof MemberStartFragment) {
            mFragmentManager.beginTransaction()
                    .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left)
                    .replace(R.id.member_fragment_container, MemberStartNameFragment.newInstance())
                    .commit();
        } else if (fragment instanceof MemberStartLocalFragment) {
            mFragmentManager.beginTransaction()
                    .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left)
                    .replace(R.id.member_fragment_container, MemberSurveyInfoFragment.newInstance())
                    .commit();
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
                startActivity(SearchActivity.newIntent(getApplicationContext()));
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
                    mFragmentManager.beginTransaction()
                            .setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right)
                            .replace(R.id.member_fragment_container, MemberStartFragment.newInstance())
                            .commit();
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
//                signInGoogle();
                startActivityForResult(UserActivity.newIntent(getApplicationContext(),
                        SIGN_IN_GOOGLE_REQUEST_CODE), SIGN_IN_GOOGLE_REQUEST_CODE);
                break;

            case 7020: /* signInNaver */
//                signInNaver();
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
            mFragmentManager.beginTransaction()
                    .setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right)
                    .replace(R.id.member_fragment_container, MemberStartFragment.newInstance())
                    .commit();
        } else if (fragment instanceof MemberConfigTermsFragment) {
            mFragmentManager.popBackStackImmediate();
        } else if (fragment instanceof MemberStartNameFragment) {
            mFragmentManager.beginTransaction()
                    .setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right)
                    .replace(R.id.member_fragment_container, MemberStartFragment.newInstance())
                    .commit();
//            if (mPlatformId == 1) {
//                revokeAccessGoogle();
//            } else if (mPlatformId == 2) {
//                revokeAccessNaver();
//            }

        } else if (fragment instanceof MemberSignInLocalFragment) {
            mFragmentManager.beginTransaction()
                    .setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right)
                    .replace(R.id.member_fragment_container, MemberStartFragment.newInstance())
                    .commit();
        } else if (fragment instanceof MemberForgetPasswordFragment) {
            mFragmentManager.beginTransaction()
                    .setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right)
                    .replace(R.id.member_fragment_container, MemberSignInLocalFragment.newInstance())
                    .commit();
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
            startActivity(SearchActivity.newIntent(getApplicationContext()));
            finish();
        } else if (fragment instanceof MemberSurveyInfoFragment) {
//            startActivity(SearchActivity.newIntent(getApplicationContext()));
//            finish();
            super.onBackPressed();
        } else {
            super.onBackPressed();
//            mBackPressCloseHandler.onBackPressed();
        }
    }
}
