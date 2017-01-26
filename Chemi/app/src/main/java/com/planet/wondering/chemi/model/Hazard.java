package com.planet.wondering.chemi.model;

/**
 * Created by yoon on 2017. 1. 26..
 */

public class Hazard {

    private int mId;
    private String mSource;
    private String mCode;
    private String mName;
    private String mDescription;
    private byte mType;
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

    public boolean isAllergy() {
        return mAllergy;
    }

    public void setAllergy(boolean allergy) {
        mAllergy = allergy;
    }
}
