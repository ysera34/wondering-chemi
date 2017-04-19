package com.planet.wondering.chemi.model;

import com.bignerdranch.expandablerecyclerview.model.Parent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yoon on 2017. 4. 19..
 */

public class Comment implements Parent<Comment> {

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

    public int getChildCommentCount() {

        int commentSize = 0;
        if (mChildComments == null) {
            commentSize = mChildComments.size();
        }
        return commentSize;
    }

    @Override
    public List<Comment> getChildList() {
        return mChildComments;
    }

    @Override
    public boolean isInitiallyExpanded() {
        return false;
    }
}
