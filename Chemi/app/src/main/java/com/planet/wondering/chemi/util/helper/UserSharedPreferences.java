package com.planet.wondering.chemi.util.helper;

import android.content.Context;
import android.preference.PreferenceManager;

/**
 * Created by yoon on 2017. 2. 17..
 */

public class UserSharedPreferences {

    private static final String PREF_TOKEN = "com.planet.wondering.chemi.user.token";
    private static final String PREF_USERNAME = "com.planet.wondering.chemi.user.name";
    private static final String PREF_GET_PUSH = "com.planet.wondering.chemi.user.get_push";
    private static final String PREF_GET_EMAIL = "com.planet.wondering.chemi.user.get_email";
    private static final String PREF_INTRO_SLIDE = "com.planet.wondering.chemi.user.intro_slide";

    public static String getStoredToken(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(PREF_TOKEN, null);
    }

    public static void setStoreToken(Context context, String token) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(PREF_TOKEN, token)
                .apply();
    }

    public static void removeStoredToken(Context context) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .remove(PREF_TOKEN)
                .apply();
    }

    public static String getStoredUserName(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(PREF_USERNAME, null);
    }

    public static void setStoreUserName(Context context, String userName) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(PREF_USERNAME, userName)
                .apply();
    }

    public static void removeStoredUserName(Context context) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .remove(PREF_USERNAME)
                .apply();
    }

    public static boolean getStoredGetPush(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean(PREF_GET_PUSH, true);
    }

    public static void setStoredGetPush(Context context, boolean getPush) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putBoolean(PREF_GET_PUSH, getPush)
                .apply();
    }

    public static void removeStoredGetPush(Context context) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .remove(PREF_GET_PUSH)
                .apply();
    }

    public static boolean getStoredGetEmail(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean(PREF_GET_EMAIL, true);
    }

    public static void setStoredGetEmail(Context context, boolean getEmail) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putBoolean(PREF_GET_EMAIL, getEmail)
                .apply();
    }

    public static void removeStoredGetEmail(Context context) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .remove(PREF_GET_EMAIL)
                .apply();
    }

    public static boolean getStoredIntroSlide(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean(PREF_INTRO_SLIDE, false);
    }

    public static void setStoredIntroSlide(Context context, boolean isIntroSlide) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putBoolean(PREF_INTRO_SLIDE, isIntroSlide)
                .apply();
    }

    public static void removeStoredIntroSlide(Context context) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .remove(PREF_INTRO_SLIDE)
                .apply();
    }
}
