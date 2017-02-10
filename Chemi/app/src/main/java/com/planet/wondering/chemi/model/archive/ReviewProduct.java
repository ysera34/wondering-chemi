package com.planet.wondering.chemi.model.archive;

import java.util.Date;

/**
 * Created by yoon on 2017. 2. 10..
 */

public class ReviewProduct {

    private int mProductId;
    private String mProductName;
    private float mRatingValue;
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
}
