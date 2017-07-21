package com.planet.wondering.chemi.model.product;

/**
 * Created by yoon on 2017. 7. 19..
 */

public class InfoChild {

    private String mTitle;
    private String mDescription;
    private boolean mLastChild;

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public boolean isLastChild() {
        return mLastChild;
    }

    public void setLastChild(boolean lastChild) {
        mLastChild = lastChild;
    }
}
