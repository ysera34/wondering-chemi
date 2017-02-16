package com.planet.wondering.chemi.model;

/**
 * Created by yoon on 2017. 2. 17..
 */

public class User {

    private int mId;
    private String mEmail;
    private String mName;
    private String mToken;

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

    @Override
    public String toString() {
        return "User{" +
                "mId=" + mId +
                ", mEmail='" + mEmail + '\'' +
                ", mName='" + mName + '\'' +
                ", mToken='" + mToken + '\'' +
                '}';
    }
}
