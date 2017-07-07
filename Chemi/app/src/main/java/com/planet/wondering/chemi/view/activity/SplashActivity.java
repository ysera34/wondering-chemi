package com.planet.wondering.chemi.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.planet.wondering.chemi.R;
import com.planet.wondering.chemi.model.home.PromoteProduct;
import com.planet.wondering.chemi.network.AppSingleton;
import com.planet.wondering.chemi.network.Parser;
import com.planet.wondering.chemi.util.helper.PromoteProductSharedPreferences;
import com.planet.wondering.chemi.util.helper.UserSharedPreferences;

import org.json.JSONObject;

import java.util.ArrayList;

import static com.planet.wondering.chemi.common.Common.CHECK_VERSION_REQUEST_CODE;
import static com.planet.wondering.chemi.common.Common.CHECK_VERSION_RESULT_USUALLY_MODE_CODE;
import static com.planet.wondering.chemi.common.Common.CHECK_VERSION_RESULT_VOLUNTARY_MODE_CODE;
import static com.planet.wondering.chemi.common.Common.EXTRA_RESULT_CHECK_VERSION;
import static com.planet.wondering.chemi.network.Config.NUMBER_OF_RETRIES;
import static com.planet.wondering.chemi.network.Config.Product.PROMOTE_PRODUCT_PATH;
import static com.planet.wondering.chemi.network.Config.SOCKET_TIMEOUT_GET_REQ;
import static com.planet.wondering.chemi.network.Config.URL_HOST;

/**
 * Created by yoon on 2017. 1. 5..
 */

public class SplashActivity extends AppCompatActivity {

    private static final String TAG = SplashActivity.class.getSimpleName();

    private static final int SPLASH_TIME_OUT = 1000;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        startActivityForResult(UpdateActivity.newIntent(getApplicationContext(),
                CHECK_VERSION_REQUEST_CODE, false), CHECK_VERSION_REQUEST_CODE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case CHECK_VERSION_REQUEST_CODE:
                if (resultCode == Activity.RESULT_OK) {
                    if (data != null) {
                        int resultModeCode = data.getIntExtra(EXTRA_RESULT_CHECK_VERSION, -1);
                        if (resultModeCode == CHECK_VERSION_RESULT_USUALLY_MODE_CODE ||
                                resultModeCode == CHECK_VERSION_RESULT_VOLUNTARY_MODE_CODE) {
                            requestPromoteProducts();
                        }
                    }
                }
                break;
        }
    }

    private void startMainActivity() {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if (UserSharedPreferences.getStoredIntroSlide(getApplicationContext())) {
                    Intent intent = new Intent(SplashActivity.this, MemberStartActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    finish();
                } else {
                    Intent intent = new Intent(SplashActivity.this, IntroActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    finish();
                }

            }
        }, SPLASH_TIME_OUT);
    }

//    private void popupUpdateDialog() {
//
//        AlertDialog.Builder builder = new AlertDialog.Builder(SplashActivity.this, R.style.SplashDialogTheme);
//        builder.setMessage("케미가 업그레이드 되었어요!\n업데이트를 통해 더욱 향상된 서비스를 경험하세요 :)");
//        builder.setPositiveButton("업데이트", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(APP_MARKET_URL)));
//                finish();
//            }
//        });
//        builder.setNegativeButton("종료", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                ActivityCompat.finishAffinity(SplashActivity.this);
//            }
//        });
//        builder.setCancelable(false);
//        AlertDialog dialog = builder.create();
//        dialog.show();
//    }

    private void requestPromoteProducts() {

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET, URL_HOST + PROMOTE_PRODUCT_PATH,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        ArrayList<PromoteProduct> promoteProducts = Parser.parsePromoteProducts(response);
                        PromoteProductSharedPreferences.setStoredPromoteProducts(getApplicationContext(), promoteProducts);
                        startMainActivity();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, error.toString());
                    }
                }
        );

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(SOCKET_TIMEOUT_GET_REQ,
                NUMBER_OF_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest, TAG);
    }


}
