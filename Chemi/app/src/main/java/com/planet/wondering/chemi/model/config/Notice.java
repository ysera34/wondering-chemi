package com.planet.wondering.chemi.model.config;

import com.bignerdranch.expandablerecyclerview.model.Parent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yoon on 2017. 4. 22..
 */

public class Notice implements Parent<NoticeBody> {

    private int mId;
    private String mTitle;
    private String mCreateDate;
    private String mModifyDate;

    private ArrayList<NoticeBody> mNoticeBodies;

    public Notice() {
        mNoticeBodies = new ArrayList<>();
    }

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

    @Override
    public List<NoticeBody> getChildList() {
        return mNoticeBodies;
    }

    @Override
    public boolean isInitiallyExpanded() {
        return false;
    }
}
