package com.planet.wondering.chemi.model.config;

import com.bignerdranch.expandablerecyclerview.model.Parent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yoon on 2017. 4. 24..
 */

public class FAQ implements Parent<FAQBody> {

    private int mId;
    private String mQuestion;
    private String mCreateDate;
    private String mModifyDate;

    private ArrayList<FAQBody> mFAQBodies;

    public FAQ() {
        mFAQBodies = new ArrayList<>();
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public String getQuestion() {
        return mQuestion;
    }

    public void setQuestion(String question) {
        mQuestion = question;
    }

    public String getCreateDate() {
        return mCreateDate;
    }

    public void setCreateDate(String createDate) {
        mCreateDate = createDate;
    }

    public String getModifyDate() {
        return mModifyDate;
    }

    public void setModifyDate(String modifyDate) {
        mModifyDate = modifyDate;
    }

    public void setFAQBodies(ArrayList<FAQBody> FAQBodies) {
        mFAQBodies = FAQBodies;
    }

    public FAQBody getFAQBody(int position) {
        return mFAQBodies.get(position);
    }

    @Override
    public List<FAQBody> getChildList() {
        return mFAQBodies;
    }

    @Override
    public boolean isInitiallyExpanded() {
        return false;
    }
}
