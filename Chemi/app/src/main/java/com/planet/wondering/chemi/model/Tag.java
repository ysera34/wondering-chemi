package com.planet.wondering.chemi.model;

import com.planet.wondering.chemi.R;

import java.util.Date;

/**
 * Created by yoon on 2017. 1. 26..
 */

public class Tag {

    private int mId;
    private String mName;
    private int mRank;
    private int mVariation;
    private Date mRankDate;

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public int getRank() {
        return mRank;
    }

    public void setRank(int rank) {
        mRank = rank;
    }

    public int getStateImageResId() {
        if (getVariation() > 0) {
            return R.drawable.ic_arrow_upward_24dp;
        } else if (getVariation() < 0){
            return R.drawable.ic_arrow_downward_24dp;
        } else {
            return R.drawable.ic_search_primary_24dp;
        }
    }

    public int getVariation() {
        return mVariation;
    }

    public void setVariation(int variation) {
        mVariation = variation;
    }

    public Date getRankDate() {
        return mRankDate;
    }

    public void setRankDate(Date rankDate) {
        mRankDate = rankDate;
    }
}
