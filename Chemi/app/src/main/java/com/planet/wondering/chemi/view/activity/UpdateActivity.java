package com.planet.wondering.chemi.view.activity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.planet.wondering.chemi.R;
import com.planet.wondering.chemi.model.Other;
import com.planet.wondering.chemi.network.AppSingleton;
import com.planet.wondering.chemi.network.Parser;
import com.planet.wondering.chemi.util.listener.OnDialogFinishedListener;
import com.planet.wondering.chemi.view.custom.CustomAlertDialogFragment;
import com.planet.wondering.chemi.view.fragment.MemberCongratulationDialogFragment;

import org.json.JSONObject;

import java.util.regex.Pattern;

import static com.planet.wondering.chemi.common.Common.APP_MARKET_URL;
import static com.planet.wondering.chemi.common.Common.APP_VERSION_NAME_KEY;
import static com.planet.wondering.chemi.common.Common.CHECK_VERSION_REQUEST_CODE;
import static com.planet.wondering.chemi.common.Common.CHECK_VERSION_RESULT_COMPULSORY_MODE_CODE;
import static com.planet.wondering.chemi.common.Common.CHECK_VERSION_RESULT_ERROR_CODE;
import static com.planet.wondering.chemi.common.Common.CHECK_VERSION_RESULT_USUALLY_MODE_CODE;
import static com.planet.wondering.chemi.common.Common.CHECK_VERSION_RESULT_VOLUNTARY_MODE_CODE;
import static com.planet.wondering.chemi.common.Common.EXTRA_RESULT_CHECK_VERSION;
import static com.planet.wondering.chemi.common.Common.NETWORK_SETTING_REQUEST_CODE;
import static com.planet.wondering.chemi.common.Common.PROMOTE_RELEASE_REQUEST_CODE;
import static com.planet.wondering.chemi.network.Config.NUMBER_OF_RETRIES;
import static com.planet.wondering.chemi.network.Config.Other.OTHER_PATH;
import static com.planet.wondering.chemi.network.Config.SOCKET_TIMEOUT_GET_REQ;
import static com.planet.wondering.chemi.network.NetworkConfig.URL_HOST;
import static com.planet.wondering.chemi.view.custom.CustomAlertDialogFragment.NETWORK_DIALOG;

/**
 * Created by yoon on 2017. 6. 10..
 */

public class UpdateActivity extends AppCompatActivity implements OnDialogFinishedListener {

    private static final String TAG = UpdateActivity.class.getSimpleName();

    private static final String EXTRA_REQUEST_CODE = "com.planet.wondering.chemi.request_code";
    private static final String EXTRA_IS_SHOW_DIALOG = "com.planet.wondering.chemi.is_show_dialog";

    public static Intent newIntent(Context packageContext, int requestCode, boolean isShowDialog) {
        Intent intent = new Intent(packageContext, UpdateActivity.class);
        intent.putExtra(EXTRA_REQUEST_CODE, requestCode);
        intent.putExtra(EXTRA_IS_SHOW_DIALOG, isShowDialog);
        return intent;
    }

