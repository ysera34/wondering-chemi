package com.planet.wondering.chemi.model.product;

import com.bignerdranch.expandablerecyclerview.model.Parent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yoon on 2017. 7. 19..
 */

public class InfoParent implements Parent<InfoChild> {

    private int mId;
    private String mTitle;
    private ArrayList<InfoChild> mInfoChildren;

    public InfoParent() {
        mInfoChildren = new ArrayList<>();
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

    public ArrayList<InfoChild> getInfoChildren() {
        return mInfoChildren;
    }

    public void setInfoChildren(ArrayList<InfoChild> infoChildren) {
        mInfoChildren = infoChildren;
    }

    @Override
    public List<InfoChild> getChildList() {
        return mInfoChildren;
    }

    @Override
    public boolean isInitiallyExpanded() {
        return false;
    }
}
