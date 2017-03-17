package com.planet.wondering.chemi.model;

/**
 * Created by yoon on 2017. 3. 17..
 */

public class CTag {

    private int mId;
    private int mChemicalId;
    private String mDescription;
    private boolean isCorrect;

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public int getChemicalId() {
        return mChemicalId;
    }

    public void setChemicalId(int chemicalId) {
        mChemicalId = chemicalId;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public boolean isCorrect() {
        return isCorrect;
    }

    public void setCorrect(boolean correct) {
        isCorrect = correct;
    }

    @Override
    public String toString() {
        return "CTag{" +
                "mId=" + mId +
                ", mChemicalId=" + mChemicalId +
                ", mDescription='" + mDescription + '\'' +
                ", isCorrect=" + isCorrect +
                '}';
    }
}
