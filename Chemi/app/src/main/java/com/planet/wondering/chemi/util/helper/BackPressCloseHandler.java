package com.planet.wondering.chemi.util.helper;

import android.app.Activity;
import android.support.v4.app.ActivityCompat;
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
//            mActivity.finish();
            mToast.cancel();
//            mActivity.moveTaskToBack(true);
//            System.exit(0);
//            android.os.Process.killProcess(android.os.Process.myPid());

//            Intent intent = new Intent(Intent.ACTION_MAIN);
//            Intent intent = new Intent(mActivity.getApplicationContext(), SplashActivity.class);
//            intent.addCategory(Intent.CATEGORY_HOME);
//            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            mActivity.startActivity(intent);
//            mActivity.finish();
            ActivityCompat.finishAffinity(mActivity);
//            System.exit(0);
//            android.os.Process.killProcess(android.os.Process.myPid());

//            Intent intent = new Intent(mActivity, SplashActivity.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//            mActivity.finishAffinity();
//            mActivity.startActivity(intent);

        }
    }

    private void showGuide() {
        mToast = Toast.makeText(mActivity, "\'뒤로\' 버튼을 한번 더 누르시면 종료됩니다.",
                Toast.LENGTH_SHORT);
        mToast.show();
    }
}
