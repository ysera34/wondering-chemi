package com.planet.wondering.chemi.model;

import com.planet.wondering.chemi.R;

import java.io.Serializable;

/**
 * Created by yoon on 2017. 1. 26..
 */

public class Hazard implements Serializable {

    private int mId;
    private String mSource;
    private String mCode;
    private String mName;
    private String mDescription;
    private byte mType;
    private int mIconResId;
    private boolean mAllergy;

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public String getSource() {
        return mSource;
    }

    public void setSource(String source) {
        mSource = source;
    }

    public String getCode() {
        return mCode;
    }

    public void setCode(String code) {
        mCode = code;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public byte getType() {
        return mType;
    }

    public void setType(byte type) {
        mType = type;
    }

    public int getIconResId() {
        return mIconResId;
    }

    public void setIconResId(byte type) {
        switch (type) {
            case 1:
                mIconResId = R.drawable.ic_hazard_1;
                break;
            case 2:
                mIconResId = R.drawable.ic_hazard_2;
                break;
            case 3:
                mIconResId = R.drawable.ic_hazard_3;
                break;
            case 4:
                mIconResId = R.drawable.ic_hazard_4;
                break;
            case 5:
                mIconResId = R.drawable.ic_hazard_5;
                break;
            case 6:
                mIconResId = R.drawable.ic_hazard_6;
                break;
            case 7:
                mIconResId = R.drawable.ic_hazard_7;
                break;
            case 8:
                mIconResId = R.drawable.ic_hazard_8;
                break;
            case 9:
                mIconResId = R.drawable.ic_hazard_9;
                break;
            case 10:
                mIconResId = R.drawable.ic_chemical_allergy_true;
                break;
            case 11:
                mIconResId = R.drawable.ic_hazard_11;
                break;
        }
    }

    @Override
    public String toString() {
        return "Hazard{" +
                "mId=" + mId +
                ", mSource='" + mSource + '\'' +
                ", mCode='" + mCode + '\'' +
                ", mName='" + mName + '\'' +
                ", mDescription='" + mDescription + '\'' +
                ", mType=" + mType +
                ", mIconResId=" + mIconResId +
                '}';
    }
}
