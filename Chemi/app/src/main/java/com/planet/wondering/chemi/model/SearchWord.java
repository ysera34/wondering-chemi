package com.planet.wondering.chemi.model;

import com.planet.wondering.chemi.R;

import java.util.Date;

/**
 * Created by yoon on 2017. 1. 5..
 */
public class SearchWord {

    private int mRatingNumber;
    private String mSearchWord;
    private int mVariationValue;
    private int mSearchWordIndex;
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

    public int getStateImageResId() {
        int stateImageResId = 0;
        if (mVariationValue > 0) {
            stateImageResId = R.drawable.ic_arrow_upward_24dp;
        } else if (mVariationValue < 0){
            stateImageResId = R.drawable.ic_arrow_downward_24dp;
        } else if (mVariationValue == 0) {
//            stateImageResId
        }
        return stateImageResId;
    }

    public int getVariationValue() {
        if (mVariationValue < 0) {
            return mVariationValue * (-1);
        }
        return mVariationValue;
    }

    public void setVariationValue(int variationValue) {
        mVariationValue = variationValue;
    }

    public int getSearchWordIndex() {
        return mSearchWordIndex;
    }

    public void setSearchWordIndex(int searchWordIndex) {
        mSearchWordIndex = searchWordIndex;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }
}
