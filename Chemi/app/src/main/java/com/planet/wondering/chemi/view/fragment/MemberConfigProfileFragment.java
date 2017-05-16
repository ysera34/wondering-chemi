package com.planet.wondering.chemi.view.fragment;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.nhn.android.naverlogin.OAuthLogin;
import com.planet.wondering.chemi.R;
import com.planet.wondering.chemi.model.BottomSheetMenu;
import com.planet.wondering.chemi.model.User;
import com.planet.wondering.chemi.network.AppSingleton;
import com.planet.wondering.chemi.network.MultipartRequest;
import com.planet.wondering.chemi.network.MultipartRequestHelper;
import com.planet.wondering.chemi.util.adapter.BottomSheetMenuAdapter;
import com.planet.wondering.chemi.util.helper.ImageHandler;
import com.planet.wondering.chemi.util.helper.UserSharedPreferences;
import com.planet.wondering.chemi.util.listener.OnMenuSelectedListener;
import com.planet.wondering.chemi.util.listener.OnUserInfoUpdateListener;
import com.planet.wondering.chemi.view.custom.CustomAlertDialogFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;
import static com.planet.wondering.chemi.common.Common.LOGOUT_DIALOG_REQUEST_CODE;
import static com.planet.wondering.chemi.common.Common.REVOKE_DIALOG_REQUEST_CODE;
import static com.planet.wondering.chemi.common.Common.WITHDRAW_DIALOG_REQUEST_CODE;
import static com.planet.wondering.chemi.network.Config.NUMBER_OF_RETRIES;
import static com.planet.wondering.chemi.network.Config.RESPONSE_DATA;
import static com.planet.wondering.chemi.network.Config.RESPONSE_MESSAGE;
import static com.planet.wondering.chemi.network.Config.RESPONSE_SUCCESS;
import static com.planet.wondering.chemi.network.Config.SOCKET_TIMEOUT_POST_REQ;
import static com.planet.wondering.chemi.network.Config.URL_HOST;
import static com.planet.wondering.chemi.network.Config.User.IMAGE_PATH;
import static com.planet.wondering.chemi.network.Config.User.Key.TOKEN;
import static com.planet.wondering.chemi.network.Config.User.Key.USER_IMAGE_PATH;
import static com.planet.wondering.chemi.network.Config.User.PATH;
import static com.planet.wondering.chemi.view.custom.CustomAlertDialogFragment.LOGOUT_DIALOG;
import static com.planet.wondering.chemi.view.custom.CustomAlertDialogFragment.REVOKE_DIALOG;
import static com.planet.wondering.chemi.view.custom.CustomAlertDialogFragment.WITHDRAW_DIALOG;

/**
 * Created by yoon on 2017. 2. 11..
 */

