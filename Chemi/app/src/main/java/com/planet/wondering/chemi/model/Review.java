package com.planet.wondering.chemi.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by yoon on 2017. 1. 17..
 */

public class Review implements Serializable {

    private int mId;
    private int mUserId;
    private int mProductId;
    private User mUser;
    private boolean mAuthor;
    private float mRatingValue;
    private String content;
//    private HashMap<Integer, String> mImagePathMap;
//    private ReviewSharedPreferences mPreferences;
    private String mDate;
    private ArrayList<String> mImagePaths;


    public Review() {
        mUser = new User();
//        mImagePathMap = new HashMap<>();
//        mPreferences = new ReviewSharedPreferences();
        mImagePaths = new ArrayList<>();
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public int getUserId() {
        return mUserId;
    }

    public void setUserId(int userId) {
        mUserId = userId;
    }

    public int getProductId() {
        return mProductId;
    }

    public void setProductId(int productId) {
        mProductId = productId;
    }

    public User getUser() {
        return mUser;
    }

    public void setUser(User user) {
        mUser = user;
    }

    public boolean isAuthor() {
        return mAuthor;
    }

    public void setAuthor(boolean author) {
        mAuthor = author;
    }

    public float getRatingValue() {
        return mRatingValue;
    }

    public void setRatingValue(float ratingValue) {
        mRatingValue = ratingValue;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

//    public HashMap<Integer, String> getImagePathMap() {
//        return mImagePathMap;
//    }
//
//    public void setImagePathMap(HashMap<Integer, String> imagePathMap) {
//        mImagePathMap = imagePathMap;
//    }
//
//    public void setImagePathMap(Context context) {
//        String image1Path = mPreferences.getStoredImage1Path(context);
//        if (image1Path != null) {
//            mImagePathMap.put(1, image1Path);
//        }
//
//        String image2Path = mPreferences.getStoredImage2Path(context);
//        if (image2Path != null) {
//            mImagePathMap.put(2, image2Path);
//        }
//
//        String image3Path = mPreferences.getStoredImage3Path(context);
//        if (image3Path != null) {
//            mImagePathMap.put(3, image3Path);
//        }
//
//        Log.d("getImage1Path", "image1Path : " + image1Path);
//        Log.d("getImage2Path", "image2Path : " + image2Path);
//        Log.d("getImage3Path", "image3Path : " + image3Path);
////        String[] imagePathArr = new String[]{image1Path, image2Path, image3Path};
////        for (int i = 1; i <= imagePathArr.length; i++) {
////            if (imagePathArr[i - 1] != null) {
////                mImagePathMap.put(i, imagePathArr[i-1]);
////                break;
////            }
////        }
//    }
//
//    public void putImagePath(Context context, int index, String imagePath) {
//        switch (index) {
//            case 1:
//                mPreferences.setStoreImage1Path(context, imagePath);
////                Log.d("putImage1Path", "image1Path : " + imagePath);
//                break;
//            case 2:
//                mPreferences.setStoreImage2Path(context, imagePath);
////                Log.d("putImage2Path", "image2Path : " + imagePath);
//                break;
//            case 3:
//                mPreferences.setStoreImage3Path(context, imagePath);
////                Log.d("putImage3Path", "image3Path : " + imagePath);
//                break;
//        }
//    }
//
//    public void removeImagePath(Context context, int index) {
//        switch (index) {
//            case 1:
//                mPreferences.removeStoredImage1Path(context);
//                break;
//            case 2:
//                mPreferences.removeStoredImage2Path(context);
//                break;
//            case 3:
//                mPreferences.removeStoredImage3Path(context);
//                break;
//        }
//        Log.i("removeImagePath", "image1Path : " + mPreferences.getStoredImage1Path(context));
//        Log.i("removeImagePath", "image2Path : " + mPreferences.getStoredImage2Path(context));
//        Log.i("removeImagePath", "image3Path : " + mPreferences.getStoredImage3Path(context));
//    }
//
//    public int getImagePathMapSize() {
//        return mImagePathMap.size();
//    }

    public String getDate() {
        return mDate;
    }

    public void setDate(String date) {
        mDate = date;
    }

    public ArrayList<String> getImagePaths() {
        return mImagePaths;
    }

    public void setImagePaths(ArrayList<String> imagePaths) {
        mImagePaths = imagePaths;
    }

    @Override
    public String toString() {
        return "Review{" +
                "mId=" + mId +
                ", mUserId=" + mUserId +
                ", mProductId=" + mProductId + "\n" +
                ", mUser=" + mUser.toString() + "\n" +
                ", mRatingValue=" + mRatingValue + "\n" +
                ", content='" + content + '\'' + "\n" +
                ", mDate='" + mDate + '\'' + "\n" +
                ", mImagePaths=" + mImagePaths +
                '}';
    }
}