    private int mRequestCode;
    private boolean mIsShowDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_transparent);

        mRequestCode = getIntent().getIntExtra(EXTRA_REQUEST_CODE, -1);
        mIsShowDialog = getIntent().getBooleanExtra(EXTRA_IS_SHOW_DIALOG, false);

        if (mRequestCode == CHECK_VERSION_REQUEST_CODE) {
            requestAppVersionName();
        } else if (mRequestCode == PROMOTE_RELEASE_REQUEST_CODE) {
            showPromoteRelease();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mRequestCode == CHECK_VERSION_REQUEST_CODE) {
            requestAppVersionName();
        }
    }

    private void requestAppVersionName() {

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET, URL_HOST + OTHER_PATH,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Other versionOtherObject;
                        versionOtherObject = Parser.parseOther(response, APP_VERSION_NAME_KEY);
                        checkVersionName(getAppVersionName(), versionOtherObject.getDescription());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, error.toString());
                        Toast.makeText(getApplicationContext(), R.string.progress_dialog_message_error,
                                Toast.LENGTH_SHORT).show();
                        showSettingDialog();
                    }
                }
        );

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(SOCKET_TIMEOUT_GET_REQ,
                NUMBER_OF_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest, TAG);
    }

    public String getAppVersionName() {

        try {
            PackageInfo packageInfo = getApplicationContext()
                    .getPackageManager().getPackageInfo(getApplicationContext().getPackageName(), 0);
            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, e.getMessage());
        }
        return null;
    }

    private void checkVersionName(String packageInfoVersionName, String serverInfoVersionName) {

//        Log.i(TAG, "packageInfoVersionName: " + packageInfoVersionName);
//        Log.i(TAG, "serverInfoVersionName: " + serverInfoVersionName);

        String[] packageInfoVersionNameArray = null;
        String[] serverInfoVersionNameArray = null;
        Intent intent = new Intent();

        if (packageInfoVersionName != null) {
            packageInfoVersionNameArray = packageInfoVersionName.split(Pattern.quote("."));
//            for (int i = 0; i < packageInfoVersionNameArray.length; i++) {
//                Log.i(TAG, "index:" + i + ", desc: " + packageInfoVersionNameArray[i]);
//            }
        }
        if (serverInfoVersionName != null) {
            serverInfoVersionNameArray = serverInfoVersionName.split(Pattern.quote("."));
//            for (int i = 0; i < serverInfoVersionNameArray.length; i++) {
//                Log.i(TAG, "index:" + i + ", desc: " + serverInfoVersionNameArray[i]);
//            }
        }

        if (packageInfoVersionNameArray != null && serverInfoVersionNameArray != null) {

            if (packageInfoVersionName.equals(serverInfoVersionName)) {
                intent.putExtra(EXTRA_RESULT_CHECK_VERSION, CHECK_VERSION_RESULT_USUALLY_MODE_CODE);
                setResult(Activity.RESULT_OK, intent);
                finish();
                return;
            }

            if (Integer.valueOf(packageInfoVersionNameArray[0]) <
                    Integer.valueOf(serverInfoVersionNameArray[0]) ||
                    Integer.valueOf(packageInfoVersionNameArray[1]) <
                            Integer.valueOf(serverInfoVersionNameArray[1])) {
                intent.putExtra(EXTRA_RESULT_CHECK_VERSION, CHECK_VERSION_RESULT_COMPULSORY_MODE_CODE);
                setResult(Activity.RESULT_OK, intent);
//                finish();
                showCompulsoryDialog();
            } else {
                intent.putExtra(EXTRA_RESULT_CHECK_VERSION, CHECK_VERSION_RESULT_VOLUNTARY_MODE_CODE);
                setResult(Activity.RESULT_OK, intent);
//                finish();
                if (mIsShowDialog) {
                    showVoluntaryDialog();
                } else {
                    finish();
                }
            }
        } else {
            intent.putExtra(EXTRA_RESULT_CHECK_VERSION, CHECK_VERSION_RESULT_ERROR_CODE);
            setResult(Activity.RESULT_OK, intent);
            finish();
            if (packageInfoVersionNameArray == null) {
                Log.e(TAG, "packageInfoVersionNameArray is null");
            }
            if (serverInfoVersionNameArray == null) {
                Log.e(TAG, "serverInfoVersionNameArray is null");
            }
        }
    }

    private void showCompulsoryDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(UpdateActivity.this, R.style.SplashDialogTheme);
        builder.setMessage("케미가 업그레이드 되었어요!\n업데이트를 통해 더욱 향상된 서비스를 경험하세요 :)");
        builder.setPositiveButton("업데이트", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(APP_MARKET_URL)));
                finish();
            }
        });
        builder.setNegativeButton("종료", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                ActivityCompat.finishAffinity(UpdateActivity.this);
                setResult(Activity.RESULT_CANCELED);
                finishAffinity();
            }
        });
        builder.setCancelable(false);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showVoluntaryDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(UpdateActivity.this, R.style.SplashDialogTheme);
        builder.setMessage("케미가 업그레이드 되었어요!\n업데이트를 통해 더욱 향상된 서비스를 경험하세요 :)");
        builder.setPositiveButton("업데이트", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(APP_MARKET_URL)));
                finish();
            }
        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        builder.setCancelable(false);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showSettingDialog() {
        CustomAlertDialogFragment dialogFragment = CustomAlertDialogFragment
                .newInstance(R.string.dialog_title_wait_message,
                        R.string.dialog_description_promote_network_setting,
                        R.string.network_dialog_positive_string,
                        R.string.network_dialog_negative_string,
                        NETWORK_SETTING_REQUEST_CODE);
        dialogFragment.show(getSupportFragmentManager(), NETWORK_DIALOG);
    }

    @Override
    public void onDialogFinished(boolean isChose, int requestCode) {
        if (requestCode == NETWORK_SETTING_REQUEST_CODE) {
            if (isChose) {
                requestAppVersionName();
            } else {
                startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        }
    }

    private void showPromoteRelease() {
        MemberCongratulationDialogFragment dialogFragment =
                MemberCongratulationDialogFragment.newInstance(true);
        dialogFragment.show(getSupportFragmentManager(), "release_dialog");
    }
}
