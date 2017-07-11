package com.planet.wondering.chemi.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.planet.wondering.chemi.R;

/**
 * Created by yoon on 2017. 6. 29..
 */

public class ImageActivity extends AppBaseActivity implements View.OnClickListener {

    private static final String TAG = ImageActivity.class.getSimpleName();

    private static final String EXTRA_IMAGE_TITLE = "com.planet.wondering.chemi.image_title";
    private static final String EXTRA_IMAGE_PATH = "com.planet.wondering.chemi.image_path";

    public static Intent newIntent(Context packageContext) {
        Intent intent = new Intent(packageContext, ImageActivity.class);
        return intent;
    }

    public static Intent newIntent(Context packageContext, String title, String imagePath) {
        Intent intent = new Intent(packageContext, ImageActivity.class);
        intent.putExtra(EXTRA_IMAGE_TITLE, title);
        intent.putExtra(EXTRA_IMAGE_PATH, imagePath);
        return intent;
    }

    private String mTitle;
    private String mImagePath;
    private TextView mImageTitleTextView;
    private ImageView mFullScreenImageView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        mTitle = getIntent().getStringExtra(EXTRA_IMAGE_TITLE);
        mImagePath = getIntent().getStringExtra(EXTRA_IMAGE_PATH);

        mImageTitleTextView = (TextView) findViewById(R.id.image_title_text_view);
        mImageTitleTextView.setText(String.valueOf(mTitle));

        findViewById(R.id.image_title_text_layout).setOnClickListener(this);
        findViewById(R.id.clear_image_view).setOnClickListener(this);
        mFullScreenImageView = (ImageView) findViewById(R.id.full_screen_image_view);

        Glide.with(getApplicationContext())
                .load(mImagePath)
                .into(mFullScreenImageView);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.image_title_text_layout:
            case R.id.clear_image_view:
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                break;
        }
    }
}
