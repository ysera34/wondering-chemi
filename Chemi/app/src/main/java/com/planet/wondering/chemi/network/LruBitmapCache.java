package com.planet.wondering.chemi.network;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.DisplayMetrics;

import com.android.volley.toolbox.ImageLoader;
import com.bumptech.glide.util.LruCache;

/**
 * Created by yoon on 2017. 1. 26..
 */

public class LruBitmapCache extends LruCache implements ImageLoader.ImageCache {

    public LruBitmapCache(int size) {
        super(size);
    }

    public LruBitmapCache(Context context) {
        super(getCacheSize(context));
    }

    public static int getCacheSize(Context context) {
        final DisplayMetrics displayMetrics = context.getResources().
                getDisplayMetrics();
        final int screenWidth = displayMetrics.widthPixels;
        final int screenHeight = displayMetrics.heightPixels;
        // 4 bytes per pixel
        final int screenBytes = screenWidth * screenHeight * 4;

        return screenBytes * 3;
    }

    @Override
    public Bitmap getBitmap(String url) {
        return getBitmap(url);
    }

    @Override
    public void putBitmap(String url, Bitmap bitmap) {
        put(url, bitmap);
    }
}
