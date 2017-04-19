package com.planet.wondering.chemi.model;

import android.os.Parcel;

import com.bignerdranch.expandablerecyclerview.model.Parent;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yoon on 2017. 4. 19..
 */

public class Comment implements Parent<Comment>, Serializable {

    private int mId;
    private int mUserId;
    private String mUserName;
    private String mUserImagePath;
    private String mDescription;
    private String mDate;
    private ArrayList<Comment> mChildComments;

    public Comment() {
        mChildComments = new ArrayList<>();
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

    public String getUserName() {
        return mUserName;
    }

    public void setUserName(String userName) {
        mUserName = userName;
    }

    public String getUserImagePath() {
        return mUserImagePath;
    }

    public void setUserImagePath(String userImagePath) {
        mUserImagePath = userImagePath;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public String getDate() {
        return mDate;
    }

    public void setDate(String date) {
        mDate = date;
    }

    public ArrayList<Comment> getChildComments() {
        return mChildComments;
    }

    public void setChildComments(ArrayList<Comment> childComments) {
        mChildComments = childComments;
    }

    @Override
    public List<Comment> getChildList() {
        return mChildComments;
    }

    @Override
    public boolean isInitiallyExpanded() {
        return false;
    }

    protected Comment(Parcel in) {
        mId = in.readInt();
        mUserId = in.readInt();
        mUserName = in.readString();
        mUserImagePath = in.readString();
        mDescription = in.readString();
        mDate = in.readString();
        if (in.readByte() == 0x01) {
            mChildComments = new ArrayList<Comment>();
            in.readList(mChildComments, Comment.class.getClassLoader());
        } else {
            mChildComments = null;
        }
    }

//    @Override
//    public int describeContents() {
//        return 0;
//    }
//
//    @Override
//    public void writeToParcel(Parcel dest, int flags) {
//        dest.writeInt(mId);
//        dest.writeInt(mUserId);
//        dest.writeString(mUserName);
//        dest.writeString(mUserImagePath);
//        dest.writeString(mDescription);
//        dest.writeString(mDate);
//        if (mChildComments == null) {
//            dest.writeByte((byte) (0x00));
//        } else {
//            dest.writeByte((byte) (0x01));
//            dest.writeList(mChildComments);
//        }
//    }
//
//    @SuppressWarnings("unused")
//    public static final Parcelable.Creator<Comment> CREATOR = new Parcelable.Creator<Comment>() {
//        @Override
//        public Comment createFromParcel(Parcel in) {
//            return new Comment(in);
//        }
//
//        @Override
//        public Comment[] newArray(int size) {
//            return new Comment[size];
//        }
//    };

    @Override
    public String toString() {
        return "Comment{" +
                "mId=" + mId +
                ", mUserId=" + mUserId +
                ", mUserName='" + mUserName + '\'' +
                ", mUserImagePath='" + mUserImagePath + '\'' +
                ", mDescription='" + mDescription + '\'' +
                ", mDate='" + mDate + '\'' +
                ", mChildComments=" + mChildComments.toString() +
                '}';
    }
}
