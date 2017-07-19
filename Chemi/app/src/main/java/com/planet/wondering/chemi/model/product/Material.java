package com.planet.wondering.chemi.model.product;

import java.io.Serializable;

/**
 * Created by yoon on 2017. 7. 19..
 */

public class Material implements Serializable {

    private int mId;
    private String mName;

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }
}
