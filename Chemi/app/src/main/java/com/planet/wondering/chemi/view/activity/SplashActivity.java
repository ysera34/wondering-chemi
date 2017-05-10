package com.planet.wondering.chemi.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.planet.wondering.chemi.common.AppBaseActivity;
import com.planet.wondering.chemi.util.helper.UserSharedPreferences;

/**
 * Created by yoon on 2017. 1. 5..
 */

public class SplashActivity extends AppBaseActivity {

    private static final String TAG = SplashActivity.class.getSimpleName();

    private static final int SPLASH_TIME_OUT = 1500;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        setContentView(R.layout.activity_splash);
//
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {

        if (UserSharedPreferences.getStoredIntroSlide(getApplicationContext())) {
            Intent intent = new Intent(SplashActivity.this, MemberStartActivity.class);
            startActivity(intent);
            finish();
        } else {
            Intent intent = new Intent(SplashActivity.this, IntroActivity.class);
            startActivity(intent);
            finish();
        }

//            }
//        }, SPLASH_TIME_OUT);
    }
}
