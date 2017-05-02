package com.planet.wondering.chemi.model.config;

import java.util.ArrayList;

/**
 * Created by yoon on 2017. 4. 22..
 */

public class NoticeBody {

    private String mDescription;
    private ArrayList<String> mImagePaths;

    public NoticeBody() {
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public ArrayList<String> getImagePaths() {
        return mImagePaths;
    }

    public void setImagePaths(ArrayList<String> imagePaths) {
        mImagePaths = imagePaths;
    }
}
