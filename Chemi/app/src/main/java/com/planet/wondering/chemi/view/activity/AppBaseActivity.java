package com.planet.wondering.chemi.view.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.tsengvn.typekit.TypekitContextWrapper;

/**
 * Created by yoon on 2017. 1. 5..
 */

public class AppBaseActivity extends AppCompatActivity {

    private static final String TAG = AppBaseActivity.class.getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Uri uriData = getIntent().getData();
        String productId;
        String contentId;

        if (uriData != null) {
//            Log.i(TAG, "uri data" + uriData.toString());
            productId = uriData.getQueryParameter("product_id");
            contentId = uriData.getQueryParameter("content_id");

            if (productId != null) {
//                Log.i(TAG, productId);
                startActivity(ProductActivity.newIntent(getApplicationContext(), Integer.valueOf(productId), (byte) 0));
                finish();
            } else if (contentId != null) {
//                Log.i(TAG, contentId);
                startActivity(ContentActivity.newIntent(getApplicationContext(), Integer.valueOf(contentId)));
                finish();
            } else {
                startActivity(new Intent(AppBaseActivity.this, MemberStartActivity.class));
                finish();
            }
        } else {
//            Log.i(TAG, "uriData : did not get it");
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
}
