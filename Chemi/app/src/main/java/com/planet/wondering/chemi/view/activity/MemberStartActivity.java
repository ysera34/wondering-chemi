package com.planet.wondering.chemi.view.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

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
import com.nhn.android.naverlogin.OAuthLoginDefine;
import com.nhn.android.naverlogin.OAuthLoginHandler;
import com.planet.wondering.chemi.R;
import com.planet.wondering.chemi.view.fragment.MemberStartFragment;
import com.planet.wondering.chemi.view.fragment.MemberStartLocalFragment;

/**
 * Created by yoon on 2017. 2. 12..
 */

public class MemberStartActivity extends AppCompatActivity
        implements GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = MemberStartActivity.class.getSimpleName();
    public static final String START_NAVER = "start.naver";
    public static final String START_GOOGLE = "start.google";
    public static final int RC_SIGN_IN = 9001;

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
        setContentView(R.layout.activity_member_start);

        GoogleSignInOptions googleSignInOptions =
                new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken(getString(R.string.google_default_web_client_id))
                        .requestEmail()
                        .build();

        mGoogleApiClient = new GoogleApiClient.Builder(MemberStartActivity.this)
                .enableAutoManage(MemberStartActivity.this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions)
                .build();

        mFirebaseAuth = FirebaseAuth.getInstance();

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Log.i(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    Log.i(TAG, "onAuthStateChanged:signed_out");
                }
                updateUI(user);
            }
        };

        mFragmentManager = getSupportFragmentManager();
        mFragment = mFragmentManager.findFragmentById(R.id.member_start_fragment_container);

        if (mFragment == null) {
            mFragment = MemberStartFragment.newInstance();
            mFragmentManager.beginTransaction()
                    .add(R.id.member_start_fragment_container, mFragment)
                    .commit();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);

    }
    @Override
    public void onStop() {
        super.onStop();
        if (mAuthStateListener != null) {
            mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
        }
        hideProgressDialog();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                Log.i(TAG, "GoogleSignInResult" + " isSuccess");
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthGoogle(account);
                // TODO(user): send token to server and validate server-side



            } else {
                Log.i(TAG, "GoogleSignInResult" + " isFail");
                updateUI(null);
            }
        }
    }

    private void firebaseAuthGoogle(GoogleSignInAccount account) {
        Log.d(TAG, "firebaseAuthWithGoogle: id : " + account.getId());
        Log.d(TAG, "firebaseAuthWithGoogle: id token : " + account.getIdToken());
        Log.d(TAG, "firebaseAuthWithGoogle: full name : " + account.getDisplayName());
        Log.d(TAG, "firebaseAuthWithGoogle: email : " + account.getEmail());
        Log.d(TAG, "firebaseAuthWithGoogle: server auth code : " + account.getServerAuthCode());
        Log.d(TAG, "FirebaseInstanceId: token : " + FirebaseInstanceId.getInstance().getToken());

        showProgressDialog();

        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mFirebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());

                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithCredential", task.getException());
                            Toast.makeText(getApplicationContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                        hideProgressDialog();
                    }
                });
    }

    public void signInGoogle() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    public void signOutGoogle() {
//        FirebaseAuth.getInstance().signOut();
        mFirebaseAuth.signOut();

        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {
                        updateUI(null);
                    }
                }
        );
    }

    public void revokeAccessGoogle() {
        mFirebaseAuth.signOut();

        Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {
                        updateUI(null);
                    }
                }
        );
    }

    private void updateUI(FirebaseUser user) {
        hideProgressDialog();
        if (user != null) {
            Log.i(TAG, getString(R.string.google_status_fmt, user.getEmail()));
            Log.i(TAG, getString(R.string.firebase_status_fmt, user.getUid()));
        } else {
            Log.i(TAG, "사용자 정보가 없거나, 로그아웃, 연동 해제 되었습니다.");
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
        Toast.makeText(this, "Google Play Services error.", Toast.LENGTH_SHORT).show();
    }

    public void signInNaver() {
        OAuthLoginDefine.DEVELOPER_VERSION = true;
        mContext = this;
        mNaverOAuthLogin = OAuthLogin.getInstance();
        mNaverOAuthLogin.init(this,
                getString(R.string.naver_oauth_client_id),
                getString(R.string.naver_oauth_client_secret),
                getString(R.string.app_name));

        mNaverOAuthLogin.startOauthLoginActivity(this, mOAuthLoginHandler);
    }

    public void signOutNaver() {
        mNaverOAuthLogin.logout(mContext);
    }

    static private OAuthLoginHandler mOAuthLoginHandler = new OAuthLoginHandler() {
        @Override
        public void run(boolean success) {
            if (success) {
                String accessToken = mNaverOAuthLogin.getAccessToken(mContext);
                String refreshToken = mNaverOAuthLogin.getRefreshToken(mContext);
                long expiresAt = mNaverOAuthLogin.getExpiresAt(mContext);
                String tokenType = mNaverOAuthLogin.getTokenType(mContext);
                Log.i(TAG, "Naver accessToken: " + accessToken);
                Log.i(TAG, "Naver refreshToken: " + refreshToken);
                Log.i(TAG, "Naver expiresAt: " + String.valueOf(expiresAt));
                Log.i(TAG, "Naver tokenType: " + tokenType);
                Log.i(TAG, "Naver oauthState: " + mNaverOAuthLogin.getState(mContext).toString());
            } else {
                String errorCode = mNaverOAuthLogin.getLastErrorCode(mContext).getCode();
                String errorDesc = mNaverOAuthLogin.getLastErrorDesc(mContext);
                Toast.makeText(mContext, "errorCode:" + errorCode + ", errorDesc:" + errorDesc, Toast.LENGTH_SHORT).show();
            }
        }
    };

    public void signInLocal() {
        mFragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.slide_up, R.anim.slide_up)
                .replace(R.id.member_start_fragment_container, MemberStartLocalFragment.newInstance())
                .commit();
    }

    public void cancelSignInLocal() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.member_start_fragment_container);
        if (fragment instanceof MemberStartLocalFragment)
        mFragmentManager.beginTransaction()
                .replace(R.id.member_start_fragment_container, MemberStartFragment.newInstance())
                .commit();
    }

    @VisibleForTesting
    public ProgressDialog mProgressDialog;

    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }
}
