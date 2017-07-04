package com.planet.wondering.chemi.model.content;

import java.io.Serializable;

/**
 * Created by yoon on 2017. 7. 4..
 */

public class Text implements Serializable {

    private int mNumber;
    private String mDescription;

    public int getNumber() {
        return mNumber;
    }

    public void setNumber(int number) {
        mNumber = number;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }
}
