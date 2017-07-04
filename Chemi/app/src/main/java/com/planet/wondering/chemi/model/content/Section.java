package com.planet.wondering.chemi.model.content;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by yoon on 2017. 7. 4..
 */

public class Section implements Serializable {

    private int mId;
    private int mNumbered;
    private int mViewType;
    private String mImagePath;
    private ArrayList<Text> mTexts;
    private ArrayList<Reference> mReferences;

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public int getNumbered() {
        return mNumbered;
    }

    public void setNumbered(int numbered) {
        mNumbered = numbered;
    }

    public int getViewType() {
        return mViewType;
    }

    public void setViewType(int viewType) {
        mViewType = viewType;
    }

    public String getImagePath() {
        return mImagePath;
    }

    public void setImagePath(String imagePath) {
        mImagePath = imagePath;
    }

    public ArrayList<Text> getTexts() {
        return mTexts;
    }

    public void setTexts(ArrayList<Text> texts) {
        mTexts = texts;
    }

    public ArrayList<Reference> getReferences() {
        return mReferences;
    }

    public void setReferences(ArrayList<Reference> references) {
        mReferences = references;
    }
}