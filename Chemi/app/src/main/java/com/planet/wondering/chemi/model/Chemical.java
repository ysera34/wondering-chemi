package com.planet.wondering.chemi.model;

import com.planet.wondering.chemi.R;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by yoon on 2017. 1. 17..
 */

public class Chemical implements Serializable {

    private int mId;
    private String mNameKo;
    private String mNameEn;
    private String mPurpose;
    private byte mMinHazard;
    private byte mMaxHazard;
    private boolean mAllergy;
    private String mAllergyDescription;
    private ArrayList<Hazard> mHazards;

    public Chemical() {
        mHazards = new ArrayList<>();
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public String getNameKo() {
        return mNameKo;
    }

    public void setNameKo(String nameKo) {
        mNameKo = nameKo;
    }

    public String getNameEn() {
        return mNameEn;
    }

    public void setNameEn(String nameEn) {
        mNameEn = nameEn;
    }

    public String getPurpose() {
        return mPurpose;
    }

    public void setPurpose(String purpose) {
        mPurpose = purpose;
    }

    public byte getMinHazard() {
        return mMinHazard;
    }

    public void setMinHazard(byte minHazard) {
        mMinHazard = minHazard;
    }

    public byte getMaxHazard() {
        return mMaxHazard;
    }

    public void setMaxHazard(byte maxHazard) {
        mMaxHazard = maxHazard;
    }

    public boolean isAllergy() {
        return mAllergy;
    }

    public void setAllergy(boolean allergy) {
        mAllergy = allergy;
    }

    public String getAllergyDescription() {
        return mAllergyDescription;
    }

    public void setAllergyDescription(String allergyDescription) {
        mAllergyDescription = allergyDescription;
    }

    public ArrayList<Hazard> getHazards() {
        return mHazards;
    }

    public void setHazards(ArrayList<Hazard> hazards) {
        mHazards = hazards;
    }

    // to array [2] and to setter

    public int getHazardIconResId() {
        int hazardIconResId = 0;
        switch (getMaxHazard()) {
            case 0:
                hazardIconResId = R.drawable.widget_solid_circle_hazard1;
                break;
            case 1:case 2:
                hazardIconResId = R.drawable.widget_solid_circle_hazard2;
                break;
            case 3:case 4:case 5:case 6:
                hazardIconResId = R.drawable.widget_solid_circle_hazard3;
                break;
            case 7:case 8:case 9:case 10:
                hazardIconResId = R.drawable.widget_solid_circle_hazard4;
                break;
        }
        return hazardIconResId;
    }

    public int getHazardLineResId() {
        int hazardLineResId = 0;
        switch (getMaxHazard()) {
            case 0:
                hazardLineResId = R.drawable.widget_line_hazard1;
                break;
            case 1:case 2:
                hazardLineResId = R.drawable.widget_line_hazard2;
                break;
            case 3:case 4:case 5:case 6:
                hazardLineResId = R.drawable.widget_line_hazard3;
                break;
            case 7:case 8:case 9:case 10:
                hazardLineResId = R.drawable.widget_line_hazard4;
                break;
        }
        return hazardLineResId;
    }

    public int getHazardColorResId() {
        int hazardColor;
        switch (getMaxHazard()) {
            case 0:
                hazardColor = R.color.hazard1;
                break;
            case 1:case 2:
                hazardColor = R.color.hazard2;
                break;
            case 3:case 4:case 5:case 6:
                hazardColor = R.color.hazard3;
                break;
            case 7:case 8:case 9:case 10:
                hazardColor = R.color.hazard4;
                break;
            default:
                hazardColor = R.color.hazard0;
        }
        return hazardColor;
    }

    public String getHazardValueString() {
        if (getMaxHazard() == 0) {
            return "-";
        }
        StringBuilder hazardLabelBuilder = new StringBuilder(String.valueOf(getMaxHazard()));
        if (getMinHazard() != 0) {
            hazardLabelBuilder.insert(0, String.valueOf(getMinHazard()) + "~");
        }
        return hazardLabelBuilder.toString();
    }

    @Override
    public String toString() {
        return "Chemical{" +
                "mId=" + mId +
                ", mNameKo='" + mNameKo + '\'' +
                ", mNameEn='" + mNameEn + '\'' +
                ", mPurpose='" + mPurpose + '\'' +
                ", mMinHazard=" + mMinHazard +
                ", mMaxHazard=" + mMaxHazard +
                ", mAllergy=" + mAllergy +
                ", mHazards=" + mHazards +
                '}';
    }
}

