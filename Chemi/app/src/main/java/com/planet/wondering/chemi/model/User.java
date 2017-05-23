package com.planet.wondering.chemi.model;

import com.planet.wondering.chemi.model.archive.Product;
import com.planet.wondering.chemi.model.archive.Content;
import com.planet.wondering.chemi.model.archive.ReviewProduct;

import java.io.Serializable;
import java.util.ArrayList;


/**
 * Created by yoon on 2017. 2. 17..
 */

public class User implements Serializable {

    private int mId;
    private String mEmail;
    private String mName;
    private String mToken;
    private String mIdToken;
    private String mPushToken;
    private byte mPlatformId;
    private String mImagePath;
    private boolean mHasExtraInfo;
    private boolean mGender;
    private int mBirthYear;
    private String mAge;
    private boolean mHasDrySkin;
    private boolean mHasOilySkin;
    private boolean mHasAllergy;
    private boolean mHasChild;
    private boolean mChildHasDrySkin;
    private boolean mChildHasAllergy;

    private String mCreateDate;
    private String mModifyDate;

    private ArrayList<Product> mArchiveProducts;
    private ArrayList<Content> mArchiveContents;
    private ArrayList<ReviewProduct> mReviewProducts;


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

    public String getIdToken() {
        return mIdToken;
    }

    public void setIdToken(String idToken) {
        mIdToken = idToken;
    }

    public String getPushToken() {
        return mPushToken;
    }

    public void setPushToken(String pushToken) {
        mPushToken = pushToken;
    }

    public byte getPlatformId() {
        return mPlatformId;
    }

    public void setPlatformId(byte platformId) {
        mPlatformId = platformId;
    }

    public String getImagePath() {
        return mImagePath;
    }

    public void setImagePath(String imagePath) {
        mImagePath = imagePath;
    }

    // female : 0; male : 1;
    public int getGender() {
        if (isGender()) {
            return 0;
        } else {
            return 1;
        }
    }

    public boolean isHasExtraInfo() {
        return mHasExtraInfo;
    }

    public void setHasExtraInfo(boolean hasExtraInfo) {
        mHasExtraInfo = hasExtraInfo;
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

    public String getAge() {
        return mAge;
    }

    public void setAge(String age) {
        mAge = age;
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


    public ArrayList<Product> getArchiveProducts() {
        return mArchiveProducts;
    }

    public void setArchiveProducts(ArrayList<Product> archiveProducts) {
        mArchiveProducts = archiveProducts;
    }

    public ArrayList<Content> getArchiveContents() {
        return mArchiveContents;
    }

    public void setArchiveContents(ArrayList<Content> archiveContents) {
        mArchiveContents = archiveContents;
    }

    public ArrayList<ReviewProduct> getReviewProducts() {
        return mReviewProducts;
    }

    public void setReviewProducts(ArrayList<ReviewProduct> reviewProducts) {
        mReviewProducts = reviewProducts;
    }

    public String getCreateDate() {
        return mCreateDate;
    }

    public void setCreateDate(String createDate) {
        mCreateDate = createDate;
    }

    public String getModifyDate() {
        return mModifyDate;
    }

    public void setModifyDate(String modifyDate) {
        mModifyDate = modifyDate;
    }

    @Override
    public String toString() {
        return "User{" +
                "mId=" + mId +
                ", mEmail='" + mEmail + '\'' +
                ", mName='" + mName + '\'' +
                ", mToken='" + mToken + '\'' +
                ", mPushToken='" + mPushToken + '\'' + '\n' +
                ", mImagePath='" + mImagePath + '\'' +
                ", mHasExtraInfo=" + mHasExtraInfo + '\n' +
                ", mGender=" + mGender +
                ", mBirthYear=" + mBirthYear +
                ", mAge='" + mAge + '\'' +
                ", mHasDrySkin=" + mHasDrySkin +
                ", mHasOilySkin=" + mHasOilySkin +
                ", mHasAllergy=" + mHasAllergy +
                ", mHasChild=" + mHasChild +
                ", mChildHasDrySkin=" + mChildHasDrySkin +
                ", mChildHasAllergy=" + mChildHasAllergy + '\n' +
                ", mCreateDate='" + mCreateDate + '\'' +
                ", mModifyDate='" + mModifyDate + '\'' + '\n' +
                ", mArchiveProducts=" + mArchiveProducts + '\n' +
                ", mArchiveContents=" + mArchiveContents + '\n' +
                ", mReviewProducts=" + mReviewProducts + '\n' +
                '}';
    }
}
