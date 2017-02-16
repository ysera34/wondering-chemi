package com.planet.wondering.chemi.util.helper;

import android.content.Context;
import android.preference.PreferenceManager;

/**
 * Created by yoon on 2017. 2. 17..
 */

public class UserSharedPreferences {

    private static final String PREF_TOKEN = "com.planet.wondering.chemi.user.token";

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
}
