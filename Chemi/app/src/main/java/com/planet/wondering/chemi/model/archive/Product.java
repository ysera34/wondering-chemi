package com.planet.wondering.chemi.model.archive;

/**
 * Created by yoon on 2017. 2. 9..
 */

public class Product {

    private int mProductId;
    private String mBrand;
    private String mName;
    private String mImagePath;

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
}
