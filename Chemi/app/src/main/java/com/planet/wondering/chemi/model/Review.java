package com.planet.wondering.chemi.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.util.LruCache;
import android.widget.ImageView;

import com.planet.wondering.chemi.util.helper.ReviewSharedPreferences;

import java.util.HashMap;

/**
 * Created by yoon on 2017. 1. 17..
 */

public class Review {

    private int mId;
    private int mUserId;
    private int mProductId;
    private float mRatingValue;
    private String content;
    private HashMap<Integer, String> mImagePathMap;
    private ReviewSharedPreferences mPreferences;
    private LruCache<String, Bitmap> mMemoryCache;

    public Review() {
        mImagePathMap = new HashMap<>();
        mPreferences = new ReviewSharedPreferences();

        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        final int cacheSize = maxMemory / 8;
        mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
//                return super.sizeOf(key, value);
                return value.getByteCount() / 1024;
            }
        };
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public int getUserId() {
        return mUserId;
    }

    public void setUserId(int userId) {
        mUserId = userId;
    }

    public int getProductId() {
        return mProductId;
    }

    public void setProductId(int productId) {
        mProductId = productId;
    }

    public float getRatingValue() {
        return mRatingValue;
    }

    public void setRatingValue(float ratingValue) {
        mRatingValue = ratingValue;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public HashMap<Integer, String> getImagePathMap() {
        return mImagePathMap;
    }

    public void setImagePathMap(HashMap<Integer, String> imagePathMap) {
        mImagePathMap = imagePathMap;
    }

    public void setImagePathMap(Context context) {
        String image1Path = mPreferences.getStoredImage1Path(context);
        if (image1Path != null) {
            mImagePathMap.put(1, image1Path);
        }

        String image2Path = mPreferences.getStoredImage2Path(context);
        if (image2Path != null) {
            mImagePathMap.put(2, image2Path);
        }

        String image3Path = mPreferences.getStoredImage3Path(context);
        if (image3Path != null) {
            mImagePathMap.put(3, image3Path);
        }

        Log.d("getImage1Path", "image1Path : " + image1Path);
        Log.d("getImage2Path", "image2Path : " + image2Path);
        Log.d("getImage3Path", "image3Path : " + image3Path);
//        String[] imagePathArr = new String[]{image1Path, image2Path, image3Path};
//        for (int i = 1; i <= imagePathArr.length; i++) {
//            if (imagePathArr[i - 1] != null) {
//                mImagePathMap.put(i, imagePathArr[i-1]);
//                break;
//            }
//        }


    }

    public void putImagePath(Context context, int index, String imagePath) {
        switch (index) {
            case 1:
                mPreferences.setStoreImage1Path(context, imagePath);
                Log.d("putImage1Path", "image1Path : " + imagePath);
                break;
            case 2:
                mPreferences.setStoreImage2Path(context, imagePath);
                Log.d("putImage2Path", "image2Path : " + imagePath);
                break;
            case 3:
                mPreferences.setStoreImage3Path(context, imagePath);
                Log.d("putImage3Path", "image3Path : " + imagePath);
                break;
        }
    }

    public int getImagePathMapSize() {
        return mImagePathMap.size();
    }

    public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemoryCache(key) == null) {
            mMemoryCache.put(key, bitmap);
        }
    }

    public Bitmap getBitmapFromMemoryCache(String key) {
        return mMemoryCache.get(key);
    }

    public void removeAllBitmapToMemoryCache() {
        mMemoryCache.evictAll();
    }

    public Bitmap loadBitmap(String imagePath, ImageView imageView) {
        Bitmap bitmap = getBitmapFromMemoryCache(imagePath);
        if (bitmap != null) {
            return bitmap;
        } else {
//            imageView.setImageResource(R.drawable.image_placeholder);
            return null;
        }
    }

    public Bitmap[] loadImageViews(ImageView[] imageViewArr) {
        if (imageViewArr.length != getImagePathMapSize()) {
            Log.e("loadImageViews", "do not match the number of bitmaps");
            return null;
        }
        Bitmap[] bitmaps = new Bitmap[imageViewArr.length];
        switch (imageViewArr.length) {
            case 3:
                bitmaps[2] = loadBitmap(mImagePathMap.get(3), imageViewArr[2]);
            case 2:
                bitmaps[1] = loadBitmap(mImagePathMap.get(2), imageViewArr[1]);
            case 1:
                bitmaps[0] = loadBitmap(mImagePathMap.get(1), imageViewArr[0]);
                break;
        }
        return bitmaps;
    }
}
