package com.planet.wondering.chemi.common;

import android.app.Application;

import com.tsengvn.typekit.Typekit;

/**
 * Created by yoon on 2017. 1. 5..
 */

public class GlobalApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Typekit.getInstance()
                .addNormal(Typekit.createFromAsset(this, "fonts/NotoSans-Regular.otf"))
                .addBold(Typekit.createFromAsset(this, "fonts/NotoSans-Bold.otf"))
                .addItalic(Typekit.createFromAsset(this, "fonts/NotoSans-Light.otf"))
                .addBoldItalic(Typekit.createFromAsset(this, "fonts/NotoSans-Medium.otf"));
    }
}
