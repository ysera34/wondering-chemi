package com.planet.wondering.chemi.model.home;

/**
 * Created by yoon on 2017. 6. 27..
 */

public class BannerContent {

    private int mId;
    private String mTitle;
    private String mSubTitle;
    private int mCategoryId;
    private String mImagePath;

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getSubTitle() {
        return mSubTitle;
    }

    public void setSubTitle(String subTitle) {
        mSubTitle = subTitle;
    }

    public int getCategoryId() {
        return mCategoryId;
    }

    public void setCategoryId(int categoryId) {
        mCategoryId = categoryId;
    }

    public String getImagePath() {
        return mImagePath;
    }

    public void setImagePath(String imagePath) {
        mImagePath = imagePath;
    }

    @Override
    public String toString() {
        return "BannerContent{" +
                "mId=" + mId +
                ", mTitle='" + mTitle + '\'' +
                ", mSubTitle='" + mSubTitle + '\'' +
                ", mCategoryId=" + mCategoryId +
                ", mImagePath='" + mImagePath + '\'' +
                '}';
    }
}