public class MemberConfigProfileFragment extends Fragment
        implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = MemberConfigProfileFragment.class.getSimpleName();

    private static final String ARG_CONFIG_USER = "config_user";

    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 1100;
    private static final int GALLERY_IMAGE_REQUEST_CODE = 2100;
    private static final int PERMISSION_EXTERNAL_STORAGE_REQUEST_CODE = 9101;
    private String[] mStoragePermissions = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};

    public static MemberConfigProfileFragment newInstance() {

        Bundle args = new Bundle();

        MemberConfigProfileFragment fragment = new MemberConfigProfileFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static MemberConfigProfileFragment newInstance(User user) {

        Bundle args = new Bundle();
        args.putSerializable(ARG_CONFIG_USER, user);

        MemberConfigProfileFragment fragment = new MemberConfigProfileFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private CircleImageView mUserCircleImageView;
    private CircleImageView mCameraCircleImageView;
    private ImageHandler mImageHandler;

    private BottomSheetDialog mMenuBottomSheetDialog;

    private TextView mProfileNameTextView;
    private LinearLayout mProfileInfoLayout;
    private TextView mPromoteTextView;

    private LinearLayout mParentLayout;
    private TextView mParentAgeTextView;
    private TextView mParentDrySkinTextView;
    private TextView mParentOilySkinTextView;
    private TextView mParentDryOilySkinTextView;
    private TextView mParentAllergyTextView;

    private LinearLayout mChildLayout;
    private TextView mChildDrySkinTextView;
    private TextView mChildAllergyTextView;

    private int[] mConfigProfileLayoutIds;
    private RelativeLayout[] mConfigProfileLayouts;
    private ImageView mRevokeTargetImageView;

    private User mUser;
    private byte mUserPlatformId;

    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private GoogleApiClient mGoogleApiClient;

    private static OAuthLogin mNaverOAuthLogin;
    private static Context mContext;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mConfigProfileLayoutIds = new int[]{
                R.id.member_config_profile_name_layout, R.id.member_config_profile_change_password_layout,
                R.id.member_config_profile_change_survey_layout, R.id.member_config_profile_sign_out_layout,
                R.id.member_config_profile_revoke_layout, R.id.member_config_profile_withdraw_layout,};
        mConfigProfileLayouts = new RelativeLayout[mConfigProfileLayoutIds.length];

        mUser = (User) getArguments().getSerializable(ARG_CONFIG_USER);
        mUserPlatformId = mUser.getPlatformId();

        mFirebaseAuth = FirebaseAuth.getInstance();
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Log.d(TAG, "onAuthStateChanged : sign_in : " + user.getUid());
                } else {
                    Log.d(TAG, "onAuthStateChanged : sign_out");
                }
            }
        };

        if (mUserPlatformId == 1) {
            GoogleSignInOptions googleSignInOptions =
                    new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                            .requestIdToken(getString(R.string.google_default_web_client_id))
                            .requestEmail()
                            .build();

            mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                    .enableAutoManage(getActivity(), 0, this)
                    .addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions)
                    .build();
        } else if (mUserPlatformId == 2) {
            mContext = getActivity();
            mNaverOAuthLogin = OAuthLogin.getInstance();
            mNaverOAuthLogin.init(getActivity(),
                    getString(R.string.naver_oauth_client_id),
                    getString(R.string.naver_oauth_client_secret),
                    getString(R.string.app_name));
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_member_config_profile, container, false);

        mUserCircleImageView = (CircleImageView) view.findViewById(R.id.member_config_profile_user_image_view);
        mCameraCircleImageView = (CircleImageView) view.findViewById(R.id.member_config_profile_camera_image_view);
        mUserCircleImageView.setOnClickListener(this);
        mCameraCircleImageView.setOnClickListener(this);

        for (int i = 0; i < mConfigProfileLayoutIds.length; i++) {
            mConfigProfileLayouts[i] = (RelativeLayout) view.findViewById(mConfigProfileLayoutIds[i]);
            mConfigProfileLayouts[i].setOnClickListener(this);
        }

        mProfileNameTextView = (TextView) view.findViewById(R.id.member_config_profile_name_text_view);
        mProfileInfoLayout = (LinearLayout) view.findViewById(R.id.member_config_profile_info_layout);
        mPromoteTextView = (TextView) view.findViewById(R.id.member_config_profile_info_promote_text_view);
        mRevokeTargetImageView = (ImageView) view.findViewById(R.id.member_config_profile_revoke_target_image_view);

        mParentLayout = (LinearLayout) view.findViewById(R.id.member_config_profile_parent_layout);
        mParentAgeTextView = (TextView) view.findViewById(R.id.member_config_profile_parent_age_text_view);
        mParentDrySkinTextView = (TextView) view.findViewById(R.id.member_config_profile_parent_dry_skin_text_view);
        mParentOilySkinTextView = (TextView) view.findViewById(R.id.member_config_profile_parent_oily_skin_text_view);
        mParentDryOilySkinTextView = (TextView) view.findViewById(R.id.member_config_profile_parent_dry_oily_skin_text_view);
        mParentAllergyTextView = (TextView) view.findViewById(R.id.member_config_profile_parent_allergy_text_view);

        mChildLayout = (LinearLayout) view.findViewById(R.id.member_config_profile_child_layout);
        mChildDrySkinTextView = (TextView) view.findViewById(R.id.member_config_profile_child_dry_skin_text_view);
        mChildAllergyTextView = (TextView) view.findViewById(R.id.member_config_profile_child_allergy_text_view);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (mUser.getImagePath() == null || mUser.getImagePath().equals("null")) {
            if (mUser.isHasExtraInfo()) {
                if (mUser.isGender()) {
                    mUserCircleImageView.setImageResource(R.drawable.ic_user_profile_mommy);
                } else {
                    mUserCircleImageView.setImageResource(R.drawable.ic_user_profile_daddy);
                }
            } else {
                mUserCircleImageView.setImageResource(R.drawable.ic_user);
            }
        } else {
            Glide.with(getActivity())
                    .load(mUser.getImagePath())
                    .crossFade()
                    .into(mUserCircleImageView);
        }

        mProfileNameTextView.setText(String.valueOf(mUser.getName()));

        if (mUser.isHasExtraInfo()) {
            mPromoteTextView.setVisibility(View.GONE);
            showUserInfo();
        } else {
            mProfileInfoLayout.setVisibility(View.GONE);
        }

        if (mUserPlatformId == 0) {
            mConfigProfileLayouts[4].setVisibility(View.GONE);
        } else if (mUserPlatformId == 1) {
            mRevokeTargetImageView.setImageResource(R.drawable.ic_google);
            mConfigProfileLayouts[1].setVisibility(View.GONE);
        } else if (mUserPlatformId == 2) {
            mRevokeTargetImageView.setImageResource(R.drawable.ic_naver);
            mConfigProfileLayouts[1].setVisibility(View.GONE);
        }
        mConfigProfileLayouts[5].setVisibility(View.GONE);
    }

    private void showUserInfo() {

        mParentAgeTextView.setText(mUser.getAge());

        if (mUser.isHasDrySkin() && mUser.isHasOilySkin()) {
            mParentDrySkinTextView.setVisibility(View.GONE);
            mParentOilySkinTextView.setVisibility(View.GONE);
            mParentDryOilySkinTextView.setVisibility(View.VISIBLE);
        } else if (mUser.isHasDrySkin() && !mUser.isHasOilySkin()) {
            mParentDrySkinTextView.setVisibility(View.VISIBLE);
            mParentOilySkinTextView.setVisibility(View.GONE);
            mParentDryOilySkinTextView.setVisibility(View.GONE);
        } else if (!mUser.isHasDrySkin() && mUser.isHasOilySkin()) {
            mParentDrySkinTextView.setVisibility(View.GONE);
            mParentOilySkinTextView.setVisibility(View.VISIBLE);
            mParentDryOilySkinTextView.setVisibility(View.GONE);
        } else if (!mUser.isHasDrySkin() && !mUser.isHasOilySkin()) {
            mParentDrySkinTextView.setVisibility(View.GONE);
            mParentOilySkinTextView.setVisibility(View.GONE);
            mParentDryOilySkinTextView.setVisibility(View.GONE);
        }

        if (mUser.isHasAllergy()) {
            mParentAllergyTextView.setVisibility(View.VISIBLE);
        } else {
            mParentAllergyTextView.setVisibility(View.GONE);
        }

        if (mUser.isHasChild()) {
            mChildLayout.setVisibility(View.VISIBLE);

            if (mUser.isChildHasDrySkin()) {
                mChildDrySkinTextView.setVisibility(View.VISIBLE);
            } else {
                mChildDrySkinTextView.setVisibility(View.GONE);
            }

            if (mUser.isChildHasAllergy()) {
                mChildAllergyTextView.setVisibility(View.VISIBLE);
            } else {
                mChildAllergyTextView.setVisibility(View.GONE);
            }

        } else {
            mChildLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.member_config_profile_user_image_view:
            case R.id.member_config_profile_camera_image_view:
//                createUserImageMenuBottomSheetDialog();
                checkStoragePermission();
                break;
            case R.id.member_config_profile_name_layout:
                mMenuSelectedListener.onMenuSelected(11);
                break;
            case R.id.member_config_profile_change_password_layout:
                mMenuSelectedListener.onMenuSelected(12);
                break;
            case R.id.member_config_profile_change_survey_layout:
                mMenuSelectedListener.onMenuSelected(13);
                break;
            case R.id.member_config_profile_sign_out_layout:
                CustomAlertDialogFragment dialogFragment1 = CustomAlertDialogFragment
                        .newInstance(R.drawable.ic_logout, R.string.logout_info_message,
                                R.string.logout_button_title, LOGOUT_DIALOG_REQUEST_CODE);
                dialogFragment1.show(getFragmentManager(), LOGOUT_DIALOG);
                break;
            case R.id.member_config_profile_revoke_layout:
                CustomAlertDialogFragment dialogFragment3 = CustomAlertDialogFragment
                        .newInstance(R.drawable.ic_logout, R.string.revoke_info_message,
                                R.string.revoke_button_title, REVOKE_DIALOG_REQUEST_CODE);
                dialogFragment3.show(getFragmentManager(), REVOKE_DIALOG);
                break;
            case R.id.member_config_profile_withdraw_layout:
                CustomAlertDialogFragment dialogFragment2 = CustomAlertDialogFragment
                        .newInstance(R.drawable.ic_logout, R.string.withdraw_info_message,
                                R.string.withdraw_button_title, WITHDRAW_DIALOG_REQUEST_CODE);
                dialogFragment2.show(getFragmentManager(), WITHDRAW_DIALOG);
                break;
        }
    }

    public void onDialogFinished(boolean isChose, int requestCode) {
        if (isChose) {
            switch (requestCode) {
                case LOGOUT_DIALOG_REQUEST_CODE:
                    // need to know platform id and have to sign out
                    UserSharedPreferences.removeStoredToken(getActivity());

                    if (mUserPlatformId == 1) {
                        signOutGoogle();
                    } else if (mUserPlatformId == 2) {
                        signOutNaver();
                    }

                    Toast.makeText(getActivity(), "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show();
                    getActivity().onBackPressed();
                    break;
                case REVOKE_DIALOG_REQUEST_CODE:
                    UserSharedPreferences.removeStoredToken(getActivity());

                    if (mUserPlatformId == 1) {
                        revokeAccessGoogle();
                    } else if (mUserPlatformId == 2) {
                        revokeAccessNaver();
                    }

                    Toast.makeText(getActivity(), "연동해제 되었습니다.", Toast.LENGTH_SHORT).show();
                    getActivity().onBackPressed();
                    break;
                case WITHDRAW_DIALOG_REQUEST_CODE:
                    // need to know platform id and have to revoke
                    // want to leave member and then have to revoke
                    Toast.makeText(getActivity(), "탈퇴 되었습니다.", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }

    private void createUserImageMenuBottomSheetDialog() {

        ArrayList<BottomSheetMenu> bottomSheetMenus = new ArrayList<>();
        bottomSheetMenus.add(new BottomSheetMenu(0, R.string.bottom_sheet_menu_default_image));
        bottomSheetMenus.add(new BottomSheetMenu(R.drawable.ic_camera, R.string.bottom_sheet_menu_camera));
        bottomSheetMenus.add(new BottomSheetMenu(R.drawable.ic_gallery, R.string.bottom_sheet_menu_gallery));

        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        View view = layoutInflater.inflate(R.layout.layout_bottom_sheet_menu_recycler_view, null);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.bottom_sheet_menu_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        BottomSheetMenuAdapter bottomSheetMenuAdapter = new BottomSheetMenuAdapter(bottomSheetMenus);
        recyclerView.setAdapter(bottomSheetMenuAdapter);
        bottomSheetMenuAdapter.setItemClickListener(new BottomSheetMenuAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BottomSheetMenuAdapter.MenuItemHolder itemHolder, int position) {
                dismissMenuBottomSheetDialog();
                mImageHandler = new ImageHandler(getActivity());
                if (position == 0) {
                    switch (mUser.getGender()) {
                        case 0:
                            mUserCircleImageView.setImageResource(R.drawable.ic_user_profile_mommy);
                            break;
                        case 1:
                            mUserCircleImageView.setImageResource(R.drawable.ic_user_profile_daddy);
                            break;
                        default:
                            mUserCircleImageView.setImageResource(R.drawable.ic_user);
                            break;
                    }
                    requestUserImage(true);
                } else if (position == 1) {
                    if (mImageHandler.hasCamera()) {
                        startActivityForResult(mImageHandler.dispatchTakePictureIntent(), CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
                    } else {
                        Toast.makeText(getActivity(), "카메라를 사용할 수 없어요.", Toast.LENGTH_SHORT).show();
                    }
                } else if (position == 2) {
                    startActivityForResult(mImageHandler.pickGalleryPictureIntent(), GALLERY_IMAGE_REQUEST_CODE);
                }
            }
        });
        mMenuBottomSheetDialog = new BottomSheetDialog(getActivity());
        mMenuBottomSheetDialog.setContentView(view);
        mMenuBottomSheetDialog.show();
    }

    private boolean dismissMenuBottomSheetDialog() {
        if (mMenuBottomSheetDialog != null && mMenuBottomSheetDialog.isShowing()) {
            mMenuBottomSheetDialog.dismiss();
            return true;
        }
        return false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case CAMERA_CAPTURE_IMAGE_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    mImageHandler.handleCameraImage(mUserCircleImageView);
                    requestUserImage(false);
                }
                break;
            case GALLERY_IMAGE_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    mImageHandler.handleGalleryImage(data, mUserCircleImageView);
                    requestUserImage(false);
                }
                break;

        }
        mImageHandler = null;
    }

    private void requestUserImage(final boolean isDefaultImage) {

        MultipartRequest multipartRequest = new MultipartRequest(
                Request.Method.PUT, URL_HOST + PATH + IMAGE_PATH,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {

//                        Log.i(TAG, "onResponse NetworkResponse : " + response.toString());
                        String responseString = new String(response.data);
//                        Log.i(TAG, "responseString " + responseString);
                        try {
                            JSONObject responseJSONObject = new JSONObject(responseString);
                            if (responseJSONObject.getString(RESPONSE_MESSAGE).equals(RESPONSE_SUCCESS)) {
                                String imagePath = responseJSONObject.getJSONObject(RESPONSE_DATA).getString(USER_IMAGE_PATH);
                                mUserInfoUpdateListener.onUserImagePathUpdate(imagePath);
                                Toast.makeText(getActivity(), "프로필 사진이 수정되었어요.", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, error.toString());
                        Toast.makeText(getActivity(),
                                R.string.progress_dialog_message_error, Toast.LENGTH_SHORT).show();
                    }
                }
        )
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
//                return super.getHeaders();
                Map<String, String> params = new HashMap<>();
                params.put(TOKEN, UserSharedPreferences.getStoredToken(getActivity()));
                return params;
            }

            @Override
            protected Map<String, DataPart> getByteData() throws AuthFailureError {
//                return super.getByteData();
                Map<String, DataPart> params = new HashMap<>();
                if (isDefaultImage) {

                } else {
                    params.put("image", new DataPart("userImage.jpg", MultipartRequestHelper.getFileDataFromDrawable(
                            getActivity(), mUserCircleImageView.getDrawable()), "image/*"));
                }
                return params;
            }
        };

        multipartRequest.setRetryPolicy(new DefaultRetryPolicy(SOCKET_TIMEOUT_POST_REQ,
                NUMBER_OF_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        AppSingleton.getInstance(getActivity()).addToRequestQueue(multipartRequest, TAG);
    }

    private void checkStoragePermission() {
        String state = Environment.getExternalStorageState();
        if (!Environment.MEDIA_MOUNTED.equals(state) || Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            dismissMenuBottomSheetDialog();
            Toast.makeText(getActivity(), "SD 카드가 없으므로 취소 되었습니다.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(getActivity(), mStoragePermissions[0]) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(getActivity(), mStoragePermissions[1]) != PackageManager.PERMISSION_GRANTED) {

                if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), mStoragePermissions[0])
                        || ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), mStoragePermissions[1])) {
                    // Should we show an explanation?

                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("저장소 권한 요청합니다.")
                            .setMessage("프로필 이미지 변경을 위한 갤러리 접근 권한을 요청합니다.");
                    builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            requestPermissions(mStoragePermissions, PERMISSION_EXTERNAL_STORAGE_REQUEST_CODE);
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();

                } else {
                    // No explanation needed, we can request the permission.
                    requestPermissions(mStoragePermissions, PERMISSION_EXTERNAL_STORAGE_REQUEST_CODE);
                }

            } else {
                // already have permission
                createUserImageMenuBottomSheetDialog();
            }
        } else {
            createUserImageMenuBottomSheetDialog();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case PERMISSION_EXTERNAL_STORAGE_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    createUserImageMenuBottomSheetDialog();

                } else {
                    Toast.makeText(getActivity(), "권한이 없으므로 취소 되었습니다.", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    OnMenuSelectedListener mMenuSelectedListener;
    OnUserInfoUpdateListener mUserInfoUpdateListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mMenuSelectedListener = (OnMenuSelectedListener) context;
            mUserInfoUpdateListener = (OnUserInfoUpdateListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnMenuSelectedListener or OnUserInfoUpdateListener");
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
    }

    public void signOutGoogle() {
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
        if (user != null) {
            Log.i(TAG, getString(R.string.google_status_fmt, user.getEmail()));
            Log.i(TAG, getString(R.string.firebase_status_fmt, user.getUid()));
        } else {
            Log.i(TAG, "Google User, Firebase User is null");
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
//        Toast.makeText(this, "Google Play Services error.", Toast.LENGTH_SHORT).show();
    }

    public void signOutNaver() {
        mFirebaseAuth.signOut();

        mNaverOAuthLogin.logout(mContext);
//        mNaverOAuthLogin.logoutAndDeleteToken(mContext);
    }

    public void revokeAccessNaver() {
        mFirebaseAuth.signOut();

        new DeleteTokenTask().execute();
    }

    private class DeleteTokenTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            boolean isSuccessDeleteToken = mNaverOAuthLogin.logoutAndDeleteToken(mContext);

            if (!isSuccessDeleteToken) {
                Log.d(TAG, "errorCode:" + mNaverOAuthLogin.getLastErrorCode(mContext));
                Log.d(TAG, "errorDese:" + mNaverOAuthLogin.getLastErrorDesc(mContext));
            }
            return null;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mUserPlatformId == 1) {
            mGoogleApiClient.stopAutoManage(getActivity());
            mGoogleApiClient.disconnect();
        } else if (mUserPlatformId == 2) {
            mContext = null;
            mNaverOAuthLogin = null;
        }
    }
}
