package com.planet.wondering.chemi.model.category;

import java.util.ArrayList;

/**
 * Created by yoon on 2017. 7. 5..
 */

public class CategoryPiece {

    private int mNumber;
    private int mNameResId;
    private String mName;
    private boolean mHasClickListener;
    private boolean mHasExpanded;
    private ArrayList<CategoryPiece> mCategoryPieces;

    public CategoryPiece(String name, int number) {
        mName = name;
        mNumber = number;
        mHasClickListener = true;
    }

    public CategoryPiece(String name) {
        mName = name;
        mHasClickListener = false;
    }

    public CategoryPiece(ArrayList<CategoryPiece> categoryPieces) {
        mHasExpanded = true;
        mCategoryPieces = categoryPieces;
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

    public boolean isHasClickListener() {
        return mHasClickListener;
    }

    public void setHasClickListener(boolean hasClickListener) {
        mHasClickListener = hasClickListener;
    }

    public boolean isHasExpanded() {
        return mHasExpanded;
    }

    public void setHasExpanded(boolean hasExpanded) {
        mHasExpanded = hasExpanded;
    }

    public ArrayList<CategoryPiece> getCategoryPieces() {
        return mCategoryPieces;
    }

    public void setCategoryPieces(ArrayList<CategoryPiece> categoryPieces) {
        mCategoryPieces = categoryPieces;
    }
}
