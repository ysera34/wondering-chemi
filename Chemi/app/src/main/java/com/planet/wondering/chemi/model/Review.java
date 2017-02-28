package com.planet.wondering.chemi.model;

import java.util.ArrayList;

/**
 * Created by yoon on 2017. 1. 17..
 */

public class Review {

    private int mId;
    private int mUserId;
    private int mProductId;
    private float mRatingValue;
    private String content;
    private ArrayList<String> mImagePaths;

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

    public ArrayList<String> getImagePaths() {
        return mImagePaths;
    }

    public void setImagePaths(ArrayList<String> imagePaths) {
        mImagePaths = imagePaths;
    }
}
