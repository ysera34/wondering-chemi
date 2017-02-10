package com.planet.wondering.chemi.model.archive;

/**
 * Created by yoon on 2017. 2. 10..
 */

public class Content {

    private int mContentId;
    private String mTitle;
    private String mImagePath;

    public int getContentId() {
        return mContentId;
    }

    public void setContentId(int contentId) {
        mContentId = contentId;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getImagePath() {
        return mImagePath;
    }

    public void setImagePath(String imagePath) {
        mImagePath = imagePath;
    }
}
