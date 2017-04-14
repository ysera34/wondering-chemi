package com.planet.wondering.chemi.model;

import java.util.ArrayList;

/**
 * Created by yoon on 2017. 3. 28..
 */

public class Content {

    private int mId;
    private int mCategoryId;
    private String mTitle;
    private String mSubTitle;
    private String mImagePath;
    private String mThumbnailImagePath;
    private ArrayList<String> mContentImagePaths;
    private int mLikeCount;
    private int mViewCount;
    private int mCommentCount;
    private boolean mLike;
    private boolean mArchive;

    public Content() {
        mContentImagePaths = new ArrayList<>();
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public int getCategoryId() {
        return mCategoryId;
    }

    public void setCategoryId(int categoryId) {
        mCategoryId = categoryId;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getSubTitle() {
        return mSubTitle;
    }

    public void setSubTitle(String subTitle) {
        mSubTitle = subTitle;
    }

    public String getImagePath() {
        return mImagePath;
    }

    public void setImagePath(String imagePath) {
        mImagePath = imagePath;
    }

    public String getThumbnailImagePath() {
        return mThumbnailImagePath;
    }

    public void setThumbnailImagePath(String thumbnailImagePath) {
        mThumbnailImagePath = thumbnailImagePath;
    }

    public ArrayList<String> getContentImagePaths() {
        return mContentImagePaths;
    }

    public void setContentImagePaths(ArrayList<String> contentImagePaths) {
        mContentImagePaths = contentImagePaths;
    }

    public int getLikeCount() {
        return mLikeCount;
    }

    public void setLikeCount(int likeCount) {
        mLikeCount = likeCount;
    }

    public int getViewCount() {
        return mViewCount;
    }

    public void setViewCount(int viewCount) {
        mViewCount = viewCount;
    }

    public int getCommentCount() {
        return mCommentCount;
    }

    public void setCommentCount(int commentCount) {
        mCommentCount = commentCount;
    }

    public boolean isLike() {
        return mLike;
    }

    public void setLike(boolean like) {
        mLike = like;
    }

    public boolean isArchive() {
        return mArchive;
    }

    public void setArchive(boolean archive) {
        mArchive = archive;
    }
}
