package com.planet.wondering.chemi.view.fragment;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.planet.wondering.chemi.R;
import com.planet.wondering.chemi.model.BottomSheetMenu;
import com.planet.wondering.chemi.model.Review;
import com.planet.wondering.chemi.network.AppSingleton;
import com.planet.wondering.chemi.network.MultipartRequest;
import com.planet.wondering.chemi.network.MultipartRequestHelper;
import com.planet.wondering.chemi.network.Parser;
import com.planet.wondering.chemi.util.adapter.BottomSheetMenuAdapter;
import com.planet.wondering.chemi.util.helper.ImageHandler;
import com.planet.wondering.chemi.util.helper.UserSharedPreferences;
import com.planet.wondering.chemi.util.listener.OnReviewEditListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_OK;
import static com.planet.wondering.chemi.network.Config.Product.PATH;
import static com.planet.wondering.chemi.network.Config.Review.Key.DESCRIPTION;
import static com.planet.wondering.chemi.network.Config.Review.Key.IMAGE_PATHS;
import static com.planet.wondering.chemi.network.Config.Review.Key.RATING;
import static com.planet.wondering.chemi.network.Config.Review.REVIEW_IMAGE_PATH;
import static com.planet.wondering.chemi.network.Config.Review.REVIEW_PATH;
import static com.planet.wondering.chemi.network.Config.SOCKET_TIMEOUT_POST_REQ;
import static com.planet.wondering.chemi.network.Config.URL_HOST;
import static com.planet.wondering.chemi.network.Config.User.Key.TOKEN;

/**
 * Created by yoon on 2017. 4. 17..
 */

