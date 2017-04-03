package com.planet.wondering.chemi.util.listener;

import com.planet.wondering.chemi.model.User;

/**
 * Created by yoon on 2017. 3. 27..
 */

public interface OnUserInfoUpdateListener {

    void onUserNameInfoUpdate(String userName);

    void onUserImagePathUpdate(String userImagePath);

    void onUserInfoValueUpdate(User user);

}
