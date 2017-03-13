package com.planet.wondering.chemi.model;

/**
 * Created by yoon on 2017. 2. 17..
 */

public class User {

    private int mId;
    private String mEmail;
    private String mName;
    private String mToken;
    private String mPushToken;
    private boolean mGender;
    private int mBirthYear;
    private boolean mHasDrySkin;
    private boolean mHasOilySkin;
    private boolean mHasAllergy;
    private boolean mHasChild;
    private boolean mChildHasDrySkin;
    private boolean mChildHasAllergy;


    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String email) {
        mEmail = email;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getToken() {
        return mToken;
    }

    public void setToken(String token) {
        mToken = token;
    }

    public String getPushToken() {
        return mPushToken;
    }

    public void setPushToken(String pushToken) {
        mPushToken = pushToken;
    }

    // female : 0; male : 1;
    public int getGender() {
        if (isGender()) {
            return 0;
        } else {
            return 1;
        }
    }

    public boolean isGender() {
        return mGender;
    }

    public void setGender(boolean gender) {
        mGender = gender;
    }

    public int getBirthYear() {
        return mBirthYear;
    }

    public void setBirthYear(int birthYear) {
        mBirthYear = birthYear;
    }

    public int getHasDrySkin() {
        if (isHasDrySkin()) {
            return 1;
        } else {
            return 0;
        }
    }

    public boolean isHasDrySkin() {
        return mHasDrySkin;
    }

    public void setHasDrySkin(boolean hasDrySkin) {
        mHasDrySkin = hasDrySkin;
    }

    public int getHasOilySkin() {
        if (isHasOilySkin()) {
            return 1;
        } else {
            return 0;
        }
    }

    public boolean isHasOilySkin() {
        return mHasOilySkin;
    }

    public void setHasOilySkin(boolean hasOilySkin) {
        mHasOilySkin = hasOilySkin;
    }

    public int getHasAllergy() {
        if (isHasAllergy()) {
            return 1;
        } else {
            return 0;
        }
    }

    public boolean isHasAllergy() {
        return mHasAllergy;
    }

    public void setHasAllergy(boolean hasAllergy) {
        mHasAllergy = hasAllergy;
    }

    public int getHasChild() {
        if (isHasChild()) {
            return 1;
        } else {
            return 0;
        }
    }

    public boolean isHasChild() {
        return mHasChild;
    }

    public void setHasChild(boolean hasChild) {
        mHasChild = hasChild;
    }

    public int getChildHasDrySkin() {
        if (isChildHasDrySkin()) {
            return 1;
        } else {
            return 0;
        }
    }

    public boolean isChildHasDrySkin() {
        return mChildHasDrySkin;
    }

    public void setChildHasDrySkin(boolean childHasDrySkin) {
        mChildHasDrySkin = childHasDrySkin;
    }

    public int getChildHasAllergy() {
        if (isChildHasAllergy()) {
            return 1;
        } else {
            return 0;
        }
    }

    public boolean isChildHasAllergy() {
        return mChildHasAllergy;
    }

    public void setChildHasAllergy(boolean childHasAllergy) {
        mChildHasAllergy = childHasAllergy;
    }

    //    @Override
//    public String toString() {
//        return "User{" +
//                "mId=" + mId +
//                ", mEmail='" + mEmail + '\'' +
//                ", mName='" + mName + '\'' +
//                ", mToken='" + mToken + '\'' +
//                ", mPushToken='" + mPushToken + '\'' +
//                '}';
//    }


    @Override
    public String toString() {
        return "User{" +
//                "mId=" + mId +
//                ", mEmail='" + mEmail + '\'' +
//                ", mName='" + mName + '\'' +
//                ", mToken='" + mToken + '\'' +
//                ", mPushToken='" + mPushToken + '\'' +
//                ", mGender=" + mGender +
//                ", mBirthYear=" + mBirthYear +
                ", mHasDrySkin=" + mHasDrySkin +
                ", mHasOilySkin=" + mHasOilySkin +
                ", mHasAllergy=" + mHasAllergy +
                ", mHasChild=" + mHasChild +
                ", mChildHasDrySkin=" + mChildHasDrySkin +
                ", mChildHasAllergy=" + mChildHasAllergy +
                '}';
    }
}
