package com.planet.wondering.chemi.util.helper;

import android.app.Activity;
import android.widget.Toast;

/**
 * Created by yoon on 2017. 3. 22..
 */

public class BackPressCloseHandler {

    private long mPressedTime = 0;

    private Activity mActivity;
    private Toast mToast;

    public BackPressCloseHandler(Activity activity) {
        mActivity = activity;
    }

    public void onBackPressed() {
        if (System.currentTimeMillis() > mPressedTime + 2000) {
            mPressedTime = System.currentTimeMillis();
            showGuide();
            return;
        }
        if (System.currentTimeMillis() <= mPressedTime + 2000) {
            mActivity.finish();
            mToast.cancel();
//            android.os.Process.killProcess(android.os.Process.myPid());

        }
    }

    private void showGuide() {
        mToast = Toast.makeText(mActivity, "\'뒤로\'버튼을 한번 더 누르시면 앱이 종료 되요.",
                Toast.LENGTH_SHORT);
        mToast.show();
    }
}
