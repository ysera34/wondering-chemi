package com.planet.wondering.chemi.model;

/**
 * Created by yoon on 2017. 4. 2..
 */

public class UserConfig {

    private boolean mGetPush;
    private boolean mGetEmail;
    private String mAppVersion;

    public boolean isGetPush() {
        return mGetPush;
    }

    public void setGetPush(boolean getPush) {
        mGetPush = getPush;
    }

    public boolean isGetEmail() {
        return mGetEmail;
    }

    public void setGetEmail(boolean getEmail) {
        mGetEmail = getEmail;
    }

    public String getAppVersion() {
        return mAppVersion;
    }

    public void setAppVersion(String appVersion) {
        mAppVersion = appVersion;
    }
}
