package com.planet.wondering.chemi.model.config;

import java.util.ArrayList;

/**
 * Created by yoon on 2017. 4. 24..
 */

public class FAQBody {

    private String mAnswer;
    private ArrayList<String> mImagePaths;

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
}
