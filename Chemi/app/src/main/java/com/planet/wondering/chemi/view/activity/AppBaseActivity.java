package com.planet.wondering.chemi.view.activity;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;

import com.tsengvn.typekit.TypekitContextWrapper;

/**
 * Created by yoon on 2017. 1. 5..
 */

public class AppBaseActivity extends AppCompatActivity {

    private static final String TAG = AppBaseActivity.class.getSimpleName();

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }

    public int getScreenWidth() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }
}
