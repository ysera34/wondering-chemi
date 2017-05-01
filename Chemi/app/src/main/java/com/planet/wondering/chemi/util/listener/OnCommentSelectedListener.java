package com.planet.wondering.chemi.util.listener;

import com.planet.wondering.chemi.model.Comment;

/**
 * Created by yoon on 2017. 4. 19..
 */

public interface OnCommentSelectedListener {

    void onCommentSelected(Comment comment, int commentClass);
}
