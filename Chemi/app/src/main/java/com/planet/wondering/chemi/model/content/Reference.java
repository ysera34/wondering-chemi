package com.planet.wondering.chemi.model.content;

import java.io.Serializable;

/**
 * Created by yoon on 2017. 7. 4..
 */

public class Reference implements Serializable {

    private String mName;
    private String mUrl;

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        mUrl = url;
    }
}
