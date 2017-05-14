package com.planet.wondering.chemi.view.custom;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;

import com.planet.wondering.chemi.R;

/**
 * Created by yoon on 2017. 5. 14..
 */

public class CustomProgressDialog extends AlertDialog {

    public CustomProgressDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    public void show() {
        super.show();
        setContentView(R.layout.layout_custom_progress_dialog);
    }
}
