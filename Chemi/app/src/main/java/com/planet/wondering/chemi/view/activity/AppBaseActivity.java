package com.planet.wondering.chemi.view.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.planet.wondering.chemi.R;
import com.tsengvn.typekit.TypekitContextWrapper;

import static com.planet.wondering.chemi.common.Common.CHECK_VERSION_REQUEST_CODE;
import static com.planet.wondering.chemi.common.Common.CHECK_VERSION_RESULT_USUALLY_MODE_CODE;
import static com.planet.wondering.chemi.common.Common.CHECK_VERSION_RESULT_VOLUNTARY_MODE_CODE;
import static com.planet.wondering.chemi.common.Common.EXTRA_RESULT_CHECK_VERSION;

/**
 * Created by yoon on 2017. 1. 5..
 */

public class AppBaseActivity extends AppCompatActivity {

    private static final String TAG = AppBaseActivity.class.getSimpleName();

    private String mProductId;
    private String mContentId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Uri uriData = getIntent().getData();

        if (uriData != null) {
            mProductId = uriData.getQueryParameter("product_id");
            mContentId = uriData.getQueryParameter("content_id");

            if (mProductId != null || mContentId != null) {
                startActivityForResult(UpdateActivity.newIntent(getApplicationContext(),
                        CHECK_VERSION_REQUEST_CODE, false), CHECK_VERSION_REQUEST_CODE);
            }
        }

    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case CHECK_VERSION_REQUEST_CODE:
                if (resultCode == Activity.RESULT_OK) {
                    if (data != null) {
                        int resultModeCode = data.getIntExtra(EXTRA_RESULT_CHECK_VERSION, -1);
                        if (resultModeCode == CHECK_VERSION_RESULT_USUALLY_MODE_CODE ||
                                resultModeCode == CHECK_VERSION_RESULT_VOLUNTARY_MODE_CODE) {
                            startProductContentActivity(mProductId, mContentId);
                        }
                    }
                }
                break;
        }
    }

    private void startProductContentActivity(final String productId, final String contentId) {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

//                Uri uriData = getIntent().getData();
//                String productId;
//                String contentId;
//
//                if (uriData != null) {
//                    productId = uriData.getQueryParameter("product_id");
//                    contentId = uriData.getQueryParameter("content_id");

                    if (productId != null) {
                        startActivity(ProductActivity.newIntent(getApplicationContext(), Integer.valueOf(productId), (byte) 0));
//                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        finish();
                    } else if (contentId != null) {
                        startActivity(ContentActivity.newIntent(getApplicationContext(), Integer.valueOf(contentId)));
//                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        finish();
                    }
                }
//            }
        }, 800);
    }

//    private void popupUpdateDialog() {
//
//        AlertDialog.Builder builder = new AlertDialog.Builder(AppBaseActivity.this, R.style.SplashDialogTheme);
//        builder.setMessage("케미가 업그레이드 되었어요!\n업데이트를 통해 더욱 향상된 서비스를 경험하세요 :)");
//        builder.setPositiveButton("업데이트", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(APP_MARKET_URL)));
//                finish();
//            }
//        });
//        builder.setNegativeButton("종료", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                ActivityCompat.finishAffinity(AppBaseActivity.this);
//            }
//        });
//        builder.setCancelable(false);
//        AlertDialog dialog = builder.create();
//        dialog.show();
//    }
}