public class ReviewUpdateFragment extends Fragment
        implements View.OnClickListener, RatingBar.OnRatingBarChangeListener {

    private static final String TAG = ReviewUpdateFragment.class.getSimpleName();

    private static final String ARG_REVIEW = "review";

    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 1001;
    private static final int GALLERY_IMAGE_REQUEST_CODE = 2001;
    private static final int PERMISSION_EXTERNAL_STORAGE_REQUEST_CODE = 9002;
    private String[] mStoragePermissions = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};

    public static ReviewUpdateFragment newInstance() {

        Bundle args = new Bundle();

        ReviewUpdateFragment fragment = new ReviewUpdateFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static ReviewUpdateFragment newInstance(Review review) {

        Bundle args = new Bundle();
        args.putSerializable(ARG_REVIEW, review);

        ReviewUpdateFragment fragment = new ReviewUpdateFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private RelativeLayout mReviewUpdateConfirmLayout;
    private ImageView mUpdateProductImageView;
    private TextView mUpdateProductBrandTextView;
    private TextView mUpdateProductNameTextView;
    private TextView mUpdateRatingMessageTextView;
    private RatingBar mUpdateReviewRatingBar;
    private TextView mUpdateReviewLengthTextView;
    private TextView mUpdateReviewTextView;
    private ImageView mUpdateReviewImage1ImageView;
    private ImageView mUpdateReviewImage2ImageView;
    private ImageView mUpdateReviewImage3ImageView;

    private String mReviewHint;
    private Review mReview;
    private ArrayList<String> mReviewImagePaths;

    private ImageHandler mReviewImageHandler;

    private BottomSheetDialog mMenuBottomSheetDialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mReviewImageHandler = new ImageHandler(getActivity());

        mReviewHint = getString(R.string.review_create_review_hint);
        mReview = (Review) getArguments().getSerializable(ARG_REVIEW);
        mReviewImagePaths = mReview.getImagePaths();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_review_update, container, false);

        mReviewUpdateConfirmLayout = (RelativeLayout) view.findViewById(R.id.review_update_confirm_layout);
        mReviewUpdateConfirmLayout.setOnClickListener(this);
        mUpdateProductImageView = (ImageView) view.findViewById(R.id.review_update_product_image_view);
        mUpdateProductBrandTextView = (TextView) view.findViewById(R.id.review_update_product_brand_text_view);
        mUpdateProductNameTextView = (TextView) view.findViewById(R.id.review_update_product_name_text_view);
        mUpdateRatingMessageTextView = (TextView) view.findViewById(R.id.review_update_message_text_view);
        mUpdateReviewRatingBar = (RatingBar) view.findViewById(R.id.review_update_rating_value_rating_bar);
        mUpdateReviewRatingBar.setOnRatingBarChangeListener(this);
        mUpdateReviewLengthTextView = (TextView) view.findViewById(R.id.review_update_review_text_length_text_view);
        mUpdateReviewTextView = (TextView) view.findViewById(R.id.review_update_review_text_view);
        mUpdateReviewTextView.setOnClickListener(this);
        mUpdateReviewImage1ImageView = (ImageView) view.findViewById(R.id.review_update_review_image1_image_view);
        mUpdateReviewImage1ImageView.setOnClickListener(this);
        mUpdateReviewImage2ImageView = (ImageView) view.findViewById(R.id.review_update_review_image2_image_view);
        mUpdateReviewImage2ImageView.setOnClickListener(this);
        mUpdateReviewImage3ImageView = (ImageView) view.findViewById(R.id.review_update_review_image3_image_view);
        mUpdateReviewImage3ImageView.setOnClickListener(this);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindProduct();
        bindReview();
    }

    private void bindProduct() {
        Glide.with(getActivity())
                .load(mReview.getProductImagePath())
//                    .placeholder(R.drawable.unloaded_image_holder)
//                    .error(R.drawable.unloaded_image_holder)
                .crossFade()
                .override(300, 200)
                .into(mUpdateProductImageView);
        mUpdateProductBrandTextView.setText(mReview.getProductBrand());
        mUpdateProductNameTextView.setText(mReview.getProductName());
    }

    private void bindReview() {
        mUpdateReviewRatingBar.setRating(mReview.getRatingValue());
        String[] messageArray = getResources().getStringArray(R.array.review_rating_message_array);
        for (int i = 0; i < messageArray.length; i++) {
            if (mUpdateReviewRatingBar.getRating() == 0.5f * i) {
                mUpdateRatingMessageTextView.setText(messageArray[i]);
                break;
            }
        }
        mUpdateReviewLengthTextView.setText(
                getString(R.string.review_create_review_length_format,
                        String.valueOf(mReview.getContent().length())));
        mUpdateReviewTextView.setText(mReview.getContent());

        switch (mReview.getImagePaths().size()) {
            case 0:
                mUpdateReviewImage2ImageView.setVisibility(View.GONE);
                mUpdateReviewImage3ImageView.setVisibility(View.GONE);
                break;

            case 1:
                isHasImage1 = true;
                Glide.with(getActivity())
                        .load(mReview.getImagePaths().get(0))
//                    .placeholder(R.drawable.unloaded_image_holder)
//                    .error(R.drawable.unloaded_image_holder)
                        .crossFade()
                        .override(210, 210)
//                                .centerCrop()
                        .into(mUpdateReviewImage1ImageView);
                mUpdateReviewImage3ImageView.setVisibility(View.GONE);
                break;
            case 2:
                isHasImage1 = true;
                isHasImage2 = true;
                Glide.with(getActivity())
                        .load(mReview.getImagePaths().get(0))
//                    .placeholder(R.drawable.unloaded_image_holder)
//                    .error(R.drawable.unloaded_image_holder)
                        .crossFade()
                        .override(210, 210)
//                                .centerCrop()
                        .into(mUpdateReviewImage1ImageView);
                Glide.with(getActivity())
                        .load(mReview.getImagePaths().get(1))
//                    .placeholder(R.drawable.unloaded_image_holder)
//                    .error(R.drawable.unloaded_image_holder)
                        .crossFade()
                        .override(210, 210)
//                                .centerCrop()
                        .into(mUpdateReviewImage2ImageView);
                break;
            case 3:
                isHasImage1 = true;
                isHasImage2 = true;
                isHasImage3 = true;
                Glide.with(getActivity())
                        .load(mReview.getImagePaths().get(0))
//                    .placeholder(R.drawable.unloaded_image_holder)
//                    .error(R.drawable.unloaded_image_holder)
                        .crossFade()
                        .override(210, 210)
//                                .centerCrop()
                        .into(mUpdateReviewImage1ImageView);
                Glide.with(getActivity())
                        .load(mReview.getImagePaths().get(1))
//                    .placeholder(R.drawable.unloaded_image_holder)
//                    .error(R.drawable.unloaded_image_holder)
                        .crossFade()
                        .override(210, 210)
//                                .centerCrop()
                        .into(mUpdateReviewImage2ImageView);
                Glide.with(getActivity())
                        .load(mReview.getImagePaths().get(2))
//                    .placeholder(R.drawable.unloaded_image_holder)
//                    .error(R.drawable.unloaded_image_holder)
                        .crossFade()
                        .override(210, 210)
//                                .centerCrop()
                        .into(mUpdateReviewImage3ImageView);
                break;
        }
    }

    private boolean isRatingBarChanged = false;
    private boolean isWriteContent = false;
    private boolean isHasImage1 = false;
    private boolean isHasImage2 = false;
    private boolean isHasImage3 = false;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.review_update_confirm_layout:
                if (!isHasUploadImage1 && !isHasUploadImage2 && !isHasUploadImage3) {
                    mReviewImagePaths.clear();
                    requestUpdateReview();
                } else {
                    if (isHasUploadImage1) {

                        requestUpdateReviewImage(1);
                    }
                    if (isHasUploadImage2) {
                        requestUpdateReviewImage(2);
                    }
                    if (isHasUploadImage3) {
                        requestUpdateReviewImage(3);
                    }
                }
