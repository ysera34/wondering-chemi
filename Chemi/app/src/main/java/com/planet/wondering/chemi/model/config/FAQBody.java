package com.planet.wondering.chemi.model.config;

import java.util.ArrayList;

/**
 * Created by yoon on 2017. 4. 24..
 */

public class FAQBody {

    private String mAnswer;
    private ArrayList<String> mImagePaths;
//    private String mUpdateDate;
    private boolean mIsText;
    private String mImagePath;
    private boolean mIsFirstImage;

    public FAQBody() {
    }

    public String getAnswer() {
        return mAnswer;
    }

    public void setAnswer(String answer) {
        mAnswer = answer;
    }

    public ArrayList<String> getImagePaths() {
        return mImagePaths;
    }

    public void setImagePaths(ArrayList<String> imagePaths) {
        mImagePaths = imagePaths;
    }

//    public String getUpdateDate() {
//        return mUpdateDate;
//    }
//
//    public void setUpdateDate(String updateDate) {
//        mUpdateDate = updateDate;
//    }

    public boolean isText() {
        return mIsText;
    }

    public void setText(boolean text) {
        mIsText = text;
    }

    public String getImagePath() {
        return mImagePath;
    }

    public void setImagePath(String imagePath) {
        mImagePath = imagePath;
    }

    public boolean isFirstImage() {
        return mIsFirstImage;
    }

    public void setFirstImage(boolean firstImage) {
        mIsFirstImage = firstImage;
    }
}
