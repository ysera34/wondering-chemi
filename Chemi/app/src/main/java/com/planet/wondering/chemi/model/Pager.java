package com.planet.wondering.chemi.model;

/**
 * Created by yoon on 2017. 2. 7..
 */

public class Pager {

    private int mTotal;
    private String mPrevQuery;
    private String mNextQuery;

    public int getTotal() {
        return mTotal;
    }

    public void setTotal(int total) {
        mTotal = total;
    }

    public String getPrevQuery() {
        return mPrevQuery;
    }

    public void setPrevQuery(String prevQuery) {
        mPrevQuery = prevQuery;
    }

    public String getNextQuery() {
        return mNextQuery;
    }

    public void setNextQuery(String nextQuery) {
        mNextQuery = nextQuery;
    }
}
