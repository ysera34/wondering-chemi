package com.planet.wondering.chemi.model;

import com.planet.wondering.chemi.R;

import java.util.Date;

/**
 * Created by yoon on 2017. 1. 5..
 */
public class SearchWord {

    private int mRatingNumber;
    private String mSearchWord;
    private boolean mVariationState;
    private int mStateImageResId;
    private int mVariationValue;
    private Date mDate;

    public SearchWord() {
        mDate = new Date();
    }

    public int getRatingNumber() {
        return mRatingNumber;
    }

    public void setRatingNumber(int ratingNumber) {
        mRatingNumber = ratingNumber;
    }

    public String getSearchWord() {
        return mSearchWord;
    }

    public void setSearchWord(String searchWord) {
        mSearchWord = searchWord;
    }

    public boolean isVariationState() {
        return mVariationState;
    }

    public void setVariationState(boolean variationState) {
        mVariationState = variationState;
    }

    public int getStateImageResId() {
        if (isVariationState()) {
            mStateImageResId = R.drawable.ic_arrow_upward_24dp;
        } else {
            mStateImageResId = R.drawable.ic_arrow_downward_24dp;
        }
        return mStateImageResId;
    }

    public void setStateImageResId(int stateImageResId) {
        mStateImageResId = stateImageResId;
    }

    public int getVariationValue() {
        return mVariationValue;
    }

    public void setVariationValue(int variationValue) {
        mVariationValue = variationValue;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }
}
