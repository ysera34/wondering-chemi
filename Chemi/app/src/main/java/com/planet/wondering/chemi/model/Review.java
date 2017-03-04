package com.planet.wondering.chemi.model;

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

    public Review() {
        mImagePathMap = new HashMap<>();
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
}
