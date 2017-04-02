package com.planet.wondering.chemi.model.archive;

import java.io.Serializable;

/**
 * Created by yoon on 2017. 2. 9..
 */

public class Product implements Serializable {

    private int mProductId;
    private String mBrand;
    private String mName;
    private String mImagePath;
    private String mKeepDate;

    public int getProductId() {
        return mProductId;
    }

    public void setProductId(int productId) {
        mProductId = productId;
    }

    public String getBrand() {
        return mBrand;
    }

    public void setBrand(String brand) {
        mBrand = brand;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getImagePath() {
        return mImagePath;
    }

    public void setImagePath(String imagePath) {
        mImagePath = imagePath;
    }

    public String getKeepDate() {
        return mKeepDate;
    }

    public void setKeepDate(String keepDate) {
        mKeepDate = keepDate;
    }

    @Override
    public String toString() {
        return "Product{" +
                "mProductId=" + mProductId +
                ", mBrand='" + mBrand + '\'' +
                ", mName='" + mName + '\'' +
                ", mImagePath='" + mImagePath + '\'' +
                ", mKeepDate='" + mKeepDate + '\'' +
                '}';
    }
}
