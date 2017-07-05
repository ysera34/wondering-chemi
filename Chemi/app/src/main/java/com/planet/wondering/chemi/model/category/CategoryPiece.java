package com.planet.wondering.chemi.model.category;

import java.util.ArrayList;

/**
 * Created by yoon on 2017. 7. 5..
 */

public class CategoryPiece {

    private int mNumber;
    private int mNameResId;
    private String mName;
    private boolean mHasExpanded;
    private ArrayList<CategoryPiece> mPieces;

    public CategoryPiece(String name, int number) {
        mName = name;
        mNumber = number;
    }

    public CategoryPiece(int number, int nameResId, boolean hasExpanded) {
        mNumber = number;
        mNameResId = nameResId;
        mHasExpanded = hasExpanded;
        if (mHasExpanded) {
            mPieces = new ArrayList<>();
        }
    }

    public int getNumber() {
        return mNumber;
    }

    public void setNumber(int number) {
        mNumber = number;
    }

    public int getNameResId() {
        return mNameResId;
    }

    public void setNameResId(int nameResId) {
        mNameResId = nameResId;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public boolean isHasExpanded() {
        return mHasExpanded;
    }

    public void setHasExpanded(boolean hasExpanded) {
        mHasExpanded = hasExpanded;
    }

    public ArrayList<CategoryPiece> getPieces() {
        return mPieces;
    }

    public void setPieces(ArrayList<CategoryPiece> pieces) {
        mPieces = pieces;
    }
}
