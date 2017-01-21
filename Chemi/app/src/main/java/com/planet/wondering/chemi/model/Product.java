package com.planet.wondering.chemi.model;

import java.util.ArrayList;

/**
 * Created by yoon on 2017. 1. 17..
 */

public class Product {

    private int mId;
    private byte mCategoryId;
    private String mName;
    private String mMaker;
    private String mBrand;
    private String mType;
    private String mPurpose;
    private String mImagePath;
    private float mRatingValue;
    private int mRatingCount;
    private boolean mArchive;
    private ArrayList<Chemical> mChemicals;

    public Product() {
        mChemicals = new ArrayList<>();
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public byte getCategoryId() {
        return mCategoryId;
    }

    public void setCategoryId(byte categoryId) {
        mCategoryId = categoryId;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getMaker() {
        return mMaker;
    }

    public void setMaker(String maker) {
        mMaker = maker;
    }

    public String getBrand() {
        return mBrand;
    }

    public void setBrand(String brand) {
        mBrand = brand;
    }

    public String getType() {
        return mType;
    }

    public void setType(String type) {
        mType = type;
    }

    public String getPurpose() {
        return mPurpose;
    }

    public void setPurpose(String purpose) {
        mPurpose = purpose;
    }

    public String getImagePath() {
        return mImagePath;
    }

    public void setImagePath(String imagePath) {
        mImagePath = imagePath;
    }

    public float getRatingValue() {
        return mRatingValue;
    }

    public void setRatingValue(float ratingValue) {
        mRatingValue = ratingValue;
//        mRatingValue = Math.round(ratingValue * 100) /100.f;
    }

    public int getRatingCount() {
        return mRatingCount;
    }

    public void setRatingCount(int ratingCount) {
        mRatingCount = ratingCount;
    }

    public boolean isArchive() {
        return mArchive;
    }

    public void setArchive(boolean archive) {
        mArchive = archive;
    }

    public ArrayList<Chemical> getChemicals() {
        return mChemicals;
    }

    public void setChemicals(ArrayList<Chemical> chemicals) {
        mChemicals = chemicals;
    }

    public int[] getNumberOfEachEWGRating() {
        int[] counts = new int[5];
        counts[0] = mChemicals.size();
        for (Chemical chemical : mChemicals) {
            switch (chemical.getMaxHazard()) {
                case 0:
                    counts[4]++;
                    break;
                case 1:case 2:
                    counts[1]++;
                    break;
                case 3:case 4:case 5:case 6:
                    counts[2]++;
                    break;
                case 7:case 8:case 9:case 10:
                    counts[3]++;
                    break;
            }
        }
        return counts;
    }

    public ArrayList<Chemical> getChemicalListOfEachEWGRating(int hexagonFilterIndex) {
        ArrayList<Chemical> chemicals = new ArrayList<>();
        switch (hexagonFilterIndex) {
            case 0:
                return mChemicals;
            case 1:
                for (Chemical chemical : mChemicals) {
                    if (chemical.getMaxHazard() == 1 || chemical.getMaxHazard() == 2) {
                        chemicals.add(chemical);
                    }
                }
                return chemicals;
            case 2:
                for (Chemical chemical : mChemicals) {
                    if (chemical.getMaxHazard() >= 3 && chemical.getMaxHazard() <= 6) {
                        chemicals.add(chemical);
                    }
                }
                return chemicals;
            case 3:
                for (Chemical chemical : mChemicals) {
                    if (chemical.getMaxHazard() >= 7 && chemical.getMaxHazard() <= 10) {
                        chemicals.add(chemical);
                    }
                }
                return chemicals;
            case 4:
                for (Chemical chemical : mChemicals) {
                    if (chemical.getMaxHazard() == 0) {
                        chemicals.add(chemical);
                    }
                }
                return chemicals;
        }
        return null;
    }
}
