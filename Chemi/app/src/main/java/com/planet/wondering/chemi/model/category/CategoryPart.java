package com.planet.wondering.chemi.model.category;

/**
 * Created by yoon on 2017. 7. 5..
 */

public class CategoryPart {

    private int mNameResId;
    private String mName;
    private int mImageResId;

    public CategoryPart(String name, int imageResId) {
        mName = name;
        mImageResId = imageResId;
    }

    public CategoryPart(int nameResId, int imageResId) {
        mNameResId = nameResId;
        mImageResId = imageResId;
    }

    public int getNameResId() {
        return mNameResId;
    }

    public void setNameResId(int nameResId) {
        mNameResId = nameResId;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public int getImageResId() {
        return mImageResId;
    }

    public void setImageResId(int imageResId) {
        mImageResId = imageResId;
    }
}
