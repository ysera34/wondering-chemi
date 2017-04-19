package com.planet.wondering.chemi.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by yoon on 2017. 1. 17..
 */

public class Review implements Serializable {

    private int mId;
    private int mUserId;
    private int mProductId;
    private String mProductImagePath;
    private String mProductBrand;
    private String mProductName;
    private User mUser;
    private boolean mAuthor;
    private float mRatingValue;
    private String content;
    private String mDate;
    private ArrayList<String> mImagePaths;

    public Review() {
        mUser = new User();
        mImagePaths = new ArrayList<>();
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

    public String getProductImagePath() {
        return mProductImagePath;
    }

    public void setProductImagePath(String productImagePath) {
        mProductImagePath = productImagePath;
    }

    public String getProductBrand() {
        return mProductBrand;
    }

    public void setProductBrand(String productBrand) {
        mProductBrand = productBrand;
    }

    public String getProductName() {
        return mProductName;
    }

    public void setProductName(String productName) {
        mProductName = productName;
    }

    public User getUser() {
        return mUser;
    }

    public void setUser(User user) {
        mUser = user;
    }

    public boolean isAuthor() {
        return mAuthor;
    }

    public void setAuthor(boolean author) {
        mAuthor = author;
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


    public String getDate() {
        return mDate;
    }

    public void setDate(String date) {
        mDate = date;
    }

    public ArrayList<String> getImagePaths() {
        return mImagePaths;
    }

    public void setImagePaths(ArrayList<String> imagePaths) {
        mImagePaths = imagePaths;
    }

    @Override
    public String toString() {
        return "Review{" +
                "mId=" + mId +
                ", mUserId=" + mUserId +
                ", mProductId=" + mProductId + "\n" +
                ", mUser=" + mUser.toString() + "\n" +
                ", mRatingValue=" + mRatingValue + "\n" +
                ", content='" + content + '\'' + "\n" +
                ", mDate='" + mDate + '\'' + "\n" +
                ", mImagePaths=" + mImagePaths +
                '}';
    }
}
