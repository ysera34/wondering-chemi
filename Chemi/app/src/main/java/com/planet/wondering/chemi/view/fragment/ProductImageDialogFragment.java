package com.planet.wondering.chemi.view.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.planet.wondering.chemi.R;

import static com.planet.wondering.chemi.common.Common.PRODUCT_IMAGE_DIALOG_WIDTH_HEIGHT_RATIO;

/**
 * Created by yoon on 2017. 5. 26..
 */

public class ProductImageDialogFragment extends DialogFragment implements View.OnClickListener {

    private static final String TAG = ProductImageDialogFragment.class.getSimpleName();

    private static final String ARG_PRODUCT_IMAGE = "product_image";

    public static ProductImageDialogFragment newInstance() {

        Bundle args = new Bundle();

        ProductImageDialogFragment fragment = new ProductImageDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static ProductImageDialogFragment newInstance(String imagePath) {

        Bundle args = new Bundle();
        args.putString(ARG_PRODUCT_IMAGE, imagePath);

        ProductImageDialogFragment fragment = new ProductImageDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private String mImagePath;

    private AlertDialog.Builder mBuilder;

    private ImageView mProductImageDialogImageView;
    private ImageView mProductImageDialogCancelImageView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mImagePath = getArguments().getString(ARG_PRODUCT_IMAGE);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity())
                .inflate(R.layout.fragment_product_image_dialog, null);

        int thumbnailWidth = getScreenWidth();
        int thumbnailHeight = (int) (thumbnailWidth * PRODUCT_IMAGE_DIALOG_WIDTH_HEIGHT_RATIO);

        mProductImageDialogImageView = (ImageView) view.findViewById(R.id.product_image_dialog_image_view);
        Glide.with(getActivity())
                .load(mImagePath)
//                .override(thumbnailWidth, thumbnailHeight)
                .override(thumbnailWidth, thumbnailHeight)
//                .fitCenter()
                .centerCrop()
                .into(mProductImageDialogImageView);
        mProductImageDialogCancelImageView = (ImageView)
                view.findViewById(R.id.product_image_dialog_cancel_image_view);
        mProductImageDialogCancelImageView.setOnClickListener(this);

        mBuilder = new AlertDialog.Builder(getActivity(), android.R.style.Theme_Translucent_NoTitleBar)
                .setView(view);
        return mBuilder.create();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.product_image_dialog_cancel_image_view:
                getDialog().dismiss();
                break;
        }
    }

    public int getScreenWidth() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }
}
