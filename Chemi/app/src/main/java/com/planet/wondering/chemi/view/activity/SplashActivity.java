package com.planet.wondering.chemi.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.planet.wondering.chemi.R;
import com.planet.wondering.chemi.common.AppBaseActivity;

/**
 * Created by yoon on 2017. 1. 5..
 */

public class SplashActivity extends AppBaseActivity {

    private static final String TAG = SplashActivity.class.getSimpleName();

    private static final int SPLASH_TIME_OUT = 1500;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Log.i(TAG, "onCreate Start");
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        setContentView(R.layout.activity_splash);
//
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                Log.i(TAG, "onCreate postDelayed");
                Intent intent = new Intent(SplashActivity.this, MemberStartActivity.class);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                startActivity(intent);
                finish();
//            }
//        }, SPLASH_TIME_OUT);
    }
}