//                if (!isRatingBarChanged) {
//                    Toast.makeText(getActivity(), "제품의 별점을 매겨보세요", Toast.LENGTH_SHORT).show();
//                } else if (!isWriteContent) {
//                    Toast.makeText(getActivity(), "제품의 리뷰를 작성해보세요", Toast.LENGTH_SHORT).show();
//                } else {
//                    requestUpdateReview();
//                    mReviewUpdateConfirmLayout.setEnabled(false);
//                }
                break;
            case R.id.review_update_review_text_view:
                mReviewEditListener.onReviewEdit(mUpdateReviewTextView.getText().toString(), true, 2);
                break;
            case R.id.review_update_review_image1_image_view:
                if (!isHasImage1) {
                    checkStoragePermission(1);
                } else {
                    updateEditImageMenuBottomSheetDialog(1);
                }
                break;
            case R.id.review_update_review_image2_image_view:
                if (!isHasImage2) {
                    checkStoragePermission(2);
                } else {
                    updateEditImageMenuBottomSheetDialog(2);
                }
                break;
            case R.id.review_update_review_image3_image_view:
                if (!isHasImage3) {
                    checkStoragePermission(3);
                } else {
                    updateEditImageMenuBottomSheetDialog(3);
                }
                break;
        }
    }

    @Override
    public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
        String[] messageArray = getResources().getStringArray(R.array.review_rating_message_array);

        for (int i = 0; i < messageArray.length; i++) {
            if (rating == 0.5f * i) {
                mUpdateRatingMessageTextView.setText(messageArray[i]);
                break;
            }
        }
        isRatingBarChanged = true;
    }

    public void updateContentTextView(String reviewContent) {
        if (reviewContent == null || reviewContent.equals("")) {
            mUpdateReviewTextView.setText(mReviewHint);
        } else {
            mUpdateReviewTextView.setText(reviewContent);
            isWriteContent = true;
        }
        mUpdateReviewLengthTextView.setText(
                getString(R.string.review_create_review_length_format,
                        String.valueOf(mUpdateReviewTextView.getText().length())));
    }

    OnReviewEditListener mReviewEditListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mReviewEditListener = (OnReviewEditListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implements OnReviewEditListener");
        }
    }

    private void updatePickImageMenuBottomSheetDialog(final int imagePosition) {
        if (dismissMenuBottomSheetDialog()) {
            return;
        }
        ArrayList<BottomSheetMenu> bottomSheetMenus = new ArrayList<>();
        bottomSheetMenus.add(new BottomSheetMenu(R.drawable.ic_camera, R.string.bottom_sheet_menu_camera));
        bottomSheetMenus.add(new BottomSheetMenu(R.drawable.ic_gallery, R.string.bottom_sheet_menu_gallery));

        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        View view = layoutInflater.inflate(R.layout.layout_bottom_sheet_menu_recycler_view, null);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.bottom_sheet_menu_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        BottomSheetMenuAdapter bottomSheetMenuAdapter = new BottomSheetMenuAdapter(bottomSheetMenus);
        recyclerView.setAdapter(bottomSheetMenuAdapter);
        bottomSheetMenuAdapter.setItemClickListener(new BottomSheetMenuAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BottomSheetMenuAdapter.MenuItemHolder itemHolder, int position) {
                dismissMenuBottomSheetDialog();
                if (position == 0 /* camera */) {
                    if (mReviewImageHandler.hasCamera()) {
                        startActivityForResult(mReviewImageHandler.dispatchTakePictureIntent(),
                                CAMERA_CAPTURE_IMAGE_REQUEST_CODE + imagePosition);
                    } else {
                        Toast.makeText(getActivity(), "카메라를 사용할 수 없습니다.", Toast.LENGTH_SHORT).show();
                    }
                } else if (position == 1 /* gallery */) {
                    startActivityForResult(mReviewImageHandler.pickGalleryPictureIntent(),
                            GALLERY_IMAGE_REQUEST_CODE + imagePosition);
                }
            }
        });
        mMenuBottomSheetDialog = new BottomSheetDialog(getActivity());
        mMenuBottomSheetDialog.setContentView(view);
        mMenuBottomSheetDialog.show();
    }

    private void updateEditImageMenuBottomSheetDialog(final int imagePosition) {
        if (dismissMenuBottomSheetDialog()) {
            return;
        }
        ArrayList<BottomSheetMenu> bottomSheetMenus = new ArrayList<>();
        bottomSheetMenus.add(new BottomSheetMenu(0, R.string.bottom_sheet_menu_image_pick));
        bottomSheetMenus.add(new BottomSheetMenu(0, R.string.bottom_sheet_menu_image_delete));

        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        View view = layoutInflater.inflate(R.layout.layout_bottom_sheet_menu_recycler_view, null);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.bottom_sheet_menu_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        BottomSheetMenuAdapter bottomSheetMenuAdapter = new BottomSheetMenuAdapter(bottomSheetMenus);
        recyclerView.setAdapter(bottomSheetMenuAdapter);
        bottomSheetMenuAdapter.setItemClickListener(new BottomSheetMenuAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BottomSheetMenuAdapter.MenuItemHolder itemHolder, int position) {
                dismissMenuBottomSheetDialog();
                if (position == 0 /* re_pick */) {
                    updatePickImageMenuBottomSheetDialog(imagePosition);
                } else if (position == 1 /* delete */) {
//                    deleteImage(imagePosition);
                    deleteReviewUploadImage(imagePosition);
                }
            }
        });
        mMenuBottomSheetDialog = new BottomSheetDialog(getActivity());
        mMenuBottomSheetDialog.setContentView(view);
        mMenuBottomSheetDialog.show();
    }

    private boolean dismissMenuBottomSheetDialog() {
        if (mMenuBottomSheetDialog != null && mMenuBottomSheetDialog.isShowing()) {
            mMenuBottomSheetDialog.dismiss();
            return true;
        }
        return false;
    }

    private void deleteReviewUploadImage(int imagePosition) {

        switch (imagePosition) {
            case 1:
                if (isHasImage3) {
                    mUpdateReviewImage1ImageView.setImageDrawable(mUpdateReviewImage2ImageView.getDrawable());
                    mUpdateReviewImage2ImageView.setImageDrawable(mUpdateReviewImage3ImageView.getDrawable());
                    mUpdateReviewImage3ImageView.setImageBitmap(null);
                    isHasImage3 = false;
                    if (isHasUploadImage1) isHasUploadImage1 = false;
                    if (isHasUploadImage2) isHasUploadImage1 = true;
                    if (isHasUploadImage3) isHasUploadImage2 = true; isHasUploadImage3 = false;

                } else if (isHasImage2) {
                    mUpdateReviewImage1ImageView.setImageDrawable(mUpdateReviewImage2ImageView.getDrawable());
                    mUpdateReviewImage2ImageView.setImageBitmap(null);
                    isHasImage2 = false;
                    if (isHasUploadImage1) isHasUploadImage1 = false;
                    if (isHasUploadImage2) isHasUploadImage1 = true; isHasUploadImage2 = false;

                    mUpdateReviewImage3ImageView.setVisibility(View.INVISIBLE);
                } else if (isHasImage1) {
                    mUpdateReviewImage1ImageView.setImageBitmap(null);
                    isHasImage1 = false;
                    if (isHasUploadImage1) isHasUploadImage1 = false;

                    mUpdateReviewImage2ImageView.setVisibility(View.INVISIBLE);
                }
                break;
            case 2:
                if (isHasImage3) {
                    mUpdateReviewImage2ImageView.setImageDrawable(mUpdateReviewImage3ImageView.getDrawable());
                    mUpdateReviewImage3ImageView.setImageBitmap(null);
                    isHasImage3 = false;
                    if (isHasUploadImage2) isHasUploadImage2 = false;
                    if (isHasUploadImage3) isHasUploadImage2 = true; isHasUploadImage3 = false;

                } else if (isHasImage2) {
                    mUpdateReviewImage2ImageView.setImageBitmap(null);
                    isHasImage2 = false;
                    mUpdateReviewImage3ImageView.setVisibility(View.INVISIBLE);
                    if (isHasUploadImage2) isHasUploadImage2 = false;
                }
                break;
            case 3:
                if (isHasImage3) {
                    mUpdateReviewImage3ImageView.setImageBitmap(null);
                    isHasImage3 = false;
                    if (isHasUploadImage3) isHasUploadImage3 = false;
                }
                break;
        }
    }

    private boolean isHasUploadImage1 = false;
    private boolean isHasUploadImage2 = false;
    private boolean isHasUploadImage3 = false;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case CAMERA_CAPTURE_IMAGE_REQUEST_CODE + 1:
                if (resultCode == RESULT_OK) {
                    mReviewImageHandler.handleCameraImage(mUpdateReviewImage1ImageView);
                    isHasImage1 = true;
                    isHasUploadImage1 = true;
                    mUpdateReviewImage2ImageView.setVisibility(View.VISIBLE);
                }
                break;
            case CAMERA_CAPTURE_IMAGE_REQUEST_CODE + 2:
                if (resultCode == RESULT_OK) {
                    mReviewImageHandler.handleCameraImage(mUpdateReviewImage2ImageView);
                    isHasImage2 = true;
                    isHasUploadImage2 = true;
                    mUpdateReviewImage3ImageView.setVisibility(View.VISIBLE);
                }
                break;
            case CAMERA_CAPTURE_IMAGE_REQUEST_CODE + 3:
                if (resultCode == RESULT_OK) {
                    mReviewImageHandler.handleCameraImage(mUpdateReviewImage3ImageView);
                    isHasImage3 = true;
                    isHasUploadImage3 = true;
                }
                break;
            case GALLERY_IMAGE_REQUEST_CODE + 1:
                if (resultCode == RESULT_OK) {
                    mReviewImageHandler.handleGalleryImage(data, mUpdateReviewImage1ImageView);
                    isHasImage1 = true;
                    isHasUploadImage1 = true;
                    mUpdateReviewImage2ImageView.setVisibility(View.VISIBLE);
                }
                break;
            case GALLERY_IMAGE_REQUEST_CODE + 2:
                if (resultCode == RESULT_OK) {
                    mReviewImageHandler.handleGalleryImage(data, mUpdateReviewImage2ImageView);
                    isHasImage2 = true;
                    isHasUploadImage2 = true;
                    mUpdateReviewImage3ImageView.setVisibility(View.VISIBLE);
                }
                break;
            case GALLERY_IMAGE_REQUEST_CODE + 3:
                if (resultCode == RESULT_OK) {
                    mReviewImageHandler.handleGalleryImage(data, mUpdateReviewImage3ImageView);
                    isHasImage3 = true;
                    isHasUploadImage3 = true;
                }
                break;
        }
    }

    private void checkStoragePermission(int imagePosition) {
        String state = Environment.getExternalStorageState();
        if (!Environment.MEDIA_MOUNTED.equals(state) || Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            dismissMenuBottomSheetDialog();
            Toast.makeText(getActivity(), "SD 카드가 없으므로 취소 되었습니다.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(getActivity(), mStoragePermissions[0]) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(getActivity(), mStoragePermissions[1]) != PackageManager.PERMISSION_GRANTED) {

                if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), mStoragePermissions[0])
                        || ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), mStoragePermissions[1])) {
                    // Should we show an explanation?

                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("저장소 권한을 요청합니다.")
                            .setMessage("리뷰 사진을 저장한 갤러리의 접근 권한을 요청합니다.");
                    builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            requestPermissions(mStoragePermissions, PERMISSION_EXTERNAL_STORAGE_REQUEST_CODE);
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
//                else if () {
//                go to Settings.
//
//                }
                else {
                    // No explanation needed, we can request the permission.
                    requestPermissions(mStoragePermissions, PERMISSION_EXTERNAL_STORAGE_REQUEST_CODE);
                }
            }
            else {
                // already have permission
                updatePickImageMenuBottomSheetDialog(imagePosition);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case PERMISSION_EXTERNAL_STORAGE_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    updatePickImageMenuBottomSheetDialog(1);
                } else {
                    Toast.makeText(getActivity(), "권한이 없으므로 취소 되었습니다.", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void requestUpdateReviewImage(final int imagePosition) {

        MultipartRequest multipartRequest = new MultipartRequest(
                Request.Method.POST, URL_HOST + REVIEW_PATH + mReview.getId() + REVIEW_IMAGE_PATH,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        String responseString = new String(response.data);
//                        Log.i(TAG, "responseString : " + responseString);
                        try {
                            JSONObject responseJSONObject = new JSONObject(responseString);
                            String imagePath = Parser.parseUpdateReviewImagePath(responseJSONObject);
                            Log.i(TAG, imagePosition + " imagePath : " + imagePath);
                            if (mReviewImagePaths.size() >= imagePosition) {
                                mReviewImagePaths.set(imagePosition - 1, imagePath);
                            } else {
                                mReviewImagePaths.add(imagePath);
                            }

                            if (isHasUploadImage3 && imagePosition == 3) {
                                Log.i(TAG, "request position : " + imagePosition);
                                requestUpdateReview();
                            }
                            if (!isHasUploadImage3 && isHasUploadImage2 && imagePosition == 2) {
                                Log.i(TAG, "request position : " + imagePosition);
                                requestUpdateReview();
                            }
                            if (!isHasUploadImage2 && isHasUploadImage1 && imagePosition == 1) {
                                Log.i(TAG, "request position : " + imagePosition);
                                requestUpdateReview();
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, error.toString());
                        Toast.makeText(getActivity(),
                                R.string.progress_dialog_message_error, Toast.LENGTH_SHORT).show();
                    }
                }
        )
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
//                return super.getHeaders();
                Map<String, String> params = new HashMap<>();
                params.put(TOKEN, UserSharedPreferences.getStoredToken(getActivity()));
                return params;
            }

            @Override
            protected Map<String, ArrayList<DataPart>> getByteDataArray() throws AuthFailureError {
                Map<String, ArrayList<DataPart>> params = new HashMap<>();

                ArrayList<DataPart> imageDataPart = new ArrayList<>();

                switch (imagePosition) {
                    case 1:
                        imageDataPart.add(new DataPart("reviewImage1.jpg", MultipartRequestHelper.getFileDataFromDrawable(
                                getActivity(), mUpdateReviewImage1ImageView.getDrawable()), "image/*"));
                        break;
                    case 2:
                        imageDataPart.add(new DataPart("reviewImage2.jpg", MultipartRequestHelper.getFileDataFromDrawable(
                                getActivity(), mUpdateReviewImage2ImageView.getDrawable()), "image/*"));
                        break;
                    case 3:
                        imageDataPart.add(new DataPart("reviewImage3.jpg", MultipartRequestHelper.getFileDataFromDrawable(
                                getActivity(), mUpdateReviewImage3ImageView.getDrawable()), "image/*"));
                        break;
                }
                params.put("image", imageDataPart);

                return params;
            }
        };

        multipartRequest.setRetryPolicy(new DefaultRetryPolicy(SOCKET_TIMEOUT_POST_REQ,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        AppSingleton.getInstance(getActivity()).addToRequestQueue(multipartRequest, TAG);
    }

    private void requestUpdateReview() {

//        Map<String, Object> params = new HashMap<>();
//        params.put(RATING, String.valueOf(mUpdateReviewRatingBar.getRating()));
//        params.put(DESCRIPTION, mUpdateReviewTextView.getText().toString());

            JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(RATING, String.valueOf(mUpdateReviewRatingBar.getRating()));
            jsonObject.put(DESCRIPTION, mUpdateReviewTextView.getText().toString());

            if (mReviewImagePaths.size() > 0) {
//                Gson gson = new GsonBuilder().create();
//                JsonArray mReviewImagePathsJsonArray = gson.toJsonTree(mReviewImagePaths).getAsJsonArray();
    //            params.put(IMAGE_PATHS, mReviewImagePathsJsonArray);
//                jsonObject.put(IMAGE_PATHS, mReviewImagePathsJsonArray);

                JSONArray imagePathJsonArray = new JSONArray();
                for (String str : mReviewImagePaths) {
                    imagePathJsonArray.put(str);
                }
                jsonObject.put(IMAGE_PATHS, imagePathJsonArray);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            jsonObject = null;
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.PUT, URL_HOST + PATH + mReview.getProductId() + REVIEW_PATH + mReview.getId(),
//                new JSONObject(params),
                jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(getActivity(),
                                "작성하신 리뷰가 수정되었습니다.", Toast.LENGTH_SHORT).show();
                        getActivity().onBackPressed();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, error.toString());
                        Toast.makeText(getActivity(),
                                "리뷰 수정 중에 오류가 발생하였습니다. 잠시후 다시 요쳥해주세요", Toast.LENGTH_SHORT).show();
                    }
                }
        )
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put(TOKEN, UserSharedPreferences.getStoredToken(getActivity()));
                return params;
            }
        };

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(SOCKET_TIMEOUT_POST_REQ,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        AppSingleton.getInstance(getActivity()).addToRequestQueue(jsonObjectRequest, TAG);
    }
}
