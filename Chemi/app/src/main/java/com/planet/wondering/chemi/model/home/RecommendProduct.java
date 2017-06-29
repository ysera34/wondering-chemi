package com.planet.wondering.chemi.model.home;

/**
 * Created by yoon on 2017. 6. 26..
 */

public class RecommendProduct {

    private int mId;
    private String mImagePath;
    private String mBrand;
    private String mName;
    private String mDescription;

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public String getImagePath() {
        return mImagePath;
    }

    public void setImagePath(String imagePath) {
        mImagePath = imagePath;
    }

    public String getBrand() {
        return mBrand;
    }

    public void setBrand(String brand) {
        mBrand = brand;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    @Override
    public String toString() {
        return "RecommendProduct{" +
                "mId=" + mId +
                ", mImagePath='" + mImagePath + '\'' +
                ", mBrand='" + mBrand + '\'' +
                ", mName='" + mName + '\'' +
                ", mDescription='" + mDescription + '\'' +
                '}';
    }
}
