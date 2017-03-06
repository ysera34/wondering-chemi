package com.planet.wondering.chemi.util.helper;

import android.content.Context;
import android.preference.PreferenceManager;

/**
 * Created by yoon on 2017. 3. 4..
 */

public class ReviewSharedPreferences {

    private final String PREF_RATING_VALUE = "com.planet.wondering.chemi.rating_value";
    private final String PREF_IMAGE1_PATH = "com.planet.wondering.chemi.image1_path";
    private final String PREF_IMAGE2_PATH = "com.planet.wondering.chemi.image2_path";
    private final String PREF_IMAGE3_PATH = "com.planet.wondering.chemi.image3_path";
    private static final String PREF_IMAGE_VIEW2_VISIBILITY = "com.planet.wondering.chemi.image_view2_visibility";
    private static final String PREF_IMAGE_VIEW3_VISIBILITY = "com.planet.wondering.chemi.image_view3_visibility";

    public float getStoredRatingValue(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getFloat(PREF_RATING_VALUE, 0.0f);
    }

    public void setStoreRatingValue(Context context, float ratingValue) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putFloat(PREF_RATING_VALUE, ratingValue)
                .apply();
    }

    public void removeStoredRatingValue(Context context) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .remove(PREF_RATING_VALUE)
                .apply();
    }

    public String getStoredImage1Path(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(PREF_IMAGE1_PATH, null);
    }

    public void setStoreImage1Path(Context context, String image1Path) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(PREF_IMAGE1_PATH, image1Path)
                .apply();
    }

    public void removeStoredImage1Path(Context context) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .remove(PREF_IMAGE1_PATH)
                .apply();
    }

    public String getStoredImage2Path(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(PREF_IMAGE2_PATH, null);
    }

    public void setStoreImage2Path(Context context, String image2Path) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(PREF_IMAGE2_PATH, image2Path)
                .apply();
    }

    public void removeStoredImage2Path(Context context) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .remove(PREF_IMAGE2_PATH)
                .apply();
    }

    public String getStoredImage3Path(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(PREF_IMAGE3_PATH, null);
    }

    public void setStoreImage3Path(Context context, String image3Path) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(PREF_IMAGE3_PATH, image3Path)
                .apply();
    }

    public void removeStoredImage3Path(Context context) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .remove(PREF_IMAGE3_PATH)
                .apply();
    }
}
