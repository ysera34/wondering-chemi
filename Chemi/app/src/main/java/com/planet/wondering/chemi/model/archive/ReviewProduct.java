package com.planet.wondering.chemi.model.archive;

import java.util.Date;

/**
 * Created by yoon on 2017. 2. 10..
 */

public class ReviewProduct {

    private int mProductId;
    private String mProductName;
    private String mProductImagePath;
    private float mRatingValue;
    private String mCreateDate;
    private Date mWriteDate;

    public int getProductId() {
        return mProductId;
    }

    public void setProductId(int productId) {
        mProductId = productId;
    }

    public String getProductName() {
        return mProductName;
    }

    public void setProductName(String productName) {
        mProductName = productName;
    }

    public String getProductImagePath() {
        return mProductImagePath;
    }

    public void setProductImagePath(String productImagePath) {
        mProductImagePath = productImagePath;
    }

    public float getRatingValue() {
        return mRatingValue;
    }

    public void setRatingValue(float ratingValue) {
        mRatingValue = ratingValue;
    }

    public Date getWriteDate() {
        return mWriteDate;
    }

    public void setWriteDate(Date writeDate) {
        mWriteDate = writeDate;
    }

    public String getCreateDate() {
        return mCreateDate;
    }

    public void setCreateDate(String createDate) {
        mCreateDate = createDate;
    }
}
