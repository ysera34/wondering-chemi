package com.planet.wondering.chemi.model.home;

/**
 * Created by yoon on 2017. 6. 28..
 */

public class BestReview {

    private int mId;
    private float mRatingValue;
    private String mReviewContent;
    private String mParentText;
    private String mChildText;

    private StringBuilder mParentTextBuilder;
    private StringBuilder mChildTextBuilder;

    private String mProductBrand;
    private String mProductName;
    private String mProductImagePath;

    public BestReview() {
        mParentTextBuilder = new StringBuilder();
        mChildTextBuilder = new StringBuilder();
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public float getRatingValue() {
        return mRatingValue;
    }

    public void setRatingValue(float ratingValue) {
        mRatingValue = ratingValue;
    }

    public String getReviewContent() {
        return mReviewContent;
    }

    public void setReviewContent(String reviewContent) {
        mReviewContent = reviewContent;
    }

    public String getParentText() {
        return mParentText;
    }

    public void setParentText(String parentText) {
        mParentText = parentText;
    }

    public String getChildText() {
        return mChildText;
    }

    public void setChildText(String childText) {
        mChildText = childText;
    }

    public String getProductBrand() {
        return mProductBrand;
    }

    public void setProductBrand(String productBrand) {
        mProductBrand = productBrand;
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

    public void addParentTextBuilder(String addText, boolean isDivide) {
        if (isDivide) {
            mParentTextBuilder.append(" / ");
        }
        mParentTextBuilder.append(addText);
    }

    public String getParentTextBuilder() {
        return mParentTextBuilder.toString();
    }

    public void addChildTextBuilder(String addText, boolean isDivide) {
        if (isDivide) {
            mChildTextBuilder.append(" / ");
        }
        mChildTextBuilder.append(addText);
    }

    public String getChildTextBuilder() {
        return mChildTextBuilder.toString();
    }

}
